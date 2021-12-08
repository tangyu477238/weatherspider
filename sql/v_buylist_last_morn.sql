


drop view v_buylist_last_morn;
create view v_buylist_last_morn as
select
    ROUND(c.amount/zjCount/zjPrice,-1) as num,c.account,ROUND(c.amount/zjCount/zjPrice,-1)*zjPrice as am,m.*
from (
     select
         k.symbol,k.name,k.low as lossPrice,k.`close` as zjPrice
     from gupiao_every k
              inner join v_syn_max_bizdate v on CONCAT(v.biz_date,' 14:58') = left(k.dividend_yield,16)
              left join biz_buy_qiangshu q on k.symbol = q.symbol
     where k.percent>-2 and  k.percent<2 and k.ps > 3 and k.amount>20000000
       and k.symbol like '11%' and q.symbol is null
 ) m
 inner join (
    select
       SUM(zjPrice) as total,COUNT(1) as zjCount from (
          select
              k.symbol,k.name,k.low as lossPrice,k.`close` as zjPrice,k.dividend_yield,k.ps
          from gupiao_every k
                   inner join v_syn_max_bizdate v on  CONCAT(v.biz_date,' 14:58') = left(k.dividend_yield,16)
                   left join biz_buy_qiangshu q on k.symbol = q.symbol
          where k.percent>-2 and  k.percent<2 and k.ps > 3 and k.amount>20000000
            and k.symbol like '11%' and q.symbol is null
      ) x
) t on 1 =1
         inner join biz_buy_amount c  on 1 = 1
order by am desc
