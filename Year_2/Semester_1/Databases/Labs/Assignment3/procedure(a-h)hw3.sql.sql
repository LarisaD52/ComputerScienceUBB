USE InvestigationsDB;



-- DROP tabele
DROP TABLE IF EXISTS VERSION_TABLE;
DROP TABLE IF EXISTS PROCEDURES_TABLE;
DROP TABLE IF EXISTS DemoKeys;
DROP TABLE IF EXISTS SuspectBackup;
DROP PROCEDURE IF EXISTS changeSuspectCNPtoChar15;
DROP PROCEDURE IF EXISTS revertSuspectCNPtoChar13;
DROP PROCEDURE IF EXISTS addSuspectTelefon;
DROP PROCEDURE IF EXISTS removeSuspectTelefon;
DROP PROCEDURE IF EXISTS addDefaultDescriere;
DROP PROCEDURE IF EXISTS removeDefaultDescriere;
DROP PROCEDURE IF EXISTS addPKTipProba;
DROP PROCEDURE IF EXISTS removePKTipProba;
DROP PROCEDURE IF EXISTS addPrimaryKeyDemo;
DROP PROCEDURE IF EXISTS removePrimaryKeyDemo;
DROP PROCEDURE IF EXISTS addCandidateKeyDemo;
DROP PROCEDURE IF EXISTS removeCandidateKeyDemo;
DROP PROCEDURE IF EXISTS addForeignKeyDemo;
DROP PROCEDURE IF EXISTS removeForeignKeyDemo;
DROP PROCEDURE IF EXISTS createSuspectBackup;
DROP PROCEDURE IF EXISTS dropSuspectBackup;
DROP PROCEDURE IF EXISTS goToVersion;

-- =============================
-- Tabele de versionare
-- =============================
DROP TABLE IF EXISTS VERSION_TABLE;
DROP TABLE IF EXISTS PROCEDURES_TABLE;

CREATE TABLE VERSION_TABLE(version INT PRIMARY KEY);
INSERT INTO VERSION_TABLE VALUES(0);

CREATE TABLE PROCEDURES_TABLE(
    fromVersion INT,
    toVersion INT,
    nameProc VARCHAR(255),
    PRIMARY KEY(fromVersion, toVersion)
);

DELETE FROM PROCEDURES_TABLE;

INSERT INTO PROCEDURES_TABLE VALUES(0, 1, 'changeSuspectCNPtoChar15'); 
INSERT INTO PROCEDURES_TABLE VALUES(1, 0, 'revertSuspectCNPtoChar13'); 

INSERT INTO PROCEDURES_TABLE VALUES(1, 2, 'addSuspectTelefon');        
INSERT INTO PROCEDURES_TABLE VALUES(2, 1, 'removeSuspectTelefon');     

INSERT INTO PROCEDURES_TABLE VALUES(2, 3, 'addDefaultDescriere');      
INSERT INTO PROCEDURES_TABLE VALUES(3, 2, 'removeDefaultDescriere');   

INSERT INTO PROCEDURES_TABLE VALUES(3, 4, 'addPrimaryKeyDemo');
INSERT INTO PROCEDURES_TABLE VALUES(4, 3, 'removePrimaryKeyDemo');

INSERT INTO PROCEDURES_TABLE VALUES(4, 5, 'addCandidateKeyDemo');
INSERT INTO PROCEDURES_TABLE VALUES(5, 4, 'removeCandidateKeyDemo');

INSERT INTO PROCEDURES_TABLE VALUES(5, 6, 'addForeignKeyDemo');
INSERT INTO PROCEDURES_TABLE VALUES(6, 5, 'removeForeignKeyDemo');

INSERT INTO PROCEDURES_TABLE VALUES(6, 7, 'createSuspectBackup');      
INSERT INTO PROCEDURES_TABLE VALUES(7, 6, 'dropSuspectBackup');        

-- =============================
-- Proceduri de upgrade/rollback
-- =============================

-- a) Modificare tip CNP
CREATE PROCEDURE changeSuspectCNPtoChar15 AS
BEGIN
    ALTER TABLE Suspect ALTER COLUMN cnp CHAR(15);
    PRINT 'CNP changed to CHAR(15)';
END;

CREATE PROCEDURE revertSuspectCNPtoChar13 AS
BEGIN
    ALTER TABLE Suspect ALTER COLUMN cnp CHAR(13);
    PRINT 'CNP reverted to CHAR(13)';
END;

-- b) Adaugă / elimină coloana telefon
CREATE PROCEDURE addSuspectTelefon AS
BEGIN
    IF COL_LENGTH('Suspect', 'telefon') IS NULL
        ALTER TABLE Suspect ADD telefon NVARCHAR(20);
    PRINT 'Telefon column added';
END;

CREATE PROCEDURE removeSuspectTelefon AS
BEGIN
    IF COL_LENGTH('Suspect', 'telefon') IS NOT NULL
        ALTER TABLE Suspect DROP COLUMN telefon;
    PRINT 'Telefon column removed';
END;

-- c) Default constraint
CREATE PROCEDURE addDefaultDescriere AS
BEGIN
        ALTER TABLE TipProba ADD CONSTRAINT DF_Descriere DEFAULT 'Necunoscut' FOR descriere;
    PRINT 'Default "Necunoscut" adaugat';
END;

CREATE PROCEDURE removeDefaultDescriere AS
BEGIN
        ALTER TABLE TipProba DROP CONSTRAINT DF_Descriere;
    PRINT 'Default eliminat';
END;

--tebel demo pt d,e,f
DROP TABLE IF EXISTS DemoKeys;

CREATE TABLE DemoKeys (
    id_demo INT NOT NULL,    -- NOT NULL pentru PK
    nume NVARCHAR(100),
    valoare NVARCHAR(50),
    id_detectiv INT          -- pentru FK
);

-- d) Primary Key
DROP PROCEDURE IF EXISTS addPrimaryKeyDemo;
CREATE PROCEDURE addPrimaryKeyDemo AS
BEGIN
    ALTER TABLE DemoKeys ADD CONSTRAINT PK_DemoKeys PRIMARY KEY(id_demo);
    PRINT 'Primary key added to DemoKeys';
END;

DROP PROCEDURE IF EXISTS removePrimaryKeyDemo;
CREATE PROCEDURE removePrimaryKeyDemo AS
BEGIN
    ALTER TABLE DemoKeys DROP CONSTRAINT PK_DemoKeys;
    PRINT 'Primary key removed from DemoKeys';
END;

-- e) Candidate Key
DROP PROCEDURE IF EXISTS addCandidateKeyDemo;
CREATE PROCEDURE addCandidateKeyDemo AS
BEGIN
    ALTER TABLE DemoKeys ADD CONSTRAINT CK_DemoKeys UNIQUE(nume, valoare);
    PRINT 'Candidate key added to DemoKeys';
END;

DROP PROCEDURE IF EXISTS removeCandidateKeyDemo;
CREATE PROCEDURE removeCandidateKeyDemo AS
BEGIN
    ALTER TABLE DemoKeys DROP CONSTRAINT CK_DemoKeys;
    PRINT 'Candidate key removed from DemoKeys';
END;

-- f) Foreign Key
DROP PROCEDURE IF EXISTS addForeignKeyDemo;
CREATE PROCEDURE addForeignKeyDemo AS
BEGIN
    ALTER TABLE DemoKeys ADD CONSTRAINT FK_DemoKeys_Detectiv FOREIGN KEY(id_detectiv)
        REFERENCES Detectiv(id_detectiv);
    PRINT 'Foreign key added to DemoKeys.id_detectiv';
END;

DROP PROCEDURE IF EXISTS removeForeignKeyDemo;
CREATE PROCEDURE removeForeignKeyDemo AS
BEGIN
    ALTER TABLE DemoKeys DROP CONSTRAINT FK_DemoKeys_Detectiv;
    PRINT 'Foreign key removed from DemoKeys.id_detectiv';
END;

-- g) Backup tabel Suspect
DROP PROCEDURE IF EXISTS createSuspectBackup;
CREATE PROCEDURE createSuspectBackup AS
BEGIN
    CREATE TABLE SuspectBackup (
        id_suspect INT PRIMARY KEY,
        nume NVARCHAR(100),
        prenume NVARCHAR(100),
        cnp CHAR(13),
        adresa NVARCHAR(255),
        periculos BIT DEFAULT 0
    );
    PRINT 'SuspectBackup table created';
END;

DROP PROCEDURE IF EXISTS dropSuspectBackup;
CREATE PROCEDURE dropSuspectBackup AS
BEGIN
    DROP TABLE SuspectBackup;
    PRINT 'SuspectBackup dropped';
END;

-- =============================️
	Procedura goToVersion
-- =============================
CREATE PROCEDURE goToVersion(@newVersion INT) AS
BEGIN
    DECLARE @currentVersion INT;
    DECLARE @procName VARCHAR(255);

    SELECT @currentVersion = version FROM VERSION_TABLE;

    IF @currentVersion = @newVersion
    BEGIN
        PRINT 'Already at the requested version: ' + CAST(@currentVersion AS VARCHAR);
        RETURN;
    END;

    -- Upgrade
    WHILE @currentVersion < @newVersion
    BEGIN
        SELECT @procName = nameProc 
        FROM PROCEDURES_TABLE 
        WHERE fromVersion = @currentVersion AND toVersion = @currentVersion + 1;

        IF @procName IS NULL 
        BEGIN 
            PRINT 'No upgrade procedure found from version ' + CAST(@currentVersion AS VARCHAR); 
            RETURN; 
        END

        PRINT 'Executing upgrade: ' + @procName;
        BEGIN TRY
            EXEC(@procName);
            SET @currentVersion = @currentVersion + 1;
            UPDATE VERSION_TABLE SET version = @currentVersion;
            PRINT 'Upgrade successful. Current version: ' + CAST(@currentVersion AS VARCHAR);
        END TRY
        BEGIN CATCH
            PRINT 'Error executing procedure: ' + @procName;
            PRINT ERROR_MESSAGE();
            RETURN;
        END CATCH
    END

    -- Rollback
    WHILE @currentVersion > @newVersion
    BEGIN
        SELECT @procName = nameProc 
        FROM PROCEDURES_TABLE 
        WHERE fromVersion = @currentVersion AND toVersion = @currentVersion - 1;

        IF @procName IS NULL 
        BEGIN 
            PRINT 'No rollback procedure found from version ' + CAST(@currentVersion AS VARCHAR); 
            RETURN; 
        END

        PRINT 'Executing rollback: ' + @procName;
        BEGIN TRY
            EXEC(@procName);
            SET @currentVersion = @currentVersion - 1;
            UPDATE VERSION_TABLE SET version = @currentVersion;
            PRINT 'Rollback successful. Current version: ' + CAST(@currentVersion AS VARCHAR);
        END TRY
        BEGIN CATCH
            PRINT 'Error executing procedure: ' + @procName;
            PRINT ERROR_MESSAGE();
            RETURN;
        END CATCH
    END

    PRINT 'Final version: ' + CAST(@currentVersion AS VARCHAR);
END;

-- set initial version
UPDATE VERSION_TABLE SET version = 0;

-- Verificam tabelul de versiuni si proceduri
SELECT * FROM VERSION_TABLE;
SELECT * FROM PROCEDURES_TABLE ORDER BY fromVersion, toVersion;

EXEC goToVersion 3;

EXEC goToVersion 2;

EXEC goToVersion 0;

EXEC goToVersion 7;

SELECT * FROM Suspect;


-- verificam versiunea finala 
SELECT * FROM VERSION_TABLE;

