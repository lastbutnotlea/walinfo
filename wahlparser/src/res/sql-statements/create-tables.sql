CREATE TABLE Bundeslaender (
  kuerzel VARCHAR(2) PRIMARY KEY,
  name VARCHAR(22)
);

CREATE TABLE Parteien (
  id SMALLINT PRIMARY KEY,
  kuerzel VARCHAR(20),
  name VARCHAR(93),
  farbe VARCHAR(31),
  wahljahr SMALLINT
);

CREATE TABLE Wahlkreise (
  nummer SMALLINT PRIMARY KEY,
  name VARCHAR(82),
  bundesland VARCHAR(2) REFERENCES Bundeslaender(kuerzel),
  wahljahr SMALLINT
);

CREATE TABLE Wahlberechtigte (
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(nummer),
  anzahl_wahlberechtigte SMALLINT
);

CREATE TABLE Kandidaten (
  id SMALLINT PRIMARY KEY,
  name VARCHAR(31),
  vorname VARCHAR(54),
  titel VARCHAR(15),
  namenszusatz VARCHAR(19),
  geburtsjahr SMALLINT,
  beruf VARCHAR(69),
  geschlecht VARCHAR(1),
  partei_id SMALLINT REFERENCES Parteien(id),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(nummer),
  wahljahr SMALLINT
);

CREATE TABLE Listenplaetze (
  kandidaten_id SMALLINT REFERENCES Kandidaten(id) PRIMARY KEY,
  bundesland VARCHAR(2) REFERENCES Bundeslaender(kuerzel),
  listenplatz SMALLINT
);

CREATE TABLE Erststimmen (
  kandidaten_id SMALLINT,-- REFERENCES Kandidaten(id),
  wahlkreis_id SMALLINT-- REFERENCES Wahlkreise(nummer),
);

CREATE TABLE Zweitstimmen (
  partei_id SMALLINT, --REFERENCES Parteien(id),
  wahlkreis_id SMALLINT -- REFERENCES Wahlkreise(nummer),
);

CREATE TABLE Erststimmenergebnisse (
  kandidaten_id SMALLINT REFERENCES Kandidaten(id),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(nummer),
  anzahl INT,
  PRIMARY KEY (wahlkreis_id, kandidaten_id)
);

CREATE TABLE Zweitstimmenergebnisse (
  partei_id SMALLINT REFERENCES Parteien(id),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(nummer),
  anzahl INT,
  PRIMARY KEY (wahlkreis_id, partei_id)
);

CREATE TABLE Wahltoken (
  token VARCHAR(127) PRIMARY KEY,
  benutzt VARCHAR(1),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(nummer)
);