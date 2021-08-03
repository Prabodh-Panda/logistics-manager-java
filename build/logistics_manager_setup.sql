create database if not exists logistics_manager;

use logistics_manager;

create table if not exists companies (
CMPID INT NOT NULL auto_increment,
Name varchar(50),
Location varchar(50),
primary key(CMPID)
);

create table if not exists branches (
BRNCHID INT NOT NULL auto_increment,
Name varchar(50),
Location varchar(50),
CMPID INT NOT NULL,
primary key(BRNCHID),
constraint fk_cmpid foreign key (CMPID) references companies(CMPID)
);

create table if not exists users (
username varchar(50) NOT NULL,
password varchar(50) NOT NULL,
CMPID INT NOT NULL,
primary key(username),
constraint fk_cmpid_users foreign key(CMPID) references companies(CMPID)
);

create table if not exists vehicles (
Reg_No varchar(10) NOT NULL,
Owner varchar(50) NOT NULL,
Driver varchar(50),
CMPID INT NOT NULL,
Status enum('AVAILABLE', 'BOOKED', 'IN TRANSIT') NOT NULL default 'AVAILABLE',
primary key(Reg_No),
constraint fk_cmpid_vehicles foreign key(CMPID) references companies(CMPID)
);

create table if not exists shippings(
ShippingID INT NOT NULL auto_increment,
OrderID varchar(50) NOT NULL,
FromBRNCHID INT NOT NULL,
ToBRNCHID INT NOT NULL,
BookedOn timestamp NOT NULL default current_timestamp,
Status enum('ORDERED','SHIPPED','DELIVERED','CANCELLED') NOT NULL default 'ORDERED',
VehicleRegNo varchar(10) NOT NULL,
Items varchar(100),
ItemsValue INT,
ItemsWeight INT,
primary key(ShippingID),
constraint fk_frombrnchid foreign key(FromBRNCHID) references branches(BRNCHID),
constraint fk_tobrnchid foreign key(ToBRNCHID) references branches(BRNCHID),
constraint fk_vehicleregno foreign key(VehicleRegNo) references vehicles(Reg_No)
);
