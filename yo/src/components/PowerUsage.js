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
	    this.state = {dateFrom: new Date()-0, dateTo: new Date()-0, powerUsageResult: []}	   	    	    
	}  
	
	startDateReaded(){
		console.log("DATA ODCZYTANA "+ConfigurationStore.getStartDate());
		this.setState({dateFrom: ConfigurationStore.getStartDate()});
	}
	
	componentWillMount(){
		console.log("CWM");
		ConfigurationStore.addChangeListener('STORE_START_DATE_READED', this.startDateReaded.bind(this));
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
	  			<td className="logTableTd">{element.powerUsage} W</td>
	  			<td className="logTableTd">{parseFloat(element.powerOnTime).toFixed(2)} h</td>
	  			<td className="logTableTd">{parseFloat(element.maxTime).toFixed(2)} h</td>	  			
	  			<td className="logTableTd">{parseFloat(element.cost).toFixed(2)} zł</td>
	  			</tr>);
	}
	
	renderPowerUsageTable(){
		  let rows=[];	  	  
		  this.state.powerUsageResult.forEach((element) => {
			  rows.push(this.renderOneRow(element));				  
		  });
		  let all=0;
		  this.state.powerUsageResult.forEach(ele => {all+=ele.cost});
		  return (
				  this.state.powerUsageResult.length>0 && 
				  <table className="logTable">
				  <tr className="logTableTr">  			
		  			<th className="logTableTd">Urządzenie</th>
		  			<th className="logTableTd">Zużycie</th>
		  			<th className="logTableTd">Włączony</th>
		  			<th className="logTableTd">Max</th>		  				  		
		  			<th className="logTableTd">Koszt</th>
		  			</tr>
				    {rows}
				    <tr className="logTableTr">
				    	<td className="logTableTd" colSpan="4">Razem:</td>
				    	<td className="allCostTd" colSpan="4">{parseFloat(all).toFixed(2)} zł</td>
				    </tr>
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
			        		  value={this.state.dateFrom}
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
			        		  value={this.state.dateTo}
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
