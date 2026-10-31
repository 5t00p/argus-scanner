Drop DataBase ArgusScanner;
Create DataBase ArgusScanner;
Use ArgusScanner;


create table Scan(
id int,
scan_status varchar(20),
started_at varchar(20),
foreign key (id) references Finding(id)
);

create table Finding(
id int,
reason varchar(20),
risk_score int,
foreign key (id) references Signatures(id)
);

create table Signatures(
id int,
cert_hash varchar(20) primary key,
package_name varchar(20),
type varchar(10),
app_family varchar(20),
foreign key (id) references Scan(id)
);
