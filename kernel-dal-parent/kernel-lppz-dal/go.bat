@echo off
echo start to package
REM set MAVEN_OPTS=-Xms256m -Xmx512m
REM call mvn clean package -Dmaven.test.skip=true
set currentdir=%~dp0
set targetdir=%currentdir%\target\kernel-lppz-dal
echo %currentdir%

call mvn clean dependency:copy-dependencies -DoutputDirectory=%targetdir%\lib -DincludeScope=compile

call mvn install -Dmaven.test.skip=true

copy %currentdir%\target\*.jar %targetdir%\lib

mkdir %targetdir%\conf

xcopy %currentdir%\target\props\* %targetdir%\conf /s

copy startup %targetdir%
copy init_zk_data %targetdir%

echo package successfully
pause
