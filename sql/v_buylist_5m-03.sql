
-- 计算是否可购买
drop view v_buylist_5m;
create view  v_buylist_5m as

select DISTINCT symbol,biz_date,lossPrice,`name`,period from (

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
-- inner join (
--     select x1.symbol
--     from gupiao_xinhao x1
--              inner join v_before_60m_time v on x1.biz_date = v.biz_date
--     where  x1.period = 60 and  x1.type_name = 'zjrc' and x1.sj1 = 0
-- ) x2 on  x3.symbol = x2.symbol

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
            order by biz_date desc  LIMIT 0, 2 ) t
    ) b on  x1.biz_date >= b.biz_date
    where x1.period = 60 and x1.type_name = 'zjrc' and ( x1.sj3 < x1.sj2 and x1.sj2 < x1.sj1)
) x1 on x1.symbol = x3.symbol and x3.biz_date >=x1.biz_date


union all
select k.* from (

                    select
                        k.symbol,af.biz_date,x.sj3 as lossPrice,g.name,x.period
                    from gupiao_xinhao x
                             inner join gupiao g on g.symbol = x.symbol
                             inner join gupiao_kline_15m k on k.biz_date = x.biz_date2 and k.symbol = x.symbol
                             inner join gupiao_kline_15m bf on bf.biz_date = x.biz_date3 and bf.symbol = x.symbol  and bf.low>x.sj3
                             inner join gupiao_kline_15m af on af.biz_date = x.biz_date and af.symbol = x.symbol  and af.low>x.sj3 and af.`close`>af.`open`
                    where x.type_name = 'ma' and x.period = 15  and  k.`close`>x.sj3 and k.low<x.sj3
                      and left(x.biz_date,10) >= date_format(now(),'%Y-%m-%d')

                ) k
where not exists (select m.biz_date,m.symbol,m.low from gupiao_kline_5m m where k.symbol = m.symbol and m.biz_date>k.biz_date and  m.low<k.lossPrice)

) t