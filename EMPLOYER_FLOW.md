# Employer User Flow

## Overview
This document illustrates the complete workflow for employers in the Job Portal application, from registration to managing job postings and applicants.

## Employer Journey Map

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                EMPLOYER JOURNEY                                          │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                REGISTRATION & AUTHENTICATION                             │
│                                                                                         │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  Register as  │        │  Receive JWT  │        │  Login with   │                  │
│   │  Employer     │───────►│     Token     │◄───────│  Credentials  │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                COMPANY PROFILE MANAGEMENT                                │
│                                                                                         │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  Complete     │        │  Add Company  │        │  Upload       │                  │
│   │  Profile      │───────►│  Details      │───────►│  Company Logo │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                JOB POSTING MANAGEMENT                                    │
│                                                                                         │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  Create New   │        │  Fill Job     │        │  Review &     │                  │
│   │  Job Listing  │───────►│  Details      │───────►│  Publish      │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                    │                        │                           │
│                                    ▼                        ▼                           │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  Edit/Update  │        │  Deactivate   │        │  View Job     │                  │
│   │  Job Listings │◄───────│  Listings     │◄───────│  Analytics    │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                APPLICANT MANAGEMENT                                      │
│                                                                                         │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  View         │        │  Review       │        │  Download     │                  │
│   │  Applicants   │───────►│  Applications │───────►│  Resumes      │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                    │                        │                           │
│                                    ▼                        ▼                           │
│   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐                  │
│   │  Contact      │        │  Update       │        │  Reject       │                  │
│   │  Applicants   │◄───────│  Status       │───────►│  Applications │                  │
│   └───────────────┘        └───────────────┘        └───────────────┘                  │
│                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘
```

## Technical Flow for Employer Operations

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
                        │               │ Employer      │                              │
                        │               │ Repository    │──────────────────────────────┘
                        │               └───────────────┘
                        ▼
                 ┌───────────────┐
                 │ JWT Token     │
                 │ Generation    │
                 └───────────────┘
```

### 2. Job Posting Flow

```
┌─────────┐     ┌───────────────┐     ┌───────────────┐     ┌───────────────┐
│         │     │               │     │               │     │               │
│ Client  │────►│JobController  │────►│ JobService    │────►│JobRepository  │
│         │     │               │     │               │     │               │
└─────────┘     └───────────────┘     └───────────────┘     └───────────────┘
                        │                     │                     │
                        │                     │                     ▼
                        │                     │              ┌───────────────┐
                        │                     │              │               │
                        │                     │              │   Database    │
                        │                     │              │               │
                        │                     │              └───────────────┘
                        │                     │                     ▲
                        │                     ▼                     │
                        │               ┌───────────────┐           │
                        │               │ Employer      │           │
                        │               │ Repository    │───────────┘
                        │               └───────────────┘
                        ▼
                 ┌───────────────┐
                 │ Response with │
                 │ Job Details   │
                 └───────────────┘
```

### 3. Applicant Review Flow

```
┌─────────┐     ┌───────────────┐     ┌───────────────┐     ┌───────────────┐
│         │     │               │     │               │     │               │
│ Client  │────►│ApplicationCtrl│────►│ApplicationSvc │────►│ApplicationRepo│
│         │     │               │     │               │     │               │
└─────────┘     └───────────────┘     └───────────────┘     └───────────────┘
    ▲                   │                     │                     │
    │                   │                     │                     ▼
    │                   │                     │              ┌───────────────┐
    │                   │                     │              │               │
    │                   │                     │              │   Database    │
    │                   │                     │              │               │
    │                   │                     │              └───────────────┘
    │                   │                     │                     │
    │                   ▼                     ▼                     │
    │           ┌───────────────┐     ┌───────────────┐            │
    │           │ FileController│     │ JobSeeker     │◄───────────┘
    │           └───────────────┘     │ Repository    │
    │                   │             └───────────────┘
    │                   ▼                    │
    │           ┌───────────────┐            │
    │           │ FileStorage   │◄───────────┘
    │           │ Service       │
    │           └───────────────┘
    │                   │
    │                   ▼
    │           ┌───────────────┐
    └───────────│ Applicant Data│
                │ with Resume   │
                └───────────────┘
```

## API Endpoints for Employers

### Authentication
- `POST /api/auth/register/employer` - Register as an employer
- `POST /api/auth/login` - Login and receive JWT token

### Profile Management
- `GET /api/users/profile` - Get employer profile
- `PUT /api/users/profile/update` - Update profile information
- `POST /api/employers/company-logo` - Upload company logo
- `GET /api/employers/company-logo` - Get company logo

### Job Management
- `POST /api/jobs` - Create a new job listing
- `GET /api/jobs/my-jobs` - List all jobs posted by the employer
- `GET /api/jobs/{id}` - Get specific job details
- `PUT /api/jobs/{id}` - Update job details
- `DELETE /api/jobs/{id}` - Delete/deactivate job posting
- `PUT /api/jobs/{id}/activate` - Activate a job posting
- `PUT /api/jobs/{id}/deactivate` - Deactivate a job posting

### Application Management
- `GET /api/jobs/{jobId}/applications` - Get all applications for a specific job
- `GET /api/applications/{id}` - Get application details
- `PUT /api/applications/{id}/status` - Update application status
- `GET /api/applications/{id}/resume` - Download applicant's resume
- `POST /api/applications/{id}/notes` - Add notes to an application

## Database Schema (Employer Perspective)

```
┌────────────────────────┐       ┌────────────────────────┐
│ User                   │       │ Job                    │
├────────────────────────┤       ├────────────────────────┤
│ id (PK)                │       │ id (PK)                │
│ email                  │       │ title                  │
│ password               │       │ description            │
│ name                   │       │ company                │
│ role                   │       │ location               │
│ phone                  │       │ salary                 │
│ createdAt              │       │ jobType                │
│ updatedAt              │       │ remote                 │
└────────────────────────┘       │ requirements           │
        ▲                        │ benefits               │
        │                        │ postedDate             │
        │                        │ expirationDate         │
┌───────┴────────────────┐       │ active                 │
│ Employer               │       │ contactEmail           │
├────────────────────────┤       │ contactPhone           │
│ id (PK, FK to User)    │◄──────┤ employer_id (FK)       │
│ companyName            │       └────────────────────────┘
│ industry               │              ▲
│ companySize            │              │
│ companyDescription     │              │
│ website                │              │
│ logoPath               │       ┌──────┴─────────────────┐
└────────────────────────┘       │ Application            │
                                 ├────────────────────────┤
                                 │ id (PK)                │
                                 │ jobSeeker_id (FK)      │
                                 │ job_id (FK)            │
                                 │ resumeId               │
                                 │ status                 │
                                 │ appliedDate            │
                                 │ notes                  │
                                 └────────────────────────┘
```

## Job Posting Process

1. **Job Creation**
   - Employer fills out job details form
   - Validates input fields (title, description, requirements, etc.)
   - Sets job type (FULL_TIME, PART_TIME, CONTRACT)
   - Adds salary information and benefits
   - Sets job location or marks as remote
   - Adds contact information for applicants

2. **Publication Process**
   - Job is validated by the system
   - Posted date is automatically set to current date
   - Expiration date is set (default 30 days)
   - Job is marked as active and made visible in search results
   - Employer receives confirmation of successful publication

3. **Job Management**
   - Jobs can be edited/updated at any time
   - Job listings can be deactivated temporarily
   - Expired jobs are automatically marked as inactive
   - Analytics show view count and application count

## Security Considerations for Employers

1. **Business Data Protection**
   - Company profile information is secured
   - Job posting data is protected from unauthorized modification
   - Contact information is only shared with applicants

2. **Authentication Security**
   - JWT tokens expire after 24 hours
   - Password requirements: minimum 8 characters, mixed case, numbers, special characters
   - Account lockout after multiple failed login attempts

3. **Authorization Controls**
   - Employers can only access and modify their own company profiles and job listings
   - Role-based access control prevents access to job seeker-only functions
   - Verification processes ensure employers are legitimate companies