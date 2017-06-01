import AppDispatcher from './AppDispatcher';

class AppActions {

    switchChanged(data) {
        AppDispatcher.dispatch({
            actionType: 'SWITCH_CHANGED',
            value: data
        });
    }   
    switchChange(data) {
        AppDispatcher.dispatch({
            actionType: 'SWITCH_CHANGE',
            value: data
        });
    } 
    sensorChanged(data){
    	AppDispatcher.dispatch({
            actionType: 'SENSOR_CHANGED',
            value: data
        });
    }
    logChanged(data){
    	AppDispatcher.dispatch({
            actionType: 'LOG_CHANGED',
            value: data
        });
    }
    showMoreLogs(data){
    AppDispatcher.dispatch({
            actionType: 'SHOW_MORE_LOGS',
            value: data
        });
    }    
    
    imageLoaded(data){
    AppDispatcher.dispatch({
            actionType: 'IMAGE_LOADED',
            value: data
        });
    }    
}

export default new AppActions() 