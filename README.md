# wikirace

A program that simulates [wikiracing](https://en.wikipedia.org/wiki/Wikiracing) in the fastest time possible.

# Getting started
Clone this repo and then run 

`mvn clean package`

to build an executable jar file. Next, execute the jar file to start the application with the following command:

`java -jar [path to wikiracing jar file]`

### Check if API is reachable
To verify that the application is running and the server is up, navigate to http://localhost:8080/ping and assert that "pong" is returned by the server.
> Note: the server starts on port 8080 by default.

### Running Wikirace
To start a wikirace run, make a POST call like this:

`curl -X POST 'http://localhost:8080/wikirace?start=Wikiracing&target=Nelson_Mandela'`

where [Wikiracing](https://en.wikipedia.org/wiki/Wikiracing) is the starting article and [Nelson Mandela](https://en.wikipedia.org/wiki/Nelson_Mandela) is the target.

To get the wikirace status, make the following GET call:

`curl -X GET 'http://localhost:8080/wikirace/status'`
