import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';

class LiveViewStore extends EventEmitter {

    constructor() {
        super();
        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this));
        this._image = null;          	  	
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
    
    tick(){ 
    	console.log("Get last foto");
  	  axios.get('getLastFoto').then(res => {
  	    	this._image = res.data;
  	    }).then(res => {
  	    	AppActions.imageLoaded();
  	    });
    }
    startTimer () {
        clearInterval(this.timer)
        this.timer = setInterval(this.tick.bind(this), 10000)
        this.tick();
      }
          
   
      stopTimer(){
           clearInterval(this.timer);
       }
      getImage(){
    	  return this._image;
      }
}

export default new LiveViewStore();