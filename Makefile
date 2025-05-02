run:
	mvn package && java -jar Applet/target/Applet-0.0.1-jar-with-dependencies.jar

compile:
	mvn -X compile

clean:
	mvn clean
	rm -rf Applet/target

updateParser:
	rm -rf Imperativa2/src/li2/plp/imperative2/parser/*java
	javacc -OUTPUT_DIRECTORY=Imperativa2/src/li2/plp/imperative2/parser Imperativa2/src/li2/plp/imperative2/parser/Imperative2.jj