
-- 计算是否可购买
drop view v_buylist_5m;
create view v_buylist_5m as


select
    t.symbol,t.lossPrice,IFNULL(g.`name`,t.symbol) as name
from (
         select k.symbol,
                max(k.biz_date)    as biz_date,
                min(k.down_price2) as lossPrice2,
                min(k.down_price1) as lossPrice
         from gupiao_xinhao s
                  inner join gupiao_kline_5m k
                             on k.symbol = s.symbol
                                 and k.before_date2 = s.biz_date2
                                 and k.down_price2 < k.down_price1
                                 and k.up_price2 < k.up_price1
                                 and k.yi_trend = 0
         where (s.sj4 < s.sj3 and s.sj3 < s.sj2 and s.sj2 > s.sj1)
           and s.type = 1
           and s.period = 5
           and k.biz_date >= DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d')
         group by k.symbol
         order by biz_date desc
     ) t
         left join gupiao g on g.symbol = t.symbol
         inner join (
    select
        case
            when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<35 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:05')
            when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<40 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:10')
            when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<45 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:15')
            when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<50 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:20')
            when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<55 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:25')
            when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<60 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:30')

            when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<5 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:35')
            when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<10 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:40')
            when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<15 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:45')
            when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<20 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:50')
            when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<25 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 14:55')
            when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<30 then CONCAT(DATE_FORMAT(ADDDATE(NOW(), -1), '%Y-%m-%d'),' 15:00')
            else DATE_ADD(NOW(), INTERVAL -60 MINUTE)
            end as biz_date
    from v_syn_max_bizdate v

) m
where t.biz_date >= m.biz_date

