SELECT DISTINCT COUNT(X.docid) FROM (
    SELECT *
    FROM frequency
    WHERE term = "transactions") Y
JOIN (
    SELECT *
    FROM frequency
    WHERE term = "world"
) X
ON X.docid = Y.docid;
