-- Insert mock driver data
INSERT INTO driver (first_name, last_name, license_number, phone_number, email, address, status, notes, license_expiry_date)
VALUES 
    ('John', 'Doe', 'DL123456', '+1234567890', 'john.doe@example.com', '123 Main St, City', 'ACTIVE', 'Experienced driver', '2025-12-31'),
    ('Jane', 'Smith', 'DL789012', '+1987654321', 'jane.smith@example.com', '456 Oak Ave, Town', 'ACTIVE', 'Night shift specialist', '2024-06-30'),
    ('Mike', 'Johnson', 'DL345678', '+1122334455', 'mike.j@example.com', '789 Pine Rd, Village', 'ON_LEAVE', 'On medical leave', '2024-09-15'),
    ('Sarah', 'Williams', 'DL901234', '+1555666777', 'sarah.w@example.com', '321 Elm St, City', 'ACTIVE', 'New driver', '2025-03-31'),
    ('David', 'Brown', 'DL567890', '+1888999000', 'david.b@example.com', '654 Maple Dr, Town', 'INACTIVE', 'Resigned', '2023-12-31'); 