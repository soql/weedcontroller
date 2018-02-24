require('bootstrap/dist/css/bootstrap.css');
require('normalize.css/normalize.css');
require('styles/App.css');

import React from 'react';
import axios from 'axios';
import {Button} from 'reactstrap';
import AppActions from '../actions/AppActions';
import AuditStore from '../stores/AuditStore';

class AuditTable extends React.Component {
  constructor (props) {
    super(props) ;
    this.state = {auditLogs: []};
  }
     
  componentWillMount(){
	  AuditStore.addChangeListener('STORE_AUDIT_LOG_CHANGED', this.logChanged.bind(this));	  
	  AuditStore.startTimer();
  }
  componentWillUnmount(){
	  AuditStore.stopTimer();
  }
  
  logChanged(){		  
	  this.setState({auditLogs: AuditStore.getAuditLogs()});	 
  } 
  renderLog(element){
  	return (
  			<tr className="auditLogTableTr">  			
  			<td className="auditLogTdLogin">{element.userName}</td>
  			<td className="auditLogTdDate">{element.date}</td>
  			<td className="auditLogTdDate">{element.auditOperation}</td>
  			</tr>);
  }
  
  renderAudit(){
	  
	  let rows=[];	  	  
	  this.state.auditLogs.forEach((element) => {
		  rows.push(this.renderLog(element));				  
	  });
	  return (
			  <table className="auditLogTable">
			  <tr className="auditLogTableTr">  			
	  			<td className="auditLogTdHeader">Login</td>
	  			<td className="auditLogTdHeader">Data</td>
	  			<td className="auditLogTdHeader">Operacja</td>
	  			</tr>
			    {rows}
			  	<tr><td colSpan="4"><Button color="primary" size="1g" block onClick={this.showMoreLogs.bind(this)}>...</Button></td></tr>
			  </table>
			 );   	    	  
  }
  showMoreLogs(){
	  AppActions.showMoreAuditLogs();
  }
  
  render(){
	  return this.renderAudit()      	
  }
  
 
}

export default AuditTable;
