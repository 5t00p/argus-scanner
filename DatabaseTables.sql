--done by Justin
Drop DataBase ArgusScanner;
Create DataBase ArgusScanner;
Use ArgusScanner;


create table Scan(
id int primary key auto_increment,
scan_status varchar(20),
started_at DateTime,
foreign key (id) references Finding(id)
);

create table Finding(
id int primary key auto_increment,
reason varchar(20),
risk_score int,
foreign key (id) references Signatures(id)
);

create table Signatures(
id int primary key auto_increment,
cert_hash varchar(20) primary key,
package_name varchar(20),
type varchar(10),
app_family varchar(20),
foreign key (id) references Scan(id)
);

--done by Caleb
create table Device(
    id INT primary key auto_increment,
    platform varchar(100),
    model varchar(100)
);

create table Auditlog(
    id int primary key auto_increment,
    action varchar(255),
    'timestamp' DATETIME
);

create table Case(
    id int primary key auto_increment,
    survivor_ref varchar(100),
    opened_at DateTime
    status varchar(50)
);

create table User(
    id int primary key auto_increment,
    name varchar(100),
    email varchar(100),
    role varchar(50)
);
