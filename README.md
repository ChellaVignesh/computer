# Computer Tracking System

## Task Description

For this task, you are required to write an application in your selected programming language and manage it in a Git repository. The goal is to assist the system administrator of our SampleCompany in keeping track of computers issued by the company. Computer details should be stored in an arbitrary database.

The system administrator needs to store the following elements for each computer:
- MAC address (required)
- Computer name (required)
- IP address (required)
- Employee abbreviation (optional)
- Description (optional)

The employee abbreviation consists of 3 letters (e.g., Max Mustermann should be represented as "mmu").
The system administrator wants to access the data via a REST interface. The CRUD operations (CREATE, READ, UPDATE, DELETE) for a computer are to be implemented. Additionally, the system administrator wants the ability to request information about all computers.
The system administrator would like to receive notifications if 3 or more devices are already assigned to a single user. To achieve this, an existing messaging service running in a Docker container should be called. The image can be retrieved with the command:
```bash
docker pull greenbone/exercise-admin-notification
```

The messaging service listens to requests on port 8080, and the source code can be found [here](https://github.com/greenbone/exercise-admin-notification).
The service informs the system administrator team when the following REST endpoint is called:
POST http://localhost:8080/api/notify
The expected body of this REST endpoint is defined as follows:
```json
{
  "level": "warning",
  "employeeAbbreviation": "mmu",
  "message": "some message"
}
```

## Requirements
- The system administrator should be able to add a new computer to an employee.
- The system administrator should be informed when an employee is assigned 3 or more computers.
- The system administrator should be able to retrieve information about all computers.
- The system administrator should be able to retrieve all assigned computers for an employee.
- The system administrator should be able to retrieve the data of a single computer.
- The system administrator should be able to remove a computer from an employee.
- The system administrator should be able to assign a computer to another employee.

## Tech Stack
- Java 17
- SpringBoot 3.2.1
- H2
- Maven
- Docker

### Steps to install Docker
You can follow the [documentation](https://docs.docker.com/desktop/) or install using terminal with
```bash
$ brew install --cask docker
```


### Steps to build and run the application
Assuming that mvn and docker are already installed in your system.

```bash
$ mvn clean install -DskipTests
$ docker run -it -d -p 8080:8080 greenbone/exercise-admin-notification:latest
$ mvn spring-boot:run
```

You can call the service with `curl` to see if is running
```curl
curl --header "Content-Type: application/json" \
  --request GET \
  http://localhost:9090/actuator/health
```

Once the application is up and running, you can access documentation using [Swagger Documentation](http://localhost:9090/swagger-ui/index.html).

Json docs can be fetched [here](http://localhost:9090/api-docs).
Yaml docs can be fetched [here](http://localhost:9090/api-docs.yaml)

### Steps to run Unit and Integration Tests of the application
Below script will run all the tests of the application. For running Integration tests, docker will host H2 database container and exercise-admin-notification container to test the integrity using Integration test.
```bash
$ mvn clean test
```
Once tests are complete, we can also access Code Coverage report using
```bash
$ mvn jacoco:report 
$ open target/site/jacoco/index.html
```
This will open the code coverage report in default browser


### Best Practices followed on this Project
- Kept a modular structure, separating concerns like controllers, services, repositories, etc.
- Implemented global exception handling using @ControllerAdvice and @ExceptionHandler to provide meaningful error messages and HTTP status codes in responses.
- Used Bean Validation (@Valid) for input validation to customize validation error messages. Created [custom annotations](src/main/java/com/samplecompany/computer/annotation/MacAddress.java) and [validators](src/main/java/com/samplecompany/computer/annotation/validator/MacAddressFormatValidator.java) for those annotations to throw valid error messages.
- Followed RESTful principles in API design to use HTTP methods appropriately (GET, POST, PUT, DELETE).
- TESTING: Wrote Unit tests and Integration tests using frameworks like JUnit and TestContainers. Used tools like Mockito for mocking.
- Implemented test coverage to ensure code quality.
- DOCUMENTATION: Generated API documentation using tools like Springdoc. Documented public APIs, data models, and significant code components.
- Integrated with [monitoring tool](https://www.baeldung.com/spring-boot-actuators). Implemented health checks and logging for better observability.
- Used Spring Data JPA for database access.
- Used @Transactional appropriately to ensure data consistency.
- Used DAOs (Data Access Objects): When exposing entities through APIs, use DAOs to tailor the data sent over the network. Avoid exposing JPA entities directly to clients.
- Kept business logic in service classes, separating concerns from controllers with a single responsibility per service.
- Used enums for wisely for listing down the list of [errors](src/main/java/com/samplecompany/computer/errors/Error.java) that has to be handled throughout the application.
- Resilience on client calls by implementing [retries](./src/main/java/com/samplecompany/computer/service/impl/NotificationServiceImpl.java) and fallbacks on client calls.
- Used mapstruct to create Mapper methods to avoid human mapping errors.


### Additional Improvements that can be implemented
- Use Spring Security for authentication and authorization.
- Custom Metrics using Micrometer for observability. Alerts can be setup as a part of periodical monitoring.
- Monitoring metrics along with dashboard can be created.
- Implement caching for frequently accessed and non-dynamic data for Scalability.
- NotificationClient could have been created to be called from NotificationService. Isolating different client to have their own webclient and configs.
- H2 can be replaced with Mysql or PostGresql to replace in-memory database to a solid instance running a relational database.
