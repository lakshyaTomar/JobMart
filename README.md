# Job Portal Backend Service

A comprehensive Java Spring Boot backend service providing RESTful APIs for a job portal platform. This application allows job seekers and employers to register, post jobs, search for jobs, and manage profiles including resume uploads.

## Features

- **User Authentication System**
  - Secure JWT-based authentication
  - User registration for both job seekers and employers
  - Role-based authorization

- **Job Seekers Features**
  - Profile management
  - Resume upload, download, and deletion
  - Advanced job search with multiple filters
  - View detailed job listings

- **Employers Features**
  - Company profile management
  - Create, edit, and delete job listings
  - View all posted jobs

- **Job Listings**
  - Advanced search with multiple filters
  - Pagination for job results
  - Support for various job types (full-time, part-time, contract)
  - Remote job indicators

## Technology Stack

- **Framework**: Spring Boot 3.1.3
- **Language**: Java 21
- **Database**: PostgreSQL (via Spring Data JPA)
- **Security**: Spring Security with JWT
- **Build Tool**: Maven
- **Documentation**: Comprehensive API documentation

## Project Structure

The project follows a standard MVC architecture with:

- **Controllers**: Handle HTTP requests and responses
- **Services**: Implement business logic
- **Repositories**: Interface with the database
- **Entities**: Define the data models
- **DTOs**: Transfer data between processes
- **Enums**: Define constants and types
- **Exception Handlers**: Manage errors and exceptions
- **Security Configuration**: Configure authentication and authorization

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL database

### Installation and Setup

1. Clone the repository
2. Configure your database connection in `application.properties`
3. Run the application:
   ```
   ./mvnw spring-boot:run
   ```
4. The server will start at `http://localhost:5000`

## API Documentation

For detailed API documentation, see [API_DOCUMENTATION.md](API_DOCUMENTATION.md).

### Key Endpoints

- **Authentication**
  - `POST /api/auth/register/jobseeker` - Register as a job seeker
  - `POST /api/auth/register/employer` - Register as an employer
  - `POST /api/auth/login` - Login and get JWT token

- **User Management**
  - `GET /api/users/profile` - Get current user profile
  - `PUT /api/users/profile` - Update user profile
  - `POST /api/users/resume` - Upload resume (for job seekers)
  - `GET /api/users/resume` - Download resume
  - `DELETE /api/users/resume` - Delete resume

- **Job Management**
  - `POST /api/jobs` - Create a new job (for employers)
  - `GET /api/jobs` - Get all jobs with filters and pagination
  - `GET /api/jobs/{id}` - Get job by ID
  - `PUT /api/jobs/{id}` - Update job (for job owner)
  - `DELETE /api/jobs/{id}` - Delete job (for job owner)
  - `GET /api/jobs/employer` - Get all jobs by current employer

## Security

- Passwords are securely hashed
- JWT tokens for authentication with appropriate expiration
- Role-based access control
- Input validation
- Proper error handling

## File Storage

The system includes a secure file storage service for handling resume uploads:
- Files stored in a configured directory
- Proper content-type validation
- File size limits
- Unique file naming
- Exception handling for storage errors

## Database Schema

The application uses the following entity relationships:

- **User**: Base entity for all users
- **JobSeeker**: Extends User with job seeker-specific fields
- **Employer**: Extends User with employer-specific fields
- **Job**: Represents a job listing, linked to an Employer

## Testing

A test script is provided to validate the resume upload functionality:
- `test-resume-upload.sh`: Tests user registration, authentication, and resume upload/download/delete operations

## Error Handling

The application includes comprehensive exception handling:
- Custom exception classes
- Global exception handler
- Appropriate HTTP status codes
- Detailed error messages

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgements

- Spring Boot and Spring Security for the robust framework
- JWT for secure authentication
- PostgreSQL for reliable data storage