require('normalize.css/normalize.css');
require('styles/App.css');

import React from 'react';
import axios from 'axios';

class LogTable extends React.Component {
  constructor (props) {
    super(props) ;
    this.state = {logs: []};
  }
  
  getLogs(){
	  axios.get('getLogs').then(res => {
	    	this.setLogs(res.data);
	    });
  }
  
  setLogs(logs){
	  this.setState({logs: logs});
  }
  
  componentWillMount(){
	  this.getLogs();
  }
  renderLog(element){
  	return (
  			<tr className="logTableTr">  			
  			<td className="logTableTd">{element.switchName}</td>
  			<td className="logTableTd">{element.switchState}</td>
  			<td className="logTableTd">{element.date}</td>
  			<td className="logTableTd">{element.userName}</td>
  			</tr>);
  }
  
  renderLogs(){
	  let rows=[];	  	  
	  this.state.logs.forEach((element) => {
		  rows.push(this.renderLog(element));				  
	  });
	  return (
			  <table className="logTable">{rows}</table>
			 );   	    	  
  }
  
  render(){
	  return this.renderLogs()      	
  }
 
}

export default LogTable;
