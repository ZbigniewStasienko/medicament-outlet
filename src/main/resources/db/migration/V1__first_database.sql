CREATE TABLE Picture (
    id UUID PRIMARY KEY,
    data BYTEA NOT NULL
);

CREATE TABLE Pharmacy (
    id UUID PRIMARY KEY,
    picture_id UUID REFERENCES Picture(id),
    address TEXT NOT NULL,
    phone_number TEXT,
    name TEXT NOT NULL,
    email TEXT,
    latitude TEXT,
    longitude TEXT,
    opening_hours TEXT
);

CREATE TABLE Medicine (
    id UUID PRIMARY KEY,
    pharmacy_id UUID REFERENCES Pharmacy(id),
    picture_id UUID REFERENCES Picture(id),
    name TEXT NOT NULL,
    description TEXT,
    size TEXT
);

CREATE TABLE Product (
    id UUID PRIMARY KEY,
    medicine_id UUID REFERENCES Medicine(id),
    expiration_date DATE,
    is_reserved BOOLEAN,
    base_price DOUBLE PRECISION,
    price DOUBLE PRECISION
);

CREATE TABLE users (
    id UUID PRIMARY KEY
);

CREATE TABLE Reservation (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    pharmacy_id UUID REFERENCES Pharmacy(id),
    realization_date DATE,
    is_realized BOOLEAN,
    status INTEGER
);

CREATE TABLE ReservedProduct (
    id UUID PRIMARY KEY,
    product_id UUID REFERENCES Product(id),
    reservation_id UUID REFERENCES Reservation(id)
);
