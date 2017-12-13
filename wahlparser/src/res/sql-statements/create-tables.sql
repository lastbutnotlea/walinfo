CREATE TABLE Bundeslaender (
  kuerzel VARCHAR(2) PRIMARY KEY,
  name    VARCHAR(22)
);

CREATE TABLE Parteien (
  id       SMALLINT PRIMARY KEY,
  kuerzel  VARCHAR(20),
  name     VARCHAR(93),
  farbe    VARCHAR(31),
  wahljahr SMALLINT
);

CREATE TABLE Wahlkreise (
  id                     SMALLINT PRIMARY KEY,
  nummer                 SMALLINT,
  name                   VARCHAR(82),
  bundesland             VARCHAR(2) REFERENCES Bundeslaender (kuerzel),
  anzahl_wahlberechtigte INT,
  wahljahr               SMALLINT
);

CREATE TABLE Kandidaten (
  id           SMALLINT PRIMARY KEY,
  name         VARCHAR(31),
  vorname      VARCHAR(54),
  titel        VARCHAR(15),
  namenszusatz VARCHAR(19),
  geburtsjahr  SMALLINT,
  beruf        VARCHAR(69),
  geschlecht   VARCHAR(1),
  partei_id    SMALLINT REFERENCES Parteien (id),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise (id),
  wahljahr     SMALLINT
);

CREATE TABLE Listenplaetze (
  kandidaten_id SMALLINT REFERENCES Kandidaten (id) PRIMARY KEY,
  bundesland    VARCHAR(2) REFERENCES Bundeslaender (kuerzel),
  partei_id     SMALLINT REFERENCES Parteien (id),
  listenplatz   SMALLINT
);

CREATE TABLE Erststimmen (
  kandidaten_id SMALLINT, -- REFERENCES Kandidaten(id),
  wahlkreis_id  SMALLINT-- REFERENCES Wahlkreise(nummer),
);

CREATE TABLE Zweitstimmen (
  partei_id    SMALLINT, --REFERENCES Parteien(id),
  wahlkreis_id SMALLINT -- REFERENCES Wahlkreise(nummer),
);

CREATE TABLE Erststimmenergebnisse (
  kandidaten_id SMALLINT REFERENCES Kandidaten (id),
  wahlkreis_id  SMALLINT REFERENCES Wahlkreise (id),
  anzahl        INT,
  UNIQUE (wahlkreis_id, kandidaten_id)
);

CREATE TABLE Zweitstimmenergebnisse (
  partei_id    SMALLINT REFERENCES Parteien (id),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise (id),
  anzahl       INT,
  UNIQUE (wahlkreis_id, partei_id)
);

-- Deutsche Bevölkerung
CREATE TABLE Dt_Bevoelkerung (
  bundesland VARCHAR(2), -- REFERENCES Bundeslaender(kuerzel),
  wahljahr   SMALLINT,
  anzahl     INT,
  PRIMARY KEY (bundesland, wahljahr)
);

INSERT INTO Dt_Bevoelkerung (bundesland, wahljahr, anzahl) VALUES
  -- 2013
  ('SH', 2013, 2686085), ('NW', 2013, 15895182),
  ('MV', 2013, 1585032), ('SN', 2013, 4005278),
  ('HH', 2013, 1559655), ('HE', 2013, 5388350),
  ('NI', 2013, 7354892), ('TH', 2013, 2154202),
  ('HB', 2013, 575805), ('RP', 2013, 3672888),
  ('BB', 2013, 2418267), ('BY', 2013, 11353264),
  ('ST', 2013, 2247673), ('BW', 2013, 9482902),
  ('BE', 2013, 3025288), ('SL', 2013, 919402),
  -- 2017
  ('SH', 2017, 2673803), ('NW', 2017, 15707569),
  ('MV', 2017, 1548400), ('SN', 2017, 3914671),
  ('HH', 2017, 1525090), ('HE', 2017, 5281198),
  ('NI', 2017, 7278789), ('TH', 2017, 2077901),
  ('HB', 2017, 568510), ('RP', 2017, 3661245),
  ('BB', 2017, 2391746), ('BY', 2017, 11362245),
  ('ST', 2017, 2145671), ('BW', 2017, 9365001),
  ('BE', 2017, 2975745), ('SL', 2017, 899748);

CREATE TABLE words (
  word_id INT,
  word VARCHAR(12)
);

CREATE TABLE tokens (
  token VARCHAR(130) UNIQUE,
  wahlkreis_nr SMALLINT,
  gueltig VARCHAR(1)
);