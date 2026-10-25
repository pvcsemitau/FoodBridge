# FoodBridge — Project Report Template

> Replace all bracketed text with your team's details. Add screenshots of your running application and paste public Draw.io links before submission.

## 1. Title
**FoodBridge: Surplus Food Donation and NGO Coordination System**

Team members: [Names and roll numbers]  
Course / batch: [Course and batch]  
Guide: [Faculty name]

## 2. Problem Statement
Restaurants, cafés and event organizers may have safe surplus food at the end of the day, while NGOs and shelters need food for community distribution. Without a coordinated platform, it can be difficult to find nearby available donations, check expiry times and track collection. FoodBridge provides a shared web interface where providers list surplus food and verified NGOs discover, accept and track collections.

## 3. Software Requirements Specification
### Functional requirements
- Users can register as a restaurant/café or NGO.
- Users can sign in with an email and password.
- Restaurants can publish donations with food type, quantity, unit, pickup location, preparation time and expiry time.
- NGOs can search/filter available donations and accept them after admin verification.
- NGOs can mark accepted donations as picked up and completed.
- Administrators can verify NGO accounts and view users and donations.
- Expired available donations are excluded from available listings and marked expired.

### Non-functional requirements
- Responsive, readable GUI.
- Passwords are stored as hashes, not plain text.
- Data is persisted in MySQL.
- Server-side validation is used for donation and account inputs.
- Role checks restrict actions to the appropriate user type.

### Software / hardware
- Java 17+, Maven, Spring Boot, Thymeleaf, HTML/CSS
- MySQL 8+, Spring Data JPA
- Draw.io for DFD and use case diagrams
- Computer with internet/browser and enough memory to run Java and MySQL

## 4. Highlights of the Project
FoodBridge brings food providers and verified NGOs into one workflow. Donation expiry is considered when listing available food; the collection status is tracked from availability through acceptance, pickup and completion. An administrator verifies NGOs before they can accept donations. Dashboard metrics are calculated from database records.

## 5. Tools, Concepts and Software Used
- **GUI:** Thymeleaf templates with HTML and CSS
- **Backend:** Java and Spring Boot MVC controllers
- **Database:** MySQL relational database
- **Persistence:** Spring Data JPA / Hibernate
- **Concepts:** MVC, CRUD operations, entity relationships, form validation, session-based login, role checks, filtering and status workflow

## 6. Data Flow Diagram (DFD)
Create a Level 0 / Level 1 DFD in Draw.io. Suggested external entities: Restaurant, NGO, Admin. Suggested processes: Account Management, Manage Donation, Search/Accept Donation, Verify NGO, Track Collection. Suggested data stores: Users, Donations. Show the flow of registration/login, donation details, verification, acceptance and status updates.

**Public Draw.io DFD link:** [Paste link here]

## 7. Use Case Diagram
Actors: Restaurant, NGO, Administrator. Use cases: Register, Login, Create Donation, View Own Donations, Search/Filter Donations, Accept Donation, Mark Pickup, Complete Collection, Verify NGO, View Users, View Donations, Logout.

**Public Draw.io Use Case link:** [Paste link here]

## 8. Database Description (approximately half a page)
The application uses two relational tables. The `users` table stores organization name, location, unique email, password hash, role, verification status and creation timestamp. The `donations` table stores food name/type, quantity/unit, pickup location, preparation and expiry timestamps, status, and links to the restaurant and accepting NGO. `restaurant_id` and `ngo_id` reference the `users` table. One restaurant can create many donations; an NGO can accept many donations. The NGO reference is empty until a donation is accepted. The application queries users by email for login, users by role for dashboards, donations by restaurant for provider history, donations by status and expiry for available food, and donations by NGO for collection tracking.

## 9. Testing
Record your actual test results before submission.

| Test | Expected result | Actual result |
|---|---|---|
| Register with unique email | Account created | [Fill in] |
| Register duplicate email | Clear error | [Fill in] |
| Login with incorrect password | Access denied | [Fill in] |
| Restaurant submits valid donation | Donation saved in MySQL | [Fill in] |
| Invalid quantity / expiry | Validation error | [Fill in] |
| Unverified NGO accepts donation | Rejected | [Fill in] |
| Admin verifies NGO | NGO can accept donations | [Fill in] |
| NGO completes collection | Status becomes COMPLETED | [Fill in] |
| Expired donation search | Donation not shown as available | [Fill in] |

## 10. GitHub Repository
**Public repository URL:** [Paste GitHub link here]  
Include this source code and a README.md. Confirm the repository is public and do not commit database passwords or real user data.
