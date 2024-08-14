#!/bin/bash
mvn clean package install -Pall -U -Dmaven.test.skip=true
