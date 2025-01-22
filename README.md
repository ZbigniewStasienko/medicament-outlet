# Medicament Outlet

This project is a Spring Boot application that manages pharmaceutical products with short expiration dates.

## How to Run
Since the project utilizes some external APIs, there is currently no straightforward way to run it locally without additional configuration.

You can create a `.env` file containing the secret credentials for the email-sending API and authorization API. However, these credentials alone are not sufficient because multiple enhancements must be made to the authorization API for full compatibility.

## Features

The application provides separate interfaces for different types of users on the platform: individual clients, pharmacies, and administrators. Below are brief descriptions and screenshots of each interface.

### Admin

To access the **Admin** interface, navigate to:

    http://localhost:8080/admin

This endpoint is protected and requires you to log in with the appropriate role.

![Screenshot from 2025-01-22 18-09-16](https://github.com/user-attachments/assets/7534f609-7134-4652-9520-1c74ea34d53f)

After a successful login, you will see the main screen, where an admin can add or delete pharmacies (editing pharmacy info is handled by pharmacy workers only).

![Screenshot from 2025-01-22 18-29-55](https://github.com/user-attachments/assets/c529736a-6791-4474-b98a-68b55da74f29)

### Pharmacy

To access the **Pharmacy** interface, navigate to:

    http://localhost:8080/pharmacy
    
This endpoint is also protected and requires you to log in with the appropriate credentials. After a successful login, the following menu will appear:

![image](https://github.com/user-attachments/assets/58cba6a6-5cc3-4d86-ab6b-8123b8f951c6)

From the main menu, pharmacy workers can:

- Add new medicines
- Edit pharmacy information
- View pending reservations

In the screenshot above, there are currently no reservations for this particular pharmacy (`0` above the “Show Reservations” button).

There is a small distinction between `medicine` and `product`. A `medicine` contains information shared among multiple `products`, such as the name, size, and picture. A `product` contains details specific to a particular item, such as the expiration date or price. This approach enhances code reusability. Below is an example of how a new `product` is added to the pharmacy's offer:

![Screenshot from 2025-01-22 19-19-17](https://github.com/user-attachments/assets/6fbbda82-5016-4608-98f5-b07ca3132727)


### Client

To access the **Client** interface, navigate to:

    http://localhost:8080

This opens the default application view, where end users (clients) can place reservations for medicines:

![image](https://github.com/user-attachments/assets/db94e43d-ad3f-427e-9699-4976bf41aef5)

Users can:

- Sort products (by price or pharmacy location)
- Filter products
- View pharmacy details and inspect only that pharmacy’s products

![image](https://github.com/user-attachments/assets/47ff4877-ef2e-4c8e-98e2-3a5d0174f58f)

### Additional features

- **Automatic Removal of Expired Medicines**: Every 24 hours, the system checks for medicines that are past their due date and removes them from the database.
- **Email Notifications**:

    - Pharmacies receive an email when a new reservation is placed for one of their products.
    - Clients receive an email when their reservation is marked as “ready to collect.”

- **Secure User Data**: All user data is securely stored using an external identity provider service.
- **Account Management**:

    - Individual clients can create their own accounts directly.
    - Pharmacies must request an account via email to the admin, who will set up the pharmacy account.
    - Admin accounts are created by existing admins.
