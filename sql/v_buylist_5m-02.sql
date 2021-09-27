
-- 计算是否可购买
drop view v_buylist_5m;
create view v_buylist_5m as

select * from (
      select k.symbol,
             min(k.biz_date)    as biz_date,
             min(k.down_price2) as lossPrice2,
             min(k.down_price1) as lossPrice
      from gupiao_xinhao s
          inner join gupiao_kline_5m k
              on k.symbol = s.symbol
                     and k.before_date2 = s.biz_date2
                     and k.down_price2 < k.down_price1
                     and  k.yi_trend = 1
      where (s.sj4 < s.sj3 and s.sj3 < s.sj2 and s.sj2 > s.sj1)
        and s.type = 1
        and s.period = 5
        and s.biz_date >= DATE_FORMAT(ADDDATE(NOW(), 0), '%Y-%m-%d')
      group by k.symbol
      order by biz_date
) t