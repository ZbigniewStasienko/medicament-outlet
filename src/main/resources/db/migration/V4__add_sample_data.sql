INSERT INTO Pharmacy (id, picture_id, address, phone_number, name, email, opening_hours) VALUES
('84f2a565-5429-4844-b0e1-a50cd509121b', '00000000-0000-0000-0000-000000000011', '789 Elm Street', '555-7890', 'Pharmacy One', 'support@pharmacy.com', 'Mon-Sun 7am-9pm'),
('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000012', '1010 Oak Avenue', '555-1010', 'Pharmacy Two', 'contact@pharmacy.com', 'Mon-Fri 8am-6pm'),
('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000013', '202 Maple Road', '555-2020', 'Pharmacy Three', 'info@pharmacy.com', '24/7');

INSERT INTO Medicine (id, pharmacy_id, picture_id, name, description, size) VALUES
('00000000-0000-0000-0000-000000000013', '84f2a565-5429-4844-b0e1-a50cd509121b', '00000000-0000-0000-0000-000000000001','Paracetamol', 'Fever reducer', '500mg'),
('00000000-0000-0000-0000-000000000014', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000002','Cetirizine', 'Antihistamine', '10mg'),
('00000000-0000-0000-0000-000000000015', '84f2a565-5429-4844-b0e1-a50cd509121b', '00000000-0000-0000-0000-000000000003', 'Amoxicillin', 'Antibiotic', '250mg'),
('00000000-0000-0000-0000-000000000016', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000004', 'Lisinopril', 'Blood pressure medication', '10mg'),
('00000000-0000-0000-0000-000000000017', '00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000005', 'Metformin', 'Type 2 diabetes treatment', '500mg');

INSERT INTO Product (id, medicine_id, expiration_date, is_reserved, base_price, price) VALUES
('00000000-0000-0000-0000-000000000023', '00000000-0000-0000-0000-000000000013', '2024-11-30', FALSE, 3.0, 2.5),
('00000000-0000-0000-0000-000000000024', '00000000-0000-0000-0000-000000000014', '2025-05-15', FALSE, 7.0, 6.5),
('00000000-0000-0000-0000-000000000025', '00000000-0000-0000-0000-000000000015', '2023-08-20', FALSE, 12.0, 11.0),
('00000000-0000-0000-0000-000000000026', '00000000-0000-0000-0000-000000000016', '2026-02-28', FALSE, 15.0, 14.0),
('00000000-0000-0000-0000-000000000027', '00000000-0000-0000-0000-000000000017', '2025-09-10', FALSE, 9.0, 8.5),
('00000000-0000-0000-0000-000000000028', '00000000-0000-0000-0000-000000000013', '2024-03-31', FALSE, 5.0, 4.5),
('00000000-0000-0000-0000-000000000029', '00000000-0000-0000-0000-000000000014', '2024-12-31', FALSE, 8.0, 7.0);