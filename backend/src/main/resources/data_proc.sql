use inzynierka;

DELIMITER |
CREATE OR REPLACE PROCEDURE insert_voivoideships()
INSERT IGNORE INTO voivodeships(voivodeships.voivodeship_id, voivodeships.name)
VALUES (1, 'dolnośląskie'),
       (2, 'kujawsko-pomorskie'),
       (3, 'lubelskie'),
       (4, 'lubuskie'),
       (5, 'łódzkie'),
       (6, 'małopolskie'),
       (7, 'mazowieckie'),
       (8, 'opolskie'),
       (9, 'podkarpackie'),
       (10, 'podlaskie'),
       (11, 'pomorskie'),
       (12, 'śląskie'),
       (13, 'świętokrzyskie'),
       (14, 'warmińsko-mazurskie'),
       (15, 'wielkopolskie'),
       (16, 'zachodniopomorskie');
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
INSERT IGNORE INTO addresses(id, apartment_no, building_no, city, created_at, modified_at, street, voivodeship_id)
VALUES (1, '5', '43', 'Kraków', NOW(), NOW(), 'Swojska', 1),
       (2, '2', '23', 'Lublin', NOW(), NOW(), 'Wasza', 2),
       (3, '1', '23', 'Szczebrzeszyn', NOW(), NOW(), 'Koziołka Matołka', 3),
       (4, '22', '4', 'Kraków', NOW(), NOW(), 'Makówkowa', 4),
       (5, '7', '2', 'Warszawa', NOW(), NOW(), 'Ichniejsza', 5),
       (6, '3', '4', 'Poznań', NOW(), NOW(), 'Królewska', 3);
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
INSERT IGNORE INTO users(user_id, user_name, password_hash, modified_at, created_at, id_natural_person,
                         id_email_address,
                         account_type, is_enabled)
VALUES (1, 'biedronka', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), 3, 1, 0, true),
       (2, 'kurczak5', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 2, 0, true),
       (3, 'magicznyKrzystof', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 3, 0,
        false),
       (4, 'Gacek', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 4, 0, false),
       (5, 'maciejowicz', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), NULL, 5, 0,
        false),
       (6, 'jezyk', '$2a$10$lb/zKrT4Pey1JCYdGbWKn.Nn61spi./CT/rSdoSOKO/ChBoaLHhtu', NOW(), NOW(), 5, 6, 0, true);
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
       (6, 'Uroda', NOW(), NOW()),
       (7, 'Ogrodnictwo', NOW(), NOW());
|

DELIMITER ;
CALL insert_categories();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_companies()
BEGIN
    INSERT IGNORE INTO companies(id, nip, regon, company_website, created_at, description, has_branch, logo_path,
                                 modified_at, name, address_id, category_id, registerer_id, has_logo_added,
                                 companyUUID)
    VALUES (1, '1234567890', '12345678901234', null, '2019-11-20 22:58:45', 'Najlepszy w mieście mechanik.', true,
            'http://localhost:8090/static/company/c2e95305-cf61-447a-a92c-7252be6af81d/16e5b790-5cf9-4883-80b4-4806910e711f',
            '2019-11-20 22:58:45', 'Mechanikex', 1, 1, 2, 0, 'ecae2442-0be0-11ea-8c2e-707781563efb'),
           (2, '2234567890', '22345678901234', null, '2019-11-20 22:58:45', 'Najlepszy w mieście zakład fryzjerski.',
            true,
            'http://localhost:8090/static/company/c2e95305-cf61-447a-a92c-7252be6af81d/16e5b790-5cf9-4883-80b4-4806910e700e',
            '2019-11-20 22:58:45', 'Urodex', 3, 6, 3, 0, 'ecae2875-0be0-11ea-8c2e-707781563efb'),
           (3, '3243423243', '223423', null, '2019-11-20 23:00:42', '<p>Hello</p>', false,
            'http://localhost:8090/static/company/c2e95305-cf61-447a-a92c-7252be6af81d/16e5b790-5cf9-4883-80b4-4806910e700f',
            '2019-11-20 23:00:43', 'Hello', 6, 7, 3, 1, 'c2e95305-cf61-447a-a92c-7252be6af81d');
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
       (4, 3, 2, 'Urodex 2', NOW(), NOW(), 51.2367, 22.534, 4, UUID()),
       (5, 3, 2, 'Salon piękności', NOW(), NOW(), 51.2457, 22.434, 5, UUID());
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
INSERT IGNORE INTO promotion_items (promotion_item_id, created_at, non_html_content, html_content, modified_at,
                                    name, email_title, planned_sending_at, promoting_company_id,
                                    promotion_type_id, photos_number, promotion_item_uuid, sending_strategy,
                                    adding_completed)
VALUES (1, '2019-11-22 22:15:10', 'helo', null, '2019-11-22 22:15:10', 'cos tam', null, null, 2, 1, 0,
        '2a94262c-0d6d-11ea-81fa-707781563efb', 'at_will', true),
       (2, '2019-11-22 22:15:10', 'TEST1', null, '2019-11-22 22:15:10', 'TEST1', null, null, 2, 2, 0,
        '2a942941-0d6d-11ea-81fa-707781563efb', 'at_will', true),
       (3, '2019-11-22 22:24:30', null,
        'PCFkb2N0eXBlIGh0bWw+CjxodG1sIGxhbmc9ImVuIiBjbGFzcz0ibm8tanMiPgo8aGVhZD4KICAgIDxtZXRhIGNoYXJzZXQ9InV0Zi04Ij4KICAgIDxtZXRhIGh0dHAtZXF1aXY9IngtdWEtY29tcGF0aWJsZSIgY29udGVudD0iaWU9ZWRnZSI+CiAgICA8bWV0YSBuYW1lPSJ2aWV3cG9ydCIgY29udGVudD0id2lkdGg9ZGV2aWNlLXdpZHRoLCBpbml0aWFsLXNjYWxlPTEuMCI+CiAgICA8bGluayByZWw9ImNhbm9uaWNhbCIgaHJlZj0iaHR0cHM6Ly9odG1sNS10ZW1wbGF0ZXMuY29tLyIgLz4KICAgIDx0aXRsZT5SZXNwb25zaXZlIEhUTUw1IFBhZ2UgTGF5b3V0IFRlbXBsYXRlPC90aXRsZT4KICAgIDxtZXRhIG5hbWU9ImRlc2NyaXB0aW9uIiBjb250ZW50PSJTaW1wbGUgSFRNTDUgUGFnZSBsYXlvdXQgdGVtcGxhdGUgd2l0aCBoZWFkZXIsIGZvb3Rlciwgc2lkZWJhciBldGMuIj4KICAgIDxsaW5rIHJlbD0ic3R5bGVzaGVldCIgaHJlZj0ic3R5bGUuY3NzIj4KICAgIDxzY3JpcHQgc3JjPSJzY3JpcHQuanMiPjwvc2NyaXB0Pgo8L2hlYWQ+Cgo8Ym9keT4KCTxoZWFkZXI+CgkJPGRpdiBpZD0ibG9nbyI+PGltZyBzcmM9Ii9sb2dvLnBuZyI+SFRNTDUmbmJzcDtMYXlvdXQ8L2Rpdj4KCQk8bmF2PiAgCgkJCTx1bD4KCQkJCTxsaT48YSBocmVmPSIvIj5Ib21lPC9hPgoJCQkJPGxpPjxhIGhyZWY9Imh0dHBzOi8vaHRtbC1jc3MtanMuY29tLyI+SFRNTDwvYT4KCQkJCTxsaT48YSBocmVmPSJodHRwczovL2h0bWwtY3NzLWpzLmNvbS9jc3MvY29kZS8iPkNTUzwvYT4KCQkJCTxsaT48YSBocmVmPSJodHRwczovL2h0bWxjaGVhdHNoZWV0LmNvbS9qcy8iPkpTPC9hPgoJCQk8L3VsPgoJCTwvbmF2PgoJPC9oZWFkZXI+Cgk8c2VjdGlvbj4KCQk8c3Ryb25nPkRlbW9uc3RyYXRpb24gb2YgYSBzaW1wbGUgcGFnZSBsYXlvdXQgdXNpbmcgSFRNTDUgdGFnczogaGVhZGVyLCBuYXYsIHNlY3Rpb24sIG1haW4sIGFydGljbGUsIGFzaWRlLCBmb290ZXIsIGFkZHJlc3MuPC9zdHJvbmc+Cgk8L3NlY3Rpb24+Cgk8c2VjdGlvbiBpZD0icGFnZUNvbnRlbnQiPgoJCTxtYWluIHJvbGU9Im1haW4iPgoJCQk8YXJ0aWNsZT4KCQkJCTxoMj5TdGV0IGZhY2lsaXMgaXVzIHRlPC9oMj4KCQkJCTxwPkxvcmVtIGlwc3VtIGRvbG9yIHNpdCBhbWV0LCBub251bWVzIHZvbHVwdGF0dW0gbWVsIGVhLCBjdSBjYXNlIGNldGVyb3MgY3VtLiBOb3Z1bSBjb21tb2RvIG1hbG9ydW0gdml4IHV0LiBEb2xvcmVzIGNvbnNlcXV1bnR1ciBpbiBpdXMsIHNhbGUgZWxlY3RyYW0gZGlzc2VudGl1bnQgcXVvIHRlLiBDdSBkdW8gb21uZXMgaW52aWR1bnQsIGVvcyBldSBtdWNpdXMgZmFiZWxsYXMuIFN0ZXQgZmFjaWxpcyBpdXMgdGUsIHF1YW5kbyB2b2x1cHRhdGlidXMgZW9zIGluLiBBZCB2aXggbXVuZGkgYWx0ZXJ1bSwgaW50ZWdyZSB1cmJhbml0YXMgaW50ZWxsZWdhbSB2aXggaW4uPC9wPgoJCQk8L2FydGljbGU+CgkJCTxhcnRpY2xlPgoJCQkJPGgyPklsbHVkIG1vbGxpcyBtb2RlcmF0aXVzPC9oMj4KCQkJCTxwPkV1bSBmYWNldGUgaW50ZWxsZWdhdCBlaSwgdXQgbWF6aW0gbWVsaXVzIHVzdS4gSGFzIGVsaXQgc2ltdWwgcHJpbWlzIG5lLCByZWdpb25lIG1pbmltdW0gaWQgY3VtLiBTZWEgZGVsZW5pdGkgZGlzc2VudGlldCBlYS4gSWxsdWQgbW9sbGlzIG1vZGVyYXRpdXMgdXQgcGVyLCBhdCBxdWkgdWJpcXVlIHBvcHVsby4gRXVtIGFkIGNpYm8gbGVnaW11cywgdmltIGVpIHF1aWRhbSBmYXN0aWRpaS48L3A+CgkJCTwvYXJ0aWNsZT4KCQkJPGFydGljbGU+CgkJCQk8aDI+RXggaWdub3RhIGVwaWN1cmVpIHF1bzwvaDI+CgkJCQk8cD5RdW8gZGViZXQgdml2ZW5kbyBleC4gUXVpIHV0IGFkbW9kdW0gc2Vuc2VyaXQgcGFydGllbmRvLiBJZCBhZGlwaXNjaW5nIGRpc3B1dGFuZG8gZWFtLCBzZWEgaWQgbWFnbmEgcGVydGluYXggY29uY2x1ZGF0dXJxdWUuIEV4IGlnbm90YSBlcGljdXJlaSBxdW8sIGhpcyBleCBkb2N0dXMgZGVsZW5pdCBmYWJlbGxhcywgZXJhdCB0aW1lYW0gY290aWRpZXF1ZSBzaXQgaW4uIFZlbCBldSBzb2xlYXQgdm9sdXB0YXRpYnVzLCBjdW0gY3UgZXhlcmNpIG1lZGlvY3JpdGF0ZW0uIE1hbGlzIGxlZ2VyZSBhdCBwZXIsIGhhcyBicnV0ZSBwdXRhbnQgYW5pbWFsIGV0LCBpbiBjb25zdWwgdXRhbXVyIHVzdS48L3A+CgkJCTwvYXJ0aWNsZT4KCQkJPGFydGljbGU+CgkJCQk8aDI+SGlzIGF0IGF1dGVtIGluYW5pIHZvbHV0cGF0PC9oMj4KCQkJCTxwPlRlIGhhcyBhbWV0IG1vZG8gcGVyZmVjdG8sIHRlIGV1bSBtdWNpdXMgY29uY2x1c2lvbmVtcXVlLCBtZWwgdGUgZXJhdCBkZXRlcnJ1aXNzZXQuIER1byBjZXRlcm9zIHBoYWVkcnVtIGlkLCBvcm5hdHVzIHBvc3R1bGFudCBpbiBzZWEuIEhpcyBhdCBhdXRlbSBpbmFuaSB2b2x1dHBhdC4gVG9sbGl0IHBvc3NpdCBpbiBwcmksIHBsYXRvbmVtIHBlcnNlY3V0aSBhZCB2aXgsIHZlbCBuaXNsIGFsYnVjaXVzIGdsb3JpYXR1ciBuby48L3A+CgkJCTwvYXJ0aWNsZT4KCQk8L21haW4+CgkJPGFzaWRlPgoJCQk8ZGl2PlNpZGViYXIgMTwvZGl2PgoJCQk8ZGl2PlNpZGViYXIgMjwvZGl2PgoJCQk8ZGl2PlNpZGViYXIgMzwvZGl2PgoJCTwvYXNpZGU+Cgk8L3NlY3Rpb24+Cgk8Zm9vdGVyPgoJCTxwPiZjb3B5OyBZb3UgY2FuIGNvcHksIGVkaXQgYW5kIHB1Ymxpc2ggdGhpcyB0ZW1wbGF0ZSBidXQgcGxlYXNlIGxlYXZlIGEgbGluayB0byBvdXIgd2Vic2l0ZSB8IDxhIGhyZWY9Imh0dHBzOi8vaHRtbDUtdGVtcGxhdGVzLmNvbS8iIHRhcmdldD0iX2JsYW5rIiByZWw9Im5vZm9sbG93Ij5IVE1MNSBUZW1wbGF0ZXM8L2E+PC9wPgoJCTxhZGRyZXNzPgoJCQlDb250YWN0OiA8YSBocmVmPSJtYWlsdG86bWVAZXhhbXBsZS5jb20iPk1haWwgbWU8L2E+CgkJPC9hZGRyZXNzPgoJPC9mb290ZXI+CgoKPC9ib2R5PgoKPC9odG1sPg==',
        '2019-11-22 22:24:30', 'jakaś tam nazwa1', 'hello123', null, 2, 2, 0,
        '76fa7cb2-dd18-4a65-b2ed-95e2458a44d8', 'sent', true),
       (4, '2019-11-22 22:31:35', null,
        'PCFkb2N0eXBlIGh0bWw+CjxodG1sIGxhbmc9ImVuIiBjbGFzcz0ibm8tanMiPgo8aGVhZD4KICAgIDxtZXRhIGNoYXJzZXQ9InV0Zi04Ij4KICAgIDxtZXRhIGh0dHAtZXF1aXY9IngtdWEtY29tcGF0aWJsZSIgY29udGVudD0iaWU9ZWRnZSI+CiAgICA8bWV0YSBuYW1lPSJ2aWV3cG9ydCIgY29udGVudD0id2lkdGg9ZGV2aWNlLXdpZHRoLCBpbml0aWFsLXNjYWxlPTEuMCI+CiAgICA8bGluayByZWw9ImNhbm9uaWNhbCIgaHJlZj0iaHR0cHM6Ly9odG1sNS10ZW1wbGF0ZXMuY29tLyIgLz4KICAgIDx0aXRsZT5SZXNwb25zaXZlIEhUTUw1IFBhZ2UgTGF5b3V0IFRlbXBsYXRlPC90aXRsZT4KICAgIDxtZXRhIG5hbWU9ImRlc2NyaXB0aW9uIiBjb250ZW50PSJTaW1wbGUgSFRNTDUgUGFnZSBsYXlvdXQgdGVtcGxhdGUgd2l0aCBoZWFkZXIsIGZvb3Rlciwgc2lkZWJhciBldGMuIj4KICAgIDxsaW5rIHJlbD0ic3R5bGVzaGVldCIgaHJlZj0ic3R5bGUuY3NzIj4KICAgIDxzY3JpcHQgc3JjPSJzY3JpcHQuanMiPjwvc2NyaXB0Pgo8L2hlYWQ+Cgo8Ym9keT4KCTxoZWFkZXI+CgkJPGRpdiBpZD0ibG9nbyI+PGltZyBzcmM9Ii9sb2dvLnBuZyI+SFRNTDUmbmJzcDtMYXlvdXQ8L2Rpdj4KCQk8bmF2PiAgCgkJCTx1bD4KCQkJCTxsaT48YSBocmVmPSIvIj5Ib21lPC9hPgoJCQkJPGxpPjxhIGhyZWY9Imh0dHBzOi8vaHRtbC1jc3MtanMuY29tLyI+SFRNTDwvYT4KCQkJCTxsaT48YSBocmVmPSJodHRwczovL2h0bWwtY3NzLWpzLmNvbS9jc3MvY29kZS8iPkNTUzwvYT4KCQkJCTxsaT48YSBocmVmPSJodHRwczovL2h0bWxjaGVhdHNoZWV0LmNvbS9qcy8iPkpTPC9hPgoJCQk8L3VsPgoJCTwvbmF2PgoJPC9oZWFkZXI+Cgk8c2VjdGlvbj4KCQk8c3Ryb25nPkRlbW9uc3RyYXRpb24gb2YgYSBzaW1wbGUgcGFnZSBsYXlvdXQgdXNpbmcgSFRNTDUgdGFnczogaGVhZGVyLCBuYXYsIHNlY3Rpb24sIG1haW4sIGFydGljbGUsIGFzaWRlLCBmb290ZXIsIGFkZHJlc3MuPC9zdHJvbmc+Cgk8L3NlY3Rpb24+Cgk8c2VjdGlvbiBpZD0icGFnZUNvbnRlbnQiPgoJCTxtYWluIHJvbGU9Im1haW4iPgoJCQk8YXJ0aWNsZT4KCQkJCTxoMj5TdGV0IGZhY2lsaXMgaXVzIHRlPC9oMj4KCQkJCTxwPkxvcmVtIGlwc3VtIGRvbG9yIHNpdCBhbWV0LCBub251bWVzIHZvbHVwdGF0dW0gbWVsIGVhLCBjdSBjYXNlIGNldGVyb3MgY3VtLiBOb3Z1bSBjb21tb2RvIG1hbG9ydW0gdml4IHV0LiBEb2xvcmVzIGNvbnNlcXV1bnR1ciBpbiBpdXMsIHNhbGUgZWxlY3RyYW0gZGlzc2VudGl1bnQgcXVvIHRlLiBDdSBkdW8gb21uZXMgaW52aWR1bnQsIGVvcyBldSBtdWNpdXMgZmFiZWxsYXMuIFN0ZXQgZmFjaWxpcyBpdXMgdGUsIHF1YW5kbyB2b2x1cHRhdGlidXMgZW9zIGluLiBBZCB2aXggbXVuZGkgYWx0ZXJ1bSwgaW50ZWdyZSB1cmJhbml0YXMgaW50ZWxsZWdhbSB2aXggaW4uPC9wPgoJCQk8L2FydGljbGU+CgkJCTxhcnRpY2xlPgoJCQkJPGgyPklsbHVkIG1vbGxpcyBtb2RlcmF0aXVzPC9oMj4KCQkJCTxwPkV1bSBmYWNldGUgaW50ZWxsZWdhdCBlaSwgdXQgbWF6aW0gbWVsaXVzIHVzdS4gSGFzIGVsaXQgc2ltdWwgcHJpbWlzIG5lLCByZWdpb25lIG1pbmltdW0gaWQgY3VtLiBTZWEgZGVsZW5pdGkgZGlzc2VudGlldCBlYS4gSWxsdWQgbW9sbGlzIG1vZGVyYXRpdXMgdXQgcGVyLCBhdCBxdWkgdWJpcXVlIHBvcHVsby4gRXVtIGFkIGNpYm8gbGVnaW11cywgdmltIGVpIHF1aWRhbSBmYXN0aWRpaS48L3A+CgkJCTwvYXJ0aWNsZT4KCQkJPGFydGljbGU+CgkJCQk8aDI+RXggaWdub3RhIGVwaWN1cmVpIHF1bzwvaDI+CgkJCQk8cD5RdW8gZGViZXQgdml2ZW5kbyBleC4gUXVpIHV0IGFkbW9kdW0gc2Vuc2VyaXQgcGFydGllbmRvLiBJZCBhZGlwaXNjaW5nIGRpc3B1dGFuZG8gZWFtLCBzZWEgaWQgbWFnbmEgcGVydGluYXggY29uY2x1ZGF0dXJxdWUuIEV4IGlnbm90YSBlcGljdXJlaSBxdW8sIGhpcyBleCBkb2N0dXMgZGVsZW5pdCBmYWJlbGxhcywgZXJhdCB0aW1lYW0gY290aWRpZXF1ZSBzaXQgaW4uIFZlbCBldSBzb2xlYXQgdm9sdXB0YXRpYnVzLCBjdW0gY3UgZXhlcmNpIG1lZGlvY3JpdGF0ZW0uIE1hbGlzIGxlZ2VyZSBhdCBwZXIsIGhhcyBicnV0ZSBwdXRhbnQgYW5pbWFsIGV0LCBpbiBjb25zdWwgdXRhbXVyIHVzdS48L3A+CgkJCTwvYXJ0aWNsZT4KCQkJPGFydGljbGU+CgkJCQk8aDI+SGlzIGF0IGF1dGVtIGluYW5pIHZvbHV0cGF0PC9oMj4KCQkJCTxwPlRlIGhhcyBhbWV0IG1vZG8gcGVyZmVjdG8sIHRlIGV1bSBtdWNpdXMgY29uY2x1c2lvbmVtcXVlLCBtZWwgdGUgZXJhdCBkZXRlcnJ1aXNzZXQuIER1byBjZXRlcm9zIHBoYWVkcnVtIGlkLCBvcm5hdHVzIHBvc3R1bGFudCBpbiBzZWEuIEhpcyBhdCBhdXRlbSBpbmFuaSB2b2x1dHBhdC4gVG9sbGl0IHBvc3NpdCBpbiBwcmksIHBsYXRvbmVtIHBlcnNlY3V0aSBhZCB2aXgsIHZlbCBuaXNsIGFsYnVjaXVzIGdsb3JpYXR1ciBuby48L3A+CgkJCTwvYXJ0aWNsZT4KCQk8L21haW4+CgkJPGFzaWRlPgoJCQk8ZGl2PlNpZGViYXIgMTwvZGl2PgoJCQk8ZGl2PlNpZGViYXIgMjwvZGl2PgoJCQk8ZGl2PlNpZGViYXIgMzwvZGl2PgoJCTwvYXNpZGU+Cgk8L3NlY3Rpb24+Cgk8Zm9vdGVyPgoJCTxwPiZjb3B5OyBZb3UgY2FuIGNvcHksIGVkaXQgYW5kIHB1Ymxpc2ggdGhpcyB0ZW1wbGF0ZSBidXQgcGxlYXNlIGxlYXZlIGEgbGluayB0byBvdXIgd2Vic2l0ZSB8IDxhIGhyZWY9Imh0dHBzOi8vaHRtbDUtdGVtcGxhdGVzLmNvbS8iIHRhcmdldD0iX2JsYW5rIiByZWw9Im5vZm9sbG93Ij5IVE1MNSBUZW1wbGF0ZXM8L2E+PC9wPgoJCTxhZGRyZXNzPgoJCQlDb250YWN0OiA8YSBocmVmPSJtYWlsdG86bWVAZXhhbXBsZS5jb20iPk1haWwgbWU8L2E+CgkJPC9hZGRyZXNzPgoJPC9mb290ZXI+CgoKPC9ib2R5PgoKPC9odG1sPg==',
        '2019-11-22 22:31:35', 'jakaś tam nazwa12', 'hello1234', null, 2, 2, 0,
        '4f3a111e-05a8-440c-ba5d-89b93ab2a3e7', 'sent', true);

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

CREATE OR REPLACE PROCEDURE insert_social_profiles()
INSERT IGNORE INTO inzynierka.social_profiles (company_id, platform_id, social_profile_url, created_at, modified_at)
VALUES (3, 1, 'https://facebook.com/Moja-strona-106000967536283', '2019-11-20 23:14:19', '2019-11-20 23:14:19');

DELIMITER ;
CALL insert_social_profiles();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_fb_social_profiles()
INSERT IGNORE INTO inzynierka.fb_social_profiles (facebook_social_profile_id, created_at, modified_at, company_id,
                                                  platform_id,
                                                  user_id, user_name, page_id)
VALUES (1, '2019-11-20 23:14:19', '2019-11-20 23:14:19', 3, 1, 116363453141375, '', 106000967536283);

DELIMITER ;
CALL insert_fb_social_profiles();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_fb_tokens()
INSERT IGNORE INTO inzynierka.fb_tokens (facebook_token_id, created_at, modified_at, type, expires_at,
                                         data_access_expires_at,
                                         issued_at, is_valid, facebook_profile_id, access_token)
VALUES (1, '2019-11-20 23:14:20', '2019-11-20 23:14:20', 'USER', 0, 1582063339, 1574186627, 1, 1,
        'EAAFZC73l3LmcBABPz1lRMKEwGQrktL3fOdnL4fIoVZC9VTc8SxUjOpo8M768nDsZCTnIa9qZBxE0VzZC8gaohCP2YkYOS9wm8vvoJWI50zsRK4I2jWqZCK5C8qmnfLC6wlcjIykyaZCjOtFrFEccUMWQFZAkSXkLDeNEezuZBOcul2TOynEyIxOwI'),
       (2, '2019-11-20 23:14:20', '2019-11-20 23:14:20', 'PAGE', 0, 1582063339, 1574186627, 1, 1,
        'EAAFZC73l3LmcBAOEBNXD6ZCLf3VEwhRxjEBkdP0WgsZAt403EhRKfbeE9wytq4PYrJspHioofMieACK37CZAcFhdWIPrTRfaN7hhYXY1HVZAyCUo3CxfIAh3ZAVZAJaYbZAwuigwU2koK2Tbl5q5IjVdCdiv71KXMpo84TcqHzPZBVnTzaZA0XOWZCbqR59fNLMAtoZD');

DELIMITER ;
CALL insert_fb_tokens();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_fb_token_scopes()
INSERT IGNORE INTO inzynierka.fb_token_scopes (facebook_token_scope_id, created_at, token_scope_type, facebook_token_id,
                                               scope)
VALUES (1, '2019-11-20 23:14:20', 'NORMAL', 1, 'manage_pages'),
       (2, '2019-11-20 23:14:20', 'NORMAL', 1, 'pages_show_list'),
       (3, '2019-11-20 23:14:20', 'NORMAL', 1, 'publish_pages'),
       (4, '2019-11-20 23:14:20', 'NORMAL', 1, 'public_profile');

DELIMITER ;
CALL insert_fb_token_scopes();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_promotion_item_destinations()
INSERT IGNORE INTO inzynierka.promotion_item_destinations (promotion_item_destination_id, destination, promotion_item_id)
VALUES (1, 'NEWSLETTER', 3),
       (2, 'NEWSLETTER', 4);

DELIMITER ;
CALL insert_promotion_item_destinations();

DELIMITER |

CREATE OR REPLACE PROCEDURE insert_destination_arrivals()
INSERT IGNORE INTO inzynierka.destination_arrivals (destination_arrival_id, created_at,
                                                    promotion_item_destination_id, status, detail)
VALUES (1, '2019-11-22 22:24:32', 1, 'SENT', null),
       (2, '2019-11-22 22:31:35', 2, 'SENT', null);

DELIMITER ;
CALL insert_destination_arrivals();

DELIMITER |
