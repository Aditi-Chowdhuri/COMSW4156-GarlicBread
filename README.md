# Advanced Software Engineering Project

### Team Members:
- **Abhishek Paul** (ap4623)
- **Puja Singla** (ps3467)
- **Rahaf Bin Muhanna** (rb3713)
- **Aditi Chowdhuri** (anc2207)

### Instructor:
- **Gail Kaiser**

## Overview
This project proposes a service to improve accessibility for elderly individuals and persons with disabilities by enabling organizations to share detailed information about their ADA compliance and other facilities. The goal is to foster inclusivity by helping users find accessible venues and resources and schedule visits based on availability. Additionally, the service facilitates volunteer support for these visits.

### Key Features:
- **Organizational Accessibility**: Organizations can post infrastructure details (e.g., ramps, elevators) and resource availability (e.g., wheelchairs).
- **User Scheduling**: Individuals can schedule visits based on the availability of accessibility resources.
- **Volunteer Coordination**: Volunteers can sign up to assist disabled individuals and senior citizens during their visits.
- **Flexible Data Schema**: Organizations across different industries (e.g., schools, subways, theaters) can define custom data structures to suit their needs.
- **Real-Time Updates**: Facilities and resource availability are updated in real-time, enabling informed decision-making for users.

## Clients

1. **Mobile App for Volunteers**:  
   Volunteers can browse nearby organizations, view scheduled visits, and request to assist users.
   
2. **Mobile App for Elderly and Disabled Users**:  
   This app lists organizations offering accessible resources and facilities, allowing users to track live availability and schedule visits.
   
3. **Web Portal for Organizations**:  
   Organizations can manage their facilities' accessibility information, track scheduled visits, and assign volunteers.

## AuthController Endpoints

### 1. **POST `/organisation/login`**
   - **Description**: Handles login requests for organizations.
   - **Request Body**: Expects a JSON payload with the following fields:
     - `email` (String)
     - `password` (String)
   - **Response**: Returns a JWT token if the login is successful.

### 2. **POST `/volunteer/login`**
   - **Description**: Handles login requests for volunteers.
   - **Request Body**: Expects a JSON payload with the following fields:
     - `email` (String)
     - `password` (String)
   - **Response**: Returns a JWT token if the login is successful.

### 3. **POST `/user/login`**
   - **Description**: Handles login requests for regular users.
   - **Request Body**: Expects a JSON payload with the following fields:
     - `email` (String)
     - `password` (String)
   - **Response**: Returns a JWT token if the login is successful.

### Summary of Endpoints:
- `POST /organisation/login` → Organization login
- `POST /volunteer/login` → Volunteer login
- `POST /user/login` → User login
  
## OrganisationController Endpoints

### 1. **GET `/organisation/all`**
   - **Description**: Fetches a list of all organizations.
   - **Response**: 
     - Returns a list of organizations if available.
     - Returns `204 No Content` if no organizations are found.

### 2. **GET `/organisation/{id}`**
   - **Description**: Fetches the details of a specific organization by its ID.
   - **Path Variable**:
     - `id` (String): The ID of the organization to retrieve.
   - **Response**: 
     - Returns the organization details.
     - Throws `ResourceNotFoundException` if the organization is not found.

### 3. **POST `/organisation/create`**
   - **Description**: Creates a new organization.
   - **Request Body**: Expects a JSON payload representing the organization.
   - **Response**: 
     - Returns the created organization.
     - Status: `201 Created`.

### 4. **PUT `/organisation/update/{id}`**
   - **Description**: Updates an existing organization by its ID.
   - **Path Variable**:
     - `id` (String): The ID of the organization to update.
   - **Request Body**: Expects a JSON payload with the updated organization details.
   - **Response**: 
     - Returns the updated organization.
     - Status: `200 OK`.
   - **Authorization**: Requires the `ORGANISATION` authority.

### 5. **DELETE `/organisation/delete/{id}`**
   - **Description**: Deletes an organization by its ID.
   - **Path Variable**:
     - `id` (String): The ID of the organization to delete.
   - **Response**: 
     - Returns a success message.
     - Status: `204 No Content`.
     - Throws `ResourceNotFoundException` if the organization is not found.
   - **Authorization**: Requires the `ORGANISATION` authority.

### Summary of Endpoints:
- **GET** `/organisation/all` → Fetch all organizations.
- **GET** `/organisation/{id}` → Fetch organization by ID.
- **POST** `/organisation/create` → Create a new organization.
- **PUT** `/organisation/update/{id}` → Update an organization.
- **DELETE** `/organisation/delete/{id}` → Delete an organization.

## ResourceController Endpoints

### 1. **POST `/resource/createResourceType`**
   - **Description**: Creates a new resource type.
   - **Request Body**: Expects a JSON payload representing the resource type.
   - **Response**: 
     - Returns the created resource type.
     - Status: `201 Created`.
   - **Authorization**: Requires the `ORGANISATION` authority.

### 2. **GET `/resource/all`**
   - **Description**: Fetches a list of all resources.
   - **Response**: 
     - Returns a list of resources if available.
     - Returns `204 No Content` if no resources are found.
   - **Authorization**: No authorization required (`@PermitAll`).

### 3. **GET `/resource/{id}`**
   - **Description**: Fetches a specific resource by its ID.
   - **Path Variable**:
     - `id` (String): The ID of the resource to retrieve.
   - **Response**: 
     - Returns the resource details if found.
     - Throws `ResourceNotFoundException` if the resource is not found.
   - **Authorization**: No authorization required (`@PermitAll`).

### 4. **POST `/resource/add`**
   - **Description**: Adds a new resource to an organization.
   - **Request Body**: Expects a JSON payload containing the resource details.
   - **Response**: 
     - Returns the added resource.
     - Status: `201 Created`.
   - **Authorization**: Requires the `ORGANISATION` authority.

### 5. **DELETE `/resource/delete/{id}`**
   - **Description**: Deletes a resource by its ID.
   - **Path Variable**:
     - `id` (String): The ID of the resource to delete.
   - **Response**: 
     - Returns a success message upon deletion.
     - Status: `204 No Content`.
     - Throws `ResourceNotFoundException` if the resource is not found.
   - **Authorization**: Requires the `ORGANISATION` authority.

### Summary of Endpoints:
- **POST** `/resource/createResourceType` → Create a new resource type.
- **GET** `/resource/all` → Fetch all resources.
- **GET** `/resource/{id}` → Fetch resource by ID.
- **POST** `/resource/add` → Add a new resource.
- **DELETE** `/resource/delete/{id}` → Delete a resource by ID.

## UserController Endpoints

### 1. **POST `/user/createCategory`**
   - **Description**: Creates a new user category.
   - **Request Body**: Expects a JSON payload representing the user category.
   - **Response**: 
     - Returns the created user category.
     - Status: `201 Created`.
   - **Authorization**: No authorization required (`@PermitAll`).

### 2. **GET `/user/all`**
   - **Description**: Fetches a list of all users.
   - **Response**: 
     - Returns a list of users if available.
     - Returns `204 No Content` if no users are found.
   - **Authorization**: Requires the `USER` authority.

### 3. **GET `/user/{id}`**
   - **Description**: Fetches a specific user by their ID.
   - **Path Variable**:
     - `id` (String): The ID of the user to retrieve.
   - **Response**: 
     - Returns the user details if found.
     - Throws `ResourceNotFoundException` if the user is not found.
   - **Authorization**: Requires the `USER` authority.

### 4. **POST `/user/create`**
   - **Description**: Creates a new user.
   - **Request Body**: Expects a JSON payload containing the user information.
   - **Response**: 
     - Returns the created user.
     - Status: `201 Created`.
   - **Authorization**: No authorization required (`@PermitAll`).

### 5. **PUT `/user/update/{id}`**
   - **Description**: Updates an existing user by their ID.
   - **Path Variable**:
     - `id` (String): The ID of the user to update.
   - **Request Body**: Expects a JSON payload with the updated user information.
   - **Response**: 
     - Returns the updated user.
     - Status: `200 OK`.
   - **Authorization**: Requires the `USER` authority.

### 6. **DELETE `/user/delete/{id}`**
   - **Description**: Deletes a user by their ID.
   - **Path Variable**:
     - `id` (String): The ID of the user to delete.
   - **Response**: 
     - Returns a success message upon deletion.
     - Status: `204 No Content`.
     - Throws `ResourceNotFoundException` if the user is not found.
   - **Authorization**: Requires the `USER` authority.

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

### Future Integrations
The service can be integrated into platforms like Google Maps, adding an "Accessibility" category to further broaden accessibility for disabled individuals and seniors.

## Technology Stack

- **Programming Language**: Java
- **Frameworks**: Spring Boot (Backend)
- **Database**: MySQL (Storing organization and accessibility data)
- **APIs**: RESTful APIs for client interactions

## Testing Strategy

- **Unit Testing**: Using **JUnit** for core business logic testing and **Mockito** for mocking dependencies and services.
- **Integration Testing**: Using **MockMvc** for server-side endpoint testing and simulating HTTP request-response cycles.
- **End-to-End Testing**: Utilizing **Postman** to ensure real-world functionality of the API across the full system.
- **Code Coverage**: We aim for 85% branch coverage using code coverage tools integrated into the CI/CD pipeline.

## Installation

Open the terminal in the folder in which you wish to clone the repository and
enter the following command:

```
git clone https://github.com/Aditi-Chowdhuri/COMSW4156-GarlicBread.git
cd COMSW4156-GarlicBread
```

## For creating a test coverage report and code style check

Generate test coverage report:

```
mvn jacoco:report
```

Run code style checks:

```
mvn checkstyle:check
```

Using PMD

```
mvn pmd:check
```

## Tools and Technologies Used

This section outlines the tools and technologies utilized in building this
project, along with additional details where applicable.

- **Maven Package Manager**

- **Checkstyle**  
  Checkstyle is used for code reporting. Note that it is not integrated into the
  CI pipeline.

- **PMD**  
  PMD is employed for static analysis of our Java code.

- **JaCoCo**  
  JaCoCo is utilized for generating code coverage reports.

- **Postman**  
  Postman is used for testing the functionality of our APIs.