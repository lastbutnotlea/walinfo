-- INSERT STATEMENTS FÜR FARBEN + FIX UNICODE PROBLEM


UPDATE Parteien SET farbe = '#194094' WHERE kuerzel = 'CDU';
UPDATE Parteien SET farbe = '#0069C4' WHERE kuerzel = 'CSU';
UPDATE Parteien SET farbe = '#FFCC33' WHERE kuerzel = 'FDP';
UPDATE Parteien SET farbe = '#32A460' WHERE kuerzel = 'GRÜNE';
UPDATE Parteien SET farbe = '#36C3D5' WHERE kuerzel = 'AfD';
UPDATE Parteien SET farbe = '#CA57C7' WHERE kuerzel = 'DIE LINKE';
UPDATE Parteien SET farbe = '#CF0000' WHERE kuerzel = 'SPD';

-- fix unicode problem
UPDATE wahlkreise SET name = replace(name, '–', '-');
