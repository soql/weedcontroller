require('bootstrap/dist/css/bootstrap.css');
require('normalize.css/normalize.css');
require('styles/App.css');

require('styles/simplegrid.css');
import axios from 'axios';
import React from 'react';
import LiveViewStore from'../stores/LiveViewStore';
 
class LiveView extends React.Component {	
	constructor (props) {
	    super(props)
	    this.state = {image: []}	    
	    }  
	componentWillMount(){
		  LiveViewStore.addChangeListener('STORE_IMAGE_LOADED', this.imageLoaded.bind(this));
		  LiveViewStore.startTimer();
	}		 
	 
	componentWillUnmount(){
		  LiveViewStore.stopTimer();
	  }
	
	  imageLoaded(){
		  this.setState({image: LiveViewStore.getImage()});
	  }
	  renderImage(element){
		  
	  return (
		  <div className="content">        			
			<img className='image-preview' src={element} width="100%"/>		 
		</div>)
	  }
	  
	  
  render () {
	let rows=[];	  	  
	this.state.image.forEach((element) => {		
	  rows.push(this.renderImage(element));				  
	});
    return (
    	<div className="grid grid-pad">
        	<div className="col-1-1">        		
        		{rows}
        	</div>       	      
        </div>      		       	
    );
  }
}
 
export default LiveView;
