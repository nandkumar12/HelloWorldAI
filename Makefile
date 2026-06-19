build-SpringBootFunction:
	mvn clean package
	cp target/HelloWorldAI-1.0-SNAPSHOT-aws.jar $(ARTIFACTS_DIR)/
