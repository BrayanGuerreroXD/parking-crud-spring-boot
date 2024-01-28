## Backend Technical Challenge: Parking CRUD with Authentication by JWT
## Table of Contents
1. [Description](#description)
2. [Technologies](#technologies)
3. [Entity-relationship model](#entity-relationship-model)
4. [Installation and Usage](#installation-and-usage)

___
### Description: 

Rest API for the management and control of vehicle parking, the API allows the creation of partners who will manage the entry and exit of vehicles in the parking lots in which they are related. 

The API uses Spring Security and JWT Token to manage an access control system by role (ADMIN and PARTNER) through a token that expires every 6 hours.
___
### Technologies:
- [Java (JDK 17)](https://www.oracle.com/co/java/technologies/javase/jdk17-archive-downloads.html "Java JDK 17") : Minimum Java jdk version to use the API is 17.
- [Apache Maven](https://maven.apache.org/download.cgi "Apache Maven"):  Apache Maven is required to use the API.

|Backend|
|---|
|![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white) ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) ![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens) ![Git](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)|

___
### Entity-relationship model:

![ERM pgadmin image](https://github.com/BrayanGuerreroXD/parking-crud-spring-boot/blob/master/src/main/resources/MER-PARKING.png)

___

### Installation and Usage:

1. Clone the Parking CRUD project repository

Open the terminal and type the following command to download the Parking CRUD project 

```sh
git clone https://github.com/BrayanGuerreroXD/parking-crud-spring-boot.git
```

This project in the resources folder has a sql file called import.sql, which has the script to insert the two roles needed in the flow and in turn loads the admin user. This file contains the following.

```sql
-- Insert roles
INSERT INTO roles(role_name) VALUES('ADMIN');
INSERT INTO roles(role_name) VALUES('SOCIO');
-- Insert admin user
INSERT INTO users(user_email, user_password, role_id) VALUES ('admin@mail.com', '$2a$10$WHA7Rwnti3PLuYZlaxY/zORWt0awaMWoxaKv0pFphGntI3oLDqXU2', 1);
```

2. Clone the Mail Simulation project repository

Open the terminal and type the following command to download the Mail Simulation project

```sh
git clone https://github.com/BrayanGuerreroXD/mail-simulation-api-spring.git
```

This project does not handle an application.properties since it does not store data inside a database, its only function is to simulate the reception and delivery of email, this one runs on port 8080 by default.

3. Add environment variables in application.properties in the Parking CRUD project.

The application.properties of the Parking CRUD project looks as follows:

```properties
server.port=8081

#----- DATOS JWT
jwt.secret.key={JWT_SECRET_KEY}

#----- DATOS MAIL
url.api.mail=http://localhost:8080/mail/send

spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect

spring.jpa.hibernate.ddl-auto=create
# spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.show-sql=true

spring.datasource.url=jdbc:postgresql://{URL_DB}
spring.datasource.username={POSTGRES_USER}
spring.datasource.password={POSTGRES_PASSWORD}

server.error.include-message=always
```

- JWT_SECRET_KEY: It is the secret key for the creation of the authorization tokens for each user login.
- URL_DB: Is the url where your PostgreSQL database is running.
- POSTGRES_USER: Is your PostgreSQL username
- POSTGRES_PASSWORD: Is your PostgreSQL password

The line "spring.jpa.hibernate.ddl-auto=create" in the first run of the project must be in CREATE to generate the tables and relationships of the project entities and in turn runs the script mentioned above. After doing the above, its status should be changed to UPDATE so that it only updates if necessary.

4. Test postman http request collection.

After both projects are running, it is time to test the http requests to determine the correct functioning of the API. The order of the Postman collection looks as follows:

![postman collection](https://github.com/BrayanGuerreroXD/parking-crud-spring-boot/blob/master/src/main/resources/postman%20http%20request.png)

To obtain the authorization token for the ADMIN user is with the Login request, this token is obtained and can be added to each of the other requests in the ADMIN directory in the Bearer Token of type Authorization.

Likewise with the SOCIO user, in his directory there is the request to obtain his authorization token so that he can use all the collection of requests of the SOCIO user.

___
