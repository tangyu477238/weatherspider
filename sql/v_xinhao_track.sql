
-- 上级检测出现信号
drop view v_xinhao_track;

create view v_xinhao_track as

select
    s.symbol,30 as period,s.biz_date,s.price1 as loss_price, 1 as stype
from gupiao_xinhao s
where  (s.sj3<s.sj2 and s.sj2<s.sj1)  and s.type = 1 and s.period = 30
  and s.biz_date >= DATE_FORMAT(ADDDATE(NOW(),-1),'%Y-%m-%d')

union all

select
    s.symbol,30 as period,s.biz_date,s.price1 as loss_price, 1 as stype
from gupiao_xinhao s
where  (s.sj5<s.sj4 and s.sj4<s.sj3 and s.sj3>s.sj2 and s.sj2<s.sj1)  and s.type = 1 and s.period = 30
  and s.biz_date >= DATE_FORMAT(ADDDATE(NOW(),-1),'%Y-%m-%d')
;