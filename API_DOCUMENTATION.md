# Job Portal API Documentation

This document provides comprehensive information about the RESTful API endpoints available in the Job Portal application. The API enables job seekers and employers to interact with the platform for job posting, searching, and user management.

## Base URL

```
http://localhost:5000
```

## Authentication

Most endpoints require authentication. Use the following header format:

```
Authorization: Bearer <token>
```

---

## Authentication APIs

### Register as Job Seeker

**Endpoint:** `POST /api/auth/register/jobseeker`

**Description:** Register a new user with the job seeker role.

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "yourpassword",
  "skills": "Java, Spring Boot, React",
  "experience": "5 years of experience in software development",
  "education": "Bachelor's in Computer Science"
}
```

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "JOB_SEEKER",
  "phone": null,
  "skills": "Java, Spring Boot, React",
  "experience": "5 years of experience in software development",
  "education": "Bachelor's in Computer Science",
  "resume": null
}
```

### Register as Employer

**Endpoint:** `POST /api/auth/register/employer`

**Description:** Register a new user with the employer role.

**Request Body:**
```json
{
  "name": "Jane Smith",
  "email": "jane@techcompany.com",
  "password": "yourpassword",
  "companyName": "Tech Company",
  "industry": "Information Technology",
  "companyDescription": "A leading technology company",
  "website": "https://techcompany.com"
}
```

**Response:**
```json
{
  "id": 2,
  "name": "Jane Smith",
  "email": "jane@techcompany.com",
  "role": "EMPLOYER",
  "phone": null,
  "companyName": "Tech Company",
  "industry": "Information Technology",
  "companyDescription": "A leading technology company",
  "website": "https://techcompany.com"
}
```

### Login

**Endpoint:** `POST /api/auth/login`

**Description:** Authenticate a user and get a JWT token.

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "yourpassword"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "email": "john@example.com",
  "role": "JOB_SEEKER"
}
```

---

## User APIs

### Get Current User Profile

**Endpoint:** `GET /api/users/profile`

**Description:** Get the profile information of the currently authenticated user.

**Authentication Required:** Yes

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "JOB_SEEKER",
  "phone": null,
  "skills": "Java, Spring Boot, React",
  "experience": "5 years of experience in software development",
  "education": "Bachelor's in Computer Science",
  "resume": "resume_1.pdf"
}
```

### Update User Profile

**Endpoint:** `PUT /api/users/profile`

**Description:** Update the profile information of the currently authenticated user.

**Authentication Required:** Yes

**Request Body (Job Seeker):**
```json
{
  "name": "John Doe",
  "phone": "1234567890",
  "skills": "Java, Spring Boot, React, Angular",
  "experience": "6 years of experience in software development",
  "education": "Bachelor's in Computer Science"
}
```

**Request Body (Employer):**
```json
{
  "name": "Jane Smith",
  "phone": "9876543210",
  "companyName": "Tech Company Inc.",
  "industry": "Information Technology",
  "companyDescription": "A leading global technology company",
  "website": "https://techcompany.com"
}
```

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "JOB_SEEKER",
  "phone": "1234567890",
  "skills": "Java, Spring Boot, React, Angular",
  "experience": "6 years of experience in software development",
  "education": "Bachelor's in Computer Science",
  "resume": "resume_1.pdf"
}
```

### Upload Resume

**Endpoint:** `POST /api/users/resume`

**Description:** Upload a resume (PDF file) for a job seeker.

**Authentication Required:** Yes (Job Seeker role only)

**Request:** Form Data with key "file" containing the PDF file.

**Response:**
```json
{
  "fileName": "resume_1.pdf",
  "fileDownloadUri": "http://localhost:5000/api/users/resume/",
  "fileType": "application/pdf",
  "size": 125698,
  "message": "Resume uploaded successfully"
}
```

### Download Resume

**Endpoint:** `GET /api/users/resume`

**Description:** Download the resume of the currently authenticated job seeker.

**Authentication Required:** Yes (Job Seeker role only)

**Response:** The PDF file for download.

### Delete Resume

**Endpoint:** `DELETE /api/users/resume`

**Description:** Delete the resume of the currently authenticated job seeker.

**Authentication Required:** Yes (Job Seeker role only)

**Response:**
```json
{
  "fileName": null,
  "fileDownloadUri": null,
  "fileType": null,
  "size": 0,
  "message": "Resume deleted successfully"
}
```

---

## Job APIs

### Post a New Job

**Endpoint:** `POST /api/jobs`

**Description:** Create a new job listing.

**Authentication Required:** Yes (Employer role only)

**Request Body:**
```json
{
  "title": "Senior Java Developer",
  "description": "We are looking for a senior Java developer with experience in Spring Boot.",
  "location": "New York, NY",
  "salary": 120000,
  "jobType": "FULL_TIME",
  "isRemote": true,
  "requirements": "5+ years of experience with Java, Spring Boot, and microservices",
  "responsibilities": "Develop and maintain Java applications, participate in code reviews",
  "benefits": "Health insurance, 401K, remote work options",
  "expirationDate": "2025-04-30"
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Senior Java Developer",
  "description": "We are looking for a senior Java developer with experience in Spring Boot.",
  "location": "New York, NY",
  "salary": 120000,
  "jobType": "FULL_TIME",
  "isRemote": true,
  "requirements": "5+ years of experience with Java, Spring Boot, and microservices",
  "responsibilities": "Develop and maintain Java applications, participate in code reviews",
  "benefits": "Health insurance, 401K, remote work options",
  "expirationDate": "2025-04-30",
  "companyName": "Tech Company Inc.",
  "industry": "Information Technology",
  "createdAt": "2025-03-28T10:30:00",
  "updatedAt": "2025-03-28T10:30:00"
}
```

### Get All Jobs (with Pagination and Filtering)

**Endpoint:** `GET /api/jobs`

**Description:** Get a paginated list of job listings with optional filtering criteria.

**Authentication Required:** No

**Query Parameters:**
- `page` (default: 0): Page number (zero-based)
- `size` (default: 10): Number of records per page
- `keyword`: Search keyword in title or description
- `location`: Job location
- `minSalary`: Minimum salary
- `maxSalary`: Maximum salary
- `jobType`: Job type (FULL_TIME, PART_TIME, CONTRACT)
- `isRemote`: Remote job flag (true/false)

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Senior Java Developer",
      "description": "We are looking for a senior Java developer with experience in Spring Boot.",
      "location": "New York, NY",
      "salary": 120000,
      "jobType": "FULL_TIME",
      "isRemote": true,
      "requirements": "5+ years of experience with Java, Spring Boot, and microservices",
      "responsibilities": "Develop and maintain Java applications, participate in code reviews",
      "benefits": "Health insurance, 401K, remote work options",
      "expirationDate": "2025-04-30",
      "companyName": "Tech Company Inc.",
      "industry": "Information Technology",
      "createdAt": "2025-03-28T10:30:00",
      "updatedAt": "2025-03-28T10:30:00"
    }
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

### Get Job by ID

**Endpoint:** `GET /api/jobs/{id}`

**Description:** Get a specific job listing by its ID.

**Authentication Required:** No

**Response:**
```json
{
  "id": 1,
  "title": "Senior Java Developer",
  "description": "We are looking for a senior Java developer with experience in Spring Boot.",
  "location": "New York, NY",
  "salary": 120000,
  "jobType": "FULL_TIME",
  "isRemote": true,
  "requirements": "5+ years of experience with Java, Spring Boot, and microservices",
  "responsibilities": "Develop and maintain Java applications, participate in code reviews",
  "benefits": "Health insurance, 401K, remote work options",
  "expirationDate": "2025-04-30",
  "companyName": "Tech Company Inc.",
  "industry": "Information Technology",
  "createdAt": "2025-03-28T10:30:00",
  "updatedAt": "2025-03-28T10:30:00"
}
```

### Update Job

**Endpoint:** `PUT /api/jobs/{id}`

**Description:** Update an existing job listing.

**Authentication Required:** Yes (Employer role and must be the owner of the job)

**Request Body:**
```json
{
  "title": "Senior Java Developer",
  "description": "Updated description for the senior Java developer position.",
  "location": "New York, NY",
  "salary": 125000,
  "jobType": "FULL_TIME",
  "isRemote": true,
  "requirements": "5+ years of experience with Java, Spring Boot, and microservices",
  "responsibilities": "Develop and maintain Java applications, participate in code reviews",
  "benefits": "Health insurance, 401K, remote work options, flexible hours",
  "expirationDate": "2025-05-15"
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Senior Java Developer",
  "description": "Updated description for the senior Java developer position.",
  "location": "New York, NY",
  "salary": 125000,
  "jobType": "FULL_TIME",
  "isRemote": true,
  "requirements": "5+ years of experience with Java, Spring Boot, and microservices",
  "responsibilities": "Develop and maintain Java applications, participate in code reviews",
  "benefits": "Health insurance, 401K, remote work options, flexible hours",
  "expirationDate": "2025-05-15",
  "companyName": "Tech Company Inc.",
  "industry": "Information Technology",
  "createdAt": "2025-03-28T10:30:00",
  "updatedAt": "2025-03-28T11:15:00"
}
```

### Delete Job

**Endpoint:** `DELETE /api/jobs/{id}`

**Description:** Delete a job listing.

**Authentication Required:** Yes (Employer role and must be the owner of the job)

**Response:**
```json
{
  "message": "Job with ID 1 deleted successfully"
}
```

### Get Jobs by Current Employer

**Endpoint:** `GET /api/jobs/employer`

**Description:** Get all job listings posted by the currently authenticated employer.

**Authentication Required:** Yes (Employer role)

**Response:**
```json
[
  {
    "id": 1,
    "title": "Senior Java Developer",
    "description": "We are looking for a senior Java developer with experience in Spring Boot.",
    "location": "New York, NY",
    "salary": 120000,
    "jobType": "FULL_TIME",
    "isRemote": true,
    "requirements": "5+ years of experience with Java, Spring Boot, and microservices",
    "responsibilities": "Develop and maintain Java applications, participate in code reviews",
    "benefits": "Health insurance, 401K, remote work options",
    "expirationDate": "2025-04-30",
    "companyName": "Tech Company Inc.",
    "industry": "Information Technology",
    "createdAt": "2025-03-28T10:30:00",
    "updatedAt": "2025-03-28T10:30:00"
  }
]
```

---

## Error Handling

The API returns appropriate HTTP status codes and error messages for different error scenarios:

- **400 Bad Request**: Invalid input data
- **401 Unauthorized**: Missing or invalid authentication
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **409 Conflict**: Resource already exists (e.g., email already in use)
- **417 Expectation Failed**: Upload file too large
- **500 Internal Server Error**: Server-side error

Example error response:
```json
{
  "status": 400,
  "message": "Validation error",
  "path": "uri=/api/auth/register/jobseeker",
  "timestamp": "2025-03-28T10:30:00",
  "errors": {
    "name": "Name is required",
    "email": "Must be a valid email address"
  }
}
```

---

## File Upload Constraints

- **Maximum file size**: 10MB
- **Allowed formats**: PDF files only for resumes

---

## Data Models

### User
- id: Long
- name: String
- email: String
- password: String (hashed)
- phone: String (optional)
- role: UserRole (ADMIN, JOB_SEEKER, EMPLOYER)
- active: Boolean
- createdAt: LocalDateTime
- updatedAt: LocalDateTime

### JobSeeker (extends User)
- skills: String
- experience: String
- education: String
- resume: String (file name)

### Employer (extends User)
- companyName: String
- industry: String
- companyDescription: String
- website: String

### Job
- id: Long
- title: String
- description: String
- location: String
- salary: Double
- jobType: JobType (FULL_TIME, PART_TIME, CONTRACT)
- isRemote: Boolean
- requirements: String
- responsibilities: String
- benefits: String
- expirationDate: LocalDate
- employer: Employer
- createdAt: LocalDateTime
- updatedAt: LocalDateTime