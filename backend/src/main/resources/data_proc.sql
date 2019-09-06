use inzynierka;

DELIMITER |
CREATE OR REPLACE PROCEDURE insert_voivoideships()
INSERT IGNORE INTO voivodeships(name)
VALUES ('dolnośląskie'),
       ('kujawsko-pomorskie'),
       ('lubelskie'),
       ('lubuskie'),
       ('łódzkie'),
       ('małopolskie'),
       ('mazowieckie'),
       ('opolskie'),
       ('podkarpackie'),
       ('podlaskie'),
       ('pomorskie'),
       ('śląskie'),
       ('świętokrzyskie'),
       ('warmińsko-mazurskie'),
       ('wielkopolskie'),
       ('zachodniopomorskie');
|

DELIMITER ;
CALL inzynierka.insert_voivoideships();


DELIMITER |
CREATE OR REPLACE PROCEDURE insert_emails()
INSERT IGNORE INTO email_addresses(email_id, email, created_at)
VALUES (1, 'example@example.com', NOW()),
       (2, 'example2@example.com', NOW()),
       (3, 'example3@example.com', NOW()),
       (4, 'example4@example.com', NOW()),
       (5, 'example5@example.com', NOW()),
       (6, 'example6@example.com', NOW()),
       (7, 'example7@example.com', NOW()),
       (8, 'example8@example.com', NOW());
|

DELIMITER ;

CALL insert_emails();

DELIMITER |
CREATE OR REPLACE PROCEDURE insert_addresses()
INSERT IGNORE INTO addresses(apartment_no, building_no, city, created_at, modified_at, street, voivodeship_id)
VALUES ('5', '43', 'Kraków', NOW(), NOW(), 'Swojska', 1),
       ('2', '23', 'Lublin', NOW(), NOW(), 'Wasza', 2),
       ('5', '23', 'Szczebrzeszyn', NOW(), NOW(), 'Koziołka Matołka', 3),
       ('5', '4', 'Kraków', NOW(), NOW(), 'Makówkowa', 4),
       ('5', '2', 'Warszawa', NOW(), NOW(), 'Ichniejsza', 5);
|

DELIMITER ;

CALL insert_addresses();

DELIMITER |
CREATE OR REPLACE PROCEDURE insert_natural_persons()
INSERT IGNORE INTO natural_persons(id_natural_person, first_name, last_name, phone_no, created_at, modified_at,
                                   address_id)
VALUES (1, 'Karol', 'Karolak', '555111222', NOW(), NOW(), 1),
       (2, 'Maciej', 'Kozidrak', '535411222', NOW(), NOW(), 2),
       (3, 'Agata', 'Trzeciak', '355111222', NOW(), NOW(), 3),
       (4, 'Karolina', 'Piekarska', '535111221', NOW(), NOW(), 4),
       (5, 'Jerzy', 'Brzęczak', '555115221', NOW(), NOW(), 5),
       (6, 'Marika', 'Brzozowska', '225111222', NOW(), NOW(), 1),
       (7, 'Konrad', 'Pęczarski', '555111111', NOW(), NOW(), 2),
       (8, 'Karol', 'Maciejak', '555222222', NOW(), NOW(), 3),
       (9, 'Marcin', 'Lewandowski', '111111222', NOW(), NOW(), 4);
|

DELIMITER ;
CALL insert_natural_persons();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_users()
-- password - Haslo1
INSERT IGNORE INTO users(user_name, password_hash, modified_at, created_at, id_natural_person, id_email_address,
                         account_type, is_enabled)
VALUES ('biedronka', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), 3, 1, 0, true),
       ('kurczak5', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 2, 0, true),
       ('magicznyKrzystof', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 3, 0,
        false),
       ('Gacek', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 4, 0, false),
       ('maciejowicz', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 5, 0, false),
       ('jezyk', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), 5, 6, 0, true);
|

DELIMITER ;
CALL insert_users();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_categories()
INSERT IGNORE INTO categories(id, name, created_at, modified_at)
VALUES (1, 'Motoryzacja', NOW(), NOW()),
       (2, 'Technologia', NOW(), NOW()),
       (3, 'Usługi', NOW(), NOW()),
       (4, 'Fotografia', NOW(), NOW()),
       (5, 'Restauracje', NOW(), NOW()),
       (6, 'Uroda', NOW(), NOW());
|

DELIMITER ;
CALL insert_categories();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_companies()
BEGIN
    INSERT IGNORE INTO companies(id, name, NIP, REGON, description, registerer_id, category_id, created_at, modified_at,
                                 has_branch, address_id)
    VALUES (1, 'Mechanikex', '1234567890', '12345678901234', 'Najlepszy w mieście mechanik.', 2, 1, NOW(), NOW(), TRUE,
            1),
           (2, 'Urodex', '2234567890', '22345678901234', 'Najlepszy w mieście zakład fryzjerski.', 3, 6, NOW(), NOW(),
            TRUE, 3);
END |
DELIMITER ;

CALL insert_companies();

DELIMITER  |
CREATE OR REPLACE PROCEDURE insert_branches()
INSERT IGNORE INTO branches(branch_id, registerer_id, company_id, name, created_at, modified_at, x_geo_coordinate,
                            y_geo_coordinate, address_id)
VALUES (1, 2, 1, 'Mechanikex', NOW(), NOW(), 51.1079, 17.0385, 1),
       (2, 2, 1, 'Mechanikex', NOW(), NOW(), 51.1071, 17.0383, 2),
       (3, 3, 2, 'Urodex', NOW(), NOW(), 51.2167, 22.734, 3),
       (4, 3, 2, 'Urodex 2', NOW(), NOW(), 51.2367, 22.534, 4);

|

DELIMITER ;

CALL insert_branches();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_newsletter_subscriptions()
INSERT IGNORE INTO newsletter_subscriptions(id, company_id, id_email, verified, created_at, modified_at)
VALUES (1, 1, 7, FALSE, NOW(), NOW()),
       (2, 1, 8, FALSE, NOW(), NOW()),
       (3, 2, 1, FALSE, NOW(), NOW());
|

DELIMITER ;
CALL insert_newsletter_subscriptions();


DELIMITER |

CREATE OR REPLACE PROCEDURE insert_comments()
INSERT IGNORE INTO comments (comment, branch_id, user_id)
VALUES ('Very good', 4, 1),
       ('TEST', 5, 1);

DELIMITER ;
CALL insert_comments();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_favourite_branches()
INSERT IGNORE INTO favourite_branches (branch_id, user_id)
VALUES (4, 1),
       (3, 2);

DELIMITER ;
call insert_favourite_branches();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_promotion_types()
INSERT IGNORE INTO promotion_item_types (promotion_item_type_id, type)
VALUES (1, 'Product'),
       (2, 'Information');

DELIMITER ;
CALL insert_promotion_types();

DELIMITER |
CREATE OR REPLACE PROCEDURE insert_promotion_items()
INSERT IGNORE INTO promotion_items (description, name, was_sent, promoting_company_id, promotion_type_id)
VALUES ('helo', 'cos tam', 0, 2, 1),
       ('TEST', 'TEST', 0, 2, 2);

DELIMITER ;
CALL insert_promotion_items();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_social_platforms()
INSERT IGNORE INTO social_platforms (platform_id, social_media_platform)
VALUES (1, 'facebook');

DELIMITER ;
CALL insert_social_platforms();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_social_profiles()
INSERT IGNORE INTO social_profiles (company_id, platform_id, social_profile_url)
VALUES (2, 1, 'https://facebook.com/example123.example123');

DELIMITER ;
CALL insert_social_profiles();



