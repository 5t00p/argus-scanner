Drop DataBase IF EXISTS ArgusScanner;
Create DataBase ArgusScanner;
Use ArgusScanner;

-- Caleb
create table User(
    userID int primary key auto_increment,
    password_hash VARCHAR(255),
    name varchar(100),
    email varchar(100) UNIQUE,
    role varchar(50)
);

create table VCase(
    caseID int primary key auto_increment,
    survivor_ref varchar(100),
    opened_at DateTime,
    status varchar(50)
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
scan_uuid CHAR(36) NOT NULL UNIQUE,
scan_status varchar(50),
started_at DateTime,
deviceID INT,
foreign key (deviceID) references Device(deviceID)
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
package_name VARCHAR(255),
reason TEXT(100),
risk_score int,
scanID INT,
signaturesID INT,
foreign key (scanID) references Scan(scanID),
foreign key (signaturesID) references Signatures(signaturesID)
);
-- Jase (Fixed the issues regarding foreign keys, and made each of the tables connect together from the model.)
-- WIll added these two cause we need them per design doc
create table CaseAssignment(
    userID INT,
    caseID INT,
    primary key (userID, caseID),
    foreign key (userID) references User(userID),
    foreign key (caseID) references VCase(caseID)
);

create table Report(
    reportID int primary key auto_increment,
    generated_at DateTime,
    scanID INT unique,
    foreign key (scanID) references Scan(scanID)
);
