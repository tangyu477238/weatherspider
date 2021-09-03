
-- 计算是否可购买
drop view v_buylist_5m;
create view v_buylist_5m as

select
    symbol, MIN(lossPrice) as lossPrice
from (

-- 前3
         select
             base.lossPrice,s.price3 as nowPrice,s.*
         from gupiao_xinhao s
                  inner join (
             select t.symbol,
                    substring_index(group_concat( loss_Price order by t.biz_date desc ),',',1) as lossPrice,
                    substring_index(group_concat( biz_date  order by t.biz_date desc ),',',1) as biz_date
             from  v_xinhao_track t
             group by t.symbol
         ) base on s.biz_date>=base.biz_date and base.symbol = s.symbol
         where s.sj4<s.sj3 and s.sj3>s.sj2 and s.sj2>s.sj1 and s.type = 1 and s.period = 5  and base.lossPrice<=s.price3
           -- and s.biz_date = CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d %H:'),FLOOR(DATE_FORMAT(NOW(),'%i')/5)*5)
         union all
-- 前4
         select
             base.lossPrice,s.price4 as nowPrice,s.*
         from gupiao_xinhao s
                  inner join (
             select t.symbol,
                    substring_index(group_concat( loss_Price order by t.biz_date desc ),',',1) as lossPrice,
                    substring_index(group_concat( biz_date  order by t.biz_date desc ),',',1) as biz_date
             from  v_xinhao_track t
             group by t.symbol
         ) base on s.biz_date>=base.biz_date and base.symbol = s.symbol
         where s.sj5<s.sj4 and s.sj4>s.sj3 and s.sj3>s.sj2 and s.sj2>=s.sj1 and s.type = 1 and s.period = 5  and base.lossPrice<=s.price4

         union all

-- 前5
         select
             base.lossPrice,s.price5 as nowPrice,s.*
         from gupiao_xinhao s
                  inner join (
             select t.symbol,
                    substring_index(group_concat( loss_Price order by t.biz_date desc ),',',1) as lossPrice,
                    substring_index(group_concat( biz_date  order by t.biz_date desc ),',',1) as biz_date
             from  v_xinhao_track t
             group by t.symbol
         ) base on s.biz_date>=base.biz_date and base.symbol = s.symbol
         where s.sj6<s.sj5 and s.sj5>s.sj4 and s.sj4>s.sj3 and s.sj3>=s.sj2 and s.sj2>=s.sj1   and s.type = 1 and s.period = 5  and base.lossPrice<=s.price5

         union all
-- 前6
         select
             base.lossPrice,s.price6 as nowPrice,s.*
         from gupiao_xinhao s
                  inner join (
             select t.symbol,
                    substring_index(group_concat( loss_Price order by t.biz_date desc ),',',1) as lossPrice,
                    substring_index(group_concat( biz_date  order by t.biz_date desc ),',',1) as biz_date
             from  v_xinhao_track t
             group by t.symbol
         ) base on s.biz_date>=base.biz_date and base.symbol = s.symbol
         where s.sj7<s.sj6 and s.sj6>s.sj5 and s.sj5>s.sj4 and s.sj4>=s.sj3 and s.sj3>=s.sj2  and s.sj2>=s.sj1 and s.type = 1 and s.period = 5  and base.lossPrice<=s.price6

) t
group by symbol
