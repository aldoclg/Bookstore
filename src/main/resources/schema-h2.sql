



drop table if exists books cascade
drop table if exists types cascade 
drop table if exists Customers cascade
drop sequence if exists books_seq
drop sequence if exists types_seq
drop sequence if exists Customers_seq
create sequence books_seq start with 1 increment by 50
create sequence types_seq start with 1 increment by 50
create sequence Customers_seq start with 1 increment by 50

create table books (price float(53), type_id integer not null, amount bigint, book_id bigint not null, name varchar(255), primary key (book_id))
create table types (type_id integer not null, name varchar(255), primary key (type_id))
create table Customers (loyalty_points bigint, Customer_id bigint not null, primary key (Customer_id))
alter table if exists books add constraint FK5efosbu78pe11me1akmnpr05a foreign key (type_id) references types