# URL shortener service

## How to build service
To build service clone project, go to root project root folder and execute command
```
mvn clean package
```
## How to run service
After build finished you can run service with following commands:
```
cd target/
java -jar shortener-0.0.4.RELEASE.jar
```
With default settings service responds on port 8088 with http protocol

## How to use service
You can import postman collection from file shortener.postman.json, or use curl

You can see API on the Apiary: https://urlshortener8.docs.apiary.io/#