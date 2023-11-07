# internet-banking

internet-banking is an application with REST API architecture, which monitors all **clients** and **cards** in the system, it also ensures correct work on deleting, creating or changing data. The main functions of the application are:
* CRUD operations;
* crediting money to an account;
* withdrawal of money from the client's account;
* transfer money from one account to another;
* printing check after every money operation
* makink investments.

## Technologies

Project is created with:
* java 17 version
* Spring boot data jpa
* lombok
* PostgreSQL
* Swagger
* Maven
* javadoc
  

## Installation

* Install internet-banking ZIP from gitHub.
* When the project is open go to **application.properties** - path:"src/main/resources/application.properties". In order to run the application you should create new database and change the settings of db: change user, password and db url;
* After connection is made, open DDL file - path :"src/main/java/com/tms/DDL" copy and past info from this file to db console, it will make all thr needed tables;
* Upload db data from **clients.csv** and **cards.csv** to the tables - pathes :"src/main/java/com/tms/clients.csv" and "src/main/java/com/tms/cards.csv";
* After that start the **InternetBankingApplication** class.


## Usage

* open postman or swagger in order to make requests.
