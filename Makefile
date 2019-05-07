SHELL := /bin/bash

clean:
	mvn clean

deploy: clean
	mvn deploy