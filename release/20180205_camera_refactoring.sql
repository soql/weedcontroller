/*
-- Query: SELECT * FROM weedcontroller.Camera
LIMIT 0, 1000

-- Date: 2018-02-05 09:02
*/
INSERT INTO `Camera` (`name_`,`active`,`lastFoto`,`takeFotoCommand`) VALUES ('image',1,null,'/opt/camera/takeFoto.sh');
INSERT INTO `Camera` (`name_`,`active`,`lastFoto`,`takeFotoCommand`) VALUES ('internal',1,null,'/opt/camera/takeFotoInternal.sh');

delete from `Configuration_` where key_='LAST_FOTO';