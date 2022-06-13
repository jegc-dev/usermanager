# User Manager Backend 
As a user, the API should be able
- to register into the system with my first name, last name, email.
- to get the list of all the users in the system.
- to search a user by email and confirm if the user exists.
## Spring Boot Dependencies
- Lombok
- Spring Web
- Spring Security
- PostgreSQL Driver
- H2 Driver
- Spring Data JPA
- Java Mail Sender
## Others Dependencies
- MailDev: SMTP Server + Web interface
  - Install & Run
    ````
      $ npm install -g mail
      $ maildev
      ````
## Database scripts
In application.yml we have the property *spring.jpa.hibernate.ddl-auto* property is Spring Data JPA specific and 
is their way to specify a value that will eventually be passed to Hibernate under the property it knows, hibernate.hbm2ddl.auto.
The property value *create-drop* is similar to create, with the addition that Hibernate will drop the database 
after all operations are completed. Typically used for unit testing.

## Architecture Diagram

![](../../Documents/EMTEC/EMTEC_UserManager.drawio.png)

## Entity - Relationship Diagram

![](../../Documents/EMTEC/EMTEC_UserManager_ER.drawio.png)

## Application Variables

Managed by ../resouces/application.yml

````
server:
  error:
    include-message: always
    include-binding-errors: always

spring:
  datasource:
    password: postgres
    url: jdbc:postgresql://localhost:5432/registration
    username: postgres
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    show-sql: true

  mail:
    host: localhost
    port: 1025
    username: hello
    password: hello

    properties:
      mail:
        smtp:
          ssl:
            trust: "*"
          auth: true
          starttls:
            enable: true
          connectiontimeout: 5000
          timeout: 3000
          writetimeout: 5000
````
## Run Spring Boot application

````
mvn install
mvn spring-boot:run 
````

## API Curl list
### UpsertUser
````
curl --location --request POST 'localhost:8080/api/v1/registration' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=01446237C8E93010F57CC572650D5E15' \
--data-raw '{
    "firstName": "Jessica",
    "lastName": "Simpson",
    "email": "Jessica.Simpson@gmail.com",
    "password": "password"
}'
````
### IsUserExists
````
curl --location --request GET 'localhost:8080/api/v1/user?email=JESSICA.simpson@gmail.com' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=01446237C8E93010F57CC572650D5E15' \
--data-raw '{
    "firstName": "John",
    "lastname": "Doe",
    "email": "jhon.doe@gmail.com",
    "password": "password"
}'
````
### ListUsers
````
curl --location --request GET 'localhost:8080/api/v1/users' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=01446237C8E93010F57CC572650D5E15' \
--data-raw '{
    "firstName": "John",
    "lastname": "Doe",
    "email": "jhon.doe@gmail.com",
    "password": "password"
}'
````
### Confirm Token
````
curl --location --request GET 'localhost:8080/api/v1/registration/confirm?token=0c301579-cca3-4159-b8e4-c37bb9a8b692' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName": "John",
    "lastname": "Doe",
    "email": "jhon.doe@gmail.com",
    "password": "password"
}'
````