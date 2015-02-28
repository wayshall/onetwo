@echo off
gradle build uploadArchives -x test -Drepo=56
pause