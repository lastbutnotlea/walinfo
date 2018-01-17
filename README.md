# Walinformationssystem

### SQL Dateien
Alle verwendeten SQL Anfragen wurden unter [SQL-Statements](wahlparser/src/res/sql-statements) entwickelt und entsprechen zum Großteil den finalen Queries.
Allerdings werden diese Dateien nicht ausgeführt. Die finalen [SQL-Statements](backend/src/main/java/sqlbuild) werden im Java Backend zusammen gebaut.

Das dynamische Erstellen der Queries ist aus performance Gründen erforderlich. In einer früheren Version haben wir mit Views und einem Flag in der Datenbank gearbeitet. Allerdings hat Postgres dann Anfragen nicht mehr optimiert bekommen und sie waren viel zu langsam.
