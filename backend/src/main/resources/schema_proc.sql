use inzynierka;

DELIMITER  |
create or replace procedure create_database()
BEGIN

    drop table if exists comments;
    drop table if exists favourite_branches;
    drop table if exists newsletter_subscriptions;
    drop table if exists promotion_items;
    drop table if exists promotion_item_types;
    drop table if exists ratings;
    drop table if exists branches;
    drop table if exists social_profiles;
    drop table if exists companies;
    drop table if exists categories;
    drop table if exists social_platforms;
    drop table if exists users;
    drop table if exists email_addresses;
    drop table if exists natural_persons;
    drop table if exists addresses;
    drop table if exists tokens;
    drop table if exists voivodeships;

    create or replace table categories
    (
        id          smallint(6) auto_increment
            primary key,
        created_at  timestamp default current_timestamp()   not null on update current_timestamp(),
        modified_at timestamp default '0000-00-00 00:00:00' not null,
        name        varchar(30)                             not null
    );

    create or replace table email_addresses
    (
        email_id   bigint auto_increment
            primary key,
        created_at timestamp default current_timestamp() not null on update current_timestamp(),
        email      varchar(254)                          not null,
        constraint unique_email
            unique (email)
    );

    create or replace table promotion_item_types
    (
        promotion_item_type_id smallint(6) auto_increment
            primary key,
        created_at             timestamp default current_timestamp() not null on update current_timestamp(),
        type                   varchar(30)                           not null
    );

    create or replace table social_platforms
    (
        platform_id           smallint(6) auto_increment
            primary key,
        created_at            timestamp default current_timestamp()   not null on update current_timestamp(),
        modified_at           timestamp default '0000-00-00 00:00:00' not null,
        social_media_platform varchar(255)                            not null
    );

    create or replace table tokens
    (
        token_id   bigint auto_increment
            primary key,
        created_at datetime(6)  null,
        token      varchar(255) not null,
        constraint unique_token
            unique (token)
    );

    create or replace table voivodeships
    (
        voivodeship_id smallint(6) auto_increment
            primary key,
        name           varchar(25) not null,
        constraint unique_voivodeship
            unique (name)
    );

    create or replace table addresses
    (
        id             bigint auto_increment
            primary key,
        apartment_no   varchar(5)                              null,
        building_no    varchar(5)                              not null,
        city           varchar(30)                             not null,
        created_at     timestamp default current_timestamp()   not null on update current_timestamp(),
        modified_at    timestamp default '0000-00-00 00:00:00' not null,
        street         varchar(30)                             null,
        voivodeship_id smallint(6)                             not null,
        constraint voivodeship_FK
            foreign key (voivodeship_id) references voivodeships (voivodeship_id)
    );

    create or replace table natural_persons
    (
        id_natural_person bigint auto_increment
            primary key,
        created_at        timestamp default current_timestamp()   not null on update current_timestamp(),
        first_name        varchar(30)                             not null,
        last_name         varchar(30)                             not null,
        modified_at       timestamp default '0000-00-00 00:00:00' not null,
        phone_no          varchar(13)                             not null,
        second_first_name varchar(30)                             null,
        address_id        bigint                                  not null,
        constraint unique_phone_no
            unique (phone_no),
        constraint address_natural_person_FK
            foreign key (address_id) references addresses (id)
                on delete CASCADE
    );

    create or replace table companies
    (
        id              bigint auto_increment
            primary key,
        nip             varchar(10)                             not null,
        regon           varchar(14)                             not null,
        company_website varchar(255)                            null,
        created_at      timestamp default current_timestamp()   not null on update current_timestamp(),
        description     varchar(10000)                          not null,
        has_branch      bit                                     not null,
        logo_path       varchar(255)                            null,
        modified_at     timestamp default '0000-00-00 00:00:00' not null,
        name            varchar(50)                             not null,
        address_id      bigint                                  not null,
        category_id     smallint(6)                             not null,
        registerer_id   bigint                                  not null,
        has_logo_added  boolean                                 not null default false,
        companyUUID     varchar(36)                             not null,
        constraint unique_regon
            unique (regon),
        constraint unique_nip
            unique (nip),
        constraint address_company_FK
            foreign key (address_id) references addresses (id)
                on delete cascade,
        constraint company_category_FK
            foreign key (category_id) references categories (id),
        constraint company_registerer_FK
            foreign key (registerer_id) references natural_persons (id_natural_person)
                on delete cascade
    );

    create or replace table branches
    (
        branch_id        bigint auto_increment
            primary key,
        created_at       timestamp default current_timestamp()   not null on update current_timestamp(),
        x_geo_coordinate float                                   null,
        y_geo_coordinate float                                   null,
        modified_at      timestamp default '0000-00-00 00:00:00' not null,
        name             varchar(50)                             not null,
        photo_path       varchar(255)                            null,
        address_id       bigint                                  not null,
        company_id       bigint                                  not null,
        registerer_id    bigint                                  not null,
        has_logo_added   boolean                                 not null default false,
        branchUUID       varchar(36)                             not null,
        constraint address_branch_FK
            foreign key (address_id) references addresses (id)
                on delete cascade,
        constraint branch_registerer_FK
            foreign key (registerer_id) references natural_persons (id_natural_person)
                on delete cascade,
        constraint company_branch_FK
            foreign key (company_id) references companies (id)
                on delete cascade
    );

    create or replace table newsletter_subscriptions
    (
        id                    bigint auto_increment
            primary key,
        created_at            timestamp default current_timestamp()   not null on update current_timestamp(),
        modified_at           timestamp default '0000-00-00 00:00:00' not null,
        verified              bit                                     not null,
        company_id            bigint                                  not null,
        id_email              bigint                                  not null,
        id_unsubscribe_token  bigint                                  null,
        id_verification_token bigint                                  null,
        constraint unique_unsubscribe_token
            unique (id_unsubscribe_token),
        constraint unique_verification_token
            unique (id_verification_token),
        constraint newsletter_company_FK
            foreign key (company_id) references companies (id)
                on delete cascade,
        constraint newsletter_email_FK
            foreign key (id_email) references email_addresses (email_id)
                on delete cascade,
        constraint unsubscribe_newsletter_token_FK
            foreign key (id_unsubscribe_token) references tokens (token_id),
        constraint verify_newsletter_token_FK
            foreign key (id_verification_token) references tokens (token_id)
    );

    create or replace table promotion_items
    (
        promotion_item_id    bigint auto_increment
            primary key,
        created_at           timestamp default current_timestamp()   not null on update current_timestamp(),
        non_html_content     MEDIUMTEXT                              null,
        html_content         MEDIUMTEXT                              null,
        modified_at          timestamp default '0000-00-00 00:00:00' not null,
        name                 varchar(50)                             null,
        was_sent             bit                                     null,
        valid_from           timestamp                               null,
        valid_to             timestamp default '0000-00-00 00:00:00' not null,
        promoting_company_id bigint                                  not null,
        promotion_type_id    smallint(6)                             not null,
        photos_number        smallint(2)                             not null,
        constraint promoting_company_FK
            foreign key (promoting_company_id) references companies (id)
                on delete cascade,
        constraint promotion_type_FK
            foreign key (promotion_type_id) references promotion_item_types (promotion_item_type_id)
    );

    create or replace table promotion_item_destinations
    (
        promotion_item_destination_id BIGINT AUTO_INCREMENT PRIMARY KEY,
        destination                   varchar(30) NOT NULL,
        promotion_item_id             bigint      NOT NULL,
        constraint promotion_item_destination_FK
            foreign key (promotion_item_id) references promotion_items (promotion_item_id)
                on delete cascade
    );

    create or replace table social_profiles
    (
        company_id         bigint                                  not null,
        platform_id        smallint(6)                             not null,
        social_profile_url varchar(255)                            not null,
        created_at         timestamp default current_timestamp()   not null on update current_timestamp(),
        modified_at        timestamp default '0000-00-00 00:00:00' not null,
        primary key (company_id, platform_id),
        constraint company_soc_prof_FK
            foreign key (company_id) references companies (id)
                on delete cascade,
        constraint platform_FK
            foreign key (platform_id) references social_platforms (platform_id)
    );

    create or replace table users
    (
        user_id               bigint auto_increment
            primary key,
        account_type          smallint(6)  not null,
        created_at            datetime(6)  not null,
        is_enabled            bit          null,
        modified_at           datetime(6)  not null,
        user_name             varchar(30)  not null,
        password_hash         varchar(255) not null,
        id_email_address      bigint       not null,
        id_natural_person     bigint       null,
        verification_token_id bigint       null,
        constraint unique_verification_token
            unique (verification_token_id),
        constraint unique_email
            unique (id_email_address),
        constraint unique_username
            unique (user_name),
        constraint email_FK
            foreign key (id_email_address) references email_addresses (email_id),
        constraint natural_person_FK
            foreign key (id_natural_person) references natural_persons (id_natural_person)
                ON DELETE SET NULL,
        constraint verify_user_token_FK
            foreign key (verification_token_id) references tokens (token_id)
    );

    create or replace table comments
    (
        id          bigint auto_increment
            primary key,
        comment     varchar(500)                            not null,
        created_at  timestamp default current_timestamp()   not null on update current_timestamp(),
        modified_at timestamp default '0000-00-00 00:00:00' not null,
        branch_id   bigint                                  not null,
        user_id     bigint                                  not null,
        constraint commentend_branch_FK
            foreign key (branch_id) references branches (branch_id)
                on delete CASCADE,
        constraint commenting_user_FK
            foreign key (user_id) references users (user_id)
                on delete CASCADE
    );

    create or replace table favourite_branches
    (
        branch_id   bigint                                  not null,
        user_id     bigint                                  not null,
        created_at  timestamp default current_timestamp()   not null on update current_timestamp(),
        modified_at timestamp default '0000-00-00 00:00:00' not null,
        primary key (branch_id, user_id),
        constraint favourite_branches_branch_FK
            foreign key (branch_id) references branches (branch_id)
                on delete cascade,
        constraint favourite_branches_user_FK
            foreign key (user_id) references users (user_id)
                on delete cascade
    );

    create or replace table ratings
    (
        rating_id   bigint auto_increment
            primary key,
        created_at  timestamp default current_timestamp()   not null on update current_timestamp(),
        modified_at timestamp default '0000-00-00 00:00:00' not null,
        rating      int                                     not null,
        branch_id   bigint                                  not null,
        user_id     bigint                                  not null,
        constraint rated_FK
            foreign key (branch_id) references branches (branch_id)
                on delete cascade,
        constraint rating_user_FK
            foreign key (user_id) references users (user_id)
                on delete cascade,
        constraint rating
            check (`rating` <= 5 and `rating` >= 1)
    );

END;
|

DELIMITER ;
CALL create_database();

