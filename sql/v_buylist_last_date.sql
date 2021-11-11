

drop view v_buylist_ma;
create view v_buylist_ma as

select
    k.symbol,g.name,k.low as lossPrice
from gupiao_kline k
         inner join v_syn_max_bizdate v on v.biz_date = k.biz_date
         inner join gupiao g on g.symbol = k.symbol
where k.percent>-2 and  k.percent<2 and k.symbol like '11%'