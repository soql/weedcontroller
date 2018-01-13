ALTER TABLE `weedcontroller`.`SwitchLog` 
ADD COLUMN `switch__name` VARCHAR(45) NULL AFTER `user_login`;

update weedcontroller.SwitchLog sl set switch__name=(select name from Switch s where s.gpioNumber=sl.switch__gpioNumber);
delete from weedcontroller.SwitchLog where switch__name='NAWILZACZ2';

/*Odpalamy aplikację*/
INSERT weedcontroller.SwitchGPIO SELECT gpioNumber, 1, name, name from weedcontroller.Switch;
update weedcontroller.SwitchGPIO set parent_name='NAWILZACZ' where parent_name='NAWILZACZ2'
delete from Switch where name='NAWILZACZ2'

ALTER TABLE `weedcontroller`.`SwitchLog` 
DROP FOREIGN KEY `FKf4ll1d36iuc9ryobjicoytxmo`;
ALTER TABLE `weedcontroller`.`SwitchLog` 
DROP INDEX `FKf4ll1d36iuc9ryobjicoytxmo` ;

ALTER TABLE `weedcontroller`.`Switch` 
DROP COLUMN `gpioNumber`,
CHANGE COLUMN `name` `name` VARCHAR(255) CHARACTER SET 'utf8' NOT NULL DEFAULT 'NULL' ,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`name`);

ALTER TABLE `weedcontroller`.`SwitchGPIO` 
CHANGE COLUMN `parent_name` `parent_name` VARCHAR(255) CHARACTER SET 'utf8' NOT NULL DEFAULT 'BRAK' ;

ALTER TABLE `weedcontroller`.`SwitchLog` 
DROP COLUMN `switch__gpioNumber`,
CHANGE COLUMN `switch__name` `switch__name` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL ;
