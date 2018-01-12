import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';

class GpioManagerStore extends EventEmitter {

    constructor() {
        super();
        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this))
        this.managedSwitches = [];
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
    managedSwitchChange(element){
    	console.log("Zmiana gpio przelacznika "+element.gpioNumber+" na "+element.gpioState);
    	axios.get('setManagedState?gpioNumber='+element.gpioNumber+'&gpioState='+element.gpioState).then(response =>
    	{
    		this.tick();
    	});
    }
    
      dispatcherCallback(action) {
    	console.log("dipatcher "+action.actionType); 
    	 switch (action.actionType) {
         case 'MANAGED_SWITCH_CHANGE':
             this.managedSwitchChange(action.value);                
             break;           
     }

        this.emitChange('STORE_' + action.actionType);

        return true;
    }
    
    tick(){
  	  console.log('gpio manager tick switches');  	  
  	  axios.get('getManagedSwitches').then(res => {
  		  this.managedSwitches = res.data;  		  
  	  }).then(res => {
	    	AppActions.managedSwitchesChanged();
	    });
    }
    startTimer () {
        clearInterval(this.timer)
        this.timer = setInterval(this.tick.bind(this), 5000)        
      }
          
      getManagedSwitches(){
    	  return this.managedSwitches;
      }  
      stopTimer(){
           clearInterval(this.timer);
       }
}

export default new GpioManagerStore();