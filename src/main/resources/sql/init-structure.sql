DROP SCHEMA IF EXISTS `evo_exchange`;

CREATE SCHEMA IF NOT EXISTS `evo_exchange` DEFAULT CHARACTER SET utf8;

USE `evo_exchange`;

CREATE TABLE IF NOT EXISTS evo_exchange.user (
	id BIGINT AUTO_INCREMENT NOT NULL,
    role_id BIGINT NOT NULL,
    username VARCHAR(51) NOT NULL,
    password VARCHAR(300) NOT NULL,
    email VARCHAR(130) NOT NULL,
    first_name VARCHAR(50) NULL,
    last_name VARCHAR(50) NULL,
    avatar_image VARCHAR(50) NULL,
    hometown VARCHAR(100) NULL,
    on_line BIT(1) DEFAULT 0 NULL,
    last_online_time timestamp NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) DEFAULT 'ACTIVE' NOT NULL,
  CONSTRAINT PK_USER PRIMARY KEY (id), UNIQUE (id), UNIQUE (username), UNIQUE (email)
  );

CREATE TABLE IF NOT EXISTS evo_exchange.`role` (
	id BIGINT AUTO_INCREMENT NOT NULL,
	name VARCHAR(30) NULL,
  CONSTRAINT PK_ROLE PRIMARY KEY (id), UNIQUE (name), UNIQUE (id)
  );

ALTER TABLE evo_exchange.user
ADD CONSTRAINT fk_user_to_role_id
	FOREIGN KEY (role_id)
    REFERENCES evo_exchange.`role` (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

INSERT INTO evo_exchange.`role` (name) VALUES ('ROLE_USER');
INSERT INTO evo_exchange.`role` (name) VALUES ('ROLE_ADMIN');
INSERT INTO evo_exchange.`role` (name) VALUES ('ROLE_MODERATOR');

CREATE TABLE IF NOT EXISTS evo_exchange.phone (
	id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NULL,
    phone_number BIGINT NULL, `show` BIT(1) DEFAULT 1 NULL,
    default_phone BIT(1) DEFAULT 0 NULL,
  CONSTRAINT PK_PHONE PRIMARY KEY (id), UNIQUE (id)
  );

ALTER TABLE evo_exchange.phone
ADD CONSTRAINT fk_phone_to_user_id
	FOREIGN KEY (user_id)
	REFERENCES evo_exchange.user (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.child (
	id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    birth_date date NULL,
    sex VARCHAR(15) NULL,
  CONSTRAINT PK_CHILD PRIMARY KEY (id), UNIQUE (sex), UNIQUE (id)
);

ALTER TABLE evo_exchange.child
ADD CONSTRAINT fk_child_to_user_id
	FOREIGN KEY (user_id)
    REFERENCES evo_exchange.user (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.deal (
	id BIGINT AUTO_INCREMENT NOT NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) NULL, CONSTRAINT PK_DEAL PRIMARY KEY (id), UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS evo_exchange.user_deal (
	id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    deal_id BIGINT NOT NULL,
  CONSTRAINT PK_USER_DEAL PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.user_deal
ADD CONSTRAINT fk_user_deal_to_user_id
	FOREIGN KEY (user_id)
	REFERENCES evo_exchange.user (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.user_deal
ADD CONSTRAINT fk_user_deal_to_deal_id
	FOREIGN KEY (deal_id)
    REFERENCES evo_exchange.deal (id);

CREATE TABLE IF NOT EXISTS evo_exchange.advertisement (
	id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NULL,
    product_id BIGINT NULL,
    topic VARCHAR(70) NULL,
    deal_type VARCHAR(45) NULL,
    is_favourite BIT(1) DEFAULT 0 NULL,
    description VARCHAR(255) NULL,
    wishes_to_exchange VARCHAR(210) NULL,
    ready_for_offers BIT(1) DEFAULT 0 NULL,
    created timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated timestamp DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    status VARCHAR(45) NULL,
  CONSTRAINT PK_ADVERTISEMENT PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.advertisement
ADD CONSTRAINT fk_advertisement_to_user_id
	FOREIGN KEY (user_id)
    REFERENCES evo_exchange.user (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.location (
	id BIGINT AUTO_INCREMENT NOT NULL,
    advertisement_id BIGINT NULL,
    city VARCHAR(100) NULL,
    district VARCHAR(100) NULL,
  CONSTRAINT PK_LOCATION PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.location
ADD CONSTRAINT fk_location_to_advertisement_id
	FOREIGN KEY (advertisement_id)
    REFERENCES evo_exchange.advertisement (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.product (
	id BIGINT AUTO_INCREMENT NOT NULL,
    advertisement_id BIGINT NULL,
    subcategory_id BIGINT NULL,
    age VARCHAR(50) NULL,
    gender VARCHAR(50) NULL,
    size VARCHAR(50) NULL,
    season VARCHAR(50) NULL,
  CONSTRAINT PK_PRODUCT PRIMARY KEY (id), UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS evo_exchange.subcategory (
	id BIGINT AUTO_INCREMENT NOT NULL,
    category_id BIGINT NOT NULL,
    name VARCHAR(50) NULL,
  CONSTRAINT PK_SUBCATEGORY PRIMARY KEY (id), UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS evo_exchange.category (
	id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NULL,
  CONSTRAINT PK_CATEGORY PRIMARY KEY (id), UNIQUE (id)
);

ALTER TABLE evo_exchange.product
ADD CONSTRAINT fk_product_to_subcategory_id
	FOREIGN KEY (subcategory_id)
    REFERENCES evo_exchange.subcategory (id);

ALTER TABLE evo_exchange.subcategory
ADD CONSTRAINT fk_subcategory_to_category_id
	FOREIGN KEY (category_id)
    REFERENCES evo_exchange.category (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

ALTER TABLE evo_exchange.product
ADD CONSTRAINT fk_product_to_advertisement_id
	FOREIGN KEY (advertisement_id)
    REFERENCES evo_exchange.advertisement (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS evo_exchange.image (
	id BIGINT AUTO_INCREMENT NOT NULL,
    product_id BIGINT NOT NULL,
    resource_url VARCHAR(150) NULL,
    main_photo BIT(1) DEFAULT 0 NULL,
  CONSTRAINT PK_IMAGE PRIMARY KEY (id), UNIQUE (id), UNIQUE (resource_url)
);

 ALTER TABLE evo_exchange.image
 ADD CONSTRAINT fk_image_to_product_id
	FOREIGN KEY (product_id)
    REFERENCES evo_exchange.product (id)
    ON UPDATE RESTRICT
    ON DELETE CASCADE;
