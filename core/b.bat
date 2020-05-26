@echo off
@echo current dir: %~dp0
@echo run cmd: %*
@rem 换行
@echo:
@echo:


cd ..
@echo run in dir: %cd%
call %*
@echo:

cd ../dbm
@echo run in dir: %cd%
call %*
@echo:

cd ../onetwo-wechat
@echo run in dir: %cd%
call %*
@echo:

cd ../zifish-plugins
@echo run in dir: %cd%
call %*
@echo:

cd ../onetwo-tcc
@echo run in dir: %cd%
call %*
@echo:

cd %~dp0