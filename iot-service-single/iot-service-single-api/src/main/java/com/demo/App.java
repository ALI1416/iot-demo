package com.demo;

import com.demo.entity.protocol.ProtocolInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

/**
 * <h1>启动类</h1>
 *
 * <h2>打包后运行方式</h2>
 * <p>
 * java -jar 文件名.jar<br>
 * 如果中文乱码，请用以下命令运行<br>
 * javaw -Dfile.encoding=utf-8 -jar 文件名.jar
 * </p>
 *
 * <h2>配置文件可放置目录(优先级从高到低)</h2>
 * <p>
 * ./config/ (当前项目路径config目录下)<br>
 * ./ (当前项目路径下)<br>
 * classpath:/config/ (类路径config目录下)<br>
 * classpath:/ (类路径下)<br>
 * </p>
 *
 * <p>
 * createDate 2020/11/11 11:11:11
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@MapperScan("com.demo.mapper")
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class App {

    public static void main(String[] args) {
        ProtocolInfo.getTable();
        // 默认时区：北京
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        SpringApplication.run(App.class, args);
    }

}
