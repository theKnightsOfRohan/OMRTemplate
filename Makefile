FLAGS ?=
FILE ?=

build:
	# /usr/bin/env /Library/Java/JavaVirtualMachines/jdk-21.0.2.jdk/Contents/Home/bin/java -cp "${PWD}/bin:${PWD}/lib/*:" -jar lib/pkl-codegen-java-0.25.2.jar -o generated $(PWD)/src/BubbleData.pkl
	find "${PWD}/src" -name '*.java' | xargs javac -cp "${PWD}/lib/*" $(FLAGS) -d bin

run:
	make build && java -XX:+ShowCodeDetailsInExceptionMessages -cp "${PWD}/bin:${PWD}/lib/*" $(FLAGS) $(FILE)
