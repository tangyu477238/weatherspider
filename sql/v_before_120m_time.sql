
--

drop view v_before_120m_time;

create view v_before_120m_time as
select case
           when DATE_FORMAT(NOW(),'%H:%i')<'11:30' then CONCAT(DATE_FORMAT(ADDDATE(NOW(),-1),'%Y-%m-%d'),' 15:00')
           when DATE_FORMAT(NOW(),'%H:%i')<'15:00' then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 11:30')
           when DATE_FORMAT(NOW(),'%H')>14 then CONCAT(c.biz_date,' 15:00')  end as biz_date
from v_syn_max_bizdate c;