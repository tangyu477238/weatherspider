

drop view v_bizdate;
create view v_bizdate   as

select
    biz_date,
    max(if(rn = 2,bf_biz_date,null)) biz_date2,
    max(if(rn = 3,bf_biz_date,null)) biz_date3,
    max(if(rn = 4,bf_biz_date,null)) biz_date4,
    max(if(rn = 5,bf_biz_date,null)) biz_date5,
    max(if(rn = 6,bf_biz_date,null)) biz_date6,
    max(if(rn = 7,bf_biz_date,null)) biz_date7,
    max(if(rn = 8,bf_biz_date,null)) biz_date8,
    max(if(rn = 9,bf_biz_date,null)) biz_date9,
    max(if(rn = 10,bf_biz_date,null)) biz_date10,
    max(if(rn = 11,bf_biz_date,null)) biz_date11,
    max(if(rn = 12,bf_biz_date,null)) biz_date12,
    max(if(rn = 13,bf_biz_date,null)) biz_date13,
    max(if(rn = 14,bf_biz_date,null)) biz_date14,
    max(if(rn = 15,bf_biz_date,null)) biz_date15
from (
         SELECT
             c.biz_date,cc.biz_date as bf_biz_date,
             row_number() over(partition by c.biz_date order by cc.biz_date desc) as rn
         from biz_calendar c
                  inner join biz_calendar cc on cc.biz_date <=c.biz_date  and cc.holiday = 1
         where c.holiday = 1 and c.biz_date >date_add(curdate()-day(curdate())+1,interval -1 month) and  c.biz_date <=CURDATE()
) t
where t.rn < 16
group by biz_date
