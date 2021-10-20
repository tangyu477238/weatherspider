
-- 计算是否可购买
drop view v_buylist_5m;
create view  v_buylist_5m as


select
    x3.symbol, x3.biz_date, x3.price3  as lossPrice,g.name,x3.period
from gupiao_xinhao x3
         inner join gupiao g on g.symbol = x3.symbol and x3.period = 15 and x3.type_name = 'zjrc' and ( x3.sj5 >0 and x3.sj4 < x3.sj3 and x3.sj3 > x3.sj2 and x3.sj2 > x3.sj1)
         inner join (
    select
        x1.symbol,x1.biz_date
    from gupiao_xinhao x1
             inner join v_syn_max_bizdate v on x1.biz_date >= v.biz_date
    where x1.period = 30 and x1.type_name = 'zjrc' and ( x1.sj3 < x1.sj2 and x1.sj2 < x1.sj1)
) x1 on x1.symbol = x3.symbol and x3.biz_date >=x1.biz_date
inner join (
    select x1.symbol
    from gupiao_xinhao x1
             inner join v_before_60m_time v on x1.biz_date = v.biz_date
    where  x1.period = 60 and  x1.type_name = 'zjrc' and x1.sj1 = 0
) x2 on  x3.symbol = x2.symbol

union all

select
    x3.symbol,x3.biz_date,x3.price3  as lossPrice,g.name,x3.period
from gupiao_xinhao x3
         inner join gupiao g on g.symbol = x3.symbol and x3.period = 30 and x3.type_name = 'zjrc' and ( x3.sj5 >0 and x3.sj4 < x3.sj3 and x3.sj3 > x3.sj2 and x3.sj2 > x3.sj1)
         inner join (
    select
        x1.symbol,x1.biz_date
    from gupiao_xinhao x1
inner join (
    select  MIN(biz_date) as biz_date  from (
        select c.biz_date from v_syn_max_bizdate b
        inner join biz_calendar c on c.biz_date <= b.biz_date and c.holiday = 1
        order by biz_date desc  LIMIT 0, 5 ) t
) b on  x1.biz_date >= b.biz_date

    where x1.period = 60 and x1.type_name = 'zjrc' and ( x1.sj3 < x1.sj2 and x1.sj2 < x1.sj1)
) x1 on x1.symbol = x3.symbol and x3.biz_date >=x1.biz_date
