import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';
import ConfigurationStore from '../stores/ConfigurationStore';

class LogStore extends EventEmitter {

    constructor() {
        super();
        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this))        
        this.state = {logs: [], numberOfLogs: 5};
        this.tick();
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
        switch (action.actionType) {
        case 'SWITCH_CHANGED':
        case 'SWITCHES_SWITCH_LOG_CHANGE':
            this.tick();                
            break;   
        case 'SHOW_MORE_LOGS':
      	  this.state.numberOfLogs=this.state.numberOfLogs+10;
            this.tick();                
            break;     
        }    
        this.emitChange('STORE_' + action.actionType);

        return true;
      }

       
    
    tick() {
    	let switchesArray=ConfigurationStore.getSwitchesConfiguration();
    	if(!switchesArray){
    		return;
    	}
    	let switchesNames=[];
    	switchesArray.forEach(e => {
    		if(e.activeLog)
    			switchesNames.push(e.name)
    		});
    	console.log("MAMY "+switchesNames)
    	axios.get('getLogs?number='+this.state.numberOfLogs+'&switches='+switchesNames).then(res => {
	    	this._logs = res.data;
	    }).then(res => {
    	  	    	AppActions.logChanged();
    	  	    });  
    	        	   
    }
                  
      getLogs(){
    	  return this._logs;
      }
     
}

export default new LogStore();