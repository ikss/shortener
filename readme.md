# URL shortener service
Service helps to shorten URLs and retrive statistic of usage these short links.

It requires java 1.8 or higher and compatible with java 10.0.2

## How to build service
To build service clone project, go to project root folder and execute command
```
mvn clean package
```
## How to run service
After build finished you can run service with following commands:
```
cd target/
java -jar shortener-0.0.5.RELEASE.jar
```
With default settings service responds on port 8088 with http protocol

## How to use service
You can import postman collection from file *shortener.postman.json*, or use curl.
With default settings service responds on 8088 port with http protocol.

You can see API on the Apiary: https://urlshortener8.docs.apiary.io/#
