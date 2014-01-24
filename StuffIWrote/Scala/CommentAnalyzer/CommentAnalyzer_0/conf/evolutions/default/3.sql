# add sentiment values to video comments

# --- !Ups

ALTER TABLE IF EXISTS comments ADD sentimentValue float;

# --- !Downs
ALTER TABLE IF EXISTS comments DROP IF EXISTS sentimentValue;
