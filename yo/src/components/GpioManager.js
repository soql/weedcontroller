require('bootstrap/dist/css/bootstrap.css');
require('normalize.css/normalize.css');
require('styles/App.css');
require('react-date-picker/index.css');
require('react-date-picker/base.css');

require('styles/simplegrid.css');
import axios from 'axios';
import React from 'react';
import BlockUi from 'react-block-ui';
import AppActions from '../actions/AppActions';
import GpioManagerStore from '../stores/GpioManagerStore';
import {Button, Input} from 'reactstrap';
 
class GpioManager extends React.Component {	
	constructor (props) {
	    super(props)
	    this.state = {
	    	managedSwitches: GpioManagerStore.getManagedSwitches(),
	    	blocking: false
	    	};
	    }  
	     
	  componentWillMount(){
		  GpioManagerStore.startTimer();
		  GpioManagerStore.addChangeListener('STORE_MANAGED_SWITCHES_READED', this.managedSwitchChanged.bind(this));
		  GpioManagerStore.tick();	
		  
	  }
	  componentWillUnmount(){
		  GpioManagerStore.stopTimer();
	  }
	  managedSwitchChanged(){		  
		  console.log("managedSwitchChanged execute");
		  this.setState({managedSwitches: GpioManagerStore.getManagedSwitches()});	
		  this.setState({blocking: false});
	  }
	  manageGpio(element, isChecked){
		  this.setState({blocking: true});
		  console.log("Klikniete "+element.name+" isChecked: "+isChecked);
		  AppActions.managedSwitchChange({gpioNumber: element.gpioNumber, gpioState: isChecked});
	  }
	  renderChildren(children){
		  return (<tr>
		  			<td className="managedGpioText">{children.name}</td><td><Input type="checkbox" checked={children.active} className="managedGpioCheckbox" onClick={(event) => {this.manageGpio(children, event.target.checked);}}/></td>
		  			</tr>);
	  }
	  renderOneSwitch(element){
		  return (				  
				  <table>
				  <tr><td className="managedGpioParentText" colSpan="2">{element.name}</td></tr>
				  {element.gpio.map((children, i) => {					  
					  return this.renderChildren(children)
				  })}				  
				  </table>
				  );
	  }
	render(){
		this.state.managedSwitches.forEach((element) => {
			  console.log(element.name);
		  })
		return (
				<div className="grid grid-pad">
					<div className="col-1-3">
						<div className="content">
							
						</div>
					</div>
					<div className="col-1-3">  	
						<BlockUi tag="div" blocking={this.state.blocking}>
						<div className="content">
							{this.state.managedSwitches.map((element,i) => {
								return this.renderOneSwitch(element)
							})}
						</div>
						</BlockUi>
					</div>
					<div className="col-1-3">
		    			<div className="content">
			    		
		    			</div>
		    		</div>
				</div>  

				);
	}
}
export default GpioManager;