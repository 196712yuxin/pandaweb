package com.panda;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author panda
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@MapperScan("com.panda.project.*.*.mapper")
public class PandaApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(PandaApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Panda启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "            疯狂源于梦想       \n" +
                "            技术成就辉煌       \n" +
                "            E学习 更快乐       \n" +
                " http://dev.ehongqi.cn:8080/PandaFrame \n" +

                "--------------------------------------  ");
    }
}