-- πterm(σdocid=10398_txt_earn and count=1(frequency))

SELECT COUNT(*) FROM (
    SELECT term FROM (
        SELECT *
        FROM frequency
        WHERE docid = "10398_txt_earn")
    WHERE count = 1
) X;
