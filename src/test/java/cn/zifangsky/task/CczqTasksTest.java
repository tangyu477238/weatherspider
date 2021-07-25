package cn.zifangsky.task;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CczqTasksTest {
    @Resource
    private CczqTasks cczqTasks;

    @Resource
    private TodayTasks todayTasks;


    @Test
    public void todayByDay() throws Exception{

        todayTasks.todayByDay();

    }

    @Test
    public void zaopanCheck() throws Exception{

        cczqTasks.zaopanCheck();

    }

    @Test
    public void zaopan() throws Exception{

        cczqTasks.zaopan();

    }

    @Test
    public void wanpan() throws Exception{

        cczqTasks.wanpan();

    }

    @Test
    public void xintiao() throws Exception{

        cczqTasks.xintiao();

    }
}
