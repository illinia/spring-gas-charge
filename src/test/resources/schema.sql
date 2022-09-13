drop table if exists member;
create table member (
       member_id bigint not null auto_increment,
        address1 varchar(255),
        address2 varchar(255),
        zipcode varchar(255),
        name varchar(255),
        primary key (member_id)
    );