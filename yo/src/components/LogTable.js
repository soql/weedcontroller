require('bootstrap/dist/css/bootstrap.css');
require('normalize.css/normalize.css');
require('styles/App.css');



import React from 'react';
import axios from 'axios';
import {Button} from 'reactstrap';
import AppActions from '../actions/AppActions';
import LogStore from '../stores/LogStore';

class LogTable extends React.Component {
  constructor (props) {
    super(props) ;
    this.state = {logs: []};
  }
     
  componentWillMount(){
	  LogStore.addChangeListener('STORE_LOG_CHANGED', this.logChanged.bind(this));
  }
  
  logChanged(){		  
	  this.setState({logs: LogStore.getLogs()});	 
  } 
  renderLog(element){
	  var styleClassName=(element.switchState=='ON'?'logTableTd-green':'logTableTd-red');
  	return (
  			<tr className="logTableTr">  			
  			<td className={styleClassName}>{element.switchName}</td>
  			<td className={styleClassName}>{element.switchState}</td>
  			<td className={styleClassName}>{element.date}</td>
  			<td className={styleClassName}>{element.userName}</td>
  			</tr>);
  }
  
  renderLogs(){
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
