CREATE TABLE Bundeslaender (
  kuerzel VARCHAR(2) PRIMARY KEY,
  name VARCHAR(63)
);

CREATE TABLE Parteien (
  id SMALLINT PRIMARY KEY,
  kuerzel VARCHAR(63),
  name VARCHAR(127),
  farbe VARCHAR(31),
  wahljahr SMALLINT
);

CREATE TABLE Wahlkreise (
  nummer SMALLINT PRIMARY KEY,
  name VARCHAR(127),
  bundesland VARCHAR(2) REFERENCES Bundeslaender(kuerzel)
);

CREATE TABLE Wahlberechtigte (
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(nummer),
  anzahl_wahlberechtigte INT,
  wahljahr SMALLINT
);

CREATE TABLE Kandidaten (
  id SMALLINT PRIMARY KEY,
  name VARCHAR(63),
  vorname VARCHAR(63),
  titel VARCHAR(63),
  namenszusatz VARCHAR(63),
  geburtsjahr SMALLINT,
  beruf VARCHAR(127),
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
  wahlkreis_id SMALLINT,-- REFERENCES Wahlkreise(nummer),
  gueltig VARCHAR(1)
);

CREATE TABLE Zweitstimmen (
  partei_id SMALLINT REFERENCES Parteien(id),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(nummer),
  gueltig VARCHAR(1)
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