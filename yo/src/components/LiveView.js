require('bootstrap/dist/css/bootstrap.css');
require('normalize.css/normalize.css');
require('styles/App.css');

require('styles/simplegrid.css');

require('slick-carousel/slick/slick.css');
require('slick-carousel/slick/slick-theme.css');

import axios from 'axios';
import React from 'react';
import Slider from 'react-slick';
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
			  <div className="contentDiv">
				  <div className="content">    
				  <table>
					  <tr><td className="lastReadFoto">
					  	Ostatni odczyt: {element.time} sek.
					  </td></tr>
					  <tr><td>
						<img className='image-preview' src={element.foto} width="100%"/>
					  </td></tr>
				  </table>
						
				</div>
			</div>
			)
	  }
	  
	  
  render () {
	  var settings = {
		      dots: true,
		      infinite: true,
		      speed: 500,
		      slidesToShow: 1,
		      slidesToScroll: 1,
		      autoplay: false,		      
		      pauseOnHover: false
		    };
	let rows=[];	  	  
	this.state.image.forEach((element) => {		
	  rows.push(this.renderImage(element));				  
	});
    return (
    	<div className="grid grid-pad">
        	<div className="col-1-1">        		
        	  <Slider ref={c => this.slider = c } {...settings}>		        	
		        	{rows}		        			       		        	
		      </Slider>
        	</div>       	      
        </div>      		       	
    );
  }
}
 
export default LiveView;
