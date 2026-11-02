# Cottage Booking Service

## How to run (Docker)

Requires [Docker](https://www.docker.com/) (Docker Desktop must be running).

Build and start the app:

```
docker compose up -d --build
```

Open in the browser:

- App: http://localhost:8080/cottage-booking/
- Ontology: http://localhost:8080/cottage-booking/onto/cottage.owl

Stop the app:

```
docker compose down
```

## How to run locally

Requires Java 25 or newer, [Maven](https://maven.apache.org/) and [Apache Tomcat 11](https://tomcat.apache.org/).

On macOS with Homebrew:

```
brew install maven tomcat
```

Build the WAR and copy it to Tomcat:

```
mvn clean package
cp target/cottage-booking.war "$(brew --prefix tomcat)/libexec/webapps/"
```

Start Tomcat:

```
catalina run
```

Open http://localhost:8080/cottage-booking/. Stop Tomcat with Ctrl+C.
