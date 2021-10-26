-- 查询同步的最大日期

drop view v_syn_max_bizdate;
create view v_syn_max_bizdate   as

select
    max(if(num = 1,biz_date,null)) biz_date,
    max(if(num = 2,biz_date,null)) biz_date2,
    max(if(num = 3,biz_date,null)) biz_date3,
    max(if(num = 4,biz_date,null)) biz_date4,
    max(if(num = 5,biz_date,null)) biz_date5,
    max(if(num = 6,biz_date,null)) biz_date6,
    max(if(num = 7,biz_date,null)) biz_date7
from (

     select biz_date,ROW_NUMBER() OVER(PARTITION BY c.holiday order by biz_date desc) AS num
     from biz_calendar  c
     where c.holiday = 1
       and c.biz_date <= IF(date_format(now(),'%Y-%m-%d %H:%i') < date_format(now(),'%Y-%m-%d %09:%25'), date_format(ADDDATE(NOW(), -1),'%Y-%m-%d'), date_format(now(),'%Y-%m-%d'))


 ) t