# wikirace

A program that simulates [wikiracing](https://en.wikipedia.org/wiki/Wikiracing) in the fastest time possible.

# Getting started
This guide provides instructions on how to run wikirace either locally with Maven or using Docker.

### Prerequisites 
For running locally:
- Java JDK 17+
- Maven

For running with Docker:
- Docker

---

### Option 1: Build and Run Locally with Maven
**1. Clone the repository:**
```
git clone https://github.com/jasonliu2000/wikirace.git
```

**2. Navigate to the project folder and use Maven to build the project:**
```
mvn clean package
```
An executable jar file will be created in `wikirace/target`.

**3. Execute the JAR file to start the application:**
```
java -jar target/wikirace.jar
```
Replace `wikirace.jar` with the actual name of the JAR file generated in the `target` directory (usually follows the convention of `target/wikirace-<version>.jar`)

> Note: the server starts on port 8080 by default.

### Option 2: Build and Run with Docker
**1. Clone the repository:**
```
git clone https://github.com/jasonliu2000/wikirace.git
```

**2. Build the Docker image:**
```
docker build -t wikirace .
```

**3. Run the Docker container:**
```
docker run -p 8080:8080 wikirace
```
This command publishes the container's port 8080 to the local port 8080. For a different local port (e.g. `8081`), change to `-p 8081:8080`.

**4. Quick start (Optional):**
```
docker build -t wikirace . && docker run -p 8080:8080 wikirace
```

---

### Health Check
To verify that the application is running and the server is up, navigate to http://localhost:8080/ping and assert that "pong" is returned by the server.

---

### Running Wikirace
1. To start a wikirace run, make a POST call like this:
```
curl -X POST 'http://localhost:8080/wikirace?start=Wikiracing&target=Nelson_Mandela'
```
where [Wikiracing](https://en.wikipedia.org/wiki/Wikiracing) is the starting article and [Nelson Mandela](https://en.wikipedia.org/wiki/Nelson_Mandela) is the target.

2. To get the wikirace status, make the following GET call:
```
curl -X GET 'http://localhost:8080/wikirace/status'
```
