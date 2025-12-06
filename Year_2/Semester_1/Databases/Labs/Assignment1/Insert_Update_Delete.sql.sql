USE InvestigationsDB;

-- ------------------------------
-- 1. Clear all tables in the correct order to avoid FK conflicts
-- ------------------------------
DELETE FROM Investigatie_Detectiv;
DELETE FROM Raport;
DELETE FROM Proba;
DELETE FROM Investigatie;
DELETE FROM Detectiv;
DELETE FROM Suspect;
DELETE FROM TipProba;
DELETE FROM TipInfractiune;
DELETE FROM Locatie;
DELETE FROM Echipa;

-- ------------------------------
-- 2. Insert into Team first (no FK dependencies)
-- ------------------------------
INSERT INTO Echipa (id_echipa, nume_echipa, data_infiintare) VALUES
(1, 'Team Alpha', '2010-03-15'),
(2, 'Team Beta', '2015-07-01');


SELECT * FROM Echipa;

-- ------------------------------
-- 3. Insert into Detective (depends on Team)
-- ------------------------------
INSERT INTO Detectiv
VALUES
(1, 'Popescu', 'Ion', 'Inspector', 1),
(2, 'Ionescu', 'Maria', 'Commissioner', 2),
(3, 'Georgescu', 'Andrei', 'Agent', 1);

SELECT * FROM Detectiv;
INSERT INTO Detectiv (id_detectiv, nume, prenume, rang, id_echipa)
VALUES (4, 'Stan', 'Cristian', 'Agent', 1);

UPDATE Detectiv
SET rang = 'Senior Agent'
WHERE id_echipa = 1 AND rang <> 'Commissioner';

SELECT * FROM Detectiv;


-- ------------------------------
-- 4. Insert into CrimeType
-- ------------------------------
INSERT INTO TipInfractiune 
VALUES
(1, 'Theft', 'Taking property without consent'),
(2, 'Fraud', 'Financial deception');

SELECT * FROM TipInfractiune;


-- ------------------------------
-- 5. Insert into Location
-- ------------------------------
INSERT INTO Locatie
VALUES
(1, 'Libertatii St 10', 'Bucharest', 'Bucharest'),
(2, 'Independentei St 25', 'Cluj-Napoca', 'Cluj');

SELECT * FROM Locatie;

-- ------------------------------
-- 6. Insert into Investigation (depends on Location & CrimeType)
-- ------------------------------
INSERT INTO Investigatie
VALUES
(1, 'Store Theft', '2025-01-10', 'Open', 1, 1),
(2, 'Bank Fraud', '2025-02-05', 'Ongoing', 2, 2);

INSERT INTO Investigatie
VALUES
(3, ' T', '2025-02-10', 'Open', 1, 1),
(4, ' A', '2025-03-05', 'Ongoing', 2, 2);

SELECT * FROM Investigatie;

UPDATE Investigatie
SET stare = 'Closed'
WHERE data_deschidere < '2025-02-01' AND stare IS NOT NULL;

SELECT * FROM Investigatie;

-- ------------------------------
-- 7. Insert into Suspect
-- ------------------------------
INSERT INTO Suspect VALUES
(1, 'Marin', 'Vlad', '1234567890123', 'Libertatii St 15', 0),
(2, 'Dumitrescu', 'Alina', '9876543210987', 'Independentei St 30', 1);

SELECT * FROM Suspect;

UPDATE Suspect
SET periculos = 1
WHERE adresa LIKE '%Cluj%';




-- ------------------------------
-- 8. Insert into EvidenceType
-- ------------------------------
INSERT INTO TipProba 
VALUES
(1, 'Fingerprint', 'Fingerprint collected at the crime scene'),
(2, 'Photograph', 'Photo or video as evidence');

SELECT * FROM TipProba;

-- ------------------------------
-- 9. Insert into Evidence (depends on Investigation, Suspect & EvidenceType)
-- ------------------------------
INSERT INTO Proba  
VALUES
(1, 'Fingerprint on window', '2025-01-11', 1, 1, 1),
(2, 'Security camera photo', '2025-02-06', 2, 2, 2);

SELECT * FROM Proba;

-- 1️⃣ DELETE care funcționează corect (folosim BETWEEN)
DELETE FROM Proba
WHERE id_proba BETWEEN 1 AND 1;  -- ștergem doar o probă sigură

-- ------------------------------
-- 10. Insert into Report (depends on Investigation & Detective)
-- ------------------------------
INSERT INTO Raport
VALUES
(1, 'Store Theft Report', 'Investigation ongoing, evidence collected', '2025-01-12', 1, 1),
(2, 'Bank Fraud Report', 'Suspect caught, interrogation pending', '2025-02-07', 2, 2);

SELECT * FROM Raport;

-- ------------------------------
-- 11. Insert into Investigation_Detective (depends on Investigation & Detective)
-- ------------------------------
INSERT INTO Investigatie_Detectiv  
VALUES
(1, 1, 'Coordinator'),
(1, 3, 'Assistant'),
(2, 2, 'Coordinator');

--pt g
INSERT INTO Detectiv VALUES (3, 'Georgescu', 'Andrei', 'Agent', 1);
INSERT INTO Investigatie_Detectiv VALUES (2, 3, 'Assistant');

SELECT * FROM Investigatie_Detectiv;





--DELETE

-- sterg suspectul care nu e periculos
DELETE FROM Suspect
WHERE periculos = 0 AND nume LIKE 'M%';

SELECT * FROM Suspect;

-- trebuie sterse mai intai legaturile din tabelul intermediar
DELETE FROM Investigatie_Detectiv
WHERE id_detectiv = 3;
SELECT * FROM Investigatie_Detectiv;


-- acum am voie sa sterg detectivul
DELETE FROM Detectiv
WHERE id_detectiv = 3;
SELECT * FROM Detectiv;

