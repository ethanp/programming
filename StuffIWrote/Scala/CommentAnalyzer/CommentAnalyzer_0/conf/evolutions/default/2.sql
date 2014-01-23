# Add comments and

# --- !Ups

CREATE TABLE comments (
  id VARCHAR UNIQUE,
  text VARCHAR,
  published DATE,
  numReplies INT,
  videos_id VARCHAR,
  CONSTRAINT FK_videos_id
    FOREIGN KEY(videos_id)
    REFERENCES videos(id)
    MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

-- # --- !Downs
DROP TABLE IF EXISTS comments
