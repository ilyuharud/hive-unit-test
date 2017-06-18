INSERT OVERWRITE TABLE ${SCHEME}city_info
SELECT
    c.city_name,
    count(DISTINCT sc.subs_key) AS cnt_subs
FROM
    ${SCHEME}subs_city AS sc
JOIN
    ${SCHEME}city AS c
ON
    sc.city_id = c.city_id
GROUP BY
    c.city_name;