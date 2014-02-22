# make some comment depth not nullable

# --- !Ups

UPDATE comments SET depth = 0 where depth is NULL;
ALTER TABLE comments ALTER COLUMN depth SET NOT NULL;

# --- !Downs
ALTER TABLE comments ALTER COLUMN depth DROP NOT NULL;
