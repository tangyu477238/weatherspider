
-- 计算是否可购买
drop view v_buylist_5m;
create view  v_buylist_5m as
select k.* from (
    select
        k.symbol,min(k.loss_price) as lossPrice ,max(x2.biz_date) as biz_date, min(x2.price2) as lossPrice2 ,g.name,k.biz_date as biz_date2
    from v_xinhao_track k
             inner join gupiao_xinhao x2 on  x2.type = 1 and x2.period = 5   and k.symbol = x2.symbol
        and x2.biz_date2>k.biz_date and (x2.sj4 < x2.sj3 and x2.sj3 < x2.sj2 and x2.sj2 > x2.sj1)
             inner join gupiao g on g.symbol = k.symbol
    where x2.price2>=k.loss_price
    group by k.symbol,k.biz_date ,g.name

) k
where not exists (select m.biz_date,m.symbol,m.low from gupiao_kline_5m m where k.symbol = m.symbol and m.biz_date>k.biz_date and  m.low<k.lossPrice)
order by biz_date desc
;

