
-- 计算是否可购买
drop view v_buylist_5m;
create view  v_buylist_5m as


select DISTINCT x3.symbol,x3.biz_date,x3.price3  as lossPrice,g.name
from gupiao_xinhao x1
-- inner join v_before_120m_time v on x1.biz_date >= v.biz_date
inner join gupiao_xinhao x3 on x3.period = 5 and x3.symbol = x1.symbol
inner join gupiao g on g.symbol = x1.symbol
	and ( x3.sj5 >0 and x3.sj4 < x3.sj3 and x3.sj3 > x3.sj2 and x3.sj2 > x3.sj1)
inner join gupiao_xinhao x2 on x2.period = 30 and x2.symbol = x1.symbol and x2.sj1 = 0
where x1.period = 15 and ( x1.sj3 < x1.sj2 and x1.sj2 < x1.sj1)

and x1.biz_date >='2021-10-14 15:00'
and x2.biz_date ='2021-10-14 15:00'
and x3.biz_date >'2021-10-14 15:00'

order by biz_date desc




select DISTINCT x3.symbol,x3.biz_date,x3.price3  as lossPrice,g.name
from gupiao_xinhao x1
inner join gupiao g on g.symbol = x1.symbol
-- inner join v_before_120m_time v on x1.biz_date >= v.biz_date
inner join gupiao_xinhao x3 on x3.period = 15 and x3.symbol = x1.symbol
	and ( x3.sj5 >0 and x3.sj4 < x3.sj3 and x3.sj3 > x3.sj2 and x3.sj2 > x3.sj1)
inner join gupiao_xinhao x2 on x2.period = 60 and x2.symbol = x1.symbol and x2.sj1 = 0
where x1.period = 30 and ( x1.sj3 < x1.sj2 and x1.sj2 < x1.sj1)

and x1.biz_date >='2021-10-14 15:00'
and x2.biz_date ='2021-10-14 15:00'
and x3.biz_date >'2021-10-14 15:00'

order by biz_date desc



select DISTINCT x3.symbol,x3.biz_date,x3.price3  as lossPrice,g.name
from gupiao_xinhao x1
inner join gupiao g on g.symbol = x1.symbol
-- inner join v_before_120m_time v on x1.biz_date >= v.biz_date
inner join gupiao_xinhao x3 on x3.period = 30 and x3.symbol = x1.symbol
	and ( x3.sj5 >0 and x3.sj4 < x3.sj3 and x3.sj3 > x3.sj2 and x3.sj2 > x3.sj1)
inner join gupiao_xinhao x2 on x2.period = 120 and x2.symbol = x1.symbol and x2.sj1 = 0
where x1.period = 60 and ( x1.sj3 < x1.sj2 and x1.sj2 < x1.sj1)

and x1.biz_date >='2021-10-12 15:00'
and x2.biz_date ='2021-10-12 15:00'
and x3.biz_date >'2021-10-12 15:00'

order by biz_date desc


