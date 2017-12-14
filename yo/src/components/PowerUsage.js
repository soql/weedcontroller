require('bootstrap/dist/css/bootstrap.css');
require('normalize.css/normalize.css');
require('styles/App.css');
require('react-date-picker/index.css');
require('react-date-picker/base.css');

require('styles/simplegrid.css');
import axios from 'axios';
import React from 'react';

import { DateField, DatePicker, Calendar } from 'react-date-picker'
import 'react-date-picker/index.css'
import {Button} from 'reactstrap';
import ConfigurationStore from '../stores/ConfigurationStore';
 
class PowerUsage extends React.Component {	
	constructor (props) {
	    super(props)
	    this.state = {dateFrom: ConfigurationStore.getStartDate(), dateTo: new Date()-0, powerUsageResult: []}	   
	    
	    
	}  
	calculatePowerUsage(){
		axios.get('calculatePowerUsage?dateFrom='+this.state.dateFrom+"&dateTo="+this.state.dateTo).then(res => {
	    	this.setState({powerUsageResult: res.data});
	    });
	}
	dateFromChange(moment){
		this.setState({dateFrom: moment.format("x")})
	}
	
	dateToChange(moment){
		this.setState({dateTo: moment.format("x")})
	}
	renderOneRow(element){
		return (
	  			<tr className="logTableTr">  			
	  			<td className="logTableTd">{element.switchName}</td>
	  			<td className="logTableTd">{element.powerUsage}</td>
	  			<td className="logTableTd">{element.powerOnTime}</td>
	  			<td className="logTableTd">{element.maxTime}</td>	  			
	  			</tr>);
	}
	
	renderPowerUsageTable(){
		  let rows=[];	  	  
		  this.state.powerUsageResult.forEach((element) => {
			  rows.push(this.renderOneRow(element));				  
		  });
		  return (
				  <table className="logTable">
				  <tr className="logTableTr">  			
		  			<th className="logTableTd">Urządzenie</th>
		  			<th className="logTableTd">Zużycie (W)</th>
		  			<th className="logTableTd">Włączony (h)</th>
		  			<th className="logTableTd">Max (h)</th>		  				  		
		  			</tr>
				    {rows}				  	
				  </table>
				 ); 
	}
  render () {
    return (
    	<div className="grid grid-pad">
        	<div className="col-1-1">
        		<div className="content">
        		<table>
        			<tr>
        				<td>
			        		<DateField
			        		  dateFormat="YYYY-MM-DD HH:mm:ss"
			        		  forceValidDate={true}
			        		  defaultValue={this.state.dateFrom}
			        		  onChange={(dateString, {dateMoment, timestamp}) => {this.dateFromChange(dateMoment)}}>	        		
			        		  <DatePicker
			        		    navigation={true}
			        		    locale="en"
			        		    forceValidDate={true}
			        		    highlightWeekends={true}
			        		    highlightToday={true}
			        		    weekNumbers={false}
			        		    weekStartDay={1}
			        		  />
			        		  
			        		</DateField>
			        		  </td></tr><tr><td>
			        		  <DateField
			        		  dateFormat="YYYY-MM-DD HH:mm:ss"
			        		  forceValidDate={true}
			        		  defaultValue={this.state.dateTo}
			        		  onChange={(dateString, {dateMoment, timestamp}) => {this.dateToChange(dateMoment)}}>	        		
			        		  <DatePicker
			        		    navigation={true}
			        		    locale="en"
			        		    forceValidDate={true}
			        		    highlightWeekends={true}
			        		    highlightToday={true}
			        		    weekNumbers={false}
			        		    weekStartDay={1}
			        		  />
			        		</DateField>	        		  
			        		  </td>
	        		  </tr>
	        		  </table>
	        		  <table>
	        		  	<tr><td>
	        		  		<Button color="primary" block onClick={this.calculatePowerUsage.bind(this)}>Oblicz</Button>
	        		  	</td></tr>
	        		  <tr><td>	        		  
	        		  <img className='image-preview' src={this.state.image} />
	        		  </td></tr>
	        		  </table>
	        		  
        		</div>
        	</div>
        	<div>
        		{this.renderPowerUsageTable()}
        	</div>
        </div>  
    	  
    	    	
    	    	
    		   
    		  
        		
        	
    );
  }
}
 
export default PowerUsage;
