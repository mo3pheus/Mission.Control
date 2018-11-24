#!/bin/bash

echo "Initiating Houston Command Center"
java -jar target/mission.control-2.0-shaded.jar statusLogs/ false zion-portable
