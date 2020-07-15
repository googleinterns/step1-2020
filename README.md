# Snippet

This repository contains the capstone project for a pod of interns (step1-2020)
in Google's Student Training in Engineering program.

## Set Up

This project requires JDK 11.

This project also requires the an [API Key](https://developers.google.com/custom-search/v1/introduction) and three [Google Custom Search Engine IDs](https://cse.google.com/cse/all) added to the `SearchServlet.java` file per restricted site client: [StackOverflow](http://stackoverflow.com/), [W3Schools](https://www.w3schools.com/), [GeeksForGeeks](https://www.geeksforgeeks.org/).

To run this project, do `mvn package appengine:run` in the project root
directory.

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
