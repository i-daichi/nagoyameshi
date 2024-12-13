CREATE TABLE IF NOT EXISTS roles (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (50) NOT NULL
);

CREATE TABLE IF NOT EXISTS `weekdays` (
   `weekday_id` INT AUTO_INCREMENT PRIMARY KEY,
   `name` VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `restaurants` (
   `restaurant_id` int NOT NULL AUTO_INCREMENT,
   `name` varchar(50) NOT NULL,
   `image_name` varchar(255) DEFAULT NULL,
   `description` varchar(255) NOT NULL,
   `price` int NOT NULL,
   `seats` int NOT NULL,
   `postal_code` varchar(50) NOT NULL,
   `address` varchar(255) NOT NULL,
   `phone_number` varchar(50) NOT NULL,
   `category` varchar(50),
   `regular_holiday` varchar(50),
   `business_hours` varchar(50),
   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `opening_time` time DEFAULT NULL,
   `closing_time` time DEFAULT NULL,
   PRIMARY KEY (`restaurant_id`)
);

CREATE TABLE IF NOT EXISTS `restaurant_holidays` (
    `restaurant_holiday_id` INT AUTO_INCREMENT PRIMARY KEY,
    `restaurant_id` INT NOT NULL,  -- レストランのID（外部キー）
    `weekday_id` INT NOT NULL,     -- 曜日のID（外部キー）
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`restaurant_id`),
    FOREIGN KEY (`weekday_id`) REFERENCES `weekdays` (`weekday_id`),
    CONSTRAINT `unique_restaurant_weekday` UNIQUE (`restaurant_id`, `weekday_id`)  -- 同じ曜日の重複挿入を防ぐ
);

CREATE TABLE IF NOT EXISTS `genre` (
   `genre_id` int NOT NULL,
   `name` varchar(50) NOT NULL DEFAULT '',
   PRIMARY KEY (`genre_id`)
);

CREATE TABLE IF NOT EXISTS `categories` (
   `category_id` int NOT NULL AUTO_INCREMENT,
   `name` varchar(255) NOT NULL,
   `genre_id` int NOT NULL,
   PRIMARY KEY (`category_id`) USING BTREE,
   KEY `FK_categories_genre` (`genre_id`),
   CONSTRAINT `FK_categories_genre` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`genre_id`)
);

CREATE TABLE IF NOT EXISTS `restaurant_categories` (
   `restaurant_category_id` INT NOT NULL AUTO_INCREMENT,
   `restaurant_id` INT NULL DEFAULT NULL,
   `category_id` INT NULL DEFAULT NULL,
   PRIMARY KEY (`restaurant_category_id`) USING BTREE,
   UNIQUE INDEX `uq_restaurant_category` (`restaurant_id`, `category_id`) USING BTREE,
   INDEX `fk_category` (`category_id`) USING BTREE,
   CONSTRAINT `fk_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)  ,
   CONSTRAINT `fk_restaurant` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`restaurant_id`)
);

CREATE TABLE IF NOT EXISTS users (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (50) NOT NULL,
   furigana VARCHAR (50) NOT NULL,
   postal_code VARCHAR (50) NOT NULL,
   address VARCHAR (255) NOT NULL,
   phone_number VARCHAR (50) NOT NULL,
   email VARCHAR (255) NOT NULL UNIQUE,
   password VARCHAR (255) NOT NULL,
   role_id INT NOT NULL,
   enabled BOOLEAN NOT NULL,
   stripe_customer_id VARCHAR(255),
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS verification_tokens (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   user_id INT NOT NULL UNIQUE,
   token VARCHAR (255) NOT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS reservations (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   restaurant_id INT NOT NULL,
   user_id INT NOT NULL,
   number_of_people INT NOT NULL,
   reservation_date DATE NOT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (restaurant_id) REFERENCES restaurants (restaurant_id),
   FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS reviews (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   restaurant_id INT NOT NULL,
   user_id INT NOT NULL,
   score INT NOT NULL,
   content TEXT NOT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (restaurant_id) REFERENCES restaurants (restaurant_id),
   FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS favorites (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   restaurant_id INT NOT NULL,
   user_id INT NOT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   UNIQUE (restaurant_id, user_id),
   FOREIGN KEY (restaurant_id) REFERENCES restaurants (restaurant_id),
   FOREIGN KEY (user_id) REFERENCES users (id)
);
