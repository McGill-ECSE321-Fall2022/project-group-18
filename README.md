# ECSE 321 Museum Website - group 18

# Project Scope
The museum in a small town needs help managing their inventory and has hired our engineering team to design and implement a museum website. 
The website will allow customers, employees, and the museum owner to access the website. 
All employees of the museum will be able to move artifacts around from one room to another. 
Customers will be able to view artifacts on display, make loan requests, and donate new artifacts to the museum. 
The museum owner will be able to perform any employee action, but will also be able to perform additional administrative tasks.
No payments will be handled by the museum website.

## Tasks

### Deliverable 1

* Translate project description into requirements
* Create domain model based on requirements
* Create use-case diagrams based on the most important requirements
* Write scenarios explaining each use-case diagram
* Create a new Gradle project with Spring Initializr and the necessary dependencies
* Generate domain model into java code
* Annotate model with JPA annotations
* Create postgreSQL database
* Test persistence for each model class

### Deliverable 2

* Translate use cases into one or more database manipulations (deemed actions for further reference)
* Implement the DTOs required for carrying out the actions
* Implement the Service methods required for carrying out the actions
* Implement the Controller method endpoints for carrying out the actions
* Test Service methods with a mock database (using Mockito)
* Test Controller methods (using SpringBootTest)
* Report on test coverage 

# Team Members

| Team Member | GitHub | Major | Member Role | 
| --- | --- | --- | --- |
| Andrei Sandor | [andrei-sandor](https://github.com/andrei-sandor) | U2 Software Engineer | Project Manager |
| Feihong Chu | [8d5b9b](https://github.com/8d5b9b) | U3 Computer Engineering | Senior Developer |
| Matthew Litwiller | [mattlitwiller](https://github.com/mattlitwiller) | U3 Software Engineer | Scrum Master |
| Aymen Ouali | [amnbot](https://github.com/amnbot) | U2 Software Engineer | Testing Lead |
| Zachary Godden | [ZachGodden](https://github.com/ZachGodden) | U3 Computer Engineer | Software Developer |
| Mohammad Shaheer Bilal | [mohammadShaheerb](https://github.com/mohammadShaheerb) | U3 Computer Engineer | Software Developer |

## Deliverable 1

| Team Member | Deliverable 1 hours | Deliverable 1 Individual Efforts | 
| --- | --- | ---------- |
| Andrei Sandor | 20 | Creating requirements, creating use case with MSS and alternatives, helped for the domain model, created the folder structure (setting everything), did persistence layer and persistence testing for artifacts, donations and loan artifacts, created and edited the wiki  |
| Feihong Chu | 17 | Updating domain model, testing persistence of Room and Loans, editing the wiki, updating the testing files |
| Matthew Litwiller | 18 | Domain modeling with Umple, creating issues and managing the project tab, testing persistence of Business and BusinessHour classes, documentation of the wiki and readme |
| Aymen Ouali | 10 | Testing persistence of Owner and Customer classes, which extend the abstract class Person. |
| Zachary Godden | 10 | Testing Ticket persistence layer |
| Mohammad Shaheer Bilal | 12 | Testing persistence of Employee and EmployeeHour classes, creating use case with MSS and alternative scenarios and helped with domain model. |

## Deliverable 2

| Team Member | Deliverable 2 hours | Deliverable 2 Individual Efforts | 
| --- | --- | ---------- |
| Andrei Sandor | 40 | Worked on the endpoints, controller,service methods and the unit testing and the integration testing of the artifacts and donation. Helped at the begining to have our enpoints working. Also, created DTO's for the classes that I had to implement. Helped teammates for their bugs. |
| Feihong Chu | - | - |
| Matthew Litwiller | - | Ideated all necessary endpoints to accomplish the use cases + helped assign the work associated to all team members. Implemented + tested all Business, BusinessHour, MuseumSetup Services, Controller, and DTOs (+ assisted in testing with other parts of the museum - most notably Employee services + unit testing). Provided code reviews to team members, and wrote the reports in the wiki |
| Aymen Ouali | 31 | Created the DTOs, Controlller and Service for Customer, Owner and Employee. Made the Service tests for Customer, Owner and Employee. Did integration test on Customer and Owner, and contributed to the integration tests on Business and BusinessHour. |
| Zachary Godden | - | - |
| Mohammad Shaheer Bilal | - | - |

# Documentation 
[Deliverable 1](https://github.com/McGill-ECSE321-Fall2022/project-group-18/wiki/Deliverable-1) <br/>
[Deliverable 2](https://github.com/McGill-ECSE321-Fall2022/project-group-18/wiki/Deliverable-2) <br/>
[Project Report 1](https://github.com/McGill-ECSE321-Fall2022/project-group-18/wiki/Project-Report-1) <br/>
[Project Report 2](https://github.com/McGill-ECSE321-Fall2022/project-group-18/wiki/Project-Report-2) <br/> 
