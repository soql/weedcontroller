require('normalize.css/normalize.css');
require('styles/App.css');

import React from 'react';
import axios from 'axios';
import BlockUi from 'react-block-ui';
import {Button} from 'reactstrap';
import 'react-block-ui/style.css';

class Main extends React.Component {
  constructor (props) {
    super(props)
    this.state = {temperature:0, humidity:0, lastReadTimeElapse: 0,blocking: false, switches: []}
    this.toggleBlocking = this.toggleBlocking.bind(this);
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
  
  toggleBlocking() {	  
	    this.setState({blocking: !this.state.blocking});
	  }
  componentWillMount(){
	  this.tick();
	  this.startTimer();
  }
  sleep(milliseconds) {
	  var start = new Date().getTime();
	  for (var i = 0; i < 1e7; i++) {
	    if ((new Date().getTime() - start) > milliseconds){
	      break;
	    }
	  }
  }
  onButtonClick(element) {	  
	 this.toggleBlocking();
	 console.log('blokuje '+this.state.blocking);
	 this.sleep(2000);
	  let sendState='';
	 if(element.state=='ON'){
		 sendState='OFF';
	 }else{
		 sendState='ON';
	 }
	 console.log('clicked '+element.gpioNumber+" "+element.state);
	 axios.get('setState?switchNumber='+element.gpioNumber+'&switchState='+sendState);
	 this.tickSwitches();
	 console.log('Odblokuje');
	 this.toggleBlocking();
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
			  <td>			  	
			  		<div id={element.gpioNumber} className={classNameStr} onClick={this.onButtonClick.bind(this, element)}></div>			  		
			  </td>			  
			  </tr>
			  );
  }
  
  renderSwitches(){
	  console.log('renderSwitches');
	  let rows=[];	  
	  this.state.switches=[{gpioNumber: 11, name: "LAMPA", state: "ON"}];
	  this.state.switches.forEach((element) => {
		  rows.push(this.renderSwitch(element));
	  });
	  return (
			  <table>{rows}</table>
			 );  
  }
  
  render () {
    return (
    		
    <div>
      <table>
        <tr><td className="header1">Temp:</td><td className="temp">{this.state.temperature} &#8451;</td></tr>
        <tr><td className="header1">Wilg:</td><td className="humidity">{this.state.humidity} %</td></tr>
        <tr><td className="header1">Odczyt:</td><td className="lastRead">{this.state.lastReadTimeElapse} sek.</td></tr>
      </table>
      <br/>
      <br/>
      <div>
      <BlockUi tag="div" blocking={this.state.blocking}>
        {this.renderSwitches()}
      </BlockUi>
      <Button onClick={this.toggleBlocking()} color="primary">Toggle Block</Button>
      </div>
    </div>     
    
    )
  }
 
}

export default Main;
