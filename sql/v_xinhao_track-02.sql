
-- 上级检测出现信号
drop view v_xinhao_track;

create view v_xinhao_track as

select
k.symbol,k.before_date as biz_date,k.lossPrice as loss_price,30 as period,1 as stype
from (

select
k.symbol,k.before_date,
k.before_date2,k.after_date,k.after_date2,
MIN(k.down_price1) as lossPrice
from gupiao_kline_30m k
inner join (
    select  MIN(biz_date) as biz_date  from (
        select c.biz_date from v_syn_max_bizdate b
        inner join biz_calendar c on c.biz_date <= b.biz_date and c.holiday = 1
        order by biz_date desc  LIMIT 0, 3 ) t
) b on  k.before_date >= b.biz_date
where  k.down_price1 > k.down_price2 and k.yi_trend = 0
-- and k.up_price1>k.up_price2 -- 超过前高 屏蔽
group by k.symbol,k.before_date,k.before_date2,k.after_date,k.after_date2
) k
inner join gupiao_xinhao x1 on  x1.type = 1 and x1.period = 30  and k.symbol = x1.symbol and k.before_date2 = x1.biz_date2   and ( x1.sj3 < x1.sj2 and x1.sj2 > x1.sj1)


;