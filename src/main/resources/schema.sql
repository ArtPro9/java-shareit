CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512),
  owner_id BIGINT NOT NULL,
  is_available BOOLEAN,
  CONSTRAINT pk_item PRIMARY KEY (id),
  CONSTRAINT fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  text VARCHAR(500) NOT NULL,
  author_id BIGINT NOT NULL,
  item_id BIGINT NOT NULL,
  created_ts TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT pk_comment PRIMARY KEY (id),
  CONSTRAINT fk_comments_to_users FOREIGN KEY(author_id) REFERENCES users(id),
  CONSTRAINT fk_comments_to_items FOREIGN KEY(item_id) REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_date TIMESTAMP WITHOUT TIME ZONE,
  end_date TIMESTAMP WITHOUT TIME ZONE,
  status VARCHAR(50),
  booker_id BIGINT NOT NULL,
  item_id BIGINT NOT NULL,
  CONSTRAINT pk_booking PRIMARY KEY (id),
  CONSTRAINT fk_bookings_to_users FOREIGN KEY(booker_id) REFERENCES users(id),
  CONSTRAINT fk_bookings_to_items FOREIGN KEY(item_id) REFERENCES items(id)
);