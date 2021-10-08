-- 查询同步的最大日期

drop view v_syn_max_bizdate;
create view v_syn_max_bizdate   as

select max(biz_date) as  biz_date from (

select MAX(c.biz_date) as biz_date 
from biz_calendar  c
inner join (
SELECT holiday from biz_calendar where biz_date = IF(date_format(now(),'%Y-%m-%d %H:%i:%s') < date_format(now(),'%Y-%m-%d %09:%25:%00'),
date_format(ADDDATE(NOW(), -1),'%Y-%m-%d'), date_format(now(),'%Y-%m-%d'))
) x on x.holiday = 1 
where c.holiday = 1 and c.biz_date = date_format(now(),'%Y-%m-%d') and date_format(now(),'%Y-%m-%d %H:%i:%s') > date_format(now(),'%Y-%m-%d %09:%25:%00') 

union all 

select MAX(c.biz_date) as biz_date from biz_calendar  c
inner join (
SELECT holiday from biz_calendar where biz_date = IF(date_format(now(),'%Y-%m-%d %H:%i:%s') < date_format(now(),'%Y-%m-%d %09:%25:%00'),
date_format(ADDDATE(NOW(), -1),'%Y-%m-%d'), date_format(now(),'%Y-%m-%d'))
) x on x.holiday = 1 
where c.holiday = 1 and c.biz_date < date_format(now(),'%Y-%m-%d') and date_format(now(),'%Y-%m-%d %H:%i:%s') < date_format(now(),'%Y-%m-%d %09:%25:%00')

union all 

select MAX(biz_date) as biz_date from biz_calendar c
inner join (
SELECT holiday from biz_calendar where biz_date = IF(date_format(now(),'%Y-%m-%d %H:%i:%s') < date_format(now(),'%Y-%m-%d %09:%25:%00'),
date_format(ADDDATE(NOW(), -1),'%Y-%m-%d'), date_format(now(),'%Y-%m-%d'))
) x on x.holiday = 0 
where biz_date < date_format(now(),'%Y-%m-%d') and c.holiday = 1

) t

