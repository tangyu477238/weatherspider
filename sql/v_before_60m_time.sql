
--

drop view v_before_60m_time;

create view v_before_60m_time as
select case
           when DATE_FORMAT(NOW(),'%H:%i')<'10:30' then CONCAT(c.biz_date2,' 15:00')
           when DATE_FORMAT(NOW(),'%H:%i')<'11:30' then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 10:30')
           when DATE_FORMAT(NOW(),'%H:%i')<'14:00' then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 11:30')
           when DATE_FORMAT(NOW(),'%H:%i')<'15:00' then CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d'),' 14:00')
           when DATE_FORMAT(NOW(),'%H')>14 then CONCAT(c.biz_date,' 15:00')  end as biz_date
from v_syn_max_bizdate c;