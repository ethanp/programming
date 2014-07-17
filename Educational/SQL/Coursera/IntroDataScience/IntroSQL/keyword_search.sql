CREATE VIEW query_terms AS
SELECT 'q' as docid, 'washington' as term, 1 as count
UNION
SELECT 'q' as docid, 'taxes' as term, 1 as count
UNION
SELECT 'q' as docid, 'treasury' as term, 1 as count;

SELECT MAX(similarity) FROM (
SELECT f.docid, SUM(f.count * q.count) as similarity
FROM frequency f, query_terms q
WHERE f.term = q.term
GROUP BY f.docid, q.docid
) X;


-- SELECT A.docid, B.docid, SUM (A.count * B.count) as similarity
-- FROM frequency A, frequency B
-- WHERE A.term = B.term AND A.docid < B.docid
-- GROUP BY A.docid, B.docid;
