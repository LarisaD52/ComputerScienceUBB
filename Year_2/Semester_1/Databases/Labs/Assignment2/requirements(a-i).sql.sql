USE InvestigationsDB;

-- - - - - - - - - - - - - - - - -  a)
SELECT * FROM Detectiv;

SELECT d1.id_detectiv, d1.nume, d1.prenume, d1.rang 
FROM Detectiv d1
WHERE id_echipa = 1
UNION 
SELECT d2.id_detectiv, d2.nume, d2.prenume, d2.rang 
FROM Detectiv d2
WHERE rang = 'Commissioner';
-- UNION = combina rezultatele a doua SELECT-uri, eliminand duplicatele


SELECT * FROM Investigatie;
SELECT * 
FROM Investigatie
WHERE stare = 'Closed' OR id_locatie = 2
UNION ALL
SELECT * 
FROM Investigatie
WHERE data_deschidere > '2025-01-31';	
-- UNION ALL = combina rezultatele a doua SELECT-uri, pastrand duplicatele
--o sa duplice investigatia cu id = 2 deoarece in primul where noi cerem stare = 'Closed' OR id_locatie = 2 
--iar in cel de al doilea where cerem data_deschidere > '2025-01-31';	 iar aceasta data este tot a lui id = 2




-- - - - - - - - - - - - - - - - -  b)

SELECT d1.id_detectiv, d1.nume, d1.prenume
FROM Detectiv d1
WHERE d1.id_echipa = 1
INTERSECT
SELECT d2.id_detectiv, d2.nume, d2.prenume
FROM Detectiv d2
JOIN Investigatie_Detectiv idd ON d2.id_detectiv = idd.id_detectiv; 
--INTERSECT = returneaza doar randurile care apar in ambele seturi de rezultate


SELECT * 
FROM Suspect
WHERE id_suspect IN (
    SELECT id_suspect
    FROM Proba
);
-- IN = verifica daca o valoare se afla intr-un set de valori (subquery sau lista)




-- - - - - - - - - - - - - -  - - -  c)
SELECT id_detectiv, nume, prenume
FROM Detectiv
EXCEPT
SELECT id_detectiv, nume, prenume
FROM Detectiv
WHERE id_detectiv IN (
    SELECT id_detectiv
    FROM Investigatie_Detectiv
);
-- EXCEPT = returneaza randurile din primul SELECT care nu apar in al doilea SELECT


SELECT * 
FROM Proba
WHERE id_suspect NOT IN (
    SELECT id_suspect
    FROM Proba
    WHERE id_suspect = 2
);

SELECT *
FROM Detectiv
WHERE id_detectiv NOT IN (
    SELECT id_detectiv
    FROM Investigatie_Detectiv
);



-- NOT IN = returneaza randuri unde valoarea nu se afla in lista/subquery specificat


-- - - - - - - - - - - - - - - - - - d)
--INNER JOIN = Returneaza doar randurile care au corespondenţă in ambele tabele.
--LEFT JOIN = Returneaza toate randurile din tabelul din stanga + randurile corespunzatoare din dreapta (NULL daca nu exista potrivire).
--RIGHT JOIN = Returneaza toate randurile din tabelul din dreapta + randurile corespunzatoare din stanga (NULL daca nu exista potrivire). 
-- FULL JOIN = Returneaza toate randurile din ambele tabele; daca nu exista potrivire, coloanele lipsa sunt NULL.

SELECT * FROM Detectiv d;
SELECT * FROM Investigatie i;
SELECT * FROM Investigatie_Detectiv id;

SELECT 
    d.id_detectiv, d.nume, d.prenume, 
    idd.id_investigatie, idd.rol,
    i.data_deschidere, i.stare
FROM Detectiv d
INNER JOIN Investigatie_Detectiv idd 
    ON d.id_detectiv = idd.id_detectiv
INNER JOIN Investigatie i
    ON idd.id_investigatie = i.id_investigatie;


SELECT 
    d.id_detectiv, d.nume, d.prenume, 
    idd.id_investigatie, idd.rol
FROM Detectiv d
LEFT JOIN Investigatie_Detectiv idd 
    ON d.id_detectiv = idd.id_detectiv;


SELECT 
    d.id_detectiv, d.nume, d.prenume, 
    idd.id_investigatie, idd.rol
FROM Detectiv d
RIGHT JOIN Investigatie_Detectiv idd 
    ON d.id_detectiv = idd.id_detectiv;


SELECT 
    d.id_detectiv, d.nume, d.prenume, 
    idd.id_investigatie, idd.rol
FROM Detectiv d
FULL JOIN Investigatie_Detectiv idd 
    ON d.id_detectiv = idd.id_detectiv;



-- - - - - - - - - - - - - - - - - - e)
--IN = verifica daca o valoare se afla intr-un set de valori sau intr-un rezultat de subquery

SELECT * FROM Detectiv d;
SELECT * FROM Investigatie i;
SELECT * FROM Investigatie_Detectiv id;


SELECT nume, prenume
FROM Detectiv
WHERE id_detectiv IN (
    SELECT id_detectiv
    FROM Investigatie_Detectiv
);


SELECT nume, prenume
FROM Detectiv
WHERE id_detectiv IN (
    SELECT id_detectiv
    FROM Investigatie_Detectiv
    WHERE id_investigatie IN (
        SELECT id_investigatie
        FROM Investigatie
        WHERE stare = 'Closed'
    )
);


-- - - - - - - - - - - - - - - - - - f)
--EXISTS verifica daca exista cel putin un rand in subinterogare 
SELECT d.nume, d.prenume
FROM Detectiv d
WHERE EXISTS (
    SELECT 1
    FROM Investigatie_Detectiv idd
    WHERE idd.id_detectiv = d.id_detectiv
);

SELECT p.descriere, p.data_gasire
FROM Proba p
WHERE EXISTS (
    SELECT 1
    FROM Investigatie i
    WHERE i.id_investigatie = p.id_investigatie
);


-- - - - - - - - - - - - - - - - - - g)
SELECT sub.id_investigatie, sub.nr_detectivi
FROM (
    SELECT id_investigatie, COUNT(id_detectiv) AS nr_detectivi
    FROM Investigatie_Detectiv
    GROUP BY id_investigatie
) AS sub;



SELECT sub.id_investigatie, sub.nr_detectivi
FROM (
    SELECT id_investigatie, COUNT(id_detectiv) AS nr_detectivi
    FROM Investigatie_Detectiv
    GROUP BY id_investigatie
) AS sub
WHERE sub.nr_detectivi > 1;



-- - - - - - - - - - - - - - - - - - h)
SELECT id_investigatie, COUNT(id_detectiv) AS nr_detectivi
FROM Investigatie_Detectiv
GROUP BY id_investigatie;

SELECT * FROM Investigatie_Detectiv ;

SELECT id_investigatie, COUNT(id_detectiv) AS nr_detectivi
FROM Investigatie_Detectiv
GROUP BY id_investigatie
HAVING COUNT(id_detectiv) > 1;

--Retine doar investigatiile care au mai multi detectivi decat media generala
SELECT id_investigatie, COUNT(id_detectiv) AS nr_detectivi
FROM Investigatie_Detectiv
GROUP BY id_investigatie
HAVING COUNT(id_detectiv) > (
    SELECT AVG(sub.nr)
    FROM (
        SELECT COUNT(id_detectiv) AS nr
        FROM Investigatie_Detectiv
        GROUP BY id_investigatie
    ) AS sub
);

SELECT id_tipinfractiune, 
       COUNT(*) AS nr_investigatii, --COUNT(*) numaram cate investigatii usnt pentru fiecare tip
       MIN(data_deschidere) AS prima_data, 
       MAX(data_deschidere) AS ultima_data
FROM Investigatie
GROUP BY id_tipinfractiune
HAVING COUNT(*) >= 1;


-- - - - - - - - - - - - - - - - - - i)
SELECT DISTINCT nume, prenume, id_echipa, id_echipa * 2 AS team_calc
FROM Detectiv
WHERE id_echipa > ANY (
    SELECT id_echipa 
    FROM Echipa
    WHERE data_infiintare < '2010-01-01'
)
AND rang IS NOT NULL
ORDER BY nume;



SELECT TOP 3 d.nume, d.prenume, COUNT(idd.id_investigatie)*5 AS scor
FROM Detectiv d
JOIN Investigatie_Detectiv idd ON d.id_detectiv = idd.id_detectiv
WHERE idd.id_investigatie = ALL (
    SELECT id_investigatie
    FROM Investigatie
    WHERE id_tipinfractiune = 1
)
GROUP BY d.id_detectiv, d.nume, d.prenume
ORDER BY scor DESC;
-- ALL = conditia se indeplineste doar daaca detectivul a lucrat in toate investigatiile din subquery



Select * from TipInfractiune ti;
SELECT * FROM Investigatie i ;


SELECT TOP 2 ti.denumire, COUNT(i.id_investigatie) AS nr_investigatii
FROM TipInfractiune ti
LEFT JOIN Investigatie i ON ti.id_tipinfractiune = i.id_tipinfractiune
WHERE ti.id_tipinfractiune NOT IN (
    SELECT id_tipinfractiune 
    FROM Investigatie 
    WHERE stare = 'Closed'
)
GROUP BY ti.denumire
ORDER BY nr_investigatii DESC;
-- NOT IN = selectam doar investigaiiile care NU sunt inchise




SELECT * FROM  Detectiv ;

SELECT DISTINCT
    id_detectiv, nume, prenume,
    id_echipa + 100 AS cod_extins
FROM Detectiv
WHERE id_echipa IN (
    SELECT id_echipa FROM Detectiv WHERE rang <> 'Commissioner'
)
AND NOT nume LIKE '%r%';
