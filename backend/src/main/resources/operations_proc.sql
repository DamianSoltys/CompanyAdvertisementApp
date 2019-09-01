use inzynierka;


delimiter |
create or replace procedure clear_data()
begin
    SET FOREIGN_KEY_CHECKS = 0;
    truncate inzynierka.comments;
    truncate inzynierka.favourite_branches;
    truncate inzynierka.newsletter_subscriptions;
    truncate inzynierka.promotion_items;
    truncate inzynierka.ratings;
    truncate inzynierka.promotion_item_types;
    truncate inzynierka.branches;
    truncate inzynierka.social_profiles;
    truncate inzynierka.companies;
    truncate inzynierka.categories;
    truncate inzynierka.social_platforms;
    truncate inzynierka.users;
    truncate inzynierka.email_addresses;
    truncate inzynierka.natural_persons;
    truncate inzynierka.addresses;
    truncate inzynierka.tokens;
    truncate inzynierka.voivodeships;
    SET FOREIGN_KEY_CHECKS = 1;
end;
|

delimiter |
create or replace procedure refresh_data()
begin
    call inzynierka.clear_data();
    call inzynierka.insert_voivoideships();
    call inzynierka.insert_emails();
    call inzynierka.insert_addresses();
    call inzynierka.insert_natural_persons();
    call inzynierka.insert_users();
    call inzynierka.insert_categories();
    call inzynierka.insert_companies();
    call inzynierka.insert_branches();
    call inzynierka.insert_newsletter_subscriptions();
    call inzynierka.insert_comments();
    call inzynierka.insert_favourite_branches();
    call inzynierka.insert_promotion_types();
    call inzynierka.insert_promotion_items();
    call inzynierka.insert_social_platforms();
    call inzynierka.insert_social_profiles();
end;
|
