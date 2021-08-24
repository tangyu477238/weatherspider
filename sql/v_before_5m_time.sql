
--
drop view v_before_5m_time;

create view v_before_5m_time as
SELECT IF(DATE_FORMAT(NOW(),'%i')<10, CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d %H:'),'0',FLOOR(DATE_FORMAT(NOW(),'%i')/5)*5) ,CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d %H:'),FLOOR(DATE_FORMAT(NOW(),'%i')/5)*5)) as biz_date
