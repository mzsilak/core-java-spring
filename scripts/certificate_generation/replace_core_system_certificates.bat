@ECHO OFF

SET NEW_CERT_PATH=%1
SET OLD_CERT_BASE_PATH=%2

SET CORE_SYSTEM[0]=service_registry
SET CORE_SYSTEM[1]=authorization
SET CORE_SYSTEM[2]=orchestrator
SET CORE_SYSTEM[3]=event_handler
SET CORE_SYSTEM[4]=gatekeeper
SET CORE_SYSTEM[5]=gateway
SET CORE_SYSTEM[6]=choreographer
SET CORE_SYSTEM[7]=sysop

FOR /L %%a IN (0,1,6) DO ( 
	CALL :copy_cert %%CORE_SYSTEM[%%a]%%
)

EXIT /B 0

:copy_cert
SET src_file=%1
SET folder=%src_file:_=%
SET dst=%OLD_CERT_BASE_PATH%\%folder%\src\main\resources\certificates

DEL %dst%\%src_file%.p12
COPY %NEW_CERT_PATH%\%src_file%.p12 %dst%

EXIT /B 0
