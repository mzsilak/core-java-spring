#!/bin/sh
## make sure you have the master.cert and the cloud.p12 and the genSystemCert.sh in same directory

for i in service_registry authorization orchestrator event_handler gatekeeper gateway choreographer sysop
do
        sh ./genSystemCert.sh "$i" 123abc hostname 10.0.0.0 testcloud3 567890
done
