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
	    	weeks: [
	    			{label: '1', value: 1},
	    			{label: '2', value: 2}
	    			]
	    	};
	 }  	    
	     
	  componentWillMount(){
		
		  
	  }
	  componentWillUnmount(){
		  
	  }
	  
	  renderOneRow(element){
			return (
		  			<tr className="logTableTr">  			
		  			<td className="logTableTd">{element.bioBizzName}</td>
		  			<td className="logTableTd">{element.quantity} ml</td>		  			
		  			</tr>);
		}
	  
	  renderBioBizzTable(){
		  let rows=[];	  	  
		  this.state.bioBizzData.forEach((element) => {
			  rows.push(this.renderOneRow(element));				  
		  });		  
		  
		  return (
				  this.state.bioBizzData.length>0 && 
				  <table className="logTable">
				  <tr className="logTableTr">  			
		  			<th className="logTableTd">BioBizz</th>
		  			<th className="logTableTd">Ilość na 1L</th>
		  			</tr>
				    {rows}				    
				  </table>
				 ); 
	}
	render(){
	
		return (
				<div className="grid grid-pad">
		        	<div className="col-1-1">
		        		<div className="content">
		        		<table>
		        			<tr>
		        				<td>
		        					<Input type="select" options={this.state.weeks}/>
					        	</td>
					        </tr><tr>
					        	<td>
					        			        		  
					        	</td>
			        		  </tr>
			        		  </table>		        		 
		        		</div>
		        	</div>
		        	<div>
		        		{this.renderBioBizzTable()}
		        	</div>
	        	</div>
				);
	}
}
export default BioBizz;