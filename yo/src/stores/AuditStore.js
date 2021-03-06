import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';
import ConfigurationStore from '../stores/ConfigurationStore';

class AuditStore extends EventEmitter {

    constructor() {
        super();
        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this))        
        this.state = {auditLogs: [], numberOfAuditLogs: 5};
        this._auditLogs={};
        this.userRoles=[];
        this.tick();
        
    } 
    
    startTimer () {
        clearInterval(this.timer)
        this.timer = setInterval(this.tick.bind(this), 10000)
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
        case 'SHOW_MORE_AUDIT_LOGS':
      	  this.state.numberOfAuditLogs=this.state.numberOfAuditLogs+10;
            this.tick();                
            break;     
        }    
        this.emitChange('STORE_' + action.actionType);

        return true;
      }

       
    
    tick() {    	
    	this.userRoles=ConfigurationStore.getUserRoles();
    	console.log("ROLE W TICK");
    	console.log(this.userRoles);
    	if(!this.userRoles || !this.userRoles.includes("ROLE_ADMIN"))
    		return;
    	axios.get('getAuditLogs?number='+this.state.numberOfAuditLogs).then(res => {
	    	this._auditLogs = res.data;
	    }).then(res => {
    	  	    	AppActions.auditLogChanged();
    	  	    });  
    	        	   
    }
                  
      getAuditLogs(){
    	  return this._auditLogs;
      }
     
}

export default new AuditStore();
