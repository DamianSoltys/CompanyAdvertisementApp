INSERT INTO VOIVODESHIPS(name) VALUES ('dolnośląskie'),('kujawsko-pomorskie'),('lubelskie'), ('lubuskie'),
('łódzkie'), ('małopolskie'), ('mazowieckie'), ('opolskie'), ('podkarpackie'), ('podlaskie'), ('pomorskie'), ('śląskie'),
('świętokrzyskie'), ('warmińsko-mazurskie'), ('wielkopolskie'),('zachodniopomorskie');

INSERT INTO EMAIL_ADDRESSES(email_id, email, created_at) VALUES
(1, 'example@example.com', NOW()),
(2, 'example2@example.com', NOW()),
(3, 'example3@example.com', NOW()),
(4, 'example4@example.com', NOW()),
(5, 'example5@example.com', NOW()),
(6, 'example6@example.com', NOW()),
(7, 'example7@example.com', NOW()),
(8, 'example8@example.com', NOW());

INSERT INTO NATURAL_PERSONS(id_natural_person, first_name, last_name, voivodeship_id, city, street,
apartment_no, building_no, phone_no, created_at, modified_at) VALUES
(1, 'Karol', 'Karolak',1, 'Wrocław', 'Grolska','5d', '34', '555111222', NOW(), NOW()),
(2, 'Maciej', 'Kozidrak',1, 'Wrocław', 'Górska','50a', '44', '535411222', NOW(), NOW()),
(3, 'Agata', 'Trzeciak',3, 'Lublin', 'Wolska','5', '20', '355111222', NOW(), NOW()),
(4, 'Karolina', 'Piekarska',16, 'Gdynia', 'Morska','50', '20', '535111221', NOW(), NOW()),
(5, 'Jerzy', 'Brzęczak',2, 'Toruń', 'Mazowiecka','5h', '36', '555115221', NOW(), NOW()),
(6, 'Marika', 'Brzozowska',4, 'Zielona Góra', 'Kupiecka','4', '11', '225111222', NOW(), NOW()),
(7, 'Konrad', 'Pęczarski',15, 'Poznań', 'Cicha','12', '44', '555111111', NOW(), NOW()),
(8, 'Karol', 'Maciejak',7, 'Warszawa', 'Głośna','2a', '25', '555222222', NOW(), NOW()),
(9, 'Marcin', 'Lewandowski',3, 'Lublin', 'Sklepowa','116', '24', '111111222', NOW(), NOW());


--password - Haslo1
INSERT INTO USERS(user_id, user_name, password_hash, modified_at,created_at, id_natural_person, id_email_address, account_type) VALUES
(1, 'biedronka', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), 3, 1, 0 ),
(2, 'kurczak5', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 2, 0 ),
(3, 'magicznyKrzystof', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 3, 0 ),
(4, 'Gacek', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 4, 0 ),
(5, 'maciejowicz', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 5, 0 ),
(6, 'jezyk', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), 5, 6, 0 );

INSERT INTO CATEGORIES(id, name, created_at, modified_at) VALUES
(1, 'Motoryzacja', NOW(), NOW()),
(2, 'Technologia', NOW(), NOW()),
(3, 'Usługi', NOW(), NOW()),
(4, 'Fotografia', NOW(), NOW()),
(5, 'Restauracje', NOW(), NOW()),
(6, 'Uroda', NOW(), NOW());

INSERT INTO
COMPANIES(id, name, NIP, REGON, voivodeship_id, city, street, building_no, description, registerer_id, category_id, created_at, modified_at, photo_path, has_branch)
VALUES
(1, 'Mechanikex', '1234567890', '12345678901234', 1, 'Wrocław', 'Swojska', '5', 'Najlepszy w mieście mechanik.', 2, 1, NOW(), NOW(), NULL, TRUE),
(2, 'Urodex', '2234567890', '22345678901234', 3, 'Świdnik', 'Zielona', '20', 'Najlepszy w mieście zakład fryzjerski.', 3, 6, NOW(), NOW(), NULL, TRUE);

INSERT INTO
BRANCHES(branch_id, registerer_id, company_id, name, voivodeship_id, city, street, building_no, created_at, modified_at, x_geo_coordinate, y_geo_coordinate) VALUES
(1, 2, 1, 'Mechanikex', 1, 'Wrocław', 'Swojska', '5', NOW(), NOW(), 51.1079, 17.0385),
(2, 2, 1, 'Mechanikex', 1, 'Wrocław', 'Kalinowska', '25', NOW(), NOW(), 51.1071, 17.0383),
(3, 3, 2, 'Urodex', 3, 'Świdnik', 'Zielona', '20', NOW(), NOW(), 51.2167, 22.734);

INSERT INTO newsletter_subscriptions(id, company_id,id_email, verified, created_at, modified_at) VALUES
(1, 1,7, FALSE, NOW(), NOW()),
(2, 1,8, FALSE, NOW(), NOW());

INSERT INTO favourite_branches(user_id, branch_id, created_at, modified_at) VALUES
(4, 3, NOW(), NOW()),
(5, 3, NOW(), NOW());

INSERT INTO RATINGS(id, rating, user_id, branch_id, created_at, modified_at) VALUES
(1, 3, 3,3, NOW(), NOW()),
(2,4, 4,3, NOW(), NOW());

INSERT INTO COMMENTS(id, comment, created_at, modified_at, user_id, branch_id) VALUES
(1,'Przeciętna obsługa.', NOW(), NOW(), 3, 3),
(2, 'Dosyć dobra obsługa.', NOW(), NOW(), 4,3);






