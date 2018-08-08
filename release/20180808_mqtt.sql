ALTER SCHEMA `weedcontroller`  DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_polish_ci ;

DROP TABLE `weedcontroller`.`SwitchGpioLog`;

ALTER TABLE `weedcontroller`.`SwitchGPIO` 
RENAME TO  `weedcontroller`.`SwitchGPIO_BCK` ;

ALTER TABLE `weedcontroller`.`SwitchGPIO_BCK` 
DROP FOREIGN KEY `FKb68vnyso5vtq7vy5t29kpw2gp`;

ALTER TABLE `weedcontroller`.`SwitchGPIO_BCK` 
DROP INDEX `FKb68vnyso5vtq7vy5t29kpw2gp` ;

/*Odpalamy apkę*/
ALTER TABLE `weedcontroller`.`SwitchGPIO` 
CHANGE COLUMN `parent_name` `parent_name` VARCHAR(255) CHARACTER SET 'utf8' NOT NULL DEFAULT 'NULL' ;

/*Odpalamy apkę*/

INSERT INTO `SwitchGPIO` (`id`,`active`,`description`,`gpioNumber`,`mqqTopic`,`powerUsage`,`switchType`,`parent_name`) 
SELECT `gpioNumber`,`active`,`description`,`gpioNumber`,NULL,`powerUsage`,0,`parent_name` FROM SwitchGPIO_BCK

DROP TABLE `weedcontroller`.`SwitchGPIO_BCK`;


