#!/bin/bash
cd ./service-users-server
mvn clean install

cp target/microserve-users-1.0.0.jar ../topology/users-image/

cd ../topology

docker build --rm -t users-image users-image
