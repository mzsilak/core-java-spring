## Generating certificates

Usage of the certificate generating scripts
prerequisites: java installed, keytool working ...

### 1. Usage in Windows
Tested Windows versions ... : 

Create core system certificates:
Create a folder : certgen.
Copy the gen_ah_core_sys_certs.bat file into the newly crated certgen folder.
Copy the generate_system_certificate.bat file into the certgen folder.
Copy the cloud certificate (testcloud2.p12) into the certgen folder.
Copy the master.crt file into the certgen folder.

Open a command line in certgen folder.
Type: gen_ah_core_certs.bat "core.system.password" "ip.address.for.core.systems" "cloud_certificate_password" "cloud_name" "cloud_alias"
Where : 
"core.system.password" = password for the core system certifications
"ip.address.for.core.systems" = the actual ip address of your systems
"cloud_name" = cloud file name without extention 
"cloud_alias" = alias of cloud certificate 

example : gen_ah_core_sys_certs.bat 123abc 10.0.0.1 567890 testcloud2 testcloud2.aitia.arrowhead.eu

Press Enter.


### 2. Usage in Linux
Tested Linux versions ... : 
