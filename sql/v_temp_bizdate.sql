
create view v_temp_bizdate as

SELECT c.biz_date,max(v.biz_date) bb_biz_date
from biz_calendar c
inner join biz_calendar v on v.biz_date < c.biz_date and v.holiday = 1 and c.holiday = 1
GROUP BY c.biz_date




