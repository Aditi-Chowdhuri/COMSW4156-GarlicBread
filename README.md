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

## Appointment Endpoints

### 1. **POST `/appointment`**

- **Description**: Creates a new appointment.
- **Request**: Expects a JSON payload representing the appointment details.
  ```json
  {
  "organisationId": "org123",
  "userId": "user456",
  "resourceIds": ["resource789", "resource101"],
  "date": "12252024",
  "timeStart": 9,
  "timeEnd": 11,
  "description": "Wheelchair assistance appointment"
  }
- **Response**:
  - `200 Ok`: Returns the created appointment.
  - `400 Bad Request`: Invalid or missing input fields.
  - `403 Forbidden`: The authenticated user or organisation is not allowed to create the appointment.
  - `500 Internal Server Error`: Appointment creation failed.

- **Pre-requisites**: A valid appointment JSON body, JWT Bearer token for authentication.
- **Authorization**: Requires ORGANISATION or USER authority.

### 2. **GET `/appointment/organisation`**

- **Description**: Retrieves all appointments for a specific organisation.
- **Request Parameters**:
  - `organisation` (String): The ID of the organisation to fetch appointments for.
- **Response**:
  - `200 OK`: Returns a list of appointments for the organisation.
  - `403 Forbidden`: The authenticated organisation does not have permission to access the requested data.
  - `500 Internal Server Error`: Failed to retrieve appointments.
- **Pre-requisites**: Valid organisation ID., JWT Bearer token for authentication.
- **Authorization**: Requires ORGANISATION or VOLUNTEER authority.

### 3. **GET `/appointment/user`**

- **Description**: Retrieves all appointments for the authenticated user.
- **Response**:
  - `200 OK`: Returns a list of appointments for the user.
  - `500 Internal Server Error`: Failed to retrieve appointments.
- **Pre-requisites**: JWT Bearer token for authentication.
- **Authorization**: Requires USER authority.

### 4. **DELETE `/appointment/{id}`**

- **Description**: Deletes an appointment by its ID.
- **Path Variable**:
  - `id` (String): The ID of the appointment to delete.
- **Response**:
  - `204 No Content`: Appointment deleted successfully.
  - `403 Forbidden`: If the authenticated volunteer does not match the ID of the volunteer being deleted.
  - `404 Not Found`: The authenticated user or organisation is not allowed to delete the appointment.
  - `500 Internal Server Error`: Appointment deletion failed.
- **Pre-requisites**:Valid appointment id, 
JWT Bearer token for authentication.
- **Authorization**: Requires ORGANISATION or USER authority

### 5. **POST `/appointment/{id}/volunteer`**

- **Description**: Assigns a volunteer to an appointment.
- **Path Variable**:
  - `id` (String): The ID of the appointment to assign a volunteer.
- **Response**:
  - `200 OK`: Returns the updated appointment with the assigned volunteer.
  - `400 Bad Request`: The appointment already has a volunteer assigned or invalid volunteer ID.
  - `500 Internal Server Error`: Failed to assign the volunteer.
- **Pre-requisites**:Valid appointment id, 
JWT Bearer token for authentication.
- **Authorization**: Requires VOLUNTEER authority

### Summary of Endpoints:

- **POST** `/appointment` → Create a new appointment.
- **GET** `/appointment/organisation` → Get all appointments for an organisation.
- **GET** `/appointment/user ` → Get all appointments for a user.
- **DELETE** `/appointment/{id}` → Delete an appointment by ID.
- **POST** `/appointment/{id}/volunteer` → Assign a volunteer to an appointment.

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

## Service pushed to GCP App Engine

The service is currently hosted [here](https://garlicbread-includify.ue.r.appspot.com/). However, owing to maintenance 
issues it might not be available for long.

![WhatsApp Image 2024-10-25 at 2 07 15 PM](https://github.com/user-attachments/assets/b503ef7d-b272-4424-9147-b29fcb8d46a0)

## Database Pushed to Cloud (GCP Cloud SQL)

<img width="1450" alt="Screenshot 2024-10-18 at 11 18 47 PM" src="https://github.com/user-attachments/assets/58cb33bd-ee19-43f5-9dd1-c14a47594258">

## Style-checks

![WhatsApp Image 2024-10-18 at 11 01 50 PM](https://github.com/user-attachments/assets/03515699-ca94-42e6-a76d-29b81de20686)

## Jacoco Code Coverage Report

<img width="1173" alt="Screenshot 2024-10-25 at 1 54 15 PM" src="https://github.com/user-attachments/assets/a1c7b4dc-0781-4e0d-906e-3bc2364e27e5">

# **Final API Test Suit and Documentatione**

This **Final API Test Suite** consists of a comprehensive set of endpoints for testing and validating the service. The suite is categorized into the following sections:

1. **Organisation Endpoints**
2. **Registration Endpoints**
3. **Resource Endpoints**
4. **Volunteer Endpoints**
5. **Appointment Endpoints**

The API includes both **public** and **non-public** endpoints, with automated integration into CI/CD pipelines using **Newman**.

---

## **Postman Documentation**

The full Postman documentation for this API suite can be accessed here:

- [All Endpoints](https://documenter.getpostman.com/view/39266957/2sAYBVhrbR)
- [Public Endpoints](https://documenter.getpostman.com/view/39266957/2sAYBXAAPp)

## **Continuous Integration (CI)**

This project uses GitHub Actions for CI to automate the build, testing, and static analysis processes. The CI pipeline is triggered on every `push` or `pull request` to the `main` branch.

#### Key Steps

1. **Checkout Code**: The workflow fetches the latest code from the repository.
2. **Set up Java 17**: Java 17 is installed via the `actions/setup-java` action, ensuring a consistent environment.
3. **Build with Maven**: The project is built using `mvn clean install`, resolving dependencies and compiling the code.
4. **Run Tests**: Unit tests are executed with `mvn test` to verify code correctness.
5. **PMD Analysis**: Runs `mvn pmd:check` to detect code quality issues.
6. **Checkstyle Validation**: Ensures code adheres to the coding standards using `mvn checkstyle:checkstyle`.
7. **JaCoCo Code Coverage**: Code coverage reports are generated using `mvn jacoco:report`, providing insights into test coverage.
8. **Generate Reports**: Reports for PMD, Checkstyle, and JaCoCo are collected and stored in a `Reports` directory within the workflow.
9. **Upload Reports as Artifacts**: The reports are uploaded to GitHub as artifacts for easy access and review.
10. **Commit Reports to Repository**: The reports are committed to the repository under the `Reports` directory. This ensures that the latest reports are version-controlled and easily accessible. The commit uses a temporary branch created by the workflow.


### Reports Directory
The CI pipeline organizes all the static analysis and code coverage reports in a `Reports` directory:
- **JaCoCo Report**: Located at `Reports/jacoco-report.html`.
- **Checkstyle Report**: Located at `Reports/checkstyle-report.html`.
- **PMD Report**: Located at `Reports/pmd-report.html`.