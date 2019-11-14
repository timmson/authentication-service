# Authentication Service

Kata for training

## Branches
- **v1-manual-e2e** - Manual E2E
- **v2-bad-cucumber**  - Automated E2E, IT language
- **v3-good-cucumber**  - Automated E2E, user language
- **v4-component-n-adapter**  - +Component tests with Adapter
- **master** - +Unit tests

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