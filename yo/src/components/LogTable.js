require('bootstrap/dist/css/bootstrap.css');
require('normalize.css/normalize.css');
require('styles/App.css');



import React from 'react';
import axios from 'axios';
import {Button} from 'reactstrap';
import AppActions from '../actions/AppActions';
import LogStore from '../stores/LogStore';
import ConfigurationStore from '../stores/ConfigurationStore';

class LogTable extends React.Component {
  constructor (props) {
    super(props) ;
    this.state = {logs: [],
    		switchesConf: ConfigurationStore.readSwitchesConfiguration()
    		};
  }
     
  componentWillMount(){
	  LogStore.addChangeListener('STORE_LOG_CHANGED', this.logChanged.bind(this));
	  ConfigurationStore.addChangeListener('STORE_SWITCHES_CONF_CHANGED', this.switchesConfChaged.bind(this));
  }
  
  switchesConfChaged(){	  
	  this.setState({switchesConf: ConfigurationStore.getSwitchesConfiguration()}); 
  }
  
  logChanged(){		  
	  this.setState({logs: LogStore.getLogs()});	 
  } 
  renderLog(element){	  	  
	  var descr=element.switchName;
	  if(element.logType==1){
		  descr+='('+element.switchGpio+')'; 
	  }	  
	  var textColor=this.state.switchesConf.find(ele => ele.name==element.switchName).color;	  
	  var textFontWeight=element.switchState=='ON'?'bold':'normal';
  	return (
  			<tr className="logTableTr">  			
  			<td className='logTableTd' style={{color: textColor, fontWeight: textFontWeight}}>{descr}</td>
  			<td className='logTableTd' style={{color: textColor, fontWeight: textFontWeight}}>{element.switchState}</td>
  			<td className='logTableTd' style={{color: textColor, fontWeight: textFontWeight}}>{element.date}</td>
  			<td className='logTableTd' style={{color: textColor, fontWeight: textFontWeight}}>{element.userName}</td>
  			</tr>);
  }
  
  renderLogs(){
	  if(!this.state.switchesConf){
		  return (<table className="logTable">		    
		  </table>);
	  }
	  let rows=[];	  	  
	  this.state.logs.forEach((element) => {
		  rows.push(this.renderLog(element));				  
	  });
	  return (
			  <table className="logTable">
			    {rows}
			  	<tr><td colSpan="4"><Button color="primary" size="1g" block onClick={this.showMoreLogs.bind(this)}>...</Button></td></tr>
			  </table>
			 );   	    	  
  }
  showMoreLogs(){
	  AppActions.showMoreLogs();
  }
  
  render(){
	  return this.renderLogs()      	
  }
  
 
}

export default LogTable;
