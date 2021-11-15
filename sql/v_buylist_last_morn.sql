


drop view v_buylist_last_morn;
create view v_buylist_last_morn as



drop view v_buylist_last_morn;
create view v_buylist_last_morn as

select
    ROUND(c.amount/zjCount/zjPrice,-1) as num,c.account,m.*
from (
     select
         k.symbol,g.name,k.low as lossPrice,k.`close` as zjPrice
     from gupiao_kline k
      inner join v_syn_max_bizdate v on v.biz_date = k.biz_date
      inner join gupiao g on g.symbol = k.symbol
     where k.percent>-2 and  k.percent<2 and k.symbol like '11%'
 ) m
 inner join (
    select SUM(zjPrice) as total,COUNT(1) as zjCount from (
      select
          k.symbol,g.name,k.low as lossPrice,k.`close` as zjPrice
      from gupiao_kline k
               inner join v_syn_max_bizdate v on v.biz_date = k.biz_date
               inner join gupiao g on g.symbol = k.symbol
      where k.percent>-2 and  k.percent<2 and k.symbol like '11%'
    ) x
) t on 1 =1
inner join biz_buy_amount c  on 1 = 1
