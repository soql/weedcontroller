import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';
import ConfigurationStore from '../stores/ConfigurationStore';

class ChangeDetectionStore extends EventEmitter {

    constructor() {
        super();
        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this))        
        this.state = {changeDetectionLogs: [], numberOfchangeDetectionLogs: 5};
        this._auditLogs={};
        this.userRoles=[];
        this.tick();
        
    } 
    
    startTimer () {
        clearInterval(this.timer)
        this.timer = setInterval(this.tick.bind(this), 30000)
        this.tick();
    }
    
    stopTimer(){
        clearInterval(this.timer);
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
        case 'SHOW_MORE_CHANGE_DETECTION_LOGS':
      	  this.state.numberOfchangeDetectionLogs=this.state.numberOfchangeDetectionLogs+10;
            this.tick();                
            break;     
        }    
        this.emitChange('STORE_' + action.actionType);

        return true;
      }

       
    
    tick() {    	
    	this.userRoles=ConfigurationStore.getUserRoles();    	
    	console.log(this.userRoles);
    	if(!this.userRoles || !this.userRoles.includes("ROLE_ADMIN"))
    		return;
    	axios.get('getChangeDetection?number='+this.state.numberOfchangeDetectionLogs).then(res => {
	    	this._changeDetectionLogs = res.data;
	    }).then(res => {
    	  	    	AppActions.changeDetectionLogChanged();
    	  	    });  
    	        	   
    }
                  
    getChangeDetectionLogs(){
    	  return this._changeDetectionLogs;
      }
     
}

export default new ChangeDetectionStore();
