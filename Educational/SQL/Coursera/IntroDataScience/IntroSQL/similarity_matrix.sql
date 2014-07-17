SELECT SUM (A.count * B.count) as similarity
FROM (
    SELECT *
    FROM frequency
    WHERE docid = '10080_txt_crude'
) A, (
    SELECT *
    FROM frequency
    WHERE docid = '17035_txt_earn'
) B
WHERE A.term = B.term
GROUP BY A.docid, B.docid;
