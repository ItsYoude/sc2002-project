# SC2002: Internship Placement Management System

## Project Overview

This project implements an **Internship Placement Management System** as a Command Line Interface (CLI) application in Java. It allows three main user roles like **Students**, **Company Representatives**, and **Career Center Staff**, to manage internship postings, applications, approvals, and placements, fulfilling all requirements of the assignment.

---

## Getting Started

### Prerequisites

1.  **Java Development Kit (JDK) 8 or later** installed on your system.
2.  Your preferred Java IDE
3.  Ensure the necessary data files (in the `src/data` folder) are present and accessible.

### Running the Application

1.  **Clone or Download** the repository to your local machine.
2.  **Open the project** in your Java IDE.
3.  **Locate the main entry point:** The application starts execution from the `main` method within the `MainApp.java`.
4.  **Run the file.** The application will initialize data from the CSV files and display the `LoginUI`.

---

## Default User Accounts and Credentials
The system initializes with users loaded from the sample data files (CSV format).

| Role | Example User ID | Default Password | Notes |
| :--- | :--- | :--- | :--- |
| **Student** | U2345123F |`password` | Can apply for up to 3 internships |
| **Career Center Staff** | NTU Staff Account | `password` | Automatically registered from staff list file |
| **Company Representative**| company.rep@email.com  | `password` | Must be approved by Staff to log in |

**All users** can change their password after logging in.

---

## Core Functionalities Implemented

This system utilizes the **Boundary-Control-Entity (BCE)** design pattern.

### 1. User Management (All Users)

* **Login & Logout:** Secure access based on role.
* **Password Management:** Users can change their default password.

### 2. Student Features

* **View Opportunities:** Filtered automatically based on student's **Year of Study** and **Major**.
* **Application Rules:** Enforces the limit of **3 active applications**.
* **Level Eligibility:** Year 1/2 students restricted to **Basic-level** internships.
* **Placement Acceptance:** Can accept only **one** "Successful" placement, automatically withdrawing all others.
* **Withdrawal Request:** Requests are submitted to Career Center Staff for approval.

### 3. Company Representative Features

* **Registration & Approval:** CRs register and await authorization by the Career Center Staff,
* **Opportunity Creation:** Can create up to **5** internship opportunities, which start with a "Pending" status requiring Staff approval.
* **Application Review:** Can view student details and **Approve/Reject** applications.
* **Visibility Toggle:** Can turn an internship's visibility "on" or "off" for students.

### 4. Career Center Staff Features

* **Account Authorization:** Authorize or reject pending Company Representative accounts
* **Opportunity Approval:** Approve or reject internship opportunities submitted by CRs
* **Withdrawal Handling:** Approve or reject student withdrawal requests (which may affect placement status) 
* **Reporting:** Generate comprehensive reports with filters (Status, Major, Level, etc.)

---

## Object-Oriented Design (OOD) Applications

### Class Structure
The system is cleanly separated into three layers:
* **Entity (Models):** `Student`, `Internship`, `Application`, etc.
* **Control (Controllers):** `StudentController`, `ApplicationController`, `CSSController` (Business Logic).
* **Boundary (UI):** `LoginUI`, `StudentUI`, `CompanyRepUI` (Input/Output).

### OO Principles Applied
* **Inheritance & Polymorphism:** Achieved via the **`User`** abstract class, allowing various roles (`Student`, `CR`, `Staff`) to share core functionality like `login` and `changePassword`, while implementing role-specific actions polymorphically.
* **Encapsulation:** All attributes in Entity classes (e.g., `Internship` slots) are private (`-`) and managed through controlled public methods (`+ setSlots(...)`).
* **Low Coupling:** The UI classes are only coupled to the Controller classes (Dependency), not directly to the data models, making the system highly maintainable.

---

## Deliverables

This submission includes the following files/folders:

* **`README.md`** (This file)
* **Report.pdf** (UML Diagrams, Design Considerations, Reflection)
* **`src/` folder:** Contains all implementation source code (`.java` files)
* **`doc/` folder:** Contains the Java API HTML documentation generated using **Javadoc**
* **`data/` folder:** Contains the CSV files used for initialization (`sample_student_list.csv`, `internship_list.csv`, etc.) 
* **GitHub Repository Link:** [Insert your GitHub URL here]
