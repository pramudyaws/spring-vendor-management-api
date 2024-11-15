# Spring Vendor Management API
**Spring Vendor Management API** provides backend solutions for some operations, such as vendor management and user authentication/authorization

## Project setup
1. This project uses Java 17 and PostgreSQL. Please ensure you have these installed on your local computer. (If there is an error when running the project, please use PostgreSQL 15)
2. Use this command to clone the project: 
```bash
$ git clone https://github.com/pramudyaws/spring-vendor-management-api.git
```
3. Create PostgreSQL database in your local and use any DB name you like (example: spring_vendor).

## Environment setup
Create `.env` file in the root of your project. Fill the `.env` file with required attributes that is specified in `.env.example` file.

For example, your `.env` should look like this:
```
PORT=8080

# JWT configuration and expiration time in hours
JWT_SECRET_KEY=secretkey
JWT_EXPIRATION_TIME=24

# Database configuration
DB_URL=jdbc:postgresql://localhost:5432/spring_vendor
DB_USERNAME=postgres
DB_PASSWORD=password
```

> Note for `.env` file:
> - To generate `JWT_SECRET_KEY`, you can use [JwtSecret.com](https://jwtsecret.com/generate)
> - For database configuration, please ensure your DB credentials, such as DB name, port, username, and password.

## Run the project
There are 2 ways to run the project:
1. Use Visual Studio Code or IntelliJ IDEA to run the project
2. Install Maven in your local computer ([Installation Tutorial](https://youtu.be/YTvlb6eny_0?si=ntMC-PCp_T5x9zRL)). Open this project in CMD or Terminal, then use this command to run the project:
```bash
$ mvn spring-boot:run
```

## API Documentation
After you run the project, visit `http://localhost:{PORT}/swagger-ui/index.html` on your browser to see the Swagger API Documentation. Request body needed for some endpoints is already set on the Swagger API.

> Note: 
> - Replace `{PORT}` with the actual port number defined in your `.env` file.
> - For endpoints inside `Vendor Management` tag, you need to authorize the Swagger using `accessToken`. The `accessToken` can be retrieved after a successful login endpoint call.

## Rate Limiter
In this application, I use `Bucket4j` to implement rate limiter. I created `RateLimiterFilter` and use the filter in `SecurityConfig`. This filter works whenever there is a request, it will check if the bucket still have remaining token to proceed the request or not. The bucket will reset the token every second. 

I set this project to have global rate limit: 10 requests per second (check `rate.limiter.requests.per.second` attribute in `application.properties`). To check whether the project can only receive 10 requests per second, you can call endpoint `/rate-limit-test` with POST method. Put `requestCount` in your JSON payload (request body). The endpoint will return number of successful and failed requests.

## Technologies
- **Java**: A widely-used, secure programming language
- **Spring Boot**: A framework for fast Java app development
- **JWT**: JSON Web Token (JWT) for authentication and authorization
- **PostgreSQL**: Used for database management
- **Swagger**: Used for API documentations
