update weedcontroller.Sensor set command=replace(command,'/opt', '/home/psokolowski/Workspace/jee-photon/workspaces');
update weedcontroller.Camera set takefotocommand=replace(takeFotoCommand, '/opt', '/home/psokolowski/Workspace/jee-photon/workspaces');
UPDATE `weedcontroller`.`Configuration_` SET `value`='/home/psokolowski/Workspace/jee-photon/workspaces/camera' WHERE `key_`='IMAGE_FOLDER';
