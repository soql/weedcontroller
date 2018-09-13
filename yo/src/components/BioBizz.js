require('bootstrap/dist/css/bootstrap.css');
require('normalize.css/normalize.css');
require('styles/App.css');
require('react-date-picker/index.css');
require('react-date-picker/base.css');

require('styles/simplegrid.css');
import axios from 'axios';
import React from 'react';
import BlockUi from 'react-block-ui';
import AppActions from '../actions/AppActions';
import BioBizzStore from '../stores/BioBizzStore';
import {Button, Input, Select} from 'reactstrap';
 
class BioBizz extends React.Component {	
	constructor (props) {
	    super(props)
	    this.state = {
	    	bioBizzData: BioBizzStore.getBioBizz(),	    	
	    	weeks: [{'nr':'1'},{'nr':'2'},{'nr':'3'},{'nr':'4'},{'nr':'5'},{'nr':'6'},{'nr':'7'},{'nr':'8'},{'nr':'9'},{'nr':'10'}]
	    	};
	 }  	    
	     
	  componentWillMount(){
		
		  
	  }
	  componentWillUnmount(){
		  
	  }
	  
	  renderOneRow(key, element){
			return (
		  			<tr className="logTableTr">  			
		  			<td className="logTableTd">{key}</td>
		  			<td className="logTableTd">{element} ml</td>		  			
		  			</tr>);
		}
	  
	  renderBioBizzTable(){
		  let rows=[];	  	  
		  console.log(this.state.bioBizzData.bioBizzData);
		  Object.keys(this.state.bioBizzData.bioBizzData).forEach((key) => {
			  console.log(key);
			  console.log(this.state.bioBizzData.bioBizzData[key]);
			  rows.push(this.renderOneRow(key, this.state.bioBizzData.bioBizzData[key]));				  
		  });		  
		  
		  return (				  
				  <table className="logTable">
				  <tr className="logTableTr">  			
		  			<th className="logTableTd">BioBizz</th>
		  			<th className="logTableTd">Ilość na 1L</th>
		  			</tr>
				    {rows}				    
				  </table>
				 ); 
	}
	  createWeeksOptions() {
		     let items = [];         
		     this.state.weeks.map((e, key) => {             
		          items.push(<option key={e.nr} value={e.nr}>{e.nr}</option>);
		     });
		     return items;
		 }    
	  createPhaseOptions() {
		     let items = [];         
		     !!this.state.bioBizzData && !!this.state.bioBizzData.phases && this.state.bioBizzData.phases.map((e, key) => {             
		          items.push(<option key={e.id} value={e.id}>{e.name}</option>);
		     });
		     return items;
		 }  
	render(){
	
		return (				
				<div className="grid grid-pad">
		        	<div className="col-1-1">
		        		<div className="content">
		        		<table>
		        			<tr>
		        				<td className=".switchText_log_on">
		        					Tydzień: 
        						</td>
		        				<td>
		        					<Input type="select" label="Tydzień: ">
		        						{this.createWeeksOptions()}
		        					</Input>
					        	</td>
					        </tr>
					        <tr>
					    		<td className=".switchText_log_on">
        							Faza: 
        								</td>
					    		<td>
					    			<Input type="select" label="Faza: ">
					    				{this.createPhaseOptions()}
					    			</Input>
					    		</td>
			        		</tr>
			        		  </table>		        		 
		        		</div>
		        	</div>
		        	<div>
		        		{!!this.state.bioBizzData && !!this.state.bioBizzData.bioBizzData && this.renderBioBizzTable()}
		        	</div>
	        	</div>
				);
	}
}
export default BioBizz;