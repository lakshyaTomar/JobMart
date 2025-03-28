#!/bin/bash

# Base URL
BASE_URL="http://localhost:5000"

# Create test directory for resume
mkdir -p test_files

# Create a test PDF file for the resume
echo "This is a test resume" > test_files/test_resume.pdf

# Step 1: Register a new Job Seeker
echo "Step 1: Registering a new Job Seeker..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register/jobseeker" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "testuser@example.com",
    "password": "password123",
    "skills": "Java, Spring Boot",
    "experience": "5 years of experience",
    "education": "Bachelor in Computer Science"
  }')
echo $REGISTER_RESPONSE | jq . || echo $REGISTER_RESPONSE

# Step 2: Login to get JWT token
echo -e "\nStep 2: Logging in to get JWT token..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "password123"
  }')
echo $LOGIN_RESPONSE | jq . || echo $LOGIN_RESPONSE

# Extract token from login response
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*' | grep -o '[^"]*$')
echo "Token: $TOKEN"

# Step 3: Upload a resume
echo -e "\nStep 3: Uploading resume..."
UPLOAD_RESPONSE=$(curl -s -X POST "$BASE_URL/api/users/resume" \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@test_files/test_resume.pdf")
echo $UPLOAD_RESPONSE | jq . || echo $UPLOAD_RESPONSE

# Step 4: Get user profile to check if resume is updated
echo -e "\nStep 4: Getting user profile to verify resume was updated..."
PROFILE_RESPONSE=$(curl -s -X GET "$BASE_URL/api/users/profile" \
  -H "Authorization: Bearer $TOKEN")
echo $PROFILE_RESPONSE | jq . || echo $PROFILE_RESPONSE

# Step 5: Download the resume
echo -e "\nStep 5: Downloading resume..."
curl -s -X GET "$BASE_URL/api/users/resume" \
  -H "Authorization: Bearer $TOKEN" \
  -o "downloaded_resume.pdf"

echo -e "\nResume downloaded as downloaded_resume.pdf"
echo -e "\nContent of downloaded resume:"
cat downloaded_resume.pdf

# Step 6: Delete the resume
echo -e "\nStep 6: Deleting resume..."
DELETE_RESPONSE=$(curl -s -X DELETE "$BASE_URL/api/users/resume" \
  -H "Authorization: Bearer $TOKEN")
echo $DELETE_RESPONSE | jq . || echo $DELETE_RESPONSE

# Clean up
rm -rf test_files
rm -f downloaded_resume.pdf

echo -e "\nTest completed!"