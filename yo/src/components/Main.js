require('normalize.css/normalize.css');
require('styles/App.css');
require('styles/simplegrid.css');

import React from 'react';
import axios from 'axios';
import BlockUi from 'react-block-ui';
import {Button} from 'reactstrap';
import 'react-block-ui/style.css';
import LogTable from './LogTable';

class Main extends React.Component {
  constructor (props) {
    super(props)
    this.state = {temperature:0, humidity:0, lastReadTimeElapse: 0,blocking: false, switches: []}    
  }
  componentWillUnmount () {
    clearInterval(this.timer)
  }
  tick () {
    axios.get('tempAndHumidity').then(res => { 
    	const temperature=res.data.temperature;
    	const humiditiy=res.data.humidity;
    	const lastReadTimeElapse=res.data.lastReadTimeElapse;
    	this.setTemperature(temperature);
    	this.setHumidity(humiditiy);
    	this.setLastReadTimeElapse(lastReadTimeElapse);
    	});  
    
    this.tickSwitches();
  }
  
  tickSwitches(){
	  console.log('tick switches');
	  axios.get('getSwitches').then(res => {
	    	this.setSwitches(res.data);
	    });
  }
  
  startTimer () {
    clearInterval(this.timer)
    this.timer = setInterval(this.tick.bind(this), 5000)
  }
  
  stopTimer () {
    clearInterval(this.timer)
  }
  
  setTemperature(temperature){
	  this.setState({temperature: temperature});
  }
  
  setHumidity(humidity){
	  this.setState({humidity: humidity});
  }  
  setLastReadTimeElapse(lastReadTimeElapse){
	  this.setState({lastReadTimeElapse: lastReadTimeElapse});
  }
  setSwitches(switches){
	  this.setState({switches: switches});
  }
  
  toggleBlocking(block_) {	  
	    this.setState({blocking: block_});
	  }
  componentWillMount(){
	  this.tick();
	  this.startTimer();
  }

  onButtonClick(element) {		 
	 console.log('blokuje '+this.state.blocking);
	 this.toggleBlocking(true);
	 let sendState='';
	 if(element.state=='ON'){
		 sendState='OFF';
	 }else{
		 sendState='ON';
	 }
	 console.log('clicked '+element.gpioNumber+" "+element.state);
	 axios.get('setState?switchNumber='+element.gpioNumber+'&switchState='+sendState).then(response => 
	 {
		  this.tickSwitches()
	 }).then(response => {
		 this.toggleBlocking(false);
	 });
	 
	 console.log('Odblokuje');	 
	 console.log('Odblokuje '+this.state.blocking);
  }
  
  
  renderSwitch(element){
	  let classNameStr="switch";
	  if(element.state=='ON'){
		  classNameStr+=" switchOn" 
	  };
	  return (
			  <tr>
			  <td className="switchText">{element.name}</td>
			  <td className="switchTd">			  	
			  		<div id={element.gpioNumber} className={classNameStr} onClick={this.onButtonClick.bind(this, element)}></div>			  		
			  </td>			  
			  </tr>
			  );
  }
  
  renderSwitches(){	  
	  let rows=[];	  	  
	  this.state.switches.forEach((element) => {
		  rows.push(this.renderSwitch(element));
	  });
	  return (
			  <table>{rows}</table>
			 );  
  }
  
  render () {
    return (    		
    <div className="grid grid-pad">
    	<div className="col-1-3">
    		<div className="content">
	    		<table>
	    			<tr><td className="header1">Temp:</td><td className="temp">{this.state.temperature} &#8451;</td></tr>
	    			<tr><td className="header1">Wilg:</td><td className="humidity">{this.state.humidity} %</td></tr>
	    			<tr><td className="header1">Odczyt:</td><td className="lastRead">{this.state.lastReadTimeElapse} sek.</td></tr>
	    		</table>
	    	</div>
	    </div>
    	<div className="col-1-3">
    		<div className="content">
    			<BlockUi tag="div" blocking={this.state.blocking}>
    				{this.renderSwitches()}  
    			</BlockUi>
    		</div>
    	</div>
    	<div className="col-1-3">
  			<div className="content">
      			<LogTable />
      		</div>
      	</div>       
    </div>        
    )
  }
 
}

export default Main;
