

drop view v_buylist_last_morn;
create view v_buylist_last_morn as
select
    ROUND(c.amount/zjCount/zjPrice,-1) as num,c.account,ROUND(c.amount/zjCount/zjPrice,-1)*zjPrice as am,m.*
from (
         select
             k.symbol,g.name,k.low as lossPrice,k.`close` as zjPrice
         from gupiao_kline k
                  inner join v_syn_max_bizdate v on v.biz_date = k.biz_date
                  inner join gupiao g on g.symbol = k.symbol
                  left join biz_buy_qiangshu q on g.symbol = q.symbol
         where k.percent>-2 and  k.percent<2 and k.symbol like '11%' and q.symbol is null
 ) m
inner join (
    select SUM(zjPrice) as total,COUNT(1) as zjCount from (
          select
              k.symbol,g.name,k.low as lossPrice,k.`close` as zjPrice
          from gupiao_kline k
                   inner join v_syn_max_bizdate v on v.biz_date = k.biz_date
                   inner join gupiao g on g.symbol = k.symbol
                   left join biz_buy_qiangshu q on g.symbol = q.symbol
          where k.percent>-2 and  k.percent<2 and k.symbol like '11%' and q.symbol is null
      ) x
) t on 1 =1
inner join biz_buy_amount c  on 1 = 1
order by am desc