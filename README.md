# Bug Tracking System - BugHerd

A bug tracking system that helps to record, assign and track bugs in a software development project.

## Technology Stack

- **Frontend:** JSP/HTML, CSS, Bootstrap
- **Database:** MySQL
- **Backend:** Spring MVC with Interceptors, Filters, Validators, Hibernate with annotations

## Functionalities

- User can open a ticket for an issue
- Issue cannot be created for a project that has been closed
- User can assign severity level (how much that bug relates to them)
- Analytics dashboard to view open issues, sort bugs by severity and search through them
- Search through issues/tickets based on each field of the issue
- Project Manager can add or remove developers from the project
- Developers will be able to override the severity of an issue by giving a reason
- Users can add their comments on open issues
- Administrators can create, edit and delete projects and assign them to managers

## Configuration

Add jdbc.properties file to resources folder with the following attributes:
```shell
jdbc.url=jdbc:mysql://localhost:3306/DATABASE_NAME
jdbc.username=YOUR_DB_USERNAME
jdbc.password=YOUR_DB_PASSWORD
```


 
