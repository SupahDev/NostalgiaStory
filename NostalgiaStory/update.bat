@echo off
set CLASSPATH=.;dist\odinms.jar;dist\mina-core.jar;dist\slf4j-api.jar;dist\slf4j-jdk14.jar;dist\mysql-connector-java-bin.jar;dist\exttools.jar
java net.sf.odinms.exttools.update.DatabaseUpdater
pause