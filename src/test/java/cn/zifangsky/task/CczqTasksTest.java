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
