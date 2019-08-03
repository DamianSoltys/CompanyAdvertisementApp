INSERT INTO voivodeships(name) VALUES ('dolnośląskie'),('kujawsko-pomorskie'),('lubelskie'), ('lubuskie'),
('łódzkie'), ('małopolskie'), ('mazowieckie'), ('opolskie'), ('podkarpackie'), ('podlaskie'), ('pomorskie'), ('śląskie'),
('świętokrzyskie'), ('warmińsko-mazurskie'), ('wielkopolskie'),('zachodniopomorskie');

INSERT INTO email_addresses(email_id, email, created_at) VALUES
(1, 'example@example.com', NOW()),
(2, 'example2@example.com', NOW()),
(3, 'example3@example.com', NOW()),
(4, 'example4@example.com', NOW()),
(5, 'example5@example.com', NOW()),
(6, 'example6@example.com', NOW()),
(7, 'example7@example.com', NOW()),
(8, 'example8@example.com', NOW());

INSERT INTO addresses(apartment_no, building_no, city, created_at, modified_at, street, voivodeship_id)  VALUES
('5', '43','Kraków', NOW(), NOW(), 'Swojska', 1),
('2', '23','Lublin', NOW(), NOW(), 'Wasza', 2),
('5', '23','Szczebrzeszyn', NOW(), NOW(), 'Koziołka Matołka', 3),
('5', '4','Kraków', NOW(), NOW(), 'Makówkowa', 4),
('5', '2','Warszawa', NOW(), NOW(), 'Ichniejsza', 5);


INSERT INTO natural_persons(id_natural_person, first_name, last_name, phone_no, created_at, modified_at, address_id) VALUES
(1, 'Karol', 'Karolak', '555111222', NOW(), NOW(), 1 ),
(2, 'Maciej', 'Kozidrak', '535411222', NOW(), NOW(), 2),
(3, 'Agata', 'Trzeciak', '355111222', NOW(), NOW(), 3 ),
(4, 'Karolina', 'Piekarska', '535111221', NOW(), NOW(), 4),
(5, 'Jerzy', 'Brzęczak', '555115221', NOW(), NOW(),5),
(6, 'Marika', 'Brzozowska', '225111222', NOW(), NOW(),1),
(7, 'Konrad', 'Pęczarski', '555111111', NOW(), NOW(),2),
(8, 'Karol', 'Maciejak', '555222222', NOW(), NOW(),3),
(9, 'Marcin', 'Lewandowski', '111111222', NOW(), NOW(),4);


# --password - Haslo1
INSERT INTO users( user_name, password_hash, modified_at,created_at, id_natural_person, id_email_address, account_type, is_enabled) VALUES
( 'biedronka', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), 3, 1, 0, true ),
( 'kurczak5', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 2, 0 , true),
( 'magicznyKrzystof', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 3, 0, false),
( 'Gacek', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 4, 0, false ),
( 'maciejowicz', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 5, 0, false ),
( 'jezyk', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), 5, 6, 0, true );

INSERT INTO categories(id, name, created_at, modified_at) VALUES
(1, 'Motoryzacja', NOW(), NOW()),
(2, 'Technologia', NOW(), NOW()),
(3, 'Usługi', NOW(), NOW()),
(4, 'Fotografia', NOW(), NOW()),
(5, 'Restauracje', NOW(), NOW()),
(6, 'Uroda', NOW(), NOW());

INSERT INTO
companies(id, name, NIP, REGON,  description, registerer_id, category_id, created_at, modified_at, has_branch, address_id)
VALUES
(1, 'Mechanikex', '1234567890', '12345678901234', 'Najlepszy w mieście mechanik.', 2, 1, NOW(), NOW(), TRUE, 1),
(2, 'Urodex', '2234567890', '22345678901234', 'Najlepszy w mieście zakład fryzjerski.', 3, 6, NOW(), NOW(), TRUE, 3);

INSERT INTO
branches(branch_id, registerer_id, company_id, name, created_at, modified_at, x_geo_coordinate, y_geo_coordinate, address_id) VALUES
(1, 2, 1, 'Mechanikex', NOW(), NOW(), 51.1079, 17.0385, 1),
(2, 2, 1, 'Mechanikex', NOW(), NOW(), 51.1071, 17.0383, 2),
(3, 3, 2, 'Urodex', NOW(), NOW(), 51.2167, 22.734,3 );

INSERT INTO newsletter_subscriptions(id, company_id,id_email, verified, created_at, modified_at) VALUES
(1, 1,7, FALSE, NOW(), NOW()),
(2, 1,8, FALSE, NOW(), NOW());

