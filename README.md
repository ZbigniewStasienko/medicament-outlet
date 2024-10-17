# Medicament Outlet

This project is a Spring Boot application that manages pharmaceutical products with short expiration date.

## Prerequisites

To run the project locally, ensure you have the following installed:

- Java 21
- Docker 

## How to Run
In the terminal in the project folder run: 

    make clean prepare migrate run

## How to use

### Admin

To see ``admin`` menu type following in web browser:

    http://localhost:7777/admin

This should open up menu that enables ``admin`` actions:

![Screenshot from 2024-10-13 21-20-15](https://github.com/user-attachments/assets/8ffa1ac5-570b-4df9-90e5-7ac305e4504a)

### Pharmacy

To see ``pharmacy`` menu type following in web browser (pharmacy-id can be taken from 4th migration, for example: 00000000-0000-0000-0000-000000000003)

    http://localhost:7777/pharmacy/[pharmacy-id]

This should open up menu that enables ``pharmacy`` actions:

![Screenshot from 2024-10-13 21-22-43](https://github.com/user-attachments/assets/240b1369-6ddb-4c0e-9ec4-677ea28a1ab2)

### User

To see ``user`` menu type following in web browser

    http://localhost:7777/

This should open up menu that enables ``user`` actions:

![Screenshot from 2024-10-13 21-24-12](https://github.com/user-attachments/assets/8d1d5095-5f64-4510-b4e1-fbbbf4ac78d7)
