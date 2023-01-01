DROP TABLE `finance`;
DROP TABLE `savings`;
DROP TABLE `authorities`;
DROP TABLE `users`;

CREATE TABLE `authorities` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `user_id` INT NOT NULL,
   `authority` VARCHAR(50) NOT NULL,

   PRIMARY KEY (`id`),
   UNIQUE KEY `authorities_idx_1` (`user_id`, `authority`),
   CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

INSERT INTO `authorities`(`user_id`, `authority`) VALUES (1, "ROLE_USER");

INSERT INTO `authorities`(`user_id`, `authority`) VALUES (1, "ROLE_ADMIN");

CREATE TABLE `users` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(255) NOT NULL,
    `last_name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `birth_day` DATETIME,
    `password` VARCHAR(70) NOT NULL,
    `created` DATETIME NOT NULL,
    `enabled` BOOLEAN DEFAULT TRUE,
    PRIMARY KEY(`id`)
);

INSERT INTO `users`(`first_name`, `last_name`, `email`, `birth_day`, `password`, `created`, `enabled`)
VALUES ("Marcis", "Andersons", "marchel0925.dev@gmail.com", "2000-09-25 00:00:00", "$2a$12$fYTg17Yqw0KwlQO8375ynusX6MPiJi0zKFi16RcMCLw3P7tGHAWJa", NOW(), TRUE);

CREATE TABLE `finance` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `description` VARCHAR(500) DEFAULT "",
   `amount` DOUBLE DEFAULT 0.0,
   `created` DATETIME NOT NULL,
   `user_id` INT NOT NULL,
   PRIMARY KEY(`id`),
   CONSTRAINT `fk_user_finance` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

CREATE TABLE `savings` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `user_id` INT NOT NULL,
   `place` VARCHAR(500) DEFAULT "",
   `amount` DOUBLE DEFAULT 0.0,
   `created` DATETIME NOT NULL,
   PRIMARY KEY(`id`),
   CONSTRAINT `fk_user_savings` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

INSERT INTO `finance` (`description`, `amount`, `created`, `user_id`)
VALUES ("Nintendo Switch OLED", 400.0, NOW(), 1);

INSERT INTO `finance` (`description`, `amount`, `created`, `user_id`)
VALUES ("The Legend Of Zelda", 64.0, NOW(), 1);

INSERT INTO `finance` (`description`, `amount`, `created`, `user_id`)
VALUES ("Mario Kart 8 Deluxe", 64.0, NOW(), 1);

INSERT INTO `savings` (`place`, `amount`, `created`, `user_id`)
VALUES ("Bank Account", 300.0, NOW(), 1);

ALTER TABLE `finance` ADD `spent_at` DATETIME DEFAULT NULL;

UPDATE `finance`
SET `spent_at` = NOW()
WHERE `id` = 1;

UPDATE `finance`
SET `spent_at` = NOW()
WHERE `id` = 2;

