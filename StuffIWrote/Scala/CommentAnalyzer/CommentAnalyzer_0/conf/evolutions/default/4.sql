# add reference to parent comment in comments as well as depth of comment

# --- !Ups

ALTER TABLE IF EXISTS comments ADD COLUMN comments_id varchar NULL
  CONSTRAINT REF_comments_id
  references comments(id)
  on UPDATE cascade
  on DELETE cascade,
ADD COLUMN depth int;

# --- !Downs
ALTER TABLE IF EXISTS comments DROP IF EXISTS comments_id;
ALTER TABLE IF EXISTS comments DROP IF EXISTS depth;
