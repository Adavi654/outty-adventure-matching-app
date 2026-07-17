## Continuous Integration

GitHub Actions is used for continuous integration because it integrates directly with GitHub, automatically runs on pushes and pull requests to `main`, requires no separate CI platform, and is appropriate for this class project.

The backend job uses Java 21 and Maven to build the Spring Boot backend and run automated tests. The frontend job uses Node.js 24 to install dependencies, run ESLint, and build the React/Vite frontend.

Successful CI results provide evidence that the current code builds and tests successfully.

[View GitHub Actions evidence](https://github.com/Adavi654/outty-adventure-matching-app/actions)

