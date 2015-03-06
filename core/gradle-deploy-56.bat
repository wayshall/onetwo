@echo off
gradle build uploadArchives -x test -Prepo=56
pause