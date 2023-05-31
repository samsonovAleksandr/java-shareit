CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS requests (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
description VARCHAR (512) NOT NULL,
requestor_id BIGINT,
created TIMESTAMP WITHOUT TIME ZONE,
CONSTRAINT pk_requests PRIMARY KEY (id),
CONSTRAINT fk_request_user FOREIGN KEY (requestor_id) REFERENCES users (id)
);
CREATE TABLE IF NOT EXISTS items (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
name VARCHAR(255) NOT NULL,
description VARCHAR(512) NOT NULL,
is_available BOOLEAN NOT NULL,
owner_id BIGINT,
request_id BIGINT,
CONSTRAINT pk_item PRIMARY KEY (id),
CONSTRAINT fk_items_owner FOREIGN KEY (owner_id) REFERENCES users (id),
CONSTRAINT fk_items_request FOREIGN KEY (request_id) REFERENCES requests (id)
);
CREATE TABLE IF NOT EXISTS bookings (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
start_date TIMESTAMP WITHOUT TIME ZONE,
end_date TIMESTAMP WITHOUT TIME ZONE,
item_id BIGINT,
booker_id BIGINT,
status VARCHAR(64),
CONSTRAINT pk_bookings PRIMARY KEY (id),
CONSTRAINT fk_bookings_item FOREIGN KEY (item_id) REFERENCES items (id),
CONSTRAINT fk_bookings_user FOREIGN KEY (booker_id) REFERENCES users (id)
);
CREATE TABLE IF NOT EXISTS comments (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
text VARCHAR (512),
item_id BIGINT,
author_id BIGINT,
CONSTRAINT pk_comments PRIMARY KEY (id),
CONSTRAINT fk_comments_items FOREIGN KEY (item_id) REFERENCES items (id),
CONSTRAINT fk_comments_users FOREIGN KEY (author_id) REFERENCES users (id)
);

