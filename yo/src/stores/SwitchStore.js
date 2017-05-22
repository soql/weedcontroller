import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';

class SwitchStore extends EventEmitter {

    constructor() {
        super();
        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this))
        this._switches = [];
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
    
    switchChange(element){
    	console.log("Zmiana przelacznika "+element.gpioNumber+" na "+element.switchState);
    	axios.get('setState?switchNumber='+element.gpioNumber+'&switchState='+element.switchState).then(response =>
    	{
    		this.tick();
    	});
    }
      dispatcherCallback(action) {
    	console.log("dipatcher "+action.actionType);
        switch (action.actionType) {
            case 'SWITCH_CHANGE':
                this.switchChange(action.value);                
                break;           
        }

        this.emitChange('STORE_' + action.actionType);

        return true;
    }
    
    tick(){
  	  console.log('tick switches');
  	  axios.get('getSwitches').then(res => {
  	    	this._switches = res.data;
  	    }).then(res => {
  	    	AppActions.switchChanged();
  	    });
    }
    startTimer () {
        clearInterval(this.timer)
        this.timer = setInterval(this.tick.bind(this), 5000)
      }
          
      getSwitches(){
    	  return this._switches;
      }  
      stopTimer(){
           clearInterval(this.timer);
       }
}

export default new SwitchStore();