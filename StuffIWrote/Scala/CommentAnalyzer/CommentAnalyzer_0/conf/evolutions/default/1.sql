# --- !Ups

CREATE TABLE videos (
  id varchar UNIQUE,
  title varchar,
  dateLastRetrieved date
);

# --- !Downs

DROP TABLE IF EXISTS videos;
