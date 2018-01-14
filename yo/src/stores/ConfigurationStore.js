import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';

class ConfigurationStore extends EventEmitter {
	 constructor() {
	        super();
	        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this));
	        this.getConfigAsDate("START_DATE");  
	        this.readActualPhase();
	        this.readSwitchesConfiguration();
	    }    
	 startTimer () {
	        clearInterval(this.timer)
	        this.timer = setInterval(this.tick.bind(this), 10000)
	        this.tick();
	      }
	 tick(){ 
		 this.readActualPhase();		  	
	    }
	  emitChange(eventName) {
	        this.emit(eventName);
	    }

	    addChangeListener(eventName, callback) {
	        this.on(eventName, callback);
	    }

	    removeChangeListener(eventName, callback) {
	        this.removeListener(eventName, callback);
	    }
	    
	    dispatcherCallback(action) {
	    	console.log("dipatcher "+action.actionType);        
	        this.emitChange('STORE_' + action.actionType);
	        return true;
	      } 
	    getConfigAsDate(name){
	    	 axios.get('getConfigurationAsDate?key='+name).then(res => {
	   	    	this.startDate = res.data;
	   	    }).then(res => {
	  	    	AppActions.startDateReaded();
	  	    }); 	    	
	    }
	    readActualPhase(){
	    	 axios.get('getActualPhase').then(res => {
	   	    	this.actualPhase = res.data;
	   	    }).then(res => {
	  	    	AppActions.actualPhaseChanged();
	  	    }); 	    	
	    }
	    readSwitchesConfiguration(){
	    	 axios.get('getSwitchesConfiguration').then(res => {
	   	    	this.switchesConf = res.data;
	   	    }).then(res => {
	  	    	AppActions.switchesConfChanged();
	  	    }); 	    	
	    }
	    getStartDate(){
	    	return this.startDate;
	    }
	    getActualPhase(){
	    	return this.actualPhase;
	    }
	    getSwitchesConfiguration(){
	    	return this.switchesConf;
	    }
	   
}
export default new ConfigurationStore();