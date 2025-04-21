run:
	mvn package && java -jar Applet/target/Applet-0.0.1-jar-with-dependencies.jar

clean:
	mvn clean
	rm -rf Applet/target