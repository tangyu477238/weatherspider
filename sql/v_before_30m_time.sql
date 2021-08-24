
--
drop view v_before_30m_time;

create view v_before_30m_time as
SELECT IF(DATE_FORMAT(NOW(),'%i')<30, CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d %H:'),'0',FLOOR(DATE_FORMAT(NOW(),'%i')/30)*30) ,CONCAT(DATE_FORMAT(NOW(),'%Y-%m-%d %H:'),FLOOR(DATE_FORMAT(NOW(),'%i')/30)*30)) as biz_date
