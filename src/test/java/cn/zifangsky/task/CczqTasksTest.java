package cn.zifangsky.task;

import cn.zifangsky.login.Login;
import cn.zifangsky.manager.GupiaoManager;
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
    /***
     * deleteAllMyYmd
     * @throws Exception
     */
    @Test
    public void zaopan() throws Exception{

        cczqTasks.zaopan();

    }
}
