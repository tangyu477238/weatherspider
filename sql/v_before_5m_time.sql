
--
drop view v_before_5m_time;

create view v_before_5m_time as
select case
when DATE_FORMAT(NOW(),'%H')=11 and DATE_FORMAT(NOW(),'%i')>30 then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 11:30')
when DATE_FORMAT(NOW(),'%H')=12 then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 11:30')
when DATE_FORMAT(NOW(),'%H')=13 and DATE_FORMAT(NOW(),'%i')<5 then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 11:30')
when DATE_FORMAT(NOW(),'%H')>14 then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 15:00')
when DATE_FORMAT(NOW(),'%i')<5 then DATE_FORMAT(NOW(),'%Y-%m-%d %H:00')
when DATE_FORMAT(NOW(),'%i')<10 then DATE_FORMAT(NOW(),'%Y-%m-%d %H:05')
else CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d %H:'),FLOOR(DATE_FORMAT(NOW(),'%i')/5)*5) end as biz_date;