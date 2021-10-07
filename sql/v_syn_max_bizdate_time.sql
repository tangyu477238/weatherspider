-- 时间
drop view v_syn_max_bizdate_time;
create view v_syn_max_bizdate_time   as
select
	case
					when date_format(now(),'%Y-%m-%d %H:%i:%s') < date_format(now(),'%Y-%m-%d %09:%30:%00') then CONCAT(biz_date,' 14:00')

					when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<35 then CONCAT(biz_date,' 14:05')
					when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<35 then CONCAT(biz_date,' 14:05')
					when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<40 then CONCAT(biz_date,' 14:10')
					when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<45 then CONCAT(biz_date,' 14:15')
					when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<50 then CONCAT(biz_date,' 14:20')
					when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<55 then CONCAT(biz_date,' 14:25')
					when DATE_FORMAT(NOW(),'%H')=9 and DATE_FORMAT(NOW(),'%i')<60 then CONCAT(biz_date,' 14:30')

					when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<5 then CONCAT(biz_date,' 14:35')
					when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<10 then CONCAT(biz_date,' 14:40')
					when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<15 then CONCAT(biz_date,' 14:45')
					when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<20 then CONCAT(biz_date,' 14:50')
					when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<25 then CONCAT(biz_date,' 14:55')
					when DATE_FORMAT(NOW(),'%H')=10 and DATE_FORMAT(NOW(),'%i')<30 then CONCAT(biz_date,' 15:00')

					when DATE_FORMAT(NOW(),'%H')>14 then CONCAT(biz_date,' 14:00')

					else DATE_ADD(NOW(), INTERVAL -60 MINUTE)
			end as biz_date
from (

	select MAX(c.biz_date) as biz_date from biz_calendar c
	inner join (

			SELECT holiday,biz_date from biz_calendar
			where biz_date = IF(date_format(now(),'%Y-%m-%d %H:%i:%s') < date_format(now(),'%Y-%m-%d %10:%00:%00'),
			date_format(ADDDATE(NOW(), -1),'%Y-%m-%d'), date_format(now(),'%Y-%m-%d'))

	) x on c.biz_date <= x.biz_date and c.holiday = 1


)  v