<div align="center">
 <a href="https://www.speakeasy.com/" target="_blank">
   <picture>
       <source media="(prefers-color-scheme: light)" srcset="https://github.com/user-attachments/assets/21dd5d3a-aefc-4cd3-abee-5e17ef1d4dad">
       <source media="(prefers-color-scheme: dark)" srcset="https://github.com/user-attachments/assets/0a747f98-d228-462d-9964-fd87bf93adc5">
       <img width="100px" src="https://github.com/user-attachments/assets/21dd5d3a-aefc-4cd3-abee-5e17ef1d4dad#gh-light-mode-only" alt="Speakeasy">
   </picture>
 </a>
  <h1>Speakeasy</h1>
  <p>Build APIs your users love ❤️ with Speakeasy</p>
  <div>
   <a href="https://speakeasy.com/docs/create-client-sdks/" target="_blank"><b>Docs Quickstart</b></a>&nbsp;&nbsp;//&nbsp;&nbsp;<a href="https://join.slack.com/t/speakeasy-dev/shared_invite/zt-1cwb3flxz-lS5SyZxAsF_3NOq5xc8Cjw" target="_blank"><b>Join us on Slack</b></a>
  </div>
 <br />

</div>
<hr />
  
<h2>Speakeasy Spring Boot OpenAPI Example</h2>

This example Spring Boot app demonstrates recommended practices for generating clear OpenAPI specifications and SDKs.

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
