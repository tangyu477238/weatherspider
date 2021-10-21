
-- 上级检测出现信号
drop view v_xinhao_track;

create view v_xinhao_track as

select
    k.symbol,af.biz_date,x.sj3 as loss_price,x.period,x.type as stype
from gupiao_xinhao x
         inner join gupiao_kline_15m k on k.biz_date = x.biz_date2 and k.symbol = x.symbol
         inner join gupiao_kline_15m bf on bf.biz_date = x.biz_date3 and bf.symbol = x.symbol  and bf.low>x.sj3
         inner join gupiao_kline_15m af on af.biz_date = x.biz_date and af.symbol = x.symbol  and af.low>x.sj3 and af.`close`>af.`open`
where x.type_name = 'ma' and x.period = 15  and  k.`close`>x.sj3 and k.low<x.sj3
  and left(x.biz_date,10) >= date_format(now(),'%Y-%m-%d')

;