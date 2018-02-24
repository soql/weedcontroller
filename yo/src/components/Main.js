require('normalize.css/normalize.css');
require('styles/App.css');
require('styles/burger-menu.css');
require('styles/simplegrid.css');

import React from 'react';
import BlockUi from 'react-block-ui';
import axios from 'axios';
import 'react-block-ui/style.css';

import LogTable from './LogTable';
import AuditTable from './AuditTable';
import ChangeDetectionTable from './ChangeDetectionTable';
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
    	actualPhase: ConfigurationStore.getActualPhase(),
    	switchesLogConf: ConfigurationStore.getSwitchesConfiguration(),
    	roles: ConfigurationStore.getUserRoles()
    	};
    }  
     
  componentWillMount(){
	  SwitchStore.startTimer();
	  ConfigurationStore.startTimer();
	  SwitchStore.addChangeListener('STORE_SWITCH_CHANGED', this.switchChanged.bind(this));	  
	  SwitchStore.addChangeListener('STORE_SENSOR_CHANGED', this.sensorChanged.bind(this));	  	  
	  ConfigurationStore.addChangeListener('STORE_ACTUAL_PHASE_CHANGED', this.actualPhaseChaged.bind(this));
	  ConfigurationStore.addChangeListener('STORE_SWITCHES_LOG_CONF_CHANGED', this.switchesLogConfChanged.bind(this));	  
	  ConfigurationStore.addChangeListener('STORE_SWITCHES_SWITCH_LOG_CHANGE', this.switchesLogConfChanged.bind(this));
	  ConfigurationStore.addChangeListener('STORE_ROLES_READED', this.rolesLoaded.bind(this));
	  SwitchStore.tick();
	  ConfigurationStore.tick();
  }
  switchesLogConfChanged(){
	  this.setState({switchesLogConf: 
		  ConfigurationStore.getSwitchesConfiguration()
		 }); 
  }
  rolesLoaded(){
	  console.log('Ustawiam role ');
	  console.log(ConfigurationStore.getUserRoles());
	  this.setState({roles: ConfigurationStore.getUserRoles()});
  }
  componentWillUnmount(){
	  SwitchStore.stopTimer();
  }
  switchChanged(){	
	  this.toggleBlocking(false);
	  this.setState({switches: SwitchStore.getSwitches()});	 
  }   
  
  actualPhaseChaged(){	  
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
  
  onSwitchClick(element){
	  console.log(this.state.switchesLogConf);
	  let oneSwitchLogConf=this.state.switchesLogConf.find(e => {return e.name==element.name});
	  AppActions.oneSwitchLogChange({switchName: element.name, switchLogState: !oneSwitchLogConf.activeLog});
	 
  }
  
  
  renderSwitch(element){
	  let classNameStr="switch";
	  if(element.state=='ON'){
		  classNameStr+=" switchOn" 
	  };
	  let oneSwitchLogConf=this.state.switchesLogConf.find(e => {return e.name==element.name});
	  let switchClassNameStr="switchText_log_";
	  if(oneSwitchLogConf.activeLog){
		  switchClassNameStr+="on"; 
	  }else{
		  switchClassNameStr+="off"; 
	  }
	  return (
			  <tr>
			  <td className={switchClassNameStr}>
			  	<div id={element.name+"_log"} onClick={this.onSwitchClick.bind(this,element)}>
			  		{element.name}</div></td>
			  <td className="switchTd">			  	
			  		<div id={element.name+"_switch"} className={classNameStr} onClick={this.onButtonClick.bind(this, element)}></div>			  		
			  </td>			  
			  </tr>
			  );
  }
  
  renderSwitches(){
	  if(!this.state.switchesLogConf){
		  return (<table>		    
		  </table>);
	  }
	  let rows=[];	  	  
	  this.state.switches.forEach((element) => {
		  rows.push(this.renderSwitch(element));
	  });
	  return (
			  <div>				  
				  <table className="switchTable">
				  <tr><td className="phaseText" colSpan="2">{this.state.actualPhase}</td></tr>
				  	{rows}
				  </table>
			  </div>
			 );  
  }
  renderSensorRow(element){
	  let res=element.transformedResult!=null ?element.transformedResult: element.result;
	  return (<tr><td className="header1">{element.description}: </td>
	  	<td className={element.cssName}>
	  		{parseFloat(res).toFixed(2)} 
	  	</td>
	  	<td className={element.cssName}>
	  		{element.unit}
	  	</td>
	  </tr>)	  
  }
  renderSensorRows(element){
	  let rows=[];
	  Object.keys(element).forEach((value) => {
		  console.log(value);
		rows.push(this.renderSensorRow(element[value]))  
	  })
	  return rows;
  }
  renderTable(element){
	  let rows=[];
	  rows.push(this.renderSensorRows(element.results));	  
	  return (
		  <table className="sensorTable">
		  <tr>
		  <td colSpan="2" className="sensorName">
		  	{element.sensorName}
		  </td>
		  <td className="sensorName">
		  	({element.lastReadTimeElapse})
		  </td>
		  </tr>
		  {rows}			
		</table>)
  }
  
  render () {	   
	  let rows=[];	  	  
	  this.state.sensors.forEach((element) => {
		  rows.push(this.renderTable(element));
	  });
	return (  
    	<div>	    	
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
    
    <div className="grid grid-pad">	
		<div className="col-1-3">    	
			<div className="content">
  				{this.state.roles && this.state.roles.includes('ROLE_ADMIN') && <AuditTable />}
  			</div>
  		</div>   
  		<div className="col-1-3">    	
  			<div className="content">
  				{this.state.roles && this.state.roles.includes('ROLE_ADMIN') && <ChangeDetectionTable />}
  			</div>
  		</div> 
    </div>  
         
    </div>
    )
  }
 
}

export default Main;
