@echo off
gradle build uploadArchives -x test -Drepo=217
pause