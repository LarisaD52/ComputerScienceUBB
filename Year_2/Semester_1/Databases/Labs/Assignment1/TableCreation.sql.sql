


USE InvestigationsDB;

-- 2. Recreem tabelele in ordinea corecta


DROP TABLE IF EXISTS Investigatie_Detectiv;
DROP TABLE IF EXISTS Raport;
DROP TABLE IF EXISTS Proba;
DROP TABLE IF EXISTS TipProba;
DROP TABLE IF EXISTS Suspect;
DROP TABLE IF EXISTS Investigatie;
DROP TABLE IF EXISTS Locatie;
DROP TABLE IF EXISTS TipInfractiune;
DROP TABLE IF EXISTS Detectiv;
DROP TABLE IF EXISTS Echipa;



CREATE TABLE Echipa (
    id_echipa INT PRIMARY KEY,
    nume_echipa NVARCHAR(100) NOT NULL,
    data_infiintare DATE
);

CREATE TABLE Detectiv (
    id_detectiv INT PRIMARY KEY,
    nume NVARCHAR(100) NOT NULL,
    prenume NVARCHAR(100) NOT NULL,
    rang NVARCHAR(50),
    id_echipa INT,
    FOREIGN KEY (id_echipa) REFERENCES Echipa(id_echipa)
);

CREATE TABLE TipInfractiune (
    id_tipinfractiune INT PRIMARY KEY,
    denumire NVARCHAR(100) NOT NULL,
    descriere NVARCHAR(100)
);

CREATE TABLE Locatie (
    id_locatie INT PRIMARY KEY,
    adresa NVARCHAR(255),
    oras NVARCHAR(100),
    judet NVARCHAR(100)
);

CREATE TABLE Investigatie (
    id_investigatie INT PRIMARY KEY,
    titlu NVARCHAR(150) NOT NULL,
    data_deschidere DATE,
    stare NVARCHAR(50),
    id_locatie INT,
    id_tipinfractiune INT,
    FOREIGN KEY (id_locatie) REFERENCES Locatie(id_locatie),
    FOREIGN KEY (id_tipinfractiune) REFERENCES TipInfractiune(id_tipinfractiune)
);

CREATE TABLE Suspect (
    id_suspect INT PRIMARY KEY,
    nume NVARCHAR(100) NOT NULL,
    prenume NVARCHAR(100) NOT NULL,
    cnp CHAR(13),
    adresa NVARCHAR(255),
    periculos BIT DEFAULT 0
);

CREATE TABLE TipProba (
    id_tipproba INT PRIMARY KEY,
    denumire NVARCHAR(100) NOT NULL,
    descriere NVARCHAR(100)
);

CREATE TABLE Proba (
    id_proba INT PRIMARY KEY,
    descriere NVARCHAR(100),
    data_gasire DATE,
    id_investigatie INT,
    id_suspect INT,
    id_tipproba INT,
    FOREIGN KEY (id_investigatie) REFERENCES Investigatie(id_investigatie),
    FOREIGN KEY (id_suspect) REFERENCES Suspect(id_suspect),
    FOREIGN KEY (id_tipproba) REFERENCES TipProba(id_tipproba)
);

CREATE TABLE Raport (
    id_raport INT PRIMARY KEY,
    titlu NVARCHAR(150) NOT NULL,
    continut NVARCHAR(100),
    data_redactare DATE,
    id_investigatie INT,
    id_detectiv INT,
    FOREIGN KEY (id_investigatie) REFERENCES Investigatie(id_investigatie),
    FOREIGN KEY (id_detectiv) REFERENCES Detectiv(id_detectiv)
);

CREATE TABLE Investigatie_Detectiv (
    id_investigatie INT NOT NULL,
    id_detectiv INT NOT NULL,
    rol NVARCHAR(100),
    PRIMARY KEY (id_investigatie, id_detectiv),
    FOREIGN KEY (id_investigatie) REFERENCES Investigatie(id_investigatie),
    FOREIGN KEY (id_detectiv) REFERENCES Detectiv(id_detectiv)
);



