# Medicament Outlet

This project is a Spring Boot application that manages pharmaceutical products with short expiration date.

## Prerequisites

To run the project locally, ensure you have the following installed:

- Java 21
- Docker 

## How to Run
In the terminal in project folder run: 

    make clean prepare migrate run

## How to use

To see ``admin`` menu type following in web browser:

    http://localhost:7777/admin

This should open up menu that enables ``admin`` actions:

![img.png](img.png)

To see ``pharmacy`` menu type following in web browser (pharmacy-id can be taken from 4th migration, for example: 00000000-0000-0000-0000-000000000003)

    http://localhost:7777/pharmacy/[pharmacy-id]

This should open up menu that enables ``pharmacy`` actions:

![img_1.png](img_1.png)

To see ``user`` menu type following in web browser

    http://localhost:7777/

This should open up menu that enables ``user`` actions:

![img_2.png](img_2.png)
