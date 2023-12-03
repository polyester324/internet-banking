# internet-banking

internet-banking is an application with REST API architecture, performs Internet banking functions. The main functions of the application are:
* deposit and withdraw money from the card;
* transfer money from card to card of different banks and different currencies;
* providing client with check after every money operation;
* making investments in different banks online.

## Technologies

Project is created with:
* java 17 version;
* Spring (modules: Spring boot, Spring Data, Spring Security);
* lombok;
* PostgreSQL;
* Swagger;
* Maven;
* JUnit 5;
* javadoc.

## Installation

* Install internet-banking from gitHub.
* When the project is open go to **application.properties** - path:"src/main/resources/application.properties". In order to run the application you should create new database and change the settings of db: change user, password and db url;
* After connection is made, open DDL file - path :"src/main/resources/DDL" copy and past info from this file to db console, it will make all the needed tables;
* Upload db data from **clients.csv**, **security.csv**, **banks.csv**, **cards.csv**, **investments.csv** in this exact order to the tables - paths of csv files :"src/main/resources";
* Run Tests to check the application;
* After that start the **InternetBankingApplication** class.

## Registration
* go to "http://localhost:8080/security/registration" via swagger or postman
* User have to pass json format (**POST** method: {"firstName" : "testFirstName", "lastName" : "testLastName", "email": "testMail@gmail.com", "password": "testPassword"});
* the user is saved to the database with ROLE_USER;
* The first name can be changed by going to "http://localhost:8080/client/first-name/{id}" and change first name via json. ( Example: (**PUT** method: "firstName": "newFirstName" ));
* The last name can be changed by going to "http://localhost:8080/client/last-name/{id}" and change last name via json. ( Example: (**PUT** method: "lastName": "newLastName" ));
* The email can be changed by going to "http://localhost:8080/client/email/{id}" and change email via json. ( Example: (**PUT** method: "email": "newTestMail" ));
* The password can be changed by going to "http://localhost:8080/client/password/{id}" and change password via json. ( Example: (**PUT** method: "password": "newPassword1234" )).

## Login
* to log n to the application you need to follow the link "http://localhost:8080/security" and pass json format (**POST** method: {"email": "testMail@gmail.com", "password": "testPassword1234"});
* use the generated token for further operations.

## User capabilities

**Available endpointsROLE_USER:**
* "http://localhost:8080//security/registration"  –  register.
* Example:( POST method: "http://localhost:8080//security/registration" {"firstName" : "testFirstName", "lastName" : "testLastName", "email": "testMail@gmail.com", "password": "testPassword1234"});
* "http://localhost:8080//security" –  login.
* Example:( POST method: "http://localhost:8080//security" {"email": "testMail@gmail.com", "password": "testPassword"});
* "http://localhost:8080//client/first-name/{id}" –  change first name.
* Example:( PUT method: "http://localhost:8080//client/first-name/96" {"firstName": "newFirstName"});
* "http://localhost:8080//client/last-name/{id}"  –  change last name.
* Example:( PUT method: "http://localhost:8080//client/last-name/96" {"lastName": "newLastName"});
* "http://localhost:8080//client/email/{id}"  –  change email.
* Example:( PUT method: "http://localhost:8080//client/email/96" {"email": "newMail@gmail.com"});
* "http://localhost:8080//client/password/{id}"  –  change password.
* Example:( PUT method: "http://localhost:8080//client/password/96" {"password": "newPassword"});
* "http://localhost:8080//client/{id}"  – get client data from database.
* Example:( PUT method: "http://localhost:8080//client/1");
* "http://localhost:8080//bank/create-card/{id}"  – register card in chosen bank.
* Example:( POST method: "http://localhost:8080//bank/create-card/96" {"bankName": "Project bank", "moneyCurrency": "USD"});
* "http://localhost:8080//investment/{id}"  – get investment data from database.
* Example:( PUT method: "http://localhost:8080//investment/96")
* "http://localhost:8080//investment/{id}"  – make an investment in chosen bank using registered card.
* Example:( POST method: "http://localhost:8080//investment/96" {"cardNumber": "4802-8228-6113-7479", "bankName": "Project bank", "moneyCurrency": "USD", "time": "ONE_YEAR", "amount": 10});
* "http://localhost:8080//card/{id}"  – get card data from database.
* Example:( GET method: "http://localhost:8080//card/96");
* "http://localhost:8080//transaction/transfer"  – transfer money from one card to another.

## Moderator capabilities

can change info that is not connected to money operations <br><br>
**Available endpointsROLE_MODERATOR:**
* "http://localhost:8080//security/registration"  –  register.
* Example:( POST method: "http://localhost:8080//security/registration" {"firstName" : "testFirstName", "lastName" : "testLastName", "email": "testMail@gmail.com", "password": "testPassword1234"});
* "http://localhost:8080//security" –  login.
* Example:( POST method: "http://localhost:8080//security" {"email": "testMail@gmail.com", "password": "testPassword"});
* "http://localhost:8080//client/first-name/{id}" –  change first name.
* Example:( PUT method: "http://localhost:8080//client/first-name/96" {"firstName": "newFirstName"});
* "http://localhost:8080//client/last-name/{id}"  –  change last name.
* Example:( PUT method: "http://localhost:8080//client/last-name/96" {"lastName": "newLastName"});
* "http://localhost:8080//client/email/{id}"  –  change email.
* Example:( PUT method: "http://localhost:8080//client/email/96" {"email": "newMail@gmail.com"});
* "http://localhost:8080//client/password/{id}"  –  change password.
* Example:( PUT method: "http://localhost:8080//client/password/96" {"password": "newPassword"});
* "http://localhost:8080//client/{id}"  –  get client data from database.
* Example:( PUT method: "http://localhost:8080//client/1");
* "http://localhost:8080//bank/create-card/{id}"  –  register card in chosen bank.
* Example:( POST method: "http://localhost:8080//bank/create-card/96" {"bankName": "Project bank", "moneyCurrency": "USD"});
* "http://localhost:8080//investment/{id}"  –  get investment data from database.
* Example:( PUT method: "http://localhost:8080//investment/96")
* "http://localhost:8080//investment/{id}"  –  make an investment in chosen bank using registered card.
* Example:( POST method: "http://localhost:8080//investment/96" {"cardNumber": "4802-8228-6113-7479", "bankName": "Project bank", "moneyCurrency": "USD", "time": "ONE_YEAR", "amount": 10});
* "http://localhost:8080//card/{id}"  –  get card data from database.
* Example:( GET method: "http://localhost:8080//card/96");
* "http://localhost:8080//transaction/transfer"  –  transfer money from one card to another.
* Example:( PUT method: "http://localhost:8080//transaction/transfer" {"cardSender": "8888-2433-7538-9155", "cardReceiver": "1111-1111-1111-1111", "amount": 2, "moneyCurrency": "USD"});

* "http://localhost:8080//client"  –  get all clients data from database.
* Example:( GET method: "http://localhost:8080//client");
* "http://localhost:8080//client"  –  create client to database.
* Example:( POST method: "http://localhost:8080//client" {"firstName": "testFirstName", "lastName": "testLastName"});
* "http://localhost:8080//client"  –  change client.
* Example:( PUT method: "http://localhost:8080//client" {"id" = 1, "firstName": "newFirstName", "lastName": "newLastName", "created": "2023-09-25T23:48:44.273+00:00"});
* "http://localhost:8080//client/{id}"  –  delete client from database.
* Example:( DELETE method: "http://localhost:8080//client/1");
* "http://localhost:8080//bank"  –  get all banks data from database.
* Example:( GET method: "http://localhost:8080//bank");
* "http://localhost:8080//bank/{id}"  –  get bank by id from database.
* Example:( GET method: "http://localhost:8080//bank/1");
* "http://localhost:8080//bank"  –  change bank.
* Example:( PUT method: "http://localhost:8080//bank" {"id" = 1, "bankName": "Project bank", "commission": 0.01, "created": "2023-09-25T23:48:44.273+00:00"});
* "http://localhost:8080//bank/{id}"  –  delete bank from database.
* Example:( DELETE method: "http://localhost:8080//bank/1");
* "http://localhost:8080//file/checks/{user}/{filename}"  –  get file from database.
* Example:( GET method: "http://localhost:8080//file/checks/8-Polina-Kozlova/check_24762_20231202_0000-00_7aa07740-891f-48ae-8325-dc27cea4b960.txt");
* "http://localhost:8080//file/upload/checks/{user}"  –  upload file to database.
* Example:( POST method: "http://localhost:8080//file/upload/checks/8-Polina-Kozlova"
* "http://localhost:8080//file/checks/{user}"  –  get all files from database.
* Example:( GET method: "http://localhost:8080//file/checks/8-Polina-Kozlova"  )
* "http://localhost:8080//file/checks/{user}/{filename}"  –  delete file from database.
* Example:( DELETE method: "http://localhost:8080//file/checks/8-Polina-Kozlova/check_24762_20231202_0000-00_7aa07740-891f-48ae-8325-dc27cea4b960.txt");

## ADMIN capabilities

**Available endpointsROLE_ADMIN:**

* "http://localhost:8080//security/registration"  –  register.
* Example:( POST method: "http://localhost:8080//security/registration" {"firstName" : "testFirstName", "lastName" : "testLastName", "email": "testMail@gmail.com", "password": "testPassword1234"});
* "http://localhost:8080//security" –  login.
* Example:( POST method: "http://localhost:8080//security" {"email": "testMail@gmail.com", "password": "testPassword"});
* "http://localhost:8080//client/first-name/{id}" –  change first name.
* Example:( PUT method: "http://localhost:8080//client/first-name/96" {"firstName": "newFirstName"});
* "http://localhost:8080//client/last-name/{id}"  –  change last name.
* Example:( PUT method: "http://localhost:8080//client/last-name/96" {"lastName": "newLastName"});
* "http://localhost:8080//client/email/{id}"  –  change email.
* Example:( PUT method: "http://localhost:8080//client/email/96" {"email": "newMail@gmail.com"});
* "http://localhost:8080//client/password/{id}"  –  change password.
* Example:( PUT method: "http://localhost:8080//client/password/96" {"password": "newPassword"});
* "http://localhost:8080//client/{id}"  –  get client data from database.
* Example:( PUT method: "http://localhost:8080//client/1");
* "http://localhost:8080//bank/create-card/{id}"  –  register card in chosen bank.
* Example:( POST method: "http://localhost:8080//bank/create-card/96" {"bankName": "Project bank", "moneyCurrency": "USD"});
* "http://localhost:8080//investment/{id}"  –  get investment data from database.
* Example:( PUT method: "http://localhost:8080//investment/96")
* "http://localhost:8080//investment/{id}"  –  make an investment in chosen bank using registered card.
* Example:( POST method: "http://localhost:8080//investment/96" {"cardNumber": "4802-8228-6113-7479", "bankName": "Project bank", "moneyCurrency": "USD", "time": "ONE_YEAR", "amount": 10});
* "http://localhost:8080//card/{id}"  –  get card data from database.
* Example:( GET method: "http://localhost:8080//card/96");
* "http://localhost:8080//transaction/transfer"  –  transfer money from one card to another.
* Example:( PUT method: "http://localhost:8080//transaction/transfer" {"cardSender": "8888-2433-7538-9155", "cardReceiver": "1111-1111-1111-1111", "amount": 2, "moneyCurrency": "USD"});
* "http://localhost:8080//client"  –  get all clients data from database.
* Example:( GET method: "http://localhost:8080//client");
* "http://localhost:8080//client"  –  create client to database.
* Example:( POST method: "http://localhost:8080//client" {"firstName": "testFirstName", "lastName": "testLastName"});
* "http://localhost:8080//client"  –  change client.
* Example:( PUT method: "http://localhost:8080//client" {"id" = 1, "firstName": "newFirstName", "lastName": "newLastName", "created": "2023-09-25T23:48:44.273+00:00"});
* "http://localhost:8080//client/{id}"  –  delete client from database.
* Example:( DELETE method: "http://localhost:8080//client/1");
* "http://localhost:8080//bank"  –  get all banks data from database.
* Example:( GET method: "http://localhost:8080//bank");
* "http://localhost:8080//bank/{id}"  –  get bank by id from database.
* Example:( GET method: "http://localhost:8080//bank/1");
* "http://localhost:8080//bank"  –  change bank.
* Example:( PUT method: "http://localhost:8080//bank" {"id" = 1, "bankName": "Project bank", "commission": 0.01, "created": "2023-09-25T23:48:44.273+00:00"});
* "http://localhost:8080//bank/{id}"  –  delete bank from database.
* Example:( DELETE method: "http://localhost:8080//bank/1");
* "http://localhost:8080//file/checks/{user}/{filename}"  –  get file from database.
* Example:( GET method: "http://localhost:8080//file/checks/8-Polina-Kozlova/check_24762_20231202_0000-00_7aa07740-891f-48ae-8325-dc27cea4b960.txt");
* "http://localhost:8080//file/upload/checks/{user}"  –  upload file to database.
* Example:( POST method: "http://localhost:8080//file/upload/checks/8-Polina-Kozlova"
* "http://localhost:8080//file/checks/{user}"  –  get all files from database.
* Example:( GET method: "http://localhost:8080//file/checks/8-Polina-Kozlova"  )
* "http://localhost:8080//file/checks/{user}/{filename}"  –  delete file from database.
* Example:( DELETE method: "http://localhost:8080//file/checks/8-Polina-Kozlova/check_24762_20231202_0000-00_7aa07740-891f-48ae-8325-dc27cea4b960.txt");

* "http://localhost:8080//investment"  –  get all investments data from database.
* Example:( GET method: "http://localhost:8080//investment");
* "http://localhost:8080//investment"  –  post investment to database.
* Example:( POST method: "http://localhost:8080//investment" {"investmentNumber" = 887193351, "cardId": "52", "bankId": 1, "moneyCurrency": "USD", "created" = "2023-09-25T23:48:44.273+00:00", "expired": "2025-09-25T23:48:44.273+00:00", "investedAmount": 0.00, "expectedAmount": 0.00, "time" = "TWO_YEARS", "clientId": "8"});
* "http://localhost:8080//investment"  –  change investment.
* Example:( PUT method: "http://localhost:8080//investment" {"id" = 85, "investmentNumber" = 887193351, "cardId": "52", "bankId": 1, "moneyCurrency": "USD", "created" = "2023-09-25T23:48:44.273+00:00", "expired": "2025-09-25T23:48:44.273+00:00", "investedAmount": 0.00, "expectedAmount": 0.00, "time" = "TWO_YEARS", "clientId": "8"});
* "http://localhost:8080//investment/{id}"  –  delete investment from database.
* Example:( DELETE method: "http://localhost:8080//investment/41");
* "http://localhost:8080//card"  –  get all cards data from database.
* Example:( GET method: "http://localhost:8080//card");
* "http://localhost:8080//card"  –  post card to database.
* Example:( POST method: "http://localhost:8080//card" {"cardNumber" = 1010-1111-1111-1111, "clientId": 8", "balance": 1000, "moneyCurrency": "USD", "cardType" : "Project bank");
* "http://localhost:8080//card"  –  change card.
* Example:( PUT method: "http://localhost:8080//card" {"id" : 52, "cardNumber" = 1111-1111-1111-1111, "clientId": 8", "created": "2023-09-25T23:48:44.273+00:00", "balance": 1000, "moneyCurrency": "BYN", "cardType" : "Project bank");
* "http://localhost:8080//card/{id}"  –  delete card from database.
* Example:( DELETE method: "http://localhost:8080//card/52");

## Additional features
Every 30 minutes applications checks clients investments and if it is expired, the money will be deposit on client card automatically