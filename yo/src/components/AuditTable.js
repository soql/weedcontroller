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
  }
  
  logChanged(){		  
	  this.setState({auditLogs: AuditStore.getAuditLogs()});	 
  } 
  renderLog(element){
  	return (
  			<tr className="auditLogTableTr">  			
  			<td className="auditLogTd">{element.userName}</td>
  			<td className="auditLogTd">{element.date}</td>
  			</tr>);
  }
  
  renderAudit(){
	  let rows=[];	  	  
	  this.state.auditLogs.forEach((element) => {
		  rows.push(this.renderLog(element));				  
	  });
	  return (
			  <table className="auditLogTable">
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
