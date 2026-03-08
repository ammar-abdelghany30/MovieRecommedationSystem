Movie Recommendation System
Overview

The Movie Recommendation System is a Java-based application that reads movie and user data from input files, validates the information according to predefined rules, and generates movie recommendations for each user based on their preferred categories.

The system performs strict validation and stops execution immediately if any input error is detected, reporting only the first encountered error.

Features

Read movie data from movies.txt

Read user data from users.txt

Validate movie titles, movie IDs, usernames, and user IDs

Generate movie recommendations based on user preferences

Stop execution and report the first detected input error

Output results to recommendations.txt

Project Structure
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
