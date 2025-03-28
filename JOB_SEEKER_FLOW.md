# Job Seeker User Flow

## Overview
This document illustrates the complete workflow for job seekers in the Job Portal application, from registration to applying for jobs.

## Job Seeker Journey Map

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                JOB SEEKER JOURNEY                                        │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                REGISTRATION & AUTHENTICATION                             │
│                                                                                         │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  Register as  │        │  Receive JWT  │        │  Login with   │                  │
│   │  Job Seeker   │───────►│     Token     │◄───────│  Credentials  │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                PROFILE MANAGEMENT                                        │
│                                                                                         │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  Complete     │        │  Upload       │        │  Update       │                  │
│   │  Profile      │───────►│  Resume/CV    │───────►│  Skills       │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                JOB SEARCH & DISCOVERY                                    │
│                                                                                         │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  Search Jobs  │        │  Filter by    │        │  View Job     │                  │
│   │  by Keywords  │───────►│  Criteria     │───────►│  Details      │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                    │                        │                           │
│                                    ▼                        ▼                           │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  Save         │        │  View Employer│        │  Browse       │                  │
│   │  Favorites    │◄───────│  Profile      │◄───────│  Similar Jobs │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                APPLICATION PROCESS                                       │
│                                                                                         │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  Apply for    │        │  Select       │        │  Submit       │                  │
│   │  Job          │───────►│  Resume       │───────►│  Application  │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                                             │                           │
│                                    ┌───────────────┐        │                           │
│                                    │  Track        │◄───────┘                           │
│                                    │  Application  │                                    │
│                                    └───────────────┘                                    │
│                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘
```

## Technical Flow for Job Seeker Operations

### 1. Registration & Authentication Flow

```
┌─────────┐     ┌───────────────┐     ┌───────────────┐     ┌───────────────┐     ┌────────┐
│         │     │               │     │               │     │               │     │        │
│ Client  │────►│AuthController │────►│ AuthService   │────►│UserRepository │────►│Database│
│         │     │               │     │               │     │               │     │        │
└─────────┘     └───────────────┘     └───────────────┘     └───────────────┘     └────────┘
                        │                     │                                        ▲
                        │                     ▼                                        │
                        │               ┌───────────────┐                              │
                        │               │ JobSeeker     │                              │
                        │               │ Repository    │──────────────────────────────┘
                        │               └───────────────┘
                        ▼
                 ┌───────────────┐
                 │ JWT Token     │
                 │ Generation    │
                 └───────────────┘
```

### 2. Resume Upload Flow

```
┌─────────┐     ┌───────────────┐     ┌───────────────┐     ┌───────────────┐
│         │     │               │     │               │     │               │
│ Client  │────►│FileController │────►│FileStorage    │────►│ File System   │
│         │     │               │     │Service        │     │               │
└─────────┘     └───────────────┘     └───────────────┘     └───────────────┘
                        │                     │                      
                        │                     ▼                      
                        │               ┌───────────────┐            
                        │               │ JobSeeker     │            
                        │               │ Repository    │            
                        │               └───────────────┘            
                        │                     │                      
                        │                     ▼                      
                        │               ┌───────────────┐            
                        └──────────────►│ Database      │            
                                        │ (Metadata)    │            
                                        └───────────────┘            
```

### 3. Job Search Flow

```
┌─────────┐     ┌───────────────┐     ┌───────────────┐     ┌───────────────┐
│         │     │               │     │               │     │               │
│ Client  │────►│JobController  │────►│ JobService    │────►│JobRepository  │
│         │     │               │     │               │     │               │
└─────────┘     └───────────────┘     └───────────────┘     └───────────────┘
    ▲                                        │                     │
    │                                        │                     ▼
    │                                        │              ┌───────────────┐
    │                                        │              │               │
    │                                        │              │   Database    │
    │                                        │              │               │
    │                                        │              └───────────────┘
    │                                        │                     │
    │                                        │                     │
    │                                        ▼                     │
    │                               ┌───────────────┐              │
    └───────────────────────────────│ Filtered &    │◄─────────────┘
                                    │ Paginated Data│
                                    └───────────────┘
```

## API Endpoints for Job Seekers

### Authentication
- `POST /api/auth/register/job-seeker` - Register as a job seeker
- `POST /api/auth/login` - Login and receive JWT token

### Profile Management
- `GET /api/users/profile` - Get job seeker profile
- `PUT /api/users/profile/update` - Update profile information
- `GET /api/resumes/{id}` - View uploaded resume
- `DELETE /api/resumes/{id}` - Delete resume

### Resume Management
- `POST /api/resumes/upload` - Upload new resume
- `GET /api/resumes/my-resumes` - List all uploaded resumes

### Job Search
- `GET /api/jobs/search` - Search jobs with filters
  - Parameters:
    - `keyword` - Search in title and description
    - `location` - Filter by location
    - `jobType` - Filter by job type (FULL_TIME, PART_TIME, CONTRACT)
    - `minSalary` - Filter by minimum salary
    - `remote` - Filter for remote positions
    - `page` - Page number
    - `size` - Results per page
- `GET /api/jobs/{id}` - Get detailed job information

### Job Application
- `POST /api/applications/apply/{jobId}` - Apply for a job
- `GET /api/applications/my-applications` - View my job applications
- `GET /api/applications/{id}` - Get application details

## Database Schema (Job Seeker Perspective)

```
┌────────────────────────┐       ┌────────────────────────┐
│ User                   │       │ Job                    │
├────────────────────────┤       ├────────────────────────┤
│ id (PK)                │       │ id (PK)                │
│ email                  │       │ title                  │
│ password               │       │ description            │
│ name                   │       │ location               │
│ role                   │       │ salary                 │
│ phone                  │       │ jobType                │
│ createdAt              │       │ remote                 │
│ updatedAt              │       │ requirements           │
└────────────────────────┘       │ benefits               │
        ▲                        │ postedDate             │
        │                        │ expirationDate         │
        │                        │ active                 │
┌───────┴────────────────┐       │ employer_id (FK)       │
│ JobSeeker              │       └────────────────────────┘
├────────────────────────┤              ▲
│ id (PK, FK to User)    │              │
│ skills                 │       ┌──────┴─────────────────┐
│ resumePath             │       │ Application            │
│ experience             │       ├────────────────────────┤
│ education              │       │ id (PK)                │
└────────────────────────┘       │ jobSeeker_id (FK)      │
        │                        │ job_id (FK)            │
        │                        │ resumeId               │
        │                        │ status                 │
        │                        │ appliedDate            │
        │                        │ notes                  │
        ▼                        └────────────────────────┘
┌────────────────────────┐
│ Resume                 │
├────────────────────────┤
│ id (PK)                │
│ jobSeeker_id (FK)      │
│ fileName               │
│ filePath               │
│ uploadDate             │
│ fileSize               │
└────────────────────────┘
```

## Security Considerations for Job Seekers

1. **Personal Data Protection**
   - Profile information is encrypted at rest
   - Resume files are stored securely
   - Only authorized employers can see job seeker contact information

2. **Authentication Security**
   - JWT tokens expire after 24 hours
   - Password requirements: minimum 8 characters, mixed case, numbers, special characters
   - Account lockout after multiple failed login attempts

3. **Authorization Controls**
   - Job seekers can only access and modify their own profiles and resumes
   - Role-based access control prevents access to employer-only functions