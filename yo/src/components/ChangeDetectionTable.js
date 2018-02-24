require('bootstrap/dist/css/bootstrap.css');
require('normalize.css/normalize.css');
require('styles/App.css');

import React from 'react';
import axios from 'axios';
import {Button} from 'reactstrap';
import AppActions from '../actions/AppActions';
import ChangeDetectionStore from '../stores/ChangeDetectionStore';

class ChangeDetectionTable extends React.Component {
  constructor (props) {
    super(props) ;
    this.state = {changeDetectionLogs: []};
  }
     
  componentWillMount(){
	  ChangeDetectionStore.addChangeListener('STORE_CHANGE_DETECTION_LOG_CHANGED', this.logChanged.bind(this));	  
	  ChangeDetectionStore.startTimer();
  }
  componentWillUnmount(){
	  ChangeDetectionStore.stopTimer();
  }
  
  logChanged(){		  
	  this.setState({changeDetectionLogs: ChangeDetectionStore.getChangeDetectionLogs()});	 
  } 
  renderLog(element){
  	return (
  			<tr className="auditLogTableTr">  			
  			<td className="auditLogTdLogin">{element.sensorName}</td>
  			<td className="auditLogTdDate">{element.date}</td>  			
  			</tr>);
  }
  
  renderChangeDetection(){
	  
	  let rows=[];	  	  
	  this.state.changeDetectionLogs.forEach((element) => {
		  rows.push(this.renderLog(element));				  
	  });
	  return (
			  <table className="auditLogTable">
			  <tr className="auditLogTableTr">  			
	  			<td className="auditLogTdHeader">Data</td>
	  			<td className="auditLogTdHeader">Sensor</td>	  			
	  			</tr>
			    {rows}
			  	<tr><td colSpan="4"><Button color="primary" size="1g" block onClick={this.showMoreLogs.bind(this)}>...</Button></td></tr>
			  </table>
			 );   	    	  
  }
  showMoreLogs(){
	  AppActions.showMoreChangeDetectionLogs();
  }
  
  render(){
	  return this.renderChangeDetection()      	
  }
  
 
}

export default ChangeDetectionTable;
