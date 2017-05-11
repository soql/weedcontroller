import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';

class SensorStore extends EventEmitter {

    constructor() {
        super();
        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this))        
        this._temperature = 0;
        this._humidity = 0;
        this._lastReadTimeElapse=0;
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
    	    	this._temperature=res.data.temperature;
    	    	this._humidity=res.data.humidity;
    	    	this._lastReadTimeElapse=res.data.lastReadTimeElapse;    	    	
    	    	}).then(res => {
    	  	    	AppActions.sensorChanged();
    	  	    });;  
    	        	   
    }
                  
      getTemperature(){
    	  return this._temperature;
      }
      getHumidity(){
    	  return this._humidity;
      }
      getLastReadTimeElapse(){
    	  return this._lastReadTimeElapse;
      }
}

export default new SensorStore();