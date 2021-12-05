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
public class KzzTasksTest {
    @Resource
    private KzzTasks kzzTasks;

    /***
     * // 清除任务
     * @throws Exception
     */
    @Test
    public void deleteAllMyYmd() throws Exception{

        kzzTasks.deleteAllMyYmd();
    }


}
