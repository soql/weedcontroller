#!/bin/bash
rm /opt/camera/image*
ssh -o "StrictHostKeyChecking no" pi@zjc.oth.net.pl 'ls -1 /opt/camera/image-*.jpg > /home/pi/tmp/photo_list.txt; sudo tar cz -T ''/home/pi/tmp/photo_list.txt'' -f /home/pi/tmp/photos.tar.gz'

