# maha-coding-challenge

A webservice to get price for watches. 

## Development Environment 

* OS: Mac OS X
* Editor: Eclipse 2020
* JAVA version 1.8.0_191
* Maven version 3.6.3

## Helper
Started with Spring Initializr. The Initializr offers a fast way to pull in all the dependencies you need for a web-service application and did a lot of the setup. This example needs the Spring Web dependency.


## Technologies

* Spring 2.3  
* SpringBoot and SpringBootTest
* Atlas MongoDB cloud service
* Swagger UI


## Setup
Clone git repository: https://github.com/islanderz/maha-coding-challenge.git

Build with maven: maven clean install

Execute main application 'WatchesApiApplication' or run the packaged jar/war

Access Swagger UI through browser: http://localhost:8080/swagger-ui.html

Swagger provides a very handy way to visualize and interact with the API’s deployed, and to try out with sample requests. It was really helpful to test the application.


## Approach
The  first step was to setup a base web service framework using SpringBoot and swagger. Second, the atlas spring dependences and properties file were added, to fetch the data from the cloud database. Application and data should be decoupled. The controller service handles the core business logic of the application. SpringBoot mongoDB handles mapping the data to the Java objects without having to write any code. 

The calculation of the price is developed as a stand-alone function, taking the list of watches bought and list of prices. This service is unit tested to make sure that the logic works fine and help to optimise and refactor the code. 

Since data connectivity is a point of failure, a circuit-breaker pattern was implemented, so that the data can be pulled from a local file, should the database link be down.

The integration tests were very handy to test incrementally the development of the webservice and the end-to-end request/response for simple and less simple scenarios - for example, when combination of watches without discount, and discounted plus non-discounted price of the same watch are involved.  


## Improvements
For the webservice request, wrong watch IDs gave "error": "Internal Server Error". Additional validations to be added.

Another improvement on performance is on the operation to get all the watches to compute the total price. Istead of fetching all the watches, we could use a 'query by IDs' to retrieve only the watches which are part of the 'order' and for which the details are needed. 






