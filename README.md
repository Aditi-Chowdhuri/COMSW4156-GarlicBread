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
  ```json
  {
  "name": "Inclusive Support Organization",
  "email": "info@inclusivesupport.org",
  "password": "securePassword123",
  "description": "An organization dedicated to providing resources and support for individuals with disabilities.",
  "latitude": "37.7749",
  "longitude": "-122.4194",
  "address": "123 Inclusive Way, San Francisco, CA, 94103"
  }
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
  ```json
  {
  "name": "Inclusive Support Organization",
  "email": "info@inclusivesupport.org",
  "password": "securePassword123",
  "description": "An organization dedicated to providing resources and support for individuals with disabilities.",
  "latitude": "37.7749",
  "longitude": "-122.4194",
  "address": "123 Inclusive Way, San Francisco, CA, 94103"
  }
- **Response**:
     - `200 OK`: Returns the organisation.
     - `401 Unauthorized`: Invalid token in auth header.
     - `403 Forbidden` : The auth header token is of a different 
       organisation from the one you want to update.
     - `404 Resource Not Found`: No organization with given id is found.
- **Pre-requisites**: Id of an existing organisation.
- **Authorization**: Requires a JWT Bearer token in Auth Header with
  `ORGANISATION` privileges. Also note that this token should be generated 
  by the same organisation that you are trying to update.

### 5. **DELETE `/organisation/delete/{id}`**

- **Description**: Deletes an organization by its id.
- **Path Variable**:`id` (String): The id of the organization to delete.
- **Request**: None
- **Response**:
     - `204 No content`: Deleted the organisation.
     - `401 Unauthorized`: Invalid token in auth header.
     - `403 Forbidden` : The auth header token is of a different
       organisation from the one you want to delete.
     - `404 Resource Not Found`: No organization with given id is found.
- **Pre-requisites**: Id of an existing organisation.
- **Authorization**: Requires a JWT Bearer token in Auth Header with
  `ORGANISATION` privileges. Also note that this token should be generated
  by the same organisation that you are trying to delete.

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
  ```json
  {
  "title": "Tools",
  "description": "Tools like hearing loops and wheelchairs that aid individuals with disabilities.",
  }
- **Response**:
  - `201 Created`: Returns the created resource type.
  - `500 Internal Server Error`: If the resource type creation fails.
- **Pre-requisites**: A valid resource type JSON body.
- **Authorization**: Requires JWT Bearer token with `ORGANISATION` 
  privileges.

### 2. **DELETE `/resource/deleteResourceType/{id}`**

- **Description**: Delete a resource type.
- **Path Variable**:
  - `id` (integer): The ID of the resource type to delete.
- **Response**:
  - `204 Not Content`: Resource type deleted successfully.
  - `400 Bad Request`: Invalid id passed.
  - `401 Unauthorized`: Invalid JWT token in header.
  - `403 Forbidden`: Unable to delete a default resource type.
  - `404 Not Found`: Resource type of given id not found.
  - `500 Internal Server Error`: Resource type deletion failed.
- **Pre-requisites**: A valid id of a resource type.
- **Authorization**: Requires JWT Bearer token with `ORGANISATION`
  privileges.

### 3. **GET `/resource/all`**

- **Description**: Fetches all available resources.
- **Request**: None.
- **Response**:
  - `200 OK`: Returns the list of resources.
  - `204 No Content`: If no resources exist.
- **Pre-requisites**: None.
- **Authorization**: Public (No authorization required).

### 4. **GET `/resource/{id}`**

- **Description**: Fetches a specific resource by its ID.
- **Path Variable**:
  - `id` (String): The ID of the resource to retrieve.
- **Response**:
  - `200 OK`: Returns the requested resource.
  - `404 Not Found`: If the resource with the specified ID is not found.
- **Pre-requisites**: A valid resource ID.
- **Authorization**: Public (No authorization required).

### 5. **POST `/resource/add`**

- **Description**: Adds a new resource.
- **Request**: Expects a JSON payload representing the resource details.
  ```json
  {
  "organisationId": "org123",
  "resourceTypeIds": [
  "1"
  ],
  "targetUserCategoryIds": [
  "1",
  "2"
  ],
  "title": "Ramp for Wheelchairs",
  "description": "A ramp designed for wheelchair accessibility",
  "usageInstructions": "Ensure ramp is securely positioned for use",
  }
- **Response**:
  - `201 Created`: Returns the created resource.
  - `404 Not Found`: If the associated organisation or resource type IDs are invalid.
  - `500 Internal Server Error`: If resource creation fails.
- **Pre-requisites**: Valid JSON body with resource details, including organisation ID and resource type IDs.
- **Authorization**: Requires JWT Bearer token with `ORGANISATION` 
  privileges. Also note that the JWT token needs to be generated by the same 
  organisation in which you want to add the resource (organisationId field 
  in request body).

### 6. **DELETE `/resource/delete/{id}`**

- **Description**: Deletes a resource by its ID.
- **Path Variable**:
  - `id` (String): The ID of the resource to delete.
- **Response**:
  - `204 No Content`: Resource deleted successfully.
  - `404 Not Found`: If the resource with the specified ID is not found.
- **Pre-requisites**: A valid resource ID.
- **Authorization**: Requires JWT Bearer token with `ORGANISATION` 
  privileges. Also note that the JWT token needs to be generated by the same
  organisation whose resource you want to delete.

### Summary of Endpoints:

- **POST** `/resource/createResourceType` → Create a new resource type.
- **DELETE** `/resource/deleteResourceType/id` → Delete a resource type.
- **GET** `/resource/all` → Fetch all resources.
- **GET** `/resource/{id}` → Fetch resource by ID.
- **POST** `/resource/add` → Add a new resource.
- **DELETE** `/resource/delete/{id}` → Delete a resource by ID.

## User Endpoints

### 1. **POST `/user/createCategory`**

- **Description**: Creates a new user category.
- **Request**: Expects a JSON payload representing the user category.
  ```json
  {
  "title": "Senior Citizens",
  "description": "Categories for senior citizens including services and resources tailored for their needs."
  }
- **Response**:
  - `201 Created`: Returns the created user category.
  - `500 Internal Server Error`: If category creation fails.
- **Pre-requisites**: A valid user category JSON body.
- **Authorization**: Public (No authorization required).

### 2. **DELETE `/user/deleteCategory/{id}`**

- **Description**: Deletes a user category by its ID.
- **Path Variable**:
  - `id` (integer): The ID of the user category to delete.
- **Response**:
  - `204 No Content`: User category deleted successfully.
  - `400 Bad Request`: Invalid ID format passed.
  - `403 Forbidden`: Default categories cannot be deleted.
  - `404 Not Found`: If the category with the given ID is not found.
  - `500 Internal Server Error`: If the deletion fails.
- **Pre-requisites**: A valid user category ID.
- **Authorization**: Public (No authorization required).

### 3. **GET `/user/all`**

- **Description**: Retrieves all users.
- **Response**:
  - `200 OK`: Returns a list of all users.
  - `204 No Content`: If no users exist.
- **Pre-requisites**: None.
- **Authorization**: Requires JWT Bearer token with `USER` authority.

### 4. **GET `/user/{id}`**

- **Description**: Retrieves a user by their ID.
- **Path Variable**:
  - `id` (String): The ID of the user to retrieve.
- **Response**:
  - `200 OK`: Returns the user with the specified ID.
  - `404 Not Found`: If the user with the specified ID is not found.
- **Pre-requisites**: A valid user ID.
- **Authorization**: Requires JWT Bearer token with `USER` authority.

### 5. **POST `/user/create`**

- **Description**: Creates a new user.
- **Request**: Expects a JSON payload representing the user request.
  ```json
  {
  "name": "Jane Smith",
  "age": 30,
  "email": "jane.smith@example.com",
  "password": "strongPassword456",
  "categoryIds": ["1"]
  }
  ```
  Note: categoryIds are optional.
- **Response**:
  - `201 Created`: Returns the created user.
  - `404 Not Found`: If any of the category IDs are invalid.
  - `500 Internal Server Error`: If user creation fails.
- **Pre-requisites**: Valid JSON body with user details, including category IDs.
- **Authorization**: Public (No authorization required).

### 6. **PUT `/user/update/{id}`**

- **Description**: Updates an existing user.
- **Path Variable**:
  - `id` (String): The ID of the user to update.
- **Request**: Expects a JSON payload representing the updated user details.
  ```json
  {
  "name": "Jane Smith",
  "age": 30,
  "email": "jane.smith@example.com",
  "password": "strongPassword456",
  "categoryIds": ["1"]
  }
  ```
  Note: categoryIds are optional.
- **Response**:
  - `200 OK`: Returns the updated user.
  - `403 Forbidden`: If the authenticated user does not match the ID of the user being updated.
  - `404 Not Found`: If the user with the specified ID is not found.
  - `500 Internal Server Error`: If user update fails.
- **Pre-requisites**: Valid JSON body with updated user details.
- **Authorization**: Requires JWT Bearer token with `USER` authority. The 
  token must belong to the user being updated.

### 7. **DELETE `/user/delete/{id}`**

- **Description**: Deletes a user by their ID.
- **Path Variable**:
  - `id` (String): The ID of the user to delete.
- **Response**:
  - `204 No Content`: User deleted successfully.
  - `403 Forbidden`: If the authenticated user does not match the ID of the user being deleted.
  - `404 Not Found`: If the user with the specified ID is not found.
  - `500 Internal Server Error`: If user deletion fails.
- **Pre-requisites**: A valid user ID.
- **Authorization**: Requires JWT Bearer token with `USER` authority. The 
  token must belong to the user being deleted.

### Summary of Endpoints:

- **POST** `/user/createCategory` → Create a new user category.
- **DELETE** `/user/deleteCategory/{id}` → Delete a user category by its ID.
- **GET** `/user/all` → Retrieve all users.
- **GET** `/user/{id}` → Retrieve a user by ID.
- **POST** `/user/create` → Create a new user.
- **PUT** `/user/update/{id}` → Update a user by ID.
- **DELETE** `/user/delete/{id}` → Delete a user by ID.

## VolunteerController Endpoints

### 1. **POST `/volunteer/add`**

- **Description**: Adds a new volunteer.
- **Request**: Expects a JSON payload representing the volunteer details.
  ```json
  {
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "age": 25,
  "address": "123 Maple Street, Springfield, IL",
  "phone": "+1 (555) 123-4567"
  }
- **Response**:
  - `201 Created`: Returns the newly created volunteer.
  - `500 Internal Server Error`: If the volunteer creation fails.
- **Pre-requisites**: A valid volunteer JSON body.
- **Authorization**: Public (No authorization required).

### 2. **GET `/volunteer/all`**

- **Description**: Retrieves all volunteers.
- **Response**:
  - `200 OK`: Returns a list of all volunteers.
  - `204 No Content`: If no volunteers exist.
- **Pre-requisites**: None.
- **Authorization**: Public (No authorization required).

### 3. **GET `/volunteer/{id}`**

- **Description**: Retrieves a volunteer by their ID.
- **Path Variable**:
  - `id` (String): The ID of the volunteer to retrieve.
- **Response**:
  - `200 OK`: Returns the volunteer with the specified ID.
  - `404 Not Found`: If the volunteer with the specified ID is not found.
- **Pre-requisites**: A valid volunteer ID.
- **Authorization**: Public (No authorization required).

### 4. **DELETE `/volunteer/delete/{id}`**

- **Description**: Deletes a volunteer by their ID.
- **Path Variable**:
  - `id` (String): The ID of the volunteer to delete.
- **Response**:
  - `204 No Content`: Volunteer deleted successfully.
  - `403 Forbidden`: If the authenticated volunteer does not match the ID of the volunteer being deleted.
  - `404 Not Found`: If the volunteer with the specified ID is not found.
  - `500 Internal Server Error`: If volunteer deletion fails.
- **Pre-requisites**: A valid volunteer ID.
- **Authorization**: Requires JWT Bearer token with `VOLUNTEER` authority. 
  The token must belong to the volunteer being deleted.

### Summary of Endpoints:

- **POST** `/volunteer/add` → Add a new volunteer.
- **GET** `/volunteer/all` → Retrieve all volunteers.
- **GET** `/volunteer/{id}` → Retrieve a volunteer by ID.
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
