DELETE FROM projektlabor.transportationplan;
DELETE FROM projektlabor.connecteddepos;
DELETE FROM projektlabor.depocontainer;
DELETE FROM projektlabor.depo;

INSERT INTO projektlabor.depo (DepoID, DepoName, DepoLocation) VALUES ('BSA', 'Budapest', 'Budapest');
INSERT INTO projektlabor.depo (DepoID, DepoName, DepoLocation) VALUES ('CSA', 'cc', 'cc');
INSERT INTO projektlabor.depo (DepoID, DepoName, DepoLocation) VALUES ('DSA', 'Debrecen', 'Debrecen');

INSERT INTO projektlabor.connecteddepos (PipeID, LeftDepoID, RightDepoID, PipeLength, PipeDiameter) VALUES ('1BSA5000DSA1', 'BSA', 'DSA', 6000, 1);
INSERT INTO projektlabor.connecteddepos (PipeID, LeftDepoID, RightDepoID, PipeLength, PipeDiameter) VALUES ('1DSA5000CSA1', 'DSA', 'CSA', 5000, 1);

INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('BSA', 'BSA1M4000', 7000, 10000, 1);
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('BSA', 'BSA2M50000', 5000, 10000, 2);
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('CSA', 'CSA1M6000', 1000, 10000, 1);
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('CSA', 'CSA2M4000', 2000, 10000, 2);
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('DSA', 'DSA1M8000', 6000, 10000, 1);
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('DSA', 'DSA2M10000', 6000, 10000, 2);

INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID) VALUES ('1', 'DSA', 'CSA', 1, 1000, '2020-01-01 10:00:00', '2020-01-01 12:00:00', 'AD123');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID) VALUES ('2', 'BSA', 'DSA', 2, 3000, '2020-01-01 12:00:00', '2020-01-01 14:00:00', 'AD123');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID) VALUES ('3', 'DSA', 'BSA', 1, 2000, '2020-01-01 14:00:00', '2020-01-01 16:00:00', 'JSP23');