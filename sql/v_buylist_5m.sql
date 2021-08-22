
-- 计算是否可购买
drop view v_buylist_5m;

create view v_buylist_5m as  

select 
base.lossPrice,s.price3 as nowPrice,s.*
from gupiao_xinhao s
inner join (
 select t.symbol,
 substring_index(group_concat( lossPrice order by t.biz_date desc ),',',1) as lossPrice,
 substring_index(group_concat( biz_date  order by t.biz_date desc ),',',1) as biz_date
 from (
  select 
  s.price1 as lossPrice,s.biz_date,s.symbol
  from gupiao_xinhao s
  where  (s.sj3<s.sj2 and s.sj2<s.sj1)  and s.type = 1 and s.period = 30  and  s.biz_date>DATE_FORMAT(NOW(),'%Y-%m-%d')
  order by s.symbol asc,biz_date asc
 ) t
 group by t.symbol
) base on s.biz_date>base.biz_date and base.symbol = s.symbol
where s.sj4<s.sj3 and s.sj3>s.sj2 and s.sj2>s.sj1 and s.type = 1 and s.period = 5  and base.lossPrice<=s.price3