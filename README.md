# SOAP Movies — a contract-first JAX-WS refresher

Two Spring Boot apps that talk SOAP:

| App | Port | What it does |
|---|---|---|
| `server` | 8080 | Publishes the hand-written `contract/movies.wsdl` with Apache CXF and implements `searchMovies` over a static list of 32 horror movies. |
| `client` | 8081 | Generates JAX-WS stubs from the same WSDL, calls the server, and exposes the result as REST/JSON. |

Stack: Java 21, Spring Boot 4.1.1, Apache CXF 4.2.4, Gradle

## Run

```bash
./run.sh
```

Then:

```bash
curl -s 'http://localhost:8081/movies'                     # all movies
curl -s 'http://localhost:8081/movies?keyword=carpenter'   # case-insensitive: title, director, subgenre
```
WSDL: <http://localhost:8080/services/movies?wsdl> · schema: <http://localhost:8080/services/movies?xsd=movies.xsd>

## Contract

`contract/movies.wsdl` (+ `movies.xsd`)

## Code generation in Gradle

Both `server/build.gradle` and `client/build.gradle`contain a `wsdl2java` task.

- Output: `build/generated/sources/wsdl2java` (package `com.example.movies.v1`), registered as a source dir
- `processResources` copies the contract to `wsdl/` on the classpath. The generated `MovieService` uses `classpath:wsdl/movies.wsdl`, so the client never downloads the WSDL at runtime.
- Each app generates its own copy — the client depends only on the WSDL, never on server code.

```bash
./gradlew :client:wsdl2java   # run generation alone; look in client/build/generated/sources/wsdl2java
```

## Service Publication

- `MoviePortImpl` implements the generated `MoviePort` and delegates to `MovieSearchService`
- `WebServiceConfig` creates a CXF `EndpointImpl` with `wsdlLocation = classpath:wsdl/movies.wsdl` and publishes it at `/movies` under `cxf.path=/services`. CXF serves *your* WSDL (rewriting only the address and the schema import URL).

## Configurable endpoint

```bash
./run.sh --movies.soap.endpoint=http://localhost:9999/services/movies
```
or 

```bash
MOVIES_SOAP_ENDPOINT=http://localhost:9999/services/movies java -jar client/build/libs/client.jar
```

## Raw SOAP

Straight to the server:

```bash
curl -s -X POST http://localhost:8080/services/movies \
  -H 'Content-Type: text/xml; charset=utf-8' -H 'SOAPAction: ""' \
  -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:m="http://example.com/movies/v1">
        <soapenv:Body>
          <m:searchMovies><m:keyword>romero</m:keyword></m:searchMovies>
        </soapenv:Body>
      </soapenv:Envelope>'
```

Response (formatted):

```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <searchMoviesResponse xmlns="http://example.com/movies/v1">
      <movie>
        <id>6</id><title>Night of the Living Dead</title><year>1968</year>
        <director>George A. Romero</director><subgenre>zombie</subgenre>
      </movie>
      <movie>
        <id>13</id><title>Dawn of the Dead</title><year>1978</year>
        <director>George A. Romero</director><subgenre>zombie</subgenre>
      </movie>
    </searchMoviesResponse>
  </soap:Body>
</soap:Envelope>
```

