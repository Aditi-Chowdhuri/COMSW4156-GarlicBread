# Advanced Software Engineering Project

### Team Members:

- **Abhishek Paul** (ap4623)
- **Puja Singla** (ps3467)
- **Rahaf Bin Muhanna** (rb3713)
- **Aditi Chowdhuri** (anc2207)

### Instructor:

- **Gail Kaiser**

## Overview

This project proposes a service to improve accessibility for elderly individuals
and persons with disabilities by enabling organizations to share detailed
information about their ADA compliance and other facilities. The goal is to
foster inclusivity by helping users find accessible venues and resources and
schedule visits based on availability. Additionally, the service facilitates
volunteer support for these visits.

### Key Features:

- **Organizational Accessibility**: Organizations can post infrastructure
  details (e.g., ramps, elevators) and resource availability (e.g.,
  wheelchairs).
- **User Scheduling**: Individuals can schedule visits based on the availability
  of accessibility resources.
- **Volunteer Coordination**: Volunteers can sign up to assist disabled
  individuals and senior citizens during their visits.
- **Flexible Data Schema**: Organizations across different industries (e.g.,
  schools, subways, theaters) can define custom data structures to suit their
  needs.
- **Real-Time Updates**: Facilities and resource availability are updated in
  real-time, enabling informed decision-making for users.

## Clients

The possible clients who would likely use our service includes:

1. **Mobile App for Volunteers**:  
   Volunteers can browse nearby organizations, view scheduled visits, and
   request to assist users.

2. **Mobile App for Elderly and Disabled Users**:  
   This app lists organizations offering accessible resources and facilities,
   allowing users to track live availability and schedule visits.

3. **Web Portal for Organizations**:  
   Organizations can manage their facilities' accessibility information, track
   scheduled visits, and assign volunteers.

## Auth Endpoints

### 1. **POST `/organisation/login`**

- **Description**: Handles login requests for organisations.
- **Request**: Expects a JSON payload (body) with the following fields:
     - `email` (String)
     - `password` (String)
- **Response**:
     - `200 OK`: Returns a JWT token if the login is successful. This token is
       to be used in the authorization headers of all subsequent calls made by
       this organisation. It also gives the caller privileges that comes with an
       organisation profile (eg. adding resources linked to that 
       organisation)
     - `400 Bad Request`: Missing data in the request.
     - `401 Unauthorized`: Invalid username or password.
- **Pre-requisites**: Email and password associated with your organisation 
  account.
- **Authorization**: None

### 2. **POST `/volunteer/login`**

- **Description**: Handles login requests for volunteers.
- **Request**: Expects a JSON payload (body) with the following fields:
    - `email` (String)
    - `password` (String)
- **Response**: 
  - `200 OK`: Returns a JWT token if the login is successful. This token is 
    to be used in the authorization headers of all subsequent calls made by 
    this volunteer. It also gives the caller privileges that comes with a 
    volunteer profile (eg. accessing the appointments scheduled (WIP))
  - `400 Bad Request`: Missing data in the request.
  - `401 Unauthorized`: Invalid username or password.
- **Pre-requisites**: Email and password associated with your volunteer
  account.
- **Authorization**: None

### 3. **POST `/user/login`**

- **Description**: Handles login requests for target users (elderly / 
  disabled people).
- **Request**: Expects a JSON payload (body) with the following fields:
     - `email` (String)
     - `password` (String)
- **Response**:
     - `200 OK`: Returns a JWT token if the login is successful. This token is
       to be used in the authorization headers of all subsequent calls made by
       this user. It also gives the caller privileges that comes with an
       user profile (eg. fetching the organisations that offer accessible 
       resources)
     - `400 Bad Request`: Missing data in the request.
     - `401 Unauthorized`: Invalid username or password.
- **Pre-requisites**: Email and password associated with your user
  account.
- **Authorization**: None

### Summary of Endpoints:

- `POST /organisation/login` → Organization login
- `POST /volunteer/login` → Volunteer login
- `POST /user/login` → User login

## Organisation Endpoints

### 1. **GET `/organisation/all`**

- **Description**: Fetches a list of all organizations.
- **Request**: None
- **Response**:
    - `200 OK`: Returns a list of organizations if available.
    - `204 No Content`: No organizations are found.
- **Pre-requisites**: None
- **Authorization**: None

### 2. **GET `/organisation/{id}`**

- **Description**: Fetches the details of a specific organization by its id.
- **Path Variable**:`id` (String): The ID of the organization to update.
- **Request**: None
- **Response**:
     - `200 OK`: Returns the organisation.
     - `404 Resource Not Found`: No organization with given id is found.
- **Pre-requisites**: Id of an existing organisation.
- **Authorization**: None

### 3. **POST `/organisation/create`**

- **Description**: Creates a new organization.
- **Request**: Expects a JSON payload (body) representing the organization.
- **Response**:
     - `201 OK`: Returns the organisation.
     - `500 Internal Server Error`: Failed to create the organisation.
- **Pre-requisites**: None
- **Authorization**: None

### 4. **PUT `/organisation/update/{id}`**

- **Description**: Updates an existing organization by its id.
- **Path Variable**:`id` (String): The id of the organization to update.
- **Request**: Expects a JSON payload (body) with the updated organization
  details.
- **Response**:
     - `200 OK`: Returns the organisation.
     - `404 Resource Not Found`: No organization with given id is found.
- **Pre-requisites**: Id of an existing organisation.
- **Authorization**: Requires a JWT Bearer token in Auth Header with
  `ORGANISATION` privileges.

### 5. **DELETE `/organisation/delete/{id}`**

- **Description**: Deletes an organization by its id.
- **Path Variable**:`id` (String): The id of the organization to delete.
- **Request**: None
- **Response**:
     - `204 No content`: Deleted the organisation.
     - `404 Resource Not Found`: No organization with given id is found.
- **Pre-requisites**: Id of an existing organisation.
- **Authorization**: Requires a JWT Bearer token in Auth Header with
  `ORGANISATION` privileges.

### Summary of Endpoints:

- **GET** `/organisation/all` → Fetch all organizations.
- **GET** `/organisation/{id}` → Fetch organization by id.
- **POST** `/organisation/create` → Create a new organization.
- **PUT** `/organisation/update/{id}` → Update an organization.
- **DELETE** `/organisation/delete/{id}` → Delete an organization.

## Resource Endpoints

### 1. **POST `/resource/createResourceType`**

- **Description**: Creates a new resource type.
- **Request**: Expects a JSON payload representing the resource type.
- **Response**:
  - `201 Created`: Returns the created resource type.
  - `500 Internal Server Error`: If the resource type creation fails.
- **Pre-requisites**: A valid resource type JSON body.
- **Authorization**: Requires JWT Bearer token with `ORGANISATION` privileges.

### 2. **GET `/resource/all`**

- **Description**: Fetches all available resources.
- **Request**: None.
- **Response**:
  - `200 OK`: Returns the list of resources.
  - `204 No Content`: If no resources exist.
- **Pre-requisites**: None.
- **Authorization**: Public (No authorization required).

### 3. **GET `/resource/{id}`**

- **Description**: Fetches a specific resource by its ID.
- **Path Variable**:
  - `id` (String): The ID of the resource to retrieve.
- **Response**:
  - `200 OK`: Returns the requested resource.
  - `404 Not Found`: If the resource with the specified ID is not found.
- **Pre-requisites**: A valid resource ID.
- **Authorization**: Public (No authorization required).

### 4. **POST `/resource/add`**

- **Description**: Adds a new resource.
- **Request**: Expects a JSON payload representing the resource details.
- **Response**:
  - `201 Created`: Returns the created resource.
  - `404 Not Found`: If the associated organisation or resource type IDs are invalid.
  - `500 Internal Server Error`: If resource creation fails.
- **Pre-requisites**: Valid JSON body with resource details, including organisation ID and resource type IDs.
- **Authorization**: Requires JWT Bearer token with `ORGANISATION` privileges.

### 5. **DELETE `/resource/delete/{id}`**

- **Description**: Deletes a resource by its ID.
- **Path Variable**:
  - `id` (String): The ID of the resource to delete.
- **Response**:
  - `204 No Content`: Resource deleted successfully.
  - `404 Not Found`: If the resource with the specified ID is not found.
- **Pre-requisites**: A valid resource ID.
- **Authorization**: Requires JWT Bearer token with `ORGANISATION` privileges.

### Summary of Endpoints:

- **POST** `/resource/createResourceType` → Create a new resource type.
- **GET** `/resource/all` → Fetch all resources.
- **GET** `/resource/{id}` → Fetch resource by ID.
- **POST** `/resource/add` → Add a new resource.
- **DELETE** `/resource/delete/{id}` → Delete a resource by ID.

## User Endpoints

### 1. Create a User Category

- **URL**: `/user/createCategory`
- **Method**: `POST`
- **Access**: `@PermitAll` (Open to everyone)
- **Description**: Creates a new user category.
- **Request Body**:
  - A valid `UserCategory` object.
- **Response**:
  - `201 Created`: Returns the created `UserCategory` object.

### 2. Get All Users

- **URL**: `/user/all`
- **Method**: `GET`
- **Access**: `@PreAuthorize("hasAuthority('USER')")` (Restricted to users with `USER` authority)
- **Description**: Retrieves all users from the system.
- **Response**:
  - `200 OK`: Returns a list of users.
  - `204 No Content`: If no users are found.

### 3. Get User by ID

- **URL**: `/user/{id}`
- **Method**: `GET`
- **Access**: `@PreAuthorize("hasAuthority('USER')")` (Restricted to users with `USER` authority)
- **Description**: Retrieves a specific user by their ID.
- **Path Variable**:
  - `id` (String): The ID of the user to retrieve.
- **Response**:
  - `200 OK`: Returns the `User` object if found.
  - Throws `ResourceNotFoundException` if the user is not found.

### 4. Create a User

- **URL**: `/user/create`
- **Method**: `POST`
- **Access**: `@PermitAll` (Open to everyone)
- **Description**: Creates a new user.
- **Request Body**:
  - A valid `UserRequest` object.
- **Response**:
  - `201 Created`: Returns the created `User` object.

### 5. Update a User

- **URL**: `/user/update/{id}`
- **Method**: `PUT`
- **Access**: `@PreAuthorize("hasAuthority('USER')")` (Restricted to users with `USER` authority)
- **Description**: Updates an existing user.
- **Path Variable**:
  - `id` (String): The ID of the user to update.
- **Request Body**:
  - A valid `UserRequest` object containing updated information.
- **Response**:
  - `200 OK`: Returns the updated `User` object.

### 6. Delete a User

- **URL**: `/user/delete/{id}`
- **Method**: `DELETE`
- **Access**: `@PreAuthorize("hasAuthority('USER')")` (Restricted to users with `USER` authority)
- **Description**: Deletes a user by their ID.
- **Path Variable**:
  - `id` (String): The ID of the user to delete.
- **Response**:
  - `204 No Content`: If deletion is successful.
  - Throws `ResourceNotFoundException` if the user is not found.

### Summary of Endpoints:

- **POST** `/user/createCategory` → Create a new user category.
- **GET** `/user/all` → Fetch all users.
- **GET** `/user/{id}` → Fetch user by ID.
- **POST** `/user/create` → Create a new user.
- **PUT** `/user/update/{id}` → Update a user by ID.
- **DELETE** `/user/delete/{id}` → Delete a user by ID.

## VolunteerController Endpoints

### 1. **GET `/volunteer/all`**

- **Description**: Fetches a list of all volunteers.
- **Response**:
    - Returns a list of volunteers if available.
    - Returns `204 No Content` if no volunteers are found.
- **Authorization**: No authorization required.

### 2. **GET `/volunteer/{id}`**

- **Description**: Fetches a specific volunteer by their ID.
- **Path Variable**:
    - `id` (String): The ID of the volunteer to retrieve.
- **Response**:
    - Returns the volunteer details if found.
    - Throws `ResourceNotFoundException` if the volunteer is not found.
- **Authorization**: No authorization required.

### 3. **POST `/volunteer/add`**

- **Description**: Adds a new volunteer.
- **Request Body**: Expects a JSON payload containing the volunteer details.
- **Response**:
    - Returns the newly created volunteer.
    - Status: `201 Created`.
- **Authorization**: No authorization required (`@PermitAll`).

### 4. **DELETE `/volunteer/delete/{id}`**

- **Description**: Deletes a volunteer by their ID.
- **Path Variable**:
    - `id` (String): The ID of the volunteer to delete.
- **Response**:
    - Returns a success message upon deletion.
    - Status: `204 No Content`.
    - Throws `ResourceNotFoundException` if the volunteer is not found.
- **Authorization**: Requires the `VOLUNTEER` authority.

### Summary of Endpoints:

- **GET** `/volunteer/all` → Fetch all volunteers.
- **GET** `/volunteer/{id}` → Fetch volunteer by ID.
- **POST** `/volunteer/add` → Add a new volunteer.
- **DELETE** `/volunteer/delete/{id}` → Delete a volunteer by ID.

## Technology Stack

- **Programming Language**: Java
- **Frameworks**: Spring Boot (Backend)
- **Database**: MySQL (Storing organization and accessibility data)
- **APIs**: RESTful APIs for client interactions

## Testing Strategy

- **Unit Testing**: Using **JUnit** for core business logic testing and *
  *Mockito** for mocking dependencies and services.
- **Integration Testing**: Using **MockMvc** for server-side endpoint testing
  and simulating HTTP request-response cycles.
- **End-to-End Testing**: Utilizing **Postman** to ensure real-world
  functionality of the API across the full system.

## Installation

Open the terminal in the folder in which you wish to clone the repository and
enter the following command:

```
git clone https://github.com/Aditi-Chowdhuri/COMSW4156-GarlicBread.git
cd COMSW4156-GarlicBread
```

## For creating a test coverage report and code style check

Push button for all tests:

```
mvn test
```

Generate test coverage report:

```
mvn jacoco:report
```

Run code style checks:

```
mvn checkstyle:check
```

## Tools and Technologies Used

This section outlines the tools and technologies utilized in building this
project, along with additional details where applicable.

- **Maven Package Manager**

- **Checkstyle**  
  Checkstyle is used for code reporting. Note that it is not integrated into the
  CI pipeline.

- **JaCoCo**  
  JaCoCo is utilized for generating code coverage reports.

- **Postman**  
  Postman is used for testing the functionality of our APIs.

## Database Pushed to Cloud

<img width="1450" alt="Screenshot 2024-10-18 at 11 18 47 PM" src="https://github.com/user-attachments/assets/58cb33bd-ee19-43f5-9dd1-c14a47594258">


## Style-checks

![WhatsApp Image 2024-10-18 at 11 01 50 PM](https://github.com/user-attachments/assets/03515699-ca94-42e6-a76d-29b81de20686)


## Jacoco Code Coverage Report

![Image 10-19-24 at 12 46 AM](https://github.com/user-attachments/assets/1c8be81f-2036-4ca1-9089-9eddd575368c)
