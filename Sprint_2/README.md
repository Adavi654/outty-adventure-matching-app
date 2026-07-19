# Sprint 2

Sprint backlog, sprint review artifacts, screenshots, and deliverables.

_________________________

# 1. Forecast

Because this was our team's first sprint, we did not have a historical velocity to use when estimating our sprint capacity. As a result, we based our forecast on the work we planned for the sprint rather than on past performance.

After estimating the effort for each user story using story points, we divided the 12 user stories across our three sprints. We intentionally scheduled the four user stories with the lowest combined effort in the first sprint, resulting in a forecast of 21 story points for Sprint 1. This conservative estimate allowed us to establish an initial team velocity while reducing the risk of overcommitting during our first iteration.

At the end of the sprint, we planned to compare our completed story points with this forecast to establish a baseline velocity that would assist us in planning for the remaining sprints.

_________________________

# 2. Continuous Integration Rationale and Evidence

GitHub Actions is used for continuous integration because it integrates directly with GitHub, automatically runs on pushes and pull requests to `main`, requires no separate CI platform, and is appropriate for this class project.

The backend job uses Java 21 and Maven to build the Spring Boot backend and run automated tests. The frontend job uses Node.js 24 to install dependencies, run ESLint, and build the React/Vite frontend.

Successful CI results provide evidence that the current code builds and tests successfully.

[View GitHub Actions evidence](https://github.com/Adavi654/outty-adventure-matching-app/actions)

_________________________

# 3. Documentation

- Story Tasks and Kanban Board (Jira): https://swe-6733.atlassian.net/jira/software/projects/OUTTY/boards/1
- Burndown Chart Image: In Git repository under Sprint_2/Burndown_Chart
- Annotated Scrum Evidence: In Git repository under Sprint_2/Annotated_Scrum
- Mobbing Evidence: In Git repository under Sprint_2/Mobbing
- Sprint Review: In Git repository under Sprint_2/Sprint_Review
- Passed BDD and Unit Tests: In Git repository under Sprint_2/Passed_Tests
- Working application: https://outty-adventure-matching-app.onrender.com
