# GymFlex Back-End API

![Build Status](https://img.shields.io/github/actions/workflow/status/kaffouh999/Gym-flex-back-end/.gitlab-ci.yml?branch=main)
![Coverage Status](https://coveralls.io/repos/github/kaffouh999/Gym-flex-back-end/badge.svg?branch=main)
![License](https://img.shields.io/github/license/kaffouh999/Gym-flex-back-end)
![Dependencies](https://img.shields.io/librariesio/github/kaffouh999/Gym-flex-back-end)
![Code Quality](https://img.shields.io/codefactor/grade/github/kaffouh999/Gym-flex-back-end/main)
![Frontend](https://img.shields.io/badge/frontend-angular-red)
![Backend](https://img.shields.io/badge/backend-springboot-brightgreen)
[![Open in IntelliJ IDEA](https://img.shields.io/badge/Open%20in-IntelliJ%20IDEA-blue)](https://www.jetbrains.com/idea/)

## Table of Contents

- [GymFlex API](#gymflex-back-end-api)
    - [Table of Contents](#table-of-contents)
    - [Description](#description)
    - [requirements](#requirements)
    - [Installation](#installation)
    - [Usage](#usage)
    - [Support](#support)

## Description

This project is the backend of the GymFlex application. It is a RESTful API that provides endpoints for the GymFlex
frontend to interact with the database.

## Requirements

- Java 17 or higher: The project is written in Java and requires at least version 17 to
  run. [Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
- Maven 3.8.1 or higher: Maven is used for managing the project's
  build. [Download](https://maven.apache.org/download.cgi).
- PostgreSQL 13 or higher: The project uses PostgreSQL as its
  database. [Download](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads).
- Nginx: Nginx is used to serve the static files uploaded by the users. [Download](https://nginx.org/en/download.html).

## Installation

- Clone the repository

```bash
git clone https://github.com/Kaffouh999/Gym-flex-back-end.git
```
- Open the project in your favorite IDE and build it with Maven

```bash
mvn install
```
- Create the gymflex database in PostgreSQL

```sql
CREATE DATABASE gymflex;
```

- Configure the database connection and location for static files in the application.properties file

```properties
# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/gymflex
spring.datasource.username=postgres
spring.datasource.password=root
# Static files location
FILE_UPLOAD_DIRECTORY=C:\Users\myLaptop\Documents\GymFlexFiles
```

- Open ``nginx.conf`` file and configure the port and the root directory for the static files in the server block

```conf
server {
    listen 5051;
    server_name localhost;
    location / {
        root C:\Users\myLaptop\Documents\GymFlexFiles;
        index index.html;
    }
}
```


- Run the Spring Boot application

```bash
mvn spring-boot:run
```

## Usage

To use the GymFlex API, you'll need to interact with the provided endpoints. The available endpoints and their functionalities will be documented in the project's openapi specification.

## Support

If you encounter any issues or have questions about the project, please open an issue on the GitHub repository.