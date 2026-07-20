# Sprint 2

Sprint backlog, sprint review artifacts, screenshots, and deliverables.

_________________________

# 1. Forecast

During our first sprint, we scheduled the four stories with the lowest point total, resulting in a work load of 21 story points. With a team operating at 100% capacity, we comfortably finished ahead of schedule, setting this as our normalized velocity. However, while discussing the results of the sprint, we felt as though we were operating at approximately two-thirds of our potential capacity, which we opted to take into consideration while scheduling Sprint 2.

While scheduling, we determined that we would again be at 100% capacity for the duration of Sprint 2. Therefore, no adjustments needed to be made to accommodate fluctuations.

Were we to directly use our previous sprint as a baseline, our calculated target points for Sprint 2 would by 21. However, based on our conclusion that we were operating at two-thirds of our potential capacity, we decided to schedule an extra 50% of the points that we worked on for Sprint 1, resulting in 31.5 points (rounding down to 31). This is the final point load that we decided to take for Sprint 2.

As an addition, after completing the stories scheduled for this sprint, we still had a comfortable amount of time left over. Most of the group opted to work on documenting at this point, but because there was a limited amount of work remaining, Antoine Davis opted to begin researching requirements for continuous deployment. We estimated these efforts to be 3 story points and added this to the sprint, bringing our final total to 34 story points.

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
