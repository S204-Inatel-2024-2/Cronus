CREATE DATABASE IF NOT EXISTS cronus;
use cronus;

CREATE TABLE IF NOT EXISTS usuario (
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  nickname VARCHAR(255),
  email VARCHAR(255),
  password VARCHAR(256) -- Consider a larger size for password hashing
);

select * from usuario;