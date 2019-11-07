@ECHO OFF 

SET AH_CONF_DIR=%cd%

SET AH_OPERATOR=aitia
SET AH_COMPANY=arrowhead
SET AH_COUNTRY=eu

SET CLOUD_NAME=%1
SET CLOUD_PASSWORD=%2
SET MASTER_CONF_DIR=%3
SET MASTER_FILE_NAME=%4
SET MASTER_PASS_CERT=%5%
SET MASTER_ALIAS=%6

IF [%6] == [] SET MASTER_ALIAS=%MASTER_FILE_NAME%

::   ============================
:: system certificate generation starts here
::   ============================

CALL :ah_cert_signed_cloud

EXIT /B 0

::   ============================
:: system certificate generation ends here
::   ============================

::   ============================
:: ah_cert defined here
::   ============================
:ah_cert_cloud
REM	ECHO ============================
REM	ECHO ah_cert_cloud started ...
REM	ECHO parameters: %*
REM	ECHO ============================
	
	SET dst_path=%1
	SET dst_name=%2
	SET cn=%3
	SET password=%CLOUD_PASSWORD%

	SET file=%dst_path%\%dst_name%.p12
	
	IF EXIST %file% (
		ECHO %file% allready exist
		EXIT /B 102
	) ELSE (
		keytool -genkeypair -alias %dst_name% -keyalg RSA -keysize 2048 -dname CN=%dst_name%.aitia.arrowhead.eu -validity 7300 -keypass 123456  -keystore %file% -storepass 123456 -storetype PKCS12 -ext BasicConstraints=ca:true,pathlen:2
	)
	

EXIT /B 0
::   ============================
:: ah_cert_cloud definition over
::   ============================

::   ============================
:: ah_cert_signed_system defined here
::   ============================
:ah_cert_signed_cloud
REM	ECHO ============================
REM	ECHO ah_cert_signed_cloud started ...
REM	ECHO parameters: %*
REM	ECHO ============================
	SET name=%CLOUD_NAME%
	SET passwd=%CLOUD_PASSWORD%
	SET base_path=%AH_CONF_DIR%

	
	SET src_file=%MASTER_CONF_DIR%\%MASTER_FILE_NAME%.p12
	SET system_dst_file=%AH_CONF_DIR%\%name%.p12
	
	IF EXIST %system_dst_file% (
		
		ECHO %system_dst_file% allready exist
		EXIT /B 101
		
	) ELSE (

		CALL :ah_cert_cloud %base_path% %name% %name%.%AH_OPERATOR%.arrowhead.eu %passwd%
		
		ECHO src_file %src_file%

		keytool -export -alias %MASTER_ALIAS% -storepass %MASTER_PASS_CERT% -keystore %src_file% ^
		| keytool -import -trustcacerts -alias %MASTER_ALIAS% -keystore %system_dst_file% -keypass %passwd% -storepass %passwd% -storetype PKCS12 -noprompt

		keytool -certreq -alias %name% -keypass %passwd% -keystore %system_dst_file% -storepass %passwd% ^
		| keytool -gencert -rfc -alias %MASTER_ALIAS% -keypass %MASTER_PASS_CERT% -keystore %src_file% -storepass %MASTER_PASS_CERT% -validity 3650 -ext BasicConstraints=ca:true,pathlen:2 ^
		| keytool -importcert -alias %name% -keypass %passwd% -keystore %system_dst_file% -storepass %passwd% -noprompt

	)
EXIT /B 0
::   ============================
:: ah_cert_signed_cloud definition is over
