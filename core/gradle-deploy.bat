@echo off
gradle build uploadArchives -x test -Prepo=217
pause