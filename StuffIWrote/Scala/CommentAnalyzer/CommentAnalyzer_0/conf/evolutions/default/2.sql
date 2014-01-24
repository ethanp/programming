# Add comments and

# --- !Ups

CREATE TABLE comments (
  id varchar UNIQUE,
  text varchar,
  published date,
  numReplies int,
  videos_id varchar CONSTRAINT FK_videos_id
    references videos(id)
    match simple
    on update cascade
    on delete cascade
);

# --- !Downs
DROP TABLE IF EXISTS comments
