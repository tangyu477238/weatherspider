package cn.zifangsky.manager.impl;

import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.model.Gupiao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GupiaoManagerImplTest   {

    @Resource
    private GupiaoManager gupiaoManager;

    @Resource
    private DongfangManager dongfangManager;

    @Test
    public void updateAllGupiaoKline(){
        gupiaoManager.updateAllGupiaoKline();
    }
    @Test
    public void listKzz(){
        List<Gupiao> list = gupiaoManager.listKzz();
        for (Gupiao gupiao : list){
            dongfangManager.getKline(gupiao.getSymbol(),"5",false);
        }

    }


}
