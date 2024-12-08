 
     CREATE TABLE IF NOT EXISTS restaurants (
     id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(50) NOT NULL,  
     image_name VARCHAR(255),
     description VARCHAR(255) NOT NULL,
     price INT NOT NULL,
     capacity INT NOT NULL,
     postal_code VARCHAR(50) NOT NULL,
     address VARCHAR(255) NOT NULL,
     phone_number VARCHAR(50) NOT NULL,
     create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
     update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 );
 
    CREATE TABLE IF NOT EXISTS roles (
     id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(50) NOT NULL
 );
 
     CREATE TABLE IF NOT EXISTS categories (
     id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(50) NOT NULL
 );
 
 CREATE TABLE IF NOT EXISTS users (
     id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(50) NOT NULL,
     furigana VARCHAR(50) NOT NULL,
     email VARCHAR(255) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,    
     role_id INT NOT NULL, 
     enabled BOOLEAN NOT NULL,
     paid_flg CHAR(1) NOT NULL,
     credit_first_name CHAR(20),
     credit_last_name CHAR(20),
     credit_number CHAR(20) , 
     credit_year CHAR(2), 
     credit_month CHAR(2),
     credit_secure_code CHAR(4), 
     create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
     update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,    
     FOREIGN KEY (role_id) REFERENCES roles (id)
 );
 
   CREATE TABLE IF NOT EXISTS verification_tokens (
     id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     user_id INT NOT NULL UNIQUE,
     token VARCHAR(255) NOT NULL,        
     create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
     update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     FOREIGN KEY (user_id) REFERENCES users (id) 
 );
  
   CREATE TABLE IF NOT EXISTS reservations (
     id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     restaurant_id INT NOT NULL,
     user_id INT NOT NULL,
     reserve_date DATE NOT NULL,
     number_of_people INT NOT NULL,
     amount INT NOT NULL,
     create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
     update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     FOREIGN KEY (restaurant_id) REFERENCES restaurants (id),
     FOREIGN KEY (user_id) REFERENCES users (id)
 );
 
 
 CREATE TABLE IF NOT EXISTS reviews (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  restaurant_id INT NOT NULL,
  user_id INT NOT NULL,
  assessment INT NOT NULL,      
  text_comment VARCHAR(255) NOT NULL,        
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (restaurant_id) REFERENCES restaurants (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
 );
 
  CREATE TABLE IF NOT EXISTS favorites (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  restaurant_id INT NOT NULL,
  user_id INT NOT NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (restaurant_id) REFERENCES restaurants (id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  UNIQUE restaurantId_userId_index ( restaurant_id,user_id)
 );