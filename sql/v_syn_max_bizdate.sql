-- 查询同步的最大日期

drop view v_syn_max_bizdate;
create view v_syn_max_bizdate   as

select
    max(if(num = 1,biz_date,null)) b_biz_date,
    max(if(num = 2,biz_date,null)) biz_date,
    max(if(num = 3,biz_date,null)) biz_date2,
    max(if(num = 4,biz_date,null)) biz_date3,
    max(if(num = 5,biz_date,null)) biz_date4,
    max(if(num = 6,biz_date,null)) biz_date5,

    max(if(num = 7,biz_date,null)) biz_date6,
    max(if(num = 8,biz_date,null)) biz_date7,
    max(if(num = 9,biz_date,null)) biz_date8,
    max(if(num = 10,biz_date,null)) biz_date9,
    max(if(num = 11,biz_date,null)) biz_date10,

    max(if(num = 12,biz_date,null)) biz_date11,
    max(if(num = 13,biz_date,null)) biz_date12,
    max(if(num = 14,biz_date,null)) biz_date13,
    max(if(num = 15,biz_date,null)) biz_date14,
    max(if(num = 16,biz_date,null)) biz_date15,
    max(if(num = 17,biz_date,null)) biz_date16,
    max(if(num = 18,biz_date,null)) biz_date17,
    max(if(num = 19,biz_date,null)) biz_date18,
    max(if(num = 20,biz_date,null)) biz_date19,
    max(if(num = 21,biz_date,null)) biz_date20,
    max(if(num = 22,biz_date,null)) biz_date21
from (
     select
         biz_date,ROW_NUMBER() OVER(PARTITION BY c.holiday order by biz_date desc) AS num
     from biz_calendar  c
     where c.holiday = 1
       and c.biz_date <= (case
        when (date_format(now(),'%w')=0 or date_format(now(),'%w')= 6) then ADDDATE(CURDATE(),case when date_format(now(),'%w')=6  then 2  else 1 end )
        when date_format(now(),'%H:%i') < '09:25' then CURDATE() else ADDDATE(CURDATE(),case when date_format(now(),'%w')=5  then 3 when date_format(now(),'%w')=6  then 2  else 1 end )
        end)
 ) t