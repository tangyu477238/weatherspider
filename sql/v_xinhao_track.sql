
-- 上级检测出现信号
drop view v_xinhao_track;

create view v_xinhao_track as

-- 小小大
select
    s.symbol,30 as period,s.biz_date,s.price1 as loss_price, 1 as stype
from gupiao_xinhao s
inner join v_syn_max_bizdate b on  s.biz_date >= date_add(b.biz_date, interval -1 day)
where  (s.sj3<s.sj2 and s.sj2<s.sj1)  and s.type = 1 and s.period = 30


union all

-- 大小大
select
    s.symbol,30 as period,if(s.price3<s.price1,s.biz_date3,s.biz_date) as biz_date,
       if(s.price3<s.price1,s.price3,s.price1) as loss_price, 1 as stype
from gupiao_xinhao s
inner join v_syn_max_bizdate b on  s.biz_date >= date_add(b.biz_date, interval -1 day)
where  (s.sj3>s.sj2 and s.sj2<s.sj1)  and s.type = 1 and s.period = 30


;