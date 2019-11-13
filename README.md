# Authentication Service

Kata for training

## 1. Gradle clean & build
### Windows
```
gradlew.bat clean build
```

### MacOS / Linux
```
./gradlew clean build
``` 

## 2. Run application
### a. Local SpringBoot jar
#### Windows
```
gradlew.bat -p authentication-service-core bootRun
```

#### MacOS / Linux
```
./gradlew -p authentication-service-core bootRun
``` 

### b. Docker
```
docker-compose up -d --build
```

## 3. Access [Swagger UI - http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)