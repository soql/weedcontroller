INSERT INTO `Rule` (`id`,`active`,`condition_`,`expression_`,`nextTimeExecution`,`sms`,`phase_id`) VALUES (39,1,'SOIL_DET_TO_SEND!=null','r.sendSMS(\"Wykryto podlanie. Sensor: \"+SOIL_DET_TO_SEND.getSensor().getName()+\". Czas: \"+SOIL_DET_TO_SEND.getDate()); r.sCDN(SOIL_DET_TO_SEND);',NULL,NULL,NULL);