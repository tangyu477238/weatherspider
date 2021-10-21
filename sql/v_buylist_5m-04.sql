
-- 计算是否可购买
drop view v_buylist_5m;
create view  v_buylist_5m as


select
    k.symbol,af.biz_date,x.sj3,x.period
from gupiao_xinhao x
         inner join gupiao_kline_15m k on k.biz_date = x.biz_date2 and k.symbol = x.symbol
         inner join gupiao_kline_15m bf on bf.biz_date = x.biz_date3 and bf.symbol = x.symbol  and bf.low>x.sj3
         inner join gupiao_kline_15m af on af.biz_date = x.biz_date and af.symbol = x.symbol  and af.low>x.sj3 and af.`close`>af.`open`
where x.type_name = 'ma' and x.period = 15  and  k.`close`>x.sj3 and k.low<x.sj3
  and left(x.biz_date,10) >= date_format(now(),'%Y-%m-%d')
        order by af.biz_date desc



select
    k.symbol,af.biz_date,x.sj3,x.period
from gupiao_xinhao x
         inner join gupiao_kline_30m k on k.biz_date = x.biz_date2 and k.symbol = x.symbol
         inner join gupiao_kline_30m bf on bf.biz_date = x.biz_date3 and bf.symbol = x.symbol  and bf.low>x.sj3
         inner join gupiao_kline_30m af on af.biz_date = x.biz_date and af.symbol = x.symbol  and af.low>x.sj3 and af.`close`>af.`open`
where x.type_name = 'ma' and x.period = 30  and  k.`close`>x.sj3 and k.low<x.sj3
  and left(x.biz_date,10) >= date_format(now(),'%Y-%m-%d')
order by af.biz_date desc


select
    k.symbol,af.biz_date,x.sj3,x.period
from gupiao_xinhao x
         inner join gupiao_kline_60m k on k.biz_date = x.biz_date2 and k.symbol = x.symbol
         inner join gupiao_kline_60m bf on bf.biz_date = x.biz_date3 and bf.symbol = x.symbol  and bf.low>x.sj3
         inner join gupiao_kline_60m af on af.biz_date = x.biz_date and af.symbol = x.symbol  and af.low>x.sj3 and af.`close`>af.`open`
where x.type_name = 'ma' and x.period = 60  and  k.`close`>x.sj3 and k.low<x.sj3
  and left(af.biz_date,10) >= date_format(ADDDATE(NOW(), if(date_format(NOW(),'%w')=1,-3,-1)),'%Y-%m-%d')
order by af.biz_date desc


