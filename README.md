Ambulance Dispatch Backend (Spring Boot)
This is the backend service for the Ambulance Dispatch platform, built with Spring Boot, supporting features like authentication (email & Google OAuth), 
real-time ambulance tracking, and role-based access for users and dispatchers.
------------------------------------------------------------------------------------

Technologies Used
Java 17+

Spring Boot 3

Spring Security

Spring Data JPA

WebSocket (STOMP/SockJS)

PostgreSQL

Maven

OAuth2 (Google)

-------------------------------------------------------------------------------
Features
User & Dispatcher Registration/Login

Google OAuth2 Login

Real-time Ambulance Location Tracking via WebSocket

Ambulance Request & History Management

Role-based Authorization

------------------------------------------------------------------------------------------
Getting Started
1. Clone the Repository
bash
Copy
Edit
git clone https://github.com/Benny-Nwaro/flexi-med.git
cd ambulance-backend
2. Setup Environment Variables
Create a .env or application.properties file in src/main/resources/ with the following:

properties
Copy
Edit
# Server
server.port=8080

# PostgreSQL Database
spring.datasource.url=jdbc:postgresql://localhost:5432/ambulance_db
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secret=your_jwt_secret_key
jwt.expiration=3600000

# Google OAuth
spring.security.oauth2.client.registration.google.client-id=your_google_client_id
spring.security.oauth2.client.registration.google.client-secret=your_google_client_secret
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google
Ensure you have created a Google OAuth Client ID with appropriate redirect URIs.

--------------------------------------------------------------------------------------------
3. Install Dependencies & Build
bash
Copy
Edit
./mvnw clean install
4. Run the Application
bash
Copy
Edit
./mvnw spring-boot:run
The server should now be running at http://localhost:8080.

API Documentation
Once the app is running, access Swagger UI (if enabled):

bash
Copy
Edit
http://localhost:8080/swagger-ui/index.html
Running Tests
bash
Copy
Edit
./mvnw test
WebSocket Endpoint
Subscribe to ambulance location updates via WebSocket:

bash
Copy
Edit
ws://localhost:8080/ws
Topic format: /topic/ambulance-location

Folder Structure (Overview)
bash
Copy
Edit
src/
├── main/
│   ├── java/
│   │   └── com.example.ambulance/     # Main backend code
│   └── resources/
│       ├── application.properties     # Config file
│       └── static/                    # Public assets (if any)
├── test/
│   └── java/                          # Unit & Integration tests
Troubleshooting
Database connection errors: Make sure PostgreSQL is running and credentials are correct.

CORS issues: Configure CORS if frontend is on a different port.

Google login not working: Check your client ID, secret, and redirect URI.

Author
Aroh Ebenezer

License
This project is licensed under the MIT License - see the LICENSE file for details.
