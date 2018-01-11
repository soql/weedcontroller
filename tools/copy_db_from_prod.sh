#!/bin/bash
ssh -o StrictHostKeyChecking=no  pi@zjc.oth.net.pl 'sudo /opt/backup_mysql'
FILE_NAME=`ssh -o StrictHostKeyChecking=no pi@zjc.oth.net.pl 'ls /mnt/nfs/mysqldump/ -1 | tail -n 1'`
echo $FILE_NAME
scp -o StrictHostKeyChecking=no pi@zjc.oth.net.pl:/mnt/nfs/mysqldump/$FILE_NAME baza
mysql -u weedcontroller -p weedcontroller < baza
#rm baza

