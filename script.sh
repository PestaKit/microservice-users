#!/bin/bash
cd ./service-users-server
mvn clean install
docker build --rm -t users-image ../topology
