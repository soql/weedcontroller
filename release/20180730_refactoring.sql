INSERT INTO `weedcontroller`.`Configuration_` (`key_`, `value`) VALUES ('IMAGE_FOLDER', '/opt/camera');
INSERT INTO `weedcontroller`.`Configuration_` (`key_`, `value`) VALUES ('MQTT_ADDRESS', 'tcp://zjc.oth.net.pl:1883');

INSERT INTO `SwitchGpioLog` (`id`,`date`,`ruleUser`,`state`,`switchGpio_gpioNumber`,`user_login`) VALUES (1,'2018-04-01 00:00:00','INiT',1,28,NULL);
INSERT INTO `SwitchGpioLog` (`id`,`date`,`ruleUser`,`state`,`switchGpio_gpioNumber`,`user_login`) VALUES (2,'2018-04-01 00:00:00','INIT',0,23,NULL);

DELETE FROM weedcontroller.SensorResultLog where sensor_number in (3,4);
drop table SensorResultLog;
/*Po deploy*/
update Rule set expression_='r.sendSMS("Dzien:"+r.getNumberOfDays()+" Temperatura "+TEMP+" Wilgotnosc "+HUMI+" Lampa: "+r.css("LAMPA")+" Wiatraki "+r.css("WIATRAKI")+" Grzejnik "+r.css("GRZEJNIK")+" Temp(śr/min/max): "+r.gavAS("TEMPERATURE","avg",24*60)+"/"+r.gavAS("TEMPERATURE","min",24*60)+"/"+r.gavAS("TEMPERATURE","max",24*60)+" Wilgotnosc(sr/min/max): "+r.gavAS("HUMIDITY","avg",24*60)+"/"+r.gavAS("HUMIDITY","min",24*60)+"/"+r.gavAS("HUMIDITY","max",24*60),PHONE);' where condition_='RAPORT'; 

update Rule set expression_='r.sendSMS("Dzien:"+r.getNumberOfDays()+" Temperatura "+TEMP+" Wilgotnosc "+HUMI+" Lampa: "+r.css("LAMPA")+" Wiatraki "+r.css("WIATRAKI")+" Grzejnik "+r.css("GRZEJNIK")+" Temp(śr/min/max): "+r.gavAS("TEMPERATURE","avg",24*60)+"/"+r.gavAS("TEMPERATURE","min",24*60)+"/"+r.gavAS("TEMPERATURE","max",24*60)+" Wilgotnosc(sr/min/max): "+r.gavAS("HUMIDITY","avg",24*60)+"/"+r.gavAS("HUMIDITY","min",24*60)+"/"+r.gavAS("HUMIDITY","max",24*60));' where id=4;

