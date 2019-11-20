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
INSERT IGNORE INTO users(user_id,user_name, password_hash, modified_at, created_at, id_natural_person, id_email_address,
                         account_type, is_enabled)
VALUES (1,'biedronka', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), 3, 1, 0, true),
       (2,'kurczak5', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 2, 0, true),
       (3,'magicznyKrzystof', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 3, 0,
        false),
       (4,'Gacek', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 4, 0, false),
       (5,'maciejowicz', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 5, 0, false),
       (6,'jezyk', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), 5, 6, 0, true);
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
                                 has_branch, address_id, companyUUID)
    VALUES (1, 'Mechanikex', '1234567890', '12345678901234', 'Najlepszy w mieście mechanik.', 2, 1, NOW(), NOW(), TRUE,
            1, UUID()),
           (2, 'Urodex', '2234567890', '22345678901234', 'Najlepszy w mieście zakład fryzjerski.', 3, 6, NOW(), NOW(),
            TRUE, 3, UUID());
END |
DELIMITER ;

CALL insert_companies();

DELIMITER  |
CREATE OR REPLACE PROCEDURE insert_branches()
INSERT IGNORE INTO branches(branch_id, registerer_id, company_id, name, created_at, modified_at, x_geo_coordinate,
                            y_geo_coordinate, address_id, branchUUID)
VALUES (1, 2, 1, 'Mechanikex', NOW(), NOW(), 51.1079, 17.0385, 1, UUID()),
       (2, 2, 1, 'Mechanikex', NOW(), NOW(), 51.1071, 17.0383, 2, UUID()),
       (3, 3, 2, 'Urodex', NOW(), NOW(), 51.2167, 22.734, 3, UUID()),
       (4, 3, 2, 'Urodex 2', NOW(), NOW(), 51.2367, 22.534, 4, UUID());

|
DELIMITER ;

CALL insert_branches();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_newsletter_subscriptions()
INSERT IGNORE INTO newsletter_subscriptions(id, company_id, id_email, verified, created_at, modified_at)
VALUES (1, 1, 7, FALSE, NOW(), NOW()),
       (2, 1, 8, FALSE, NOW(), NOW()),
       (3, 2, 3, TRUE, NOW(), NOW());
|

DELIMITER ;
CALL insert_newsletter_subscriptions();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_comments()
INSERT IGNORE INTO comments (comment, branch_id, user_id, modified_at)
VALUES ('Very good', 4, 1, NOW()),
       ('TEST11', 4, 2, NOW()),
       ('TEST12', 4, 3, NOW()),
       ('TEST13', 4, 4, NOW()),
       ('TEST114', 1, 3, NOW()),
       ('TEST115', 1, 4, NOW()),
       ('TEST116', 4, 1, NOW()),
       ('TEST117', 5, 1, NOW()),
       ('TEST118', 4, 1, NOW()),
       ('TEST119', 5, 1, NOW()),
       ('TEST120', 4, 1, NOW()),
       ('TEST121', 5, 1, NOW()),
       ('TEST121', 4, 1, NOW()),
       ('TEST122', 5, 1, NOW()),
       ('TEST123', 4, 1, NOW()),
       ('TEST124', 5, 1, NOW()),
       ('TEST125', 4, 1, NOW()),
       ('TEST126', 5, 1, NOW()),
       ('Very good1', 4, 1, NOW()),
       ('TEST1', 4, 2, NOW()),
       ('TEST2', 4, 3, NOW()),
       ('TEST3', 4, 4, NOW()),
       ('TEST4', 3, 1, NOW()),
       ('TEST5', 3, 2, NOW()),
       ('TEST6', 3, 3, NOW()),
       ('TEST17', 3, 4, NOW()),
       ('TEST8', 2, 1, NOW()),
       ('TEST9', 2, 2, NOW()),
       ('TEST10', 2, 3, NOW()),
       ('TEST11', 2, 4, NOW()),
       ('TEST12', 1, 1, NOW()),
       ('TEST13', 1, 2, NOW()),
       ('TEST14', 1, 3, NOW()),
       ('TEST15', 1, 4, NOW()),
       ('TEST16', 4, 1, NOW()),
       ('TEST17', 5, 1, NOW()),
       ('TEST18', 4, 1, NOW()),
       ('TEST19', 5, 1, NOW()),
       ('TEST20', 4, 1, NOW()),
       ('TEST21', 5, 1, NOW()),
       ('TEST21', 4, 1, NOW()),
       ('TEST22', 5, 1, NOW()),
       ('TEST23', 4, 1, NOW()),
       ('TEST24', 5, 1, NOW()),
       ('TEST25', 4, 1, NOW()),
       ('TEST26', 5, 1, NOW()),
       ('TEST316', 4, 1, NOW()),
       ('TEST317', 5, 1, NOW()),
       ('TEST318', 4, 1, NOW()),
       ('TEST319', 5, 1, NOW()),
       ('TEST320', 4, 1, NOW()),
       ('TEST321', 5, 1, NOW()),
       ('TEST321', 4, 1, NOW()),
       ('TEST322', 5, 1, NOW()),
       ('TEST323', 4, 1, NOW()),
       ('TEST324', 5, 1, NOW()),
       ('TEST325', 4, 1, NOW()),
       ('TEST326', 5, 1, NOW());

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
       (2, 'Information'),
       (3, 'Promotion');

DELIMITER ;
CALL insert_promotion_types();

DELIMITER |
CREATE OR REPLACE PROCEDURE insert_promotion_items()
INSERT IGNORE INTO promotion_items (non_html_content, name, was_sent, promoting_company_id, promotion_type_id,
                                    modified_at)
VALUES ('helo', 'cos tam', 0, 2, 1, current_timestamp()),
       ('TEST1', 'TEST1', 0, 2, 2, current_timestamp());

DELIMITER ;
CALL insert_promotion_items();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_social_platforms()
INSERT IGNORE INTO social_platforms (platform_id, social_media_platform, created_at, modified_at)
VALUES (1, 'facebook', current_timestamp(), current_timestamp()),
       (2, 'twitter', current_timestamp(), current_timestamp());

DELIMITER ;
CALL insert_social_platforms();

DELIMITER |


DELIMITER ;
CALL insert_social_profiles();



