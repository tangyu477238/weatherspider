

drop view v_big_buylist;

create view v_big_buylist as

select k.symbol,
       max(k.biz_date)    as biz_date,
       min(k.down_price2) as lossPrice2,
       min(k.down_price1) as lossPrice,
       min(k.up_price2) as upPrice2,
       min(k.up_price1) as upPrice
from gupiao_xinhao s
         inner join gupiao_kline_30m k
                    on k.symbol = s.symbol
                        and k.before_date2 = s.biz_date2
                        and k.down_price2 < k.down_price1
                        and k.up_price2 < k.up_price1
                        and k.yi_trend = 0
where (s.sj4 < s.sj3 and s.sj3 < s.sj2 and s.sj2 > s.sj1)
  and s.type = 1
  and s.period = 30
  and k.biz_date >= DATE_FORMAT(ADDDATE(NOW(), -10), '%Y-%m-%d')
group by k.symbol
union all
select k.symbol,
       max(k.biz_date)    as biz_date,
       min(k.down_price2) as lossPrice2,
       min(k.down_price1) as lossPrice,
       min(k.up_price2) as upPrice2,
       min(k.up_price1) as upPrice
from gupiao_xinhao s
         inner join gupiao_kline k
                    on k.symbol = s.symbol
                        and k.before_date2 = s.biz_date2
                        and k.down_price2 < k.down_price1
                        and k.up_price2 < k.up_price1
                        and k.yi_trend = 0
where (s.sj4 < s.sj3 and s.sj3 < s.sj2 and s.sj2 > s.sj1)
  and s.type = 1
  and s.period = 101
  and k.biz_date >= DATE_FORMAT(ADDDATE(NOW(), -10), '%Y-%m-%d')
group by k.symbol
