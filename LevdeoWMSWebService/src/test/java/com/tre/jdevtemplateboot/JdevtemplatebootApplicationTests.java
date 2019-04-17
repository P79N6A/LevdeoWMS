package com.tre.jdevtemplateboot;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.jdevtemplateboot.domain.po.SysParm;
import com.tre.jdevtemplateboot.mapper.SysParmMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class JdevtemplatebootApplicationTests {

    @Autowired
    private SysParmMapper wmsUserMapper;

    @Autowired
    com.tre.jdevtemplateboot.service.IWarehousingScheduleService ws;

    @Test
    public void sendContent() {

//        Wrapper wrapper = new QueryWrapper();
//
//        List<SysParm> list = wmsUserMapper.selectList(wrapper);
//
//        System.out.println(("----- selectAll method test ------"));
//        list.forEach(System.out::println);


//
//
//        List<String> splist = new ArrayList<>();
//        splist.add("1");
//        splist.add("2");
//        splist.add("3");
//        ws.getStockPosition(splist, 1);


        int ret = JNATestDll.instanceDll.B_CreatePrn(13, "172.17.4.95");
        if (0 != ret)
            return;

        JNATestDll.instanceDll.B_Set_Originpoint(0, 0);
        JNATestDll.instanceDll.B_Del_Pcx("*");// delete all picture.
        JNATestDll.instanceDll.B_Draw_Box(20, 20, 4, 30, 30);

        // output.
        JNATestDll.instanceDll.B_Print_Out(1);// copy 2.

        // close port.
        JNATestDll.instanceDll.B_ClosePrn();

        System.out.println("----- selectAll method test ------" + ret);
    }
}
