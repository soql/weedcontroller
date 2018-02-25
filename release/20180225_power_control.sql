update weedcontroller.Sensor set visibleOnGui=1 where number<>5

INSERT INTO `Sensor` (`number`,`command`,`name`,`checkChanges`,`visibleOnGui`) VALUES (5,'/opt/checkPower.sh','Prąd',NULL,0);

INSERT INTO `SensorData` (`id`,`cssName`,`description`,`maxError`,`name`,`regexp_`,`unit`,`parent_number`,`transformExpression`,`maxValue_`) VALUES (7,'temp','Prąd',2,'POWER','(.*)','',5,NULL,NULL);

INSERT INTO `Rule` (`id`,`active`,`condition_`,`expression_`,`nextTimeExecution`,`sms`,`phase_id`) VALUES (37,1,'NOW_SENSORS_MAP.get(5).getResults().get(\"POWER\").getResult()==0 && PREV_SENSORS_MAP.get(5).getResults().get(\"POWER\").getResult()==1','r.sendSMS(\"Brak prądu na zalesiu.\");',NULL,NULL,NULL);
INSERT INTO `Rule` (`id`,`active`,`condition_`,`expression_`,`nextTimeExecution`,`sms`,`phase_id`) VALUES (38,1,'PREV_SENSORS_MAP.get(5).getResults().get(\"POWER\").getResult()==0 && NOW_SENSORS_MAP.get(5).getResults().get(\"POWER\").getResult()==1','r.sendSMS(\"Wznowienie dostawy prądu.\");',NULL,NULL,NULL);
