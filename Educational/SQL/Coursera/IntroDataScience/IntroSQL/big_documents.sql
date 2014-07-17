-- find all documents that have more than 300 total terms, including duplicate terms
SELECT DISTINCT COUNT(docid) FROM (
    SELECT docid, SUM(count) as sum_terms
    FROM frequency
    GROUP BY docid
    HAVING SUM(count) > 300
) X;
