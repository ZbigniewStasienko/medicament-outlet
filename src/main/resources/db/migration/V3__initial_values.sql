CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO Picture (id, picture) VALUES
    (gen_random_uuid(), 'image1.png'),
    (gen_random_uuid(), 'image2.png'),
    (gen_random_uuid(), 'image3.png');

INSERT INTO Pharmacy (id, picture_id, address, phone_number, name, email, opening_hours) VALUES
    (gen_random_uuid(), (SELECT id FROM Picture LIMIT 1), '123 Main St', '123-456-7890', 'Pharmacy One', 'contact@pharmacyone.com', '9:00 - 18:00'),
    (gen_random_uuid(), (SELECT id FROM Picture OFFSET 1 LIMIT 1), '456 Side Ave', '987-654-3210', 'Pharmacy Two', 'support@pharmacytwo.com', '10:00 - 19:00');

INSERT INTO Medicine (id, pharmacy_id, picture_id, name, description, size) VALUES
    (gen_random_uuid(), (SELECT id FROM Pharmacy LIMIT 1), (SELECT id FROM Picture LIMIT 1), 'Aspirin', 'Pain relief medicine', 100),
    (gen_random_uuid(), (SELECT id FROM Pharmacy OFFSET 1 LIMIT 1), (SELECT id FROM Picture OFFSET 1 LIMIT 1), 'Ibuprofen', 'Anti-inflammatory medicine', 50);

INSERT INTO Product (id, medicine_id, expiration_date, amount, is_reserved, base_price, price) VALUES
    (gen_random_uuid(), (SELECT id FROM Medicine LIMIT 1), '2025-01-01', 50, FALSE, 5.99, 6.99),
    (gen_random_uuid(), (SELECT id FROM Medicine OFFSET 1 LIMIT 1), '2026-01-01', 30, TRUE, 3.99, 4.99);

INSERT INTO "User" (id, name, surname) VALUES
    (gen_random_uuid(), 'John', 'Doe'),
    (gen_random_uuid(), 'Jane', 'Smith');

INSERT INTO Reservation (id, user_id, pharmacy_id, realization_date, is_realized, status) VALUES
    (gen_random_uuid(), (SELECT id FROM "User" LIMIT 1), (SELECT id FROM Pharmacy LIMIT 1), '2024-10-15', TRUE, 1),
    (gen_random_uuid(), (SELECT id FROM "User" OFFSET 1 LIMIT 1), (SELECT id FROM Pharmacy OFFSET 1 LIMIT 1), '2024-10-16', FALSE, 0);

INSERT INTO ReservedProduct (id, product_id, reservation_id, amount) VALUES
    (gen_random_uuid(), (SELECT id FROM Product LIMIT 1), (SELECT id FROM Reservation LIMIT 1), 10),
    (gen_random_uuid(), (SELECT id FROM Product OFFSET 1 LIMIT 1), (SELECT id FROM Reservation OFFSET 1 LIMIT 1), 5);
