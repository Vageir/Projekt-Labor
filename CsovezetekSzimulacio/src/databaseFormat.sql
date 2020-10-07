create database projektlabor;
use projektlabor;

create table connecteddepos
(
    LeftDepoID   varchar(30) not null
        primary key,
    RightDepoID  varchar(30) not null,
    PipeLength   int         not null,
    PipeDiameter int         not null
);

create table depo
(
    DepoID       varchar(30) not null,
    DepoName     varchar(45) not null,
    DepoLocation varchar(45) not null,
    constraint DepoID_UNIQUE
        unique (DepoID)
);

alter table depo
    add primary key (DepoID);

create table depocontainer
(
    depoID          varchar(30) not null,
    containerID     varchar(30) not null,
    CurrentCapacity int         not null,
    MaxCapacity     int         not null,
    fuelID          int         not null,
    primary key (depoID, containerID)
);

create table fuel
(
    fuelID   int         not null,
    fuelName varchar(20) null,
    constraint fuelID_UNIQUE
        unique (fuelID)
);

alter table fuel
    add primary key (fuelID);

create table operator
(
    OperatorID    varchar(30) not null,
    OperatorName  varchar(45) null,
    OperatorBirth date        null,
    constraint OperatorID_UNIQUE
        unique (OperatorID)
);

alter table operator
    add primary key (OperatorID);

create table transportationplan
(
    transportationID varchar(30) not null,
    startdepoID      varchar(30) not null,
    endDepoID        varchar(30) not null,
    fuelID           int         not null,
    fuelAmount       int         not null,
    startDate        datetime    not null,
    endDdate         datetime    not null,
    operatorID       varchar(30) not null,
    constraint transportationID_UNIQUE
        unique (transportationID)
);

alter table transportationplan
    add primary key (transportationID);

