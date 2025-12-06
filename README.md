#  BookMart Backend API

![CI/CD Pipeline](https://github.com/rangabharathkumar/Bookmart-Backend/workflows/CI-CD-Pipeline/badge.svg)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.12-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)

Online Bookstore Backend API built with Spring Boot, featuring JWT authentication, RESTful endpoints, and automated CI/CD.

##  Features

-  RESTful API for book management
- JWT-based authentication & authorization
-  Role-based access control (USER/ADMIN)
-  MySQL database integration
-  Docker containerization
-  Automated CI/CD pipeline
-  Comprehensive test coverage
- API documentation with Swagger/OpenAPI

##  Tech Stack

- **Framework:** Spring Boot 3.4.12
- **Language:** Java 21
- **Database:** MySQL 8.0
- **Security:** Spring Security + JWT
- **Build Tool:** Maven
- **Containerization:** Docker + Jib
- **CI/CD:** GitHub Actions
- **API Docs:** SpringDoc OpenAPI

##  Prerequisites

- Java 21 or higher
- Maven 3.8+
- MySQL 8.0+ (or use Docker Compose)
- Docker Desktop (optional)

##  Quick Start

### Option 1: Run with Docker Compose (Recommended)

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/bookmart-backend.git
cd bookmart-backend

# Start the application with MySQL
docker-compose up -d

# Access the API
curl http://localhost:8080/api/books
```

### Option 2: Run Locally

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/bookmart-backend.git
cd bookmart-backend

# Configure MySQL in application.properties
# Update database credentials

# Build and run
mvn clean install
mvn spring-boot:run
```

##  Configuration

Update `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/bookmart
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT
jwt.secret=your-secret-key-here
jwt.expiration=86400000
```

##  API Documentation

Once the application is running, access the Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

##  Testing

```bash
# Run all tests
mvn clean test

# Run tests with coverage
mvn clean verify

# View coverage report
open target/site/jacoco/index.html
```

##  Docker

### Build Docker Image

```bash
# Using Jib (recommended)
mvn clean compile jib:build

# Using Dockerfile
docker build -t bookmart-backend .
```

### Run Docker Container

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/bookmart \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  bookmart-backend:latest
```

## CI/CD Pipeline

The project uses GitHub Actions for automated CI/CD:

1. **Test**: Runs unit and integration tests
2. **Build**: Compiles and packages the application
3. **Docker**: Builds and pushes Docker image to Docker Hub
4. **Quality**: Generates code coverage reports

### Setup CI/CD

1. Fork this repository
2. Add GitHub Secrets:
   - `DOCKERHUB_USERNAME`
   - `DOCKERHUB_TOKEN`
3. Push to `main` branch to trigger the pipeline

##  Project Structure

```
bookmart-backend/
├── src/
│   ├── main/
│   │   ├── java/com/bookmart/
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── service/        # Business logic
│   │   │   ├── repository/     # Data access
│   │   │   ├── model/          # Entity classes
│   │   │   ├── dto/            # Data transfer objects
│   │   │   ├── security/       # Security configuration
│   │   │   └── exception/      # Exception handling
│   │   └── resources/
│   │       └── application.properties
│   └── test/                   # Unit & integration tests
├── .github/
│   └── workflows/
│       └── ci-cd.yml          # CI/CD pipeline
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

##  Security

- JWT tokens for authentication
- Password encryption with BCrypt
- Role-based access control
- CORS configuration
- SQL injection prevention

##  Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

##  License

This project is licensed under the MIT License.

##  Author

**Your Name**
- GitHub: [@YOUR_USERNAME](https://github.com/YOUR_USERNAME)

##  Acknowledgments

- Spring Boot team for the amazing framework
- All contributors who helped improve this project

---

 Star this repository if you find it helpful!
