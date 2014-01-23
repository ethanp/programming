# Add comments and update videos

# --- !Ups
ALTER TABLE videos ADD COLUMN dateLastRetrieved DATE;

CREATE TABLE comments (
  id VARCHAR,
  text VARCHAR,
  published DATE,
  numReplies INT,
  FOREIGN KEY (videos_id) REFERENCES videos(id)
);

# --- !Downs
ALTER TABLE videos DROP COLUMN dateLastRetrieved;

DROP TABLE IF EXISTS comments
