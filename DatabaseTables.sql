--Done by Josh

Drop DataBase ArgusScanner;
Create DataBase ArgusScanner;
Use ArgusScanner;


create table Scan(
scanID int primary key auto_increment,
scan_status varchar(50),
started_at DateTime,
foreign key (deviceID) references Device(deviceID)
);

create table Finding(
findingID int primary key auto_increment,
reason varchar(100),
risk_score int,
foreign key (scanID) references Scan(scanID),
foreign key (signaturesID) references Signatures(signaturesID)
);

create table Signatures(
signaturesID int primary key auto_increment,
cert_hash varchar(100),
package_name varchar(100),
type varchar(100),
app_family varchar(100)
);

--Done by Caleb

create table Device(
    deviceID INT primary key auto_increment,
    platform varchar(100),
    model varchar(100)
);

create table Auditlog(
    auditID int primary key auto_increment,
    action varchar(255),
    `timestamp` DATETIME
);

create table `Case`(
    caseID int primary key auto_increment,
    survivor_ref varchar(100),
    opened_at DateTime,
    status varchar(50)
);

create table User(
    userID int primary key auto_increment,
    name varchar(100),
    email varchar(100),
    role varchar(50)
);
