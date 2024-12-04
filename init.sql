USE mysql;
CREATE USER 'ab'@'localhost' IDENTIFIED BY '123';
FLUSH PRIVILEGES;

GRANT ALL ON jcourse.* TO 'ab'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;

USE jcourse;

DROP TABLE IF EXISTS user;

CREATE TABLE IF NOT EXISTS user (
    id int NOT NULL AUTO_INCREMENT,
    email varchar(50) NOT NULL,
	phone varchar(12) NULL,
	passw varchar(20) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;    