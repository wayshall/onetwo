@echo off
set JAVA_HOME=
set PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin
set CLASSPATH=.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar
set JAVA_OPTS="-server -Xms2048m -Xmx2048m -XX:MaxPermSize=512m"
java -jar ${artifactId}-${project.version}.jar
pause
