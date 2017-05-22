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
 
class ChartGenerator extends React.Component {	
	constructor (props) {
	    super(props)
	    this.state = {dateFrom: new Date()-172800000, dateTo: new Date()-0, image: null}	    
	    }  
	dateFromChange(moment){
		this.setState({dateFrom: moment.format("x")})
	}
	dateToChange(moment){		
		this.setState({dateTo: moment.format("x")})
	}
	generateChart(){
		axios.get('generateChart?dateFrom='+this.state.dateFrom+"&dateTo="+this.state.dateTo).then(res => {
	    	this.setState({image: res.data});
	    });
	}
  render () {
    return (
    	<div className="grid grid-pad">
        	<div className="col-1-1">
        		<div className="content">
        		<table><tr><td>
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
	        		  
	        		  </td></tr>
	        		  <tr><td>
	        		  <Button color="primary" block onClick={this.generateChart.bind(this)}>Generuj</Button>
	        		  </td></tr>
	        		  <tr><td>	        		  
	        		  <img className='image-preview' src={this.state.image} />
	        		  </td></tr>
	        		  </table>
	        		  
        		</div>
        	</div>
        	      
        </div>  
    	  
    	    	
    	    	
    		   
    		  
        		
        	
    );
  }
}
 
export default ChartGenerator;
