import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';

class ConfigurationStore extends EventEmitter {
	 constructor() {
	        super();
	        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this));
	        this.getConfigAsDate("START_DATE");          	  	
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
	   	    });	    	
	    }
	    getStartDate(){
	    	return this.startDate;
	    }
	   
}
export default new ConfigurationStore();