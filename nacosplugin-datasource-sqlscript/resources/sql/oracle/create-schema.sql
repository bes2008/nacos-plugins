-- execute this scripts using sqlplus

-- create schema
DROP USER IF EXISTS C##NACOS CASCADE;
CREATE USER IF NOT EXISTS C##NACOS IDENTIFIED BY NACOS_PASSWORD;
GRANT ALL PRIVILEGES TO C##NACOS;


exit