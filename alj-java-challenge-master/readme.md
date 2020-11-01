### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

# *Features Included in Employee CRUD Service*

### 1.  Rest Controller for Employee Service.
### 2.  Employee service class and its implementation.
### 3.  Spring security to protect rest endpoints.
### 4.  Caching layer for enabling caching for this Service using Hazelcast configuration.
### 5.  Swagger configuration for API documentation.
### 6.  WebMVC configuration and Interceptors to enable CorrelationId injection in each request.
### 7.  Custom exception handlers and API responses and Util classes.
### 8.  Actuator framework support(out of the box features enabled) for Application health check.
### 9.  Logging using Sl4j framework.
### 10. JUnits support for implementation and business logic.
### 11. Profile based configuration file.

----------------------------------------------------------------------------
----------------------------------------------------------------------------

# _Future enhancement/Scope/TODOs_
### 1. Actuator framework customization.
### 2. Monitoring framework.
### 3. Spring cloud support.
### 4. CI/CD readiness.
### 5. Integration testing.
### 6. UI integration.
### 7. Spring config server.
### 8. Containerization(Docker/Kubern8).
### 9. SonarQube/SonarLint integration for code coverage/code optimization.

