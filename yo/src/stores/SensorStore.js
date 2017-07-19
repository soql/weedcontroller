import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';

class SensorStore extends EventEmitter {

    constructor() {
        super();
        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this));    
        this._sensors = [];        
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
            this.tick();                
            break;                      
        }

        this.emitChange('STORE_' + action.actionType);

        return true;
    }
    tick() {
    	    axios.get('tempAndHumidity').then(res => {
    	    	 console.log(res);
    	    	console.log(res.data);
    	    	this._sensors=res.data;    	    		    	
    	    	}).then(res => {
    	  	    	AppActions.sensorChanged();
    	  	    }); 
    	        	   
    }
                  
      getSensors(){
    	  return this._sensors;
      }      
}

export default new SensorStore();