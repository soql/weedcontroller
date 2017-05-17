require('normalize.css/normalize.css');
require('styles/App.css');
require('styles/burger-menu.css');
require('styles/simplegrid.css');

import React from 'react';
import BlockUi from 'react-block-ui';
import axios from 'axios';
import 'react-block-ui/style.css';

import LogTable from './LogTable';
import AppActions from '../actions/AppActions';
import SwitchStore from '../stores/SwitchStore';
import SensorStore from '../stores/SensorStore';

class Main extends React.Component {
  constructor (props) {
    super(props)
    this.state = {blocking: false, 
    	switches: SwitchStore.getSwitches(), 
    	temperature: SensorStore.getTemperature(), 
    	lastReadTimeElapse: SensorStore.getLastReadTimeElapse(), 
    	humidity: SensorStore.getHumidity()
    	};
    }  
     
  componentWillMount(){
	  SwitchStore.addChangeListener('STORE_SWITCH_CHANGED', this.switchChanged.bind(this));	  
	  SwitchStore.addChangeListener('STORE_SENSOR_CHANGED', this.sensorChanged.bind(this));	  
  }
 
  switchChanged(){	
	  this.toggleBlocking(false);
	  this.setState({switches: SwitchStore.getSwitches()});	 
  }   

  sensorChanged(){
	  this.setState({
		  temperature: SensorStore.getTemperature(), 
		  lastReadTimeElapse: SensorStore.getLastReadTimeElapse(), 
		  humidity: SensorStore.getHumidity()
	  })
  }
  
  toggleBlocking(block_) {	  
	    this.setState({blocking: block_});
	  }


  onButtonClick(element) {		 	 	 
	 let sendState='';
	 if(element.state=='ON'){
		 sendState='OFF';
	 }else{
		 sendState='ON';
	 }
	 this.toggleBlocking(true);
	 AppActions.switchChange({switchState: sendState, gpioNumber:element.gpioNumber});	 			 
  }
  
  
  renderSwitch(element){
	  let classNameStr="switch";
	  if(element.state=='ON'){
		  classNameStr+=" switchOn" 
	  };
	  return (
			  <tr>
			  <td className="switchText">{element.name}</td>
			  <td className="switchTd">			  	
			  		<div id={element.gpioNumber} className={classNameStr} onClick={this.onButtonClick.bind(this, element)}></div>			  		
			  </td>			  
			  </tr>
			  );
  }
  
  renderSwitches(){	  
	  let rows=[];	  	  
	  this.state.switches.forEach((element) => {
		  rows.push(this.renderSwitch(element));
	  });
	  return (
			  <table>{rows}</table>
			 );  
  }
  
  render () {	   
    return (  
    		    	
    <div className="grid grid-pad">
	
    	<div className="col-1-3">
    		<div className="content">
	    		<table>
	    			<tr><td className="header1">Temp:</td><td className="temp">{this.state.temperature} &#8451;</td></tr>
	    			<tr><td className="header1">Wilg:</td><td className="humidity">{this.state.humidity} %</td></tr>
	    			<tr><td className="header1">Odczyt:</td><td className="lastRead">{this.state.lastReadTimeElapse} sek.</td></tr>
	    		</table>
	    	</div>
	    </div>
    	<div className="col-1-3">
    		<div className="content">
    			<BlockUi tag="div" blocking={this.state.blocking}>
    				{this.renderSwitches()}  
    			</BlockUi>
    		</div>
    	</div>
    	<div className="col-1-3">
  			<div className="content">
      			<LogTable />
      		</div>
      	</div>       
    </div>  
    
    )
  }
 
}

export default Main;
