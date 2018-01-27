
INSERT INTO `weedcontroller`.`SensorData` (`id`,`maxError`,`name`,`regexp_`,`parent_number`,`description`,`cssName`,`unit`) VALUES (1,2,'TEMPERATURE','Temp=(.*)?\\*  Humidity=.*?\\%',1,'Temp','temp','℃');
INSERT INTO `weedcontroller`.`SensorData` (`id`,`maxError`,`name`,`regexp_`,`parent_number`,`description`,`cssName`,`unit`) VALUES (2,5,'HUMIDITY','Temp=.*?\\*  Humidity=(.*)?\\%',1,'Wilg','humidity','%');
INSERT INTO `weedcontroller`.`SensorData` (`id`,`maxError`,`name`,`regexp_`,`parent_number`,`description`,`cssName`,`unit`) VALUES (3,2,'TEMPERATURE','Temp=(.*)?\\*  Humidity=.*?\\%',2,'Temp','temp','℃');
INSERT INTO `weedcontroller`.`SensorData` (`id`,`maxError`,`name`,`regexp_`,`parent_number`,`description`,`cssName`,`unit`) VALUES (4,5,'HUMIDITY','Temp=.*?\\*  Humidity=(.*)?\\%',2,'Wilg','humidity','%');

DELETE FROM `weedcontroller`.`Configuration_` WHERE `key_`='MAX_HUMI_ERROR_KEY';
DELETE FROM `weedcontroller`.`Configuration_` WHERE `key_`='MAX_TEMP_ERROR_KEY';
