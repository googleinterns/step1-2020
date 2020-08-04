# Snippet

Snippet is a full stack Custom Search web application with Knowledge Panel suggestions and user ratings for coding related Google Custom Search queries. 

This repository contains the Google STEP program capstone project of [@AaronLopes](https://github.com/AaronLopes), [irisliu77](https://github.com/irisliu77), and [@kaylaleung](https://github.com/kaylaleung).

## Set Up

This project requires JDK 8.

This project also requires an
[API Key](https://developers.google.com/custom-search/v1/introduction) 
and three total [Google Custom Search Engine IDs](https://cse.google.com/cse/all) 
added to the `SearchServlet.java` file, one per restricted site client: 
[StackOverflow](https://stackoverflow.com/), 
[W3Schools](https://www.w3schools.com/), 
[GeeksForGeeks](https://www.geeksforgeeks.org/).

To run this project, do `mvn package appengine:run` in the project root
directory.

To run tests in this project do `mvn test` in the project root directory.

## GitHub Checks

This repository runs checks on every pull request and commit. You can run these
locally from the root directory of the project.

- Java Continuous Integration: Run `mvn package`
- Java Format: Follow the instructions at
  https://github.com/google/google-java-format. Note that this formats your code
  without telling you the changes first.
- JavaScript Lint:
  - One time setup: run `npm install`
  - Run `./node_modules/.bin/eslint .`
