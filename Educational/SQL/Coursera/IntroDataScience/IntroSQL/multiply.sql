-- Express A X B as a SQL query

SELECT v FROM (
    SELECT A.row_num as r, B.col_num as c, SUM(A.value * B.value) as v
    FROM A, B
    WHERE A.col_num = B.row_num
    GROUP BY A.row_num, B.col_num
) X
where r = 2 AND c = 3;
