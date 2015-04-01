@echo off
gradle clean build uploadArchives -x test -Prepo=217
pause