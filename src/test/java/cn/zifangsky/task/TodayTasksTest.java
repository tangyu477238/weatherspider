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
public class TodayTasksTest {

    @Resource
    private TodayTasks todayTasks;


    @Test
    public void gupiaoByAll() throws Exception{

        todayTasks.gupiaoByAll();

    }
//
//
//    @Test
//    public void todayByDay() throws Exception{
//
//        todayTasks.todayByDay();
//
//    }
//
//
//    @Test
//    public void kzzBy30m() throws Exception{
//
//        todayTasks.kzzBy30m();
//
//    }
//
//    @Test
//    public void kzzBy5m() throws Exception{
//
//        todayTasks.kzzBy5m();
//
//    }


}
