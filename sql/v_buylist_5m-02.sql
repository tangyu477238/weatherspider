
-- 计算是否可购买
drop view v_buylist_5m;
create view v_buylist_5m as


select
k.symbol,k.before_date,min(k.lossPrice) as lossPrice ,max(x2.biz_date) as biz_date, min(x2.price2) as lossPrice2 ,g.name
from (
select
k.symbol,k.before_date,k.lossPrice
from (

select
k.symbol,k.before_date,
k.before_date2,k.after_date,k.after_date2,
MIN(k.down_price1) as lossPrice
from gupiao_kline_30m k
inner join v_syn_max_bizdate b on  k.before_date >= date_add(b.biz_date, interval -2 day)
where  k.down_price1 > k.down_price2 and k.yi_trend = 0
-- and k.up_price1>k.up_price2 -- 超过前高 屏蔽
group by k.symbol,k.before_date,k.before_date2,k.after_date,k.after_date2
) k
inner join gupiao_xinhao x1 on  x1.type = 1 and x1.period = 30  and k.symbol = x1.symbol and k.before_date2 = x1.biz_date2   and ( x1.sj3 < x1.sj2 and x1.sj2 > x1.sj1)

) k
inner join gupiao_xinhao x2 on  x2.type = 1 and x2.period = 5   and k.symbol = x2.symbol and x2.biz_date2>k.before_date and (x2.sj4 < x2.sj3 and x2.sj3 < x2.sj2 and x2.sj2 > x2.sj1)
inner join gupiao g on g.symbol = k.symbol

group by k.symbol,k.before_date ,g.name


