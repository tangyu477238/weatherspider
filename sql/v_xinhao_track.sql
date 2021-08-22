
-- 上级检测出现信号
drop view v_xinhao_track;

create view v_xinhao_track as

select
s.symbol,30 as period,s.biz_date,s.price1 as loss_price, 1 as stype
from gupiao_xinhao s
inner join v_syn_max_bizdate v on s.biz_date > v.biz_date
where  (s.sj3<s.sj2 and s.sj2<s.sj1)  and s.type = 1 and s.period = 30  ;