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
  id SMALLINT PRIMARY KEY ,
  nummer SMALLINT,
  name VARCHAR(127),
  anzahl_wahlberechtigte INT,
  bundesland VARCHAR(2) REFERENCES Bundeslaender(kuerzel),
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
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(id),
  wahljahr SMALLINT
);

CREATE TABLE Listenplaetze (
  kandidaten_id SMALLINT REFERENCES Kandidaten(id) PRIMARY KEY,
  bundesland VARCHAR(2) REFERENCES Bundeslaender(kuerzel),
  listenplatz SMALLINT
);

CREATE TABLE Wahlzettel (
  id INT PRIMARY KEY,
  partei_id SMALLINT REFERENCES Parteien(id),
  kandidaten_id SMALLINT REFERENCES Kandidaten(id),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(id)
  --gueltig VARCHAR(1)
);

CREATE TABLE Erststimmenergebnisse (
  kandidaten_id SMALLINT REFERENCES Kandidaten(id),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(id),
  anzahl INT,
  PRIMARY KEY (wahlkreis_id, kandidaten_id)
);

CREATE TABLE Zweitstimmenergebnisse (
  partei_id SMALLINT REFERENCES Parteien(id),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(id),
  anzahl INT,
  PRIMARY KEY (wahlkreis_id, partei_id)
);

CREATE TABLE Wahltoken (
  token VARCHAR(127) PRIMARY KEY,
  benutzt VARCHAR(1),
  wahlkreis_id SMALLINT REFERENCES Wahlkreise(id)
);

CREATE SEQUENCE Wahlzettel_SEQ;