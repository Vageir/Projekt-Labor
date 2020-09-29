create database projektlabor
use projektlabor

create table TransportationPlan
(
	TransportationID varchar(30) not null,
	FuelType varchar(30) not null,
	EndDepoID varchar(30) not null,
	StartDepoID varchar(30) not null,
	FuelAmount int not null,
	StartDate date not null,
	EndDate date not null,
	OperatorID varchar(30) not null,
	OperatorName varchar(30) not null
);

create unique index TransportationPlan_TransportationID_uindex
	on TransportationPlan (TransportationID);

alter table TransportationPlan
	add constraint TransportationPlan_pk
		primary key (TransportationID);

