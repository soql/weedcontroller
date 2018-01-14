#!/bin/bash
SSH='pi@192.168.1.168'
ssh -o StrictHostKeyChecking=no  $SSH 'sudo /opt/backup_mysql'
FILE_NAME=`ssh -o StrictHostKeyChecking=no $SSH 'ls /mnt/nfs/mysqldump/ -1 | tail -n 1'`
echo $FILE_NAME
scp -o StrictHostKeyChecking=no $SSH:/mnt/nfs/mysqldump/$FILE_NAME baza
mysql -u weedcontroller -p weedcontroller < baza
#rm baza

