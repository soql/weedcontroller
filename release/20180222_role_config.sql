INSERT INTO `Role` (`id`,`name`) VALUES (1,'ROLE_ADMIN');
INSERT INTO `Role` (`id`,`name`) VALUES (2,'ROLE_USER');

INSERT INTO weedcontroller.UserRoles (login, roleId)
SELECT login, 2 FROM weedcontroller.User;
