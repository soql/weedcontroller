#!/bin/bash
echo $1
if [ "$1" == "LOCAL" ]
then
	SSH='pi@192.168.1.168'
else
	SSH='pi@zjc.oth.net.pl'
fi
ssh -o StrictHostKeyChecking=no  $SSH 'sudo /opt/backup_mysql'
FILE_NAME=`ssh -o StrictHostKeyChecking=no $SSH 'ls /mnt/nfs/mysqldump/ -1 | tail -n 1'`
echo $FILE_NAME
scp -o StrictHostKeyChecking=no $SSH:/mnt/nfs/mysqldump/$FILE_NAME baza
mysql -u weedcontroller -pweedcontroller weedcontroller < baza
echo "Zmiana parametrów na DEV"
mysql -u weedcontroller -pweedcontroller weedcontroller < update_prod_to_dev.sql
#rm baza

