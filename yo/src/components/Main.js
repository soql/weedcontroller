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
import ConfigurationStore from '../stores/ConfigurationStore';

class Main extends React.Component {
  constructor (props) {
    super(props)
    this.state = {blocking: false, 
    	switches: SwitchStore.getSwitches(), 
    	sensors: SensorStore.getSensors(),
    	actualPhase: ConfigurationStore.getActualPhase()    	
    	};
    }  
     
  componentWillMount(){
	  SwitchStore.startTimer();
	  ConfigurationStore.startTimer();
	  SwitchStore.addChangeListener('STORE_SWITCH_CHANGED', this.switchChanged.bind(this));	  
	  SwitchStore.addChangeListener('STORE_SENSOR_CHANGED', this.sensorChanged.bind(this));	  	  
	  ConfigurationStore.addChangeListener('STORE_ACTUAL_PHASE_CHANGED', this.actualPhaseChaged.bind(this));
	  
	  SwitchStore.tick();
	  ConfigurationStore.tick();
  }
 
  componentWillUnmount(){
	  SwitchStore.stopTimer();
  }
  switchChanged(){	
	  this.toggleBlocking(false);
	  this.setState({switches: SwitchStore.getSwitches()});	 
  }   
  
  actualPhaseChaged(){
	  console.log("actualPhaseChaged "+ConfigurationStore.getActualPhase());
	this.setState({actualPhase: ConfigurationStore.getActualPhase()});  
  }
  
  sensorChanged(){
	  this.setState({
		  sensors: SensorStore.getSensors(), 		
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
	 AppActions.switchChange({switchState: sendState, switchName:element.name});	 			 
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
			  		<div id={element.name} className={classNameStr} onClick={this.onButtonClick.bind(this, element)}></div>			  		
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
			  <div>				  
				  <table>
				  <tr><td className="phaseText" colSpan="2">{this.state.actualPhase}</td></tr>
				  	{rows}
				  </table>
			  </div>
			 );  
  }
  
  renderTable(element){
	  return (
	  <table>
	  <tr><td colSpan="2" className="sensorName">{element.sensorName}</td></tr>
		<tr><td className="header1">Temp:</td><td className="temp">{element.temperature} &#8451;</td></tr>
		<tr><td className="header1">Wilg:</td><td className="humidity">{element.humidity} %</td></tr>
		<tr><td className="header1">Odczyt:</td><td className="lastRead">{element.lastReadTimeElapse} sek.</td></tr>
	</table>)
  }
  
  render () {	   
	  let rows=[];	  	  
	  this.state.sensors.forEach((element) => {
		  rows.push(this.renderTable(element));
	  });
	return (  
    		    	
    <div className="grid grid-pad">	
    	<div className="col-1-3">
    		<div className="content">
	    		{rows}
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
