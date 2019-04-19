# Inzynierka

SERVER PART INSTALLATION
***
First install:
* OpenJDK 11.0.3 2019-04-16
* OpenJDK Runtime Environment (build 11.0.3+4)


If you already had Java installed, 
modify you **JAVA_HOME** environmental variable to include newly installed JRE version.

***
Database:

MYSQL/MARIADB

Open terminal and run: 

**myslq -u root -p**

In db console run: 

**CREATE DATABASE inzynierka;**  
**CREATE USER \`user\`@\`localhost\` IDENTIFIED BY \`password\`;**
**GRANT ALL PRIVILEGES ON inzynierka.\* TO \`user\`@\`localhost\`**

In **application.properties** file set following properties:
 
spring.datasource.username=user  
spring.datasource.password=password


***

Navigate to root folder and run:


* mvn spring-boot:build

to build application

* mvn spring-boot:run 

to run application

***
Open application navigating to **localhost:8090** in your web browser



