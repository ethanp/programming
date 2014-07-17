SELECT COUNT(*) FROM (
    SELECT term FROM (
        SELECT *
        FROM frequency
        WHERE docid = "10398_txt_earn")
    WHERE count = 1
    UNION
    SELECT term FROM (
        SELECT *
        FROM frequency
        WHERE docid = "925_txt_trade")
    WHERE count = 1
) X;
