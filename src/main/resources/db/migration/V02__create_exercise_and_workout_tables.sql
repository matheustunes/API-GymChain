CREATE TABLE `exercise` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `muscle_group` VARCHAR(50) NOT NULL,
  `difficulty_level` VARCHAR(20),
  `video_url` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `workout` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `workout_date` DATE NOT NULL,
  `duration_minutes` INT NOT NULL,
  `notes` TEXT,
  `total_hp_earned` INT DEFAULT 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `workout_exercise` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `workout_id` BIGINT(20) NOT NULL,
  `exercise_id` BIGINT(20) NOT NULL,
  `sets` INT NOT NULL,
  `reps` VARCHAR(50) NOT NULL,
  `weight_kg` DECIMAL(5,2),
  `rest_seconds` INT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`workout_id`) REFERENCES `workout`(`id`),
  FOREIGN KEY (`exercise_id`) REFERENCES `exercise`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;