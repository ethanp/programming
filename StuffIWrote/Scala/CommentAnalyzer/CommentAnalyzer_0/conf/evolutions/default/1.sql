# --- !Ups

CREATE TABLE videos (
  id varchar,
  title varchar
);

# --- !Downs

DROP TABLE IF EXISTS videos;
