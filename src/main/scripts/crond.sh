#!/bin/bash
source /etc/profile
ps -ef | grep AutoApplication | grep -v "grep"
if [ $? -ne 0 ]
then
	nohup /home/auto/auto/bin/run.sh &
else
	kill -9 $(ps -ef | grep AutoApplication | grep -v "grep" | awk '{print $2}')
	nohup /home/auto/auto/bin/run.sh &
fi
