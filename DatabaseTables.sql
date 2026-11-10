Drop DataBase IF EXISTS ArgusScanner;
Create DataBase ArgusScanner;
Use ArgusScanner;

-- Caleb
create table User(
    userID int primary key auto_increment,
    name varchar(100),
    email varchar(100),
    role varchar(50)
);

create table VCase(
    caseID int primary key auto_increment,
    survivor_ref varchar(100),
    opened_at DateTime,
    status varchar(50),
    userID INT,
    foreign key (userID) references User(userID)
);

create table Auditlog(
    auditID int primary key auto_increment,
    action varchar(255),
    `timestamp` DATETIME,
    userID INT,
    caseID INT,
    foreign key (userID) references User(userID),
    foreign key (caseID) references VCase(caseID)
);

create table Device(
    deviceID INT primary key auto_increment,
    platform varchar(100),
    model varchar(100),
    caseID INT,
    foreign key (caseID) references VCase(caseID)
);

-- Josh
create table Scan(
scanID int primary key auto_increment,
scan_status varchar(50),
started_at DateTime,
deviceID INT,
caseID INT,
foreign key (deviceID) references Device(deviceID),
foreign key (caseID) references VCase(caseID)
);

create table Signatures(
signaturesID int primary key auto_increment,
cert_hash varchar(100),
package_name varchar(100),
type varchar(100),
app_family varchar(100)
);

create table Finding(
findingID int primary key auto_increment,
reason varchar(100),
risk_score int,
scanID INT,
signaturesID INT,
foreign key (scanID) references Scan(scanID),
foreign key (signaturesID) references Signatures(signaturesID)
);
-- Jase (Fixed the issues regarding foreign keys, and made each of the tables connect together from the model.)
