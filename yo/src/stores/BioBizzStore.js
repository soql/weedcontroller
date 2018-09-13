import AppDispatcher from '../actions/AppDispatcher';
import AppActions from '../actions/AppActions';
import axios from 'axios';
import { EventEmitter } from 'events';

class BioBizzStore extends EventEmitter {

    constructor() {
        super();
        this.dispatchToken = AppDispatcher.register(this.dispatcherCallback.bind(this))
        this.bioBizz = [];
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
     }
        this.emitChange('STORE_' + action.actionType);

        return true;
    }
    
    tick(){
  	  console.log('get Bio Bizz');  	  
  	  axios.get('getBioBizz').then(res => {
  		  this.bioBizz = res.data;  		  
  	  }).then(res => {
	    	AppActions.bioBizzLoaded();
	    });
    }    
          
      getBioBizz(){
    	  return this.bioBizz;
      }  
    
}

export default new BioBizzStore();