CREATE TABLE IF NOT EXISTS Property (
    id IDENTITY PRIMARY KEY,
    title VARCHAR(255),
    location VARCHAR(255),
    price DOUBLE,
    type VARCHAR(20)
);