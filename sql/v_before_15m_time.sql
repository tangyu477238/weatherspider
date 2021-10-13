
--
drop view v_before_15m_time;

create view v_before_15m_time as

select
case
   when DATE_FORMAT(NOW(),'%H')<9  then CONCAT(DATE_FORMAT(ADDDATE(NOW(),-1),'%Y-%m-%d'),' 15:00')
   when DATE_FORMAT(NOW(),'%H')=9  and DATE_FORMAT(NOW(),'%i')<45 then CONCAT(DATE_FORMAT(ADDDATE(NOW(),-1),'%Y-%m-%d'),' 15:00')
   when DATE_FORMAT(NOW(),'%H')=12 then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 11:30')
   when DATE_FORMAT(NOW(),'%H')=13 and DATE_FORMAT(NOW(),'%i')<15 then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 11:30')
   when DATE_FORMAT(NOW(),'%H')>14 then CONCAT(c.biz_date,' 15:00')
   when DATE_FORMAT(NOW(),'%i')<15 then DATE_FORMAT(NOW(),'%Y-%m-%d %H:00') else CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d %H:'),FLOOR(DATE_FORMAT(NOW(),'%i')/15)*15) end as biz_date
from v_syn_max_bizdate c;
