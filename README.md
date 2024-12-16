# wikirace

A program that simulates [wikiracing](https://en.wikipedia.org/wiki/Wikiracing) in the fastest time possible.

# Getting started
This guide provides instructions on how to run wikirace either locally with Apache Maven and React or using Docker. If you're interested in only running the backend, please see the [backend README](backend/README.md). Alternatively, if you want to run frontend locally using the React server and the backend as a Docker container, please skip down to Option 1 React Instructions and then check out the [backend README](backend/README.md).

### Prerequisites 
For running locally:
- Java JDK 17+
- Apache Maven
- Node.js v16+

For running with Docker:
- Docker *(how can you hate this option?)*

---

### Option 1: Build and Run Locally with Apache Maven & React
###### Apache Maven Instructions
**1. Clone the repository:**
```
git clone https://github.com/jasonliu2000/wikirace.git
```

**2. Navigate to the backend folder and use Apache Maven to build the project:**
```
cd backend
mvn clean package
```
An executable jar file will be created in the `target` subdirectory.

**3. Execute the JAR file to start the application:**
```
java -jar target/wikirace.jar
```
Replace `wikirace.jar` with the actual name of the JAR file generated in the `target` directory (usually follows the convention of `target/wikirace-<version>.jar`)

> Note: the server starts on port 8080 by default.

###### React Instructions
**4. Navigate back to the parent directory, install the package dependencies, and start the server:**
```
cd ..
npm install
npm start
```
Then open http://localhost:3000/ to access the frontend.

### Option 2: Build and Run with Docker
**1. Clone the repository:**
```
git clone https://github.com/jasonliu2000/wikirace.git
```

**2. Run services with Docker Compose:**
```
docker compose up
```