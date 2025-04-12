# Student Management System

## Overview

This is a simple student management system with a functional login and account creation feature backed by a database. Once logged in, users can add students to the database with the following details:

- First Name
- Last Name
- Major
- Department

The application also provides enhanced UI features including dark/light mode toggle and a table view to display all students.

## Features

### 🔐 Account Creation and Login
- Users can **create an account** and securely log in using credentials stored in the database.
- **Validation** ensures users enter appropriate information when creating an account or signing in.

### ➕ Add Students
- After logging in, users can add student records with:
  - First Name
  - Last Name
  - Major
  - Department
- Records are inserted directly into the underlying database.

### 📋 Show Students
- A **"Show Students"** button allows users to fetch and view all student records stored in the database.
- Results are displayed in a **TableView** for easy readability.

### 🌙 Light & Dark Mode
- Toggle between **light and dark themes** to enhance user experience and accessibility.

### 💅 Enhanced UI
- The interface has been improved for a more intuitive and pleasant user experience.
- Clear layout, improved responsiveness, and feedback messages guide users through the process.

## Technologies Used

- **JavaFX** (UI)
- **JDBC** (Database Connectivity)
- **SQLite / MySQL** (or whichever DB you used — specify here)
- **Scene Builder** (Optional, if used for FXML)

## Setup Instructions

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-username/student-db-app.git
   cd student-db-app
