# Add comment replies table

# --- !Ups

CREATE TABLE comment_replies (
  id varchar UNIQUE,
  text varchar,
  published date,
  numReplies int,
  depth int,
  comments_id varchar,
  sentimentValue float
);

# --- !Downs
DROP TABLE IF EXISTS comment_replies
