DROP DATABASE IF EXISTS timesheet_entry_system;
CREATE DATABASE timesheet_entry_system;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin';
GRANT ALL ON timesheet_entry_system.* TO 'admin'@'localhost';
GRANT ALL ON timesheet_entry_system.* TO 'admin'@'%';

USE timesheet_entry_system;

DROP TABLE IF EXISTS Employees;
CREATE TABLE Employees(
    EmpNo INT(5) AUTO_INCREMENT NOT NULL UNIQUE,
    EmpName VARCHAR(50) NOT NULL,
    EmpUserName VARCHAR(10) NOT NULL UNIQUE,
    CONSTRAINT PKEmployee PRIMARY KEY (EmpNo)
);

DROP TABLE IF EXISTS Admins;
CREATE TABLE Admins(
    EmpNo INT(5) NOT NULL UNIQUE,
    CONSTRAINT PKAdmin PRIMARY KEY (EmpNo), 
    CONSTRAINT FKAdminEmpNo
        FOREIGN KEY (EmpNo)
            REFERENCES Employees(EmpNo)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
);

DROP TABLE IF EXISTS Credentials;
CREATE TABLE Credentials(
    EmpNo INT(5) NOT NULL UNIQUE,
    EmpUserName VARCHAR(20) NOT NULL UNIQUE,
    EmpToken BINARY(32) NOT NULL,
    CONSTRAINT FKCredentialEmpNo
        FOREIGN KEY (EmpNo)
            REFERENCES Employees(EmpNo)
            ON UPDATE CASCADE
            ON DELETE CASCADE,
    CONSTRAINT FKCredentialEmpUserName
        FOREIGN KEY (EmpUserName)
            REFERENCES Employees(EmpUserName)
);

DROP TABLE IF EXISTS Timesheets;
CREATE TABLE Timesheets(
    EmpNo INT(5) NOT NULL,
    EndWeek DATE NOT NULL,
    TimesheetID INT(5) NOT NULL UNIQUE AUTO_INCREMENT,
    CONSTRAINT PKTimesheet PRIMARY KEY (EmpNo, EndWeek),
    CONSTRAINT FKTimesheetEmpNo
        FOREIGN KEY (EmpNo)
            REFERENCES Employees (EmpNo)
            ON UPDATE CASCADE 
            ON DELETE CASCADE
);

DROP TABLE IF EXISTS TimesheetRows;
CREATE TABLE TimesheetRows(
	TimesheetRowID INT(10) NOT NULL UNIQUE AUTO_INCREMENT,
    TimesheetID INT(5) NOT NULL,
    ProjectID INT(5) NOT NULL,
    WorkPackage VARCHAR(10) NOT NULL,
    Notes VARCHAR(50) NOT NULL,
    HoursForWeek VARCHAR(50) NOT NULL,
    CONSTRAINT PKTimesheetRows PRIMARY KEY (TimesheetID, ProjectID, WorkPackage),
    CONSTRAINT FKTimesheetRowsTimesheetID
        FOREIGN KEY (TimesheetID)
            REFERENCES Timesheets (TimesheetID)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

INSERT INTO Employees VALUES (531699, "Bruce Link", "bdlink");
INSERT INTO Credentials VALUES (531699, "bdlink", X'A11D2C3B39081586F79B867DF1533396CE4C2E8E45D88D22E5FE30996D54B00B');
INSERT INTO Admins VALUES (531699)


