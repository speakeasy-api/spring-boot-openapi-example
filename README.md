<div align="center">
<a href="https://speakeasyapi.dev/">
<img src="https://github.com/speakeasy-api/speakeasy/assets/68016351/e959f81a-b250-4003-8c5c-a45b9463fc95" alt="Speakeasy Logo" width="400">
<h2>Speakeasy Spring Boot OpenAPI Example</h2>
</a>
</div>

This example Spring Boot app demonstrates Speakeasy-recommended practices for generating clear OpenAPI specifications and SDKs.

## Prerequisites

You need to have Java and Maven installed on your system to run this project. If you don't have these installed, you can download them from [here](https://www.oracle.com/java/technologies/downloads/) and [here](https://maven.apache.org/download.cgi).

To generate an SDK, you'll also need the Speakeasy CLI installed, or use the Speakeasy dashboard.

## Installation

To install the application on your local machine:

1. Clone the repository:
```bash
git clone https://github.com/speakeasy-api/spring-boot-openapi-example.git
```

2. Navigate into the directory:
```bash
cd spring-boot-openapi-example
```

3. Install all dependencies using Maven:
```bash
./mvnw clean install
```

4. [Install Speakeasy CLI](https://github.com/speakeasy-api/speakeasy#installation):
```bash
brew install speakeasy-api/homebrew-tap/speakeasy
```

## Running the application

Start the server:
```bash
./mvnw spring-boot:run
```

### Working with the OpenAPI specification

Spring Boot automatically generates an OpenAPI specification. You can view it at:
- `/swagger-ui.html` - Swagger UI
- `/api-docs` - Raw OpenAPI JSON
- `/api-docs.yaml` - Raw OpenAPI YAML

To save the OpenAPI specification to a file:
```bash
curl http://localhost:8080/api-docs.yaml > openapi.yaml
```

To generate an SDK, run:
```bash
speakeasy generate sdk \
  --schema openapi.yaml \
  --lang typescript \
  --out ./sdk
```

You can replace `typescript` with any supported language of your choice.

## License

This project is licensed under the terms of the Apache 2.0 license.