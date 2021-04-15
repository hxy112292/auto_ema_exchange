#!/bin/bash
binDir=$(cd "$(dirname "$0")"; pwd)
baseDir=$binDir/../
libDir=$baseDir/lib
confDir=$baseDir/conf

for jarPath in $libDir/*.jar
 do
   CLASSPATH=$CLASSPATH:$jarPath
 done

for confPath in $confDir
 do
   CLASSPATH=$CLASSPATH:$confPath
 done

export CLASSPATH

java -Ddefault.client.encoding="UTF-8" -Dfile.encoding="UTF-8" -Duser.language="Zh" -Duser.region="CN" -Duser.timezone="GMT+08" -Xms512m -Xmx512m -Xss1024K com.okex.auto.AutoApplication
