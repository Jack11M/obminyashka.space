DROP SCHEMA IF EXISTS `evo_exchange`;

CREATE SCHEMA IF NOT EXISTS `evo_exchange` DEFAULT CHARACTER SET utf8;

USE `evo_exchange`;

CREATE TABLE IF NOT EXISTS evo_exchange.users(
    id BIGINT AUTO_INCREMENT NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(300) NOT NULL,
    email VARCHAR(130) NOT NULL,
    first_name VARCHAR(50) NULL,
    last_name VARCHAR(50) NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) DEFAULT 'ACTIVE' NOT NULL,
  CONSTRAINT PK_USERS PRIMARY KEY (id), UNIQUE (email), UNIQUE (id), UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS evo_exchange.roles (
	id BIGINT AUTO_INCREMENT NOT NULL,
	name VARCHAR(45) NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) DEFAULT 'ACTIVE' NOT NULL,
  CONSTRAINT PK_ROLES PRIMARY KEY (id), UNIQUE (name), UNIQUE (id)
  );

CREATE TABLE IF NOT EXISTS evo_exchange.user_roles (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
  CONSTRAINT PK_USER_ROLES PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.user_roles
ADD CONSTRAINT fk_user_roles_to_user_id
    FOREIGN KEY (user_id)
    REFERENCES evo_exchange.users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.locations (
    id BIGINT AUTO_INCREMENT NOT NULL,
    city VARCHAR(100) NULL,
    district VARCHAR(100) NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) DEFAULT 'ACTIVE' NOT NULL,
  CONSTRAINT PK_LOCATIONS PRIMARY KEY (id), UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS evo_exchange.user_locations (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
  CONSTRAINT PK_USER_LOCATIONS PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.user_locations
ADD CONSTRAINT fk_user_location_to_user_id
    FOREIGN KEY (user_id)
    REFERENCES evo_exchange.users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.user_locations
ADD CONSTRAINT fk_user_location_to_location_id
    FOREIGN KEY (location_id)
    REFERENCES evo_exchange.locations (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.deals (
    id BIGINT AUTO_INCREMENT NOT NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    deal_status VARCHAR(45) NOT NULL,
  CONSTRAINT PK_DEALS PRIMARY KEY (id), UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS evo_exchange.user_deals (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    deal_id BIGINT NOT NULL,
  CONSTRAINT PK_USER_DEALS PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.user_deals
ADD CONSTRAINT fk_user_deal_to_user_id
    FOREIGN KEY (user_id)
    REFERENCES evo_exchange.users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.user_deals
ADD CONSTRAINT fk_user_deal_to_deal_id
    FOREIGN KEY (deal_id)
    REFERENCES evo_exchange.deals (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.advertisements (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NULL, product_id BIGINT NULL,
    topic VARCHAR(70) NULL,
    deal_type VARCHAR(45) NULL,
    is_favourite BIT(1) DEFAULT 0 NULL,
    description VARCHAR(2000) NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) NOT NULL,
  CONSTRAINT PK_ADVERTISEMENTS PRIMARY KEY (id), UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS evo_exchange.user_advertisements (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    advertisement_id BIGINT NOT NULL,
  CONSTRAINT PK_USER_ADVERTISEMENTS PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.user_advertisements
ADD CONSTRAINT fk_user_advertisements_to_user_id
    FOREIGN KEY (user_id)
    REFERENCES evo_exchange.users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.user_advertisements
ADD CONSTRAINT fk_user_advertisements_to_advertisement_id
    FOREIGN KEY (advertisement_id)
    REFERENCES evo_exchange.advertisements (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.advertisement_locations (
    id BIGINT AUTO_INCREMENT NOT NULL,
    advertisement_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
  CONSTRAINT PK_ADVERTISEMENT_LOCATIONS PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.advertisement_locations
ADD CONSTRAINT fk_advertisement_location_to_advertisement_id
    FOREIGN KEY (advertisement_id)
    REFERENCES evo_exchange.advertisements (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.advertisement_locations
ADD CONSTRAINT fk_advertisement_location_to_location_id
    FOREIGN KEY (location_id)
    REFERENCES evo_exchange.locations (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.phones (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NULL,
    phone_number BIGINT NULL,
    `show` BIT(1) DEFAULT 1 NULL,
    default_phone BIT(1) DEFAULT 0 NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) DEFAULT 'ACTIVE' NOT NULL,
  CONSTRAINT PK_PHONES PRIMARY KEY (id), UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS evo_exchange.user_photos (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    resource_url VARCHAR(150) NULL,
    default_photo BIT(1) DEFAULT 0 NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) NOT NULL,
  CONSTRAINT PK_USER_PHOTOS PRIMARY KEY (id), UNIQUE (id), UNIQUE (resource_url)
);

CREATE TABLE IF NOT EXISTS evo_exchange.children (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    birth_date date NULL,
    sex VARCHAR(15) NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) NOT NULL,
  CONSTRAINT PK_CHILDREN PRIMARY KEY (id), UNIQUE (sex), UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS evo_exchange.advertisement_images (
    id BIGINT AUTO_INCREMENT NOT NULL,
    advertisement_id BIGINT NOT NULL,
    resource_url VARCHAR(150) NULL,
    default_photo BIT(1) DEFAULT 0 NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) NOT NULL,
  CONSTRAINT PK_ADVERTISEMENT_IMAGES PRIMARY KEY (id), UNIQUE (id), UNIQUE (resource_url)
);

ALTER TABLE evo_exchange.phones
ADD CONSTRAINT fk_phones_to_user_id
    FOREIGN KEY (user_id)
    REFERENCES evo_exchange.users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.user_photos
ADD CONSTRAINT fk_user_photos_to_user_id
    FOREIGN KEY (user_id)
    REFERENCES evo_exchange.users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.children
ADD CONSTRAINT fk_children_to_user_id
    FOREIGN KEY (user_id)
    REFERENCES evo_exchange.users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.advertisement_images
ADD CONSTRAINT fk_advertisement_images_to_advertisement_id
    FOREIGN KEY (advertisement_id)
    REFERENCES evo_exchange.advertisements (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.products_to_exchange (
    id BIGINT AUTO_INCREMENT NOT NULL,
    advertisement_id BIGINT NOT NULL,
    name VARCHAR(50) DEFAULT 'Your proposition' NOT NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) NOT NULL,
    CONSTRAINT PK_PRODUCTS_TO_EXCHANGE PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.products_to_exchange
ADD CONSTRAINT fk_products_to_exchange_to_advertisement_id
    FOREIGN KEY (advertisement_id)
    REFERENCES evo_exchange.advertisements (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

INSERT INTO evo_exchange.roles (name) VALUES ('ROLE_USER');

INSERT INTO evo_exchange.roles (name) VALUES ('ROLE_ADMIN');

CREATE TABLE IF NOT EXISTS evo_exchange.products (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NULL,
    advertisement_id BIGINT NULL,
    subcategory_id BIGINT NULL,
    age VARCHAR(50) NULL,
    gender VARCHAR(50) NULL,
    size VARCHAR(50) NULL,
    season VARCHAR(50) NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) DEFAULT 'ACTIVE' NOT NULL,
    CONSTRAINT PK_PRODUCTS PRIMARY KEY (id), UNIQUE (id)
);

CREATE TABLE evo_exchange.subcategories (
    id BIGINT AUTO_INCREMENT NOT NULL,
    category_id BIGINT NULL,
    name VARCHAR(50) NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) DEFAULT 'ACTIVE' NOT NULL,
  CONSTRAINT PK_SUBCATEGORIES PRIMARY KEY (id), UNIQUE (id)
);

CREATE TABLE evo_exchange.categories (
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) DEFAULT 'ACTIVE' NOT NULL,
  CONSTRAINT PK_CATEGORIES PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.products
ADD CONSTRAINT fk_products_to_user_id
    FOREIGN KEY (user_id)
    REFERENCES evo_exchange.users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.products
ADD CONSTRAINT fk_products_to_subcategory_id
    FOREIGN KEY (subcategory_id)
    REFERENCES evo_exchange.subcategories (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.subcategories
ADD CONSTRAINT fk_subcategory_to_category_id
    FOREIGN KEY (category_id)
    REFERENCES evo_exchange.categories (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.products
ADD CONSTRAINT fk_products_to_advertisement_id
    FOREIGN KEY (advertisement_id)
    REFERENCES evo_exchange.advertisements (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE evo_exchange.chat_rooms (
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) DEFAULT 'ACTIVE' NOT NULL,
  CONSTRAINT PK_CHAT_ROOMS PRIMARY KEY (id), UNIQUE (id)
);

CREATE TABLE evo_exchange.user_chats (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    chat_id BIGINT NOT NULL,
  CONSTRAINT PK_USER_CHATS PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.user_chats
ADD CONSTRAINT fk_user_chats_to_chat_id
    FOREIGN KEY (chat_id)
    REFERENCES evo_exchange.chat_rooms (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.user_chats
ADD CONSTRAINT fk_user_chats_to_user_id
    FOREIGN KEY (user_id)
    REFERENCES evo_exchange.users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE evo_exchange.messages (
    id BIGINT AUTO_INCREMENT NOT NULL,
    chat_id BIGINT NULL,
    user_id BIGINT NULL,
    content VARCHAR(2000) NULL,
    `read` BIT(1) DEFAULT 0 NULL,
    message_time timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) DEFAULT 'ACTIVE' NOT NULL,
  CONSTRAINT PK_MESSAGES PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.messages
ADD CONSTRAINT fk_messages_to_user_id
    FOREIGN KEY (user_id)
    REFERENCES evo_exchange.users (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.messages
ADD CONSTRAINT fk_messages_to_chat_id
    FOREIGN KEY (chat_id)
    REFERENCES evo_exchange.chat_rooms (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

INSERT INTO evo_exchange.categories (name) VALUES ('Одежда');
INSERT INTO evo_exchange.categories (name) VALUES ('Обувь');
INSERT INTO evo_exchange.categories (name) VALUES ('Игрушки');
INSERT INTO evo_exchange.categories (name) VALUES ('Детская мебель');
INSERT INTO evo_exchange.categories (name) VALUES ('Транспорт для детей');
INSERT INTO evo_exchange.categories (name) VALUES ('Малыши до года');
INSERT INTO evo_exchange.categories (name) VALUES ('Книги');
INSERT INTO evo_exchange.categories (name) VALUES ('Другое');

ALTER TABLE evo_exchange.products
ADD category_id BIGINT NULL;

DELETE FROM evo_exchange.categories;

ALTER TABLE evo_exchange.products
ADD CONSTRAINT fk_products_to_category_id
    FOREIGN KEY (category_id)
    REFERENCES evo_exchange.categories (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;


