# Coding Challenge for Zendesk
Intern Coding Challenge for Zendesk. A simple CLI based ticket viewing application that connects to the Zendesk API.

* Author : Aleksandar Bojanic
* Language : Java

## Installation and Usage

Some requirements and libraries:

* JDK 1.17 (while older JDKs may work, for best/safest functionality please try to use the 1.17 version)
* JUnit Jupiter 5
* json-20210307.jar
* codehaus.jackson.mapper.asl

How to run this app (on Windows)
* Clone this repository or download it onto your computer;
* Open this project in the IDE of your choice;
* Select the Main.java file and Run. 
* To be able to actually view the tickets, you will need to enter a valid subdomain, email, and password.

In order to run the tickets, you have to ensure your account has been correctly setup, (including making sure your account supports for basic authentication, and also that the database has imported the test tickets described [here](https://gist.github.com/svizzari/c7ffed8e10d3a456b40ac9d18f34289c). The detailed instructions for both have been included in the Zendesk Coding Challenge instructions pdf.

In order to run the JUnit tests, please look in the TestResources.txt file, and replace the credentials placeholders with your own.

## Insights
With more time I would definitely like to expand the coverage of my tests to look into special edge cases, yes I did test them manually but it would be good practice to have a lot more of them covered in the automated JUnit tests.

If there are any sort of issues with the project please do not hesitate to contact me through my email, which is on my GitHub account.
