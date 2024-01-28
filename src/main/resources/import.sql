-- Import SQL file to insert data

-- Insert roles
INSERT INTO roles(role_name) VALUES('ADMIN');

INSERT INTO roles(role_name) VALUES('SOCIO');

-- Insert admin user
INSERT INTO users(user_email, user_password, role_id) VALUES ('admin@mail.com', '$2a$10$WHA7Rwnti3PLuYZlaxY/zORWt0awaMWoxaKv0pFphGntI3oLDqXU2', 1);