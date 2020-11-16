INSERT INTO projektlabor.depo (DepoID, DepoName, DepoLocation) VALUES ('BSA', 'BSA', 'BSA');
INSERT INTO projektlabor.depo (DepoID, DepoName, DepoLocation) VALUES ('CSA', 'CSA', 'CSA');
INSERT INTO projektlabor.depo (DepoID, DepoName, DepoLocation) VALUES ('DSA', 'DSA', 'DSA');

INSERT INTO projektlabor.connecteddepos (PipeID, LeftDepoID, RightDepoID, PipeLength, PipeDiameter) VALUES ('BSADSA1', 'BSA', 'DSA', 5000, 1);
INSERT INTO projektlabor.connecteddepos (PipeID, LeftDepoID, RightDepoID, PipeLength, PipeDiameter) VALUES ('BSADSA2', 'BSA', 'DSA', 5000, 1);
INSERT INTO projektlabor.connecteddepos (PipeID, LeftDepoID, RightDepoID, PipeLength, PipeDiameter) VALUES ('DSACSA1', 'DSA', 'CSA', 6000, 1);
INSERT INTO projektlabor.connecteddepos (PipeID, LeftDepoID, RightDepoID, PipeLength, PipeDiameter) VALUES ('DSACSA2', 'DSA', 'CSA', 6000, 1);
-- CONTAINERS
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('BSA', 'BSA1M15000', 8000, 15000, 1);
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('BSA', 'BSA2M9000', 4000, 9000, 2);
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('CSA', 'CSA1M12000', 10000, 12000, 1);
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('CSA', 'CSA2M13000', 7000, 13000, 2);
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('DSA', 'DSA1M20000', 4000, 20000, 1);
INSERT INTO projektlabor.depocontainer (depoID, containerID, CurrentCapacity, MaxCapacity, fuelID) VALUES ('DSA', 'DSA2M10000', 4000, 10000, 2);

-- TRANSPORTAIONPLANS Vételi oldalon megtelik a tartály
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('1', 'BSA', 'DSA', 1, 3000, '2020-11-16 13:00:00', '2020-11-16 15:00:00', 'AD123', 'BSADSA1');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('2', 'DSA', 'CSA', 2, 2000, '2020-11-16 14:00:00', '2020-11-16 16:00:00', 'AD123', 'DSACSA1');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('3', 'DSA', 'BSA', 2, 4000, '2020-11-16 16:00:00', '2020-11-16 18:00:00', 'AD123', 'BSADSA2');

-- TRANSPORTATIONPLANS Az induló oldalon nincs elég üzemanyag
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('1', 'BSA', 'DSA', 1, 3000, '2020-11-16 13:00:00', '2020-11-16 15:00:00', 'AD123', 'BSADSA1');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('2', 'DSA', 'CSA', 2, 1000, '2020-11-16 14:00:00', '2020-11-16 16:00:00', 'AD123', 'DSACSA1');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('3', 'DSA', 'BSA', 2, 4000, '2020-11-16 16:00:00', '2020-11-16 18:00:00', 'AD123', 'BSADSA2');

-- TRANSPORTAIONPLANS Nincs elég idő
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('1', 'BSA', 'DSA', 1, 5000, '2020-11-16 13:00:00', '2020-11-16 14:00:00', 'AD123', 'BSADSA1');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('2', 'DSA', 'CSA', 2, 1000, '2020-11-16 14:00:00', '2020-11-16 16:00:00', 'AD123', 'DSACSA1');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('3', 'DSA', 'BSA', 1, 2000, '2020-11-16 16:00:00', '2020-11-16 18:00:00', 'AD123', 'BSADSA2');

-- TRANSPORTAIONPLANS Nincs elég üzemanyago hogy át lehessen tolni
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('1', 'BSA', 'DSA', 1, 3000, '2020-11-16 13:00:00', '2020-11-16 15:00:00', 'AD123', 'BSADSA1');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('2', 'DSA', 'CSA', 2, 1000, '2020-11-16 14:00:00', '2020-11-16 16:00:00', 'AD123', 'DSACSA1');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('3', 'BSA', 'DSA', 2, 4000, '2020-11-16 16:00:00', '2020-11-16 18:00:00', 'AD123', 'BSADSA2');

-- TRANSPORTATIONPLANS Hibátlan lefuttás
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('1', 'BSA', 'DSA', 1, 3000, '2020-11-16 13:00:00', '2020-11-16 15:00:00', 'AD123', 'BSADSA1');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('2', 'DSA', 'CSA', 2, 1000, '2020-11-16 14:00:00', '2020-11-16 16:00:00', 'AD123', 'DSACSA1');
INSERT INTO projektlabor.transportationplan (transportationID, startdepoID, endDepoID, fuelID, fuelAmount, startDate, endDdate, operatorID, pipeID) VALUES ('3', 'DSA', 'BSA', 1, 2000, '2020-11-16 16:00:00', '2020-11-16 17:30:00', 'AD123', 'BSADSA2');
