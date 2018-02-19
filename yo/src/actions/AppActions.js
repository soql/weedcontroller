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
    managedSwitchChange(data){
    	AppDispatcher.dispatch({
            actionType: 'MANAGED_SWITCH_CHANGE',
            value: data
        });
    }
    managedSwitchesChanged(data){
    	  AppDispatcher.dispatch({
              actionType: 'MANAGED_SWITCHES_READED',
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
    startDateReaded(data){
    	  AppDispatcher.dispatch({
              actionType: 'START_DATE_READED',
              value: data
          });
      }
    actualPhaseChanged(data){
    	AppDispatcher.dispatch({
            actionType: 'ACTUAL_PHASE_CHANGED',
            value: data
        });
    }
    switchesConfChanged(data){
    	AppDispatcher.dispatch({
            actionType: 'SWITCHES_CONF_CHANGED',
            value: data
        });
    }
    switchesLogConfChanged(data){
    	AppDispatcher.dispatch({
            actionType: 'SWITCHES_LOG_CONF_CHANGED',
            value: data
        });
    }
    oneSwitchLogChange(data){
    	AppDispatcher.dispatch({
            actionType: 'SWITCHES_SWITCH_LOG_CHANGE',
            value: data
        });
    }
    rolesReaded(data){
    	AppDispatcher.dispatch({
            actionType: 'ROLES_READED',
            value: data
        });
    }
}

export default new AppActions() 