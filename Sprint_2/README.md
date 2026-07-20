# Sprint 2

Sprint backlog, sprint review artifacts, screenshots, and deliverables.

_________________________

# 1. Forecast

During Sprint 1, our team completed all 21 forecasted story points ahead of schedule while operating at full team availability. This established an initial baseline velocity of 21 story points using the Yesterday's Weather forecasting pattern.

All team members were again available at 100% capacity for Sprint 2, so no adjustments were required for team availability. Although Yesterday's Weather would suggest forecasting another 21 story points, our experience during Sprint 1 indicated that the team had completed its work comfortably and consistently had additional development capacity remaining. Based on this observation, we decided to increase our Sprint 2 forecast by approximately 50%, resulting in a planned workload of 31 story points (31.5 rounded down).

During Sprint 2, the team completed the planned work ahead of schedule. With development work remaining, Antoine Davis began researching continuous deployment requirements. This additional work was estimated at 3 story points and added to the sprint backlog during the sprint, bringing the total completed work to 34 story points. These additional points were not part of the original Sprint 2 forecast but will be reflected in the team's velocity when planning future sprints.

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
