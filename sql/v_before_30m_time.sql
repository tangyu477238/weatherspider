
--

drop view v_before_30m_time;

create view v_before_30m_time as
select case
   when DATE_FORMAT(NOW(),'%H')=12 then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 11:30')
   when DATE_FORMAT(NOW(),'%H')=13 and DATE_FORMAT(NOW(),'%i')<30 then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 11:30')
   when DATE_FORMAT(NOW(),'%H')>14 then CONCAT(c.biz_date,' 15:00')
   when DATE_FORMAT(NOW(),'%H')<10 then CONCAT(c.biz_date2,' 15:00')
   when DATE_FORMAT(NOW(),'%i')<30 then DATE_FORMAT(NOW(),'%Y-%m-%d %H:00') else DATE_FORMAT(NOW(),'%Y-%m-%d %H:30') end as biz_date
from v_syn_max_bizdate c;
