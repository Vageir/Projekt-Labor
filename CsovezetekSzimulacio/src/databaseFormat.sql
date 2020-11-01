CREATE TABLE `depo` (
                        `DepoID` varchar(30) NOT NULL,
                        `DepoName` varchar(45) NOT NULL,
                        `DepoLocation` varchar(45) NOT NULL,
                        PRIMARY KEY (`DepoID`),
                        UNIQUE KEY `DepoID_UNIQUE` (`DepoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `fuel` (
                        `fuelID` int NOT NULL,
                        `fuelName` varchar(20) DEFAULT NULL,
                        PRIMARY KEY (`fuelID`),
                        UNIQUE KEY `fuelID_UNIQUE` (`fuelID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `depocontainer` (
                                 `depoID` varchar(30) NOT NULL,
                                 `containerID` varchar(30) NOT NULL,
                                 `CurrentCapacity` int NOT NULL,
                                 `MaxCapacity` int NOT NULL,
                                 `fuelID` int NOT NULL,
                                 PRIMARY KEY (`depoID`,`containerID`),
                                 KEY `fuelid_idx` (`fuelID`),
                                 CONSTRAINT `depoidfk` FOREIGN KEY (`depoID`) REFERENCES `depo` (`DepoID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                 CONSTRAINT `fuelidfk` FOREIGN KEY (`fuelID`) REFERENCES `fuel` (`fuelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `connecteddepos` (
                                  `PipeID` varchar(30) NOT NULL,
                                  `LeftDepoID` varchar(30) DEFAULT NULL,
                                  `RightDepoID` varchar(30) DEFAULT NULL,
                                  `PipeLength` int DEFAULT NULL,
                                  `PipeDiameter` int DEFAULT NULL,
                                  PRIMARY KEY (`PipeID`),
                                  UNIQUE KEY `PipeID_UNIQUE` (`PipeID`),
                                  KEY `leftdepoidfk_idx` (`LeftDepoID`),
                                  KEY `rightdepoidfk_idx` (`RightDepoID`),
                                  CONSTRAINT `leftdepoidfk` FOREIGN KEY (`LeftDepoID`) REFERENCES `depo` (`DepoID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                  CONSTRAINT `rightdepoidfk` FOREIGN KEY (`RightDepoID`) REFERENCES `depo` (`DepoID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `operator` (
                            `OperatorID` varchar(30) NOT NULL,
                            `OperatorName` varchar(45) DEFAULT NULL,
                            `OperatorBirth` date DEFAULT NULL,
                            PRIMARY KEY (`OperatorID`),
                            UNIQUE KEY `OperatorID_UNIQUE` (`OperatorID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transportationplan` (
                                      `transportationID` varchar(30) NOT NULL,
                                      `startdepoID` varchar(30) NOT NULL,
                                      `endDepoID` varchar(30) NOT NULL,
                                      `fuelID` int DEFAULT NULL,
                                      `fuelAmount` int NOT NULL,
                                      `startDate` datetime NOT NULL,
                                      `endDdate` datetime NOT NULL,
                                      `operatorID` varchar(30) NOT NULL,
                                      PRIMARY KEY (`transportationID`),
                                      UNIQUE KEY `transportationID_UNIQUE` (`transportationID`),
                                      KEY `startdepoidfk_idx` (`startdepoID`,`endDepoID`),
                                      KEY `endepoidfk_idx` (`endDepoID`),
                                      KEY `fuelidfk_idx` (`fuelID`),
                                      KEY `operatoridfk_idx` (`operatorID`),
                                      CONSTRAINT `endepoidfk` FOREIGN KEY (`endDepoID`) REFERENCES `depo` (`DepoID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `operatoridfk` FOREIGN KEY (`operatorID`) REFERENCES `operator` (`OperatorID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `startdepoidfk` FOREIGN KEY (`startdepoID`) REFERENCES `depo` (`DepoID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `tfuelidfk` FOREIGN KEY (`fuelID`) REFERENCES `fuel` (`fuelID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
                        `uname` varchar(30) NOT NULL,
                        `password` varchar(30) NOT NULL,
                        PRIMARY KEY (`uname`),
                        UNIQUE KEY `username_UNIQUE` (`uname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
