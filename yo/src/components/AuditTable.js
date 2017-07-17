require('bootstrap/dist/css/bootstrap.css');
require('normalize.css/normalize.css');
require('styles/App.css');

import React from 'react';
import axios from 'axios';
import {Button} from 'reactstrap';
import AppActions from '../actions/AppActions';
import LogStore from '../stores/LogStore';

class AuditTable extends React.Component {
  constructor (props) {
    super(props) ;
    this.state = {logs: []};
  }
     
  componentWillMount(){
	  AuditStore.addChangeListener('STORE_LOG_CHANGED', this.logChanged.bind(this));
  }
  
  logChanged(){		  
	  this.setState({logs: LogStore.getLogs()});	 
  } 
  renderLog(element){
  	return (
  			<tr className="AuditTableTr">  			
  			<td className="AuditTableTd">{element.name}</td>
  			<td className="AuditTableTd">{element.time}</td>
  			</tr>);
  }
  
  renderAudit(){
	  let rows=[];	  	  
	  this.state.logs.forEach((element) => {
		  rows.push(this.renderLog(element));				  
	  });
	  return (
			  <table className="AuditTable">
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

export default AuditTable;
