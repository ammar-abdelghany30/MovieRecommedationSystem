# Movie Recommendation System

## Overview

The **Movie Recommendation System** is a Java-based application that reads movie and user data from input files, validates the information according to predefined rules, and generates movie recommendations for each user based on their preferred categories.

The system performs strict validation and stops execution immediately if any input error is detected, reporting only the first encountered error.

---

## Features

* Read movie data from `movies.txt`
* Read user data from `users.txt`
* Validate movie titles, movie IDs, usernames, and user IDs
* Generate movie recommendations based on user preferences
* Stop execution and report the first detected input error
* Output results to `recommendations.txt`

---

## Project Structure

```
MovieRecommendationSystem
│
├── src
│   └── main
│       ├── java
│       │   ├── Main.java
│       │   ├── Movie.java
│       │   ├── User.java
│       │   ├── Validator.java
│       │   ├── FileReader.java
│       │   └── RecommendationEngine.java
│       │
│       └── resources
│           ├── movies.txt
│           └── users.txt
│
├── recommendations.txt
├── pom.xml
└── README.md
```

---

## Input Files

### movies.txt

```
Movie Title,MovieID
category1,category2,...
```

Example:

```
The Matrix,MATRIX001
action,sci-fi
```

---

### users.txt

```
Username,UserID
liked_category1,liked_category2,...
```

Example:

```
John Smith,12345678A
action,drama
```

---

## Output File

### recommendations.txt

Example output:

```
For User: John Smith,12345678A
{action}: MATRIX001-The Matrix
{drama}: MOV123-The Godfather
```

---

## Validation Rules

### Movie Data

* Movie Title: Every word must start with a capital letter.
* Movie ID: Must contain uppercase letters followed by three digits.

### User Data

* Username: Alphabetic characters and spaces only and must not start with a space.
* User ID: Exactly 9 characters starting with numbers and optionally ending with one alphabetic character.

---

## Error Handling

If any validation rule fails, the system stops immediately and writes the first encountered error message to `recommendations.txt`.

Example:

```
Movie Title ERROR: the matrix is wrong
```

---

## Technologies Used

* Java
* Maven
* File I/O
* Exception Handling

---

## Authors

- Ammar Abdelghany
- Ali Abdelmaboud
- Basil Sherif
- Amr Emad
- Islam el demerdash 

## Course
Software Testing – Spring 2026  
Faculty of Engineering – Ain Shams University
