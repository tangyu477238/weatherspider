

drop view v_buylist_ma;
create view v_buylist_ma as

select
    x.symbol,k.low as lossPrice,g.name
from gupiao_xinhao x
         inner join v_syn_max_bizdate v on v.biz_date = x.biz_date
         inner join gupiao g on g.symbol = x.symbol
         inner join gupiao_kline k on k.biz_date = x.biz_date and k.symbol = x.symbol and k.`close` > x.sj3
         left join gupiao_kline k1 on k1.symbol = x.symbol and k1.biz_date = v.biz_date
where x.type_name = 'ma' and x.period = 101 and x.sj2 < x.sj3 ;