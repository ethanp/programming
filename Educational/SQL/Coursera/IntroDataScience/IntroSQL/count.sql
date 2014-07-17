-- count the number of documents containing the word "parliament"
SELECT DISTINCT count(*) FROM (
    SELECT docid
    FROM frequency
    WHERE term = "parliament"
) X;
