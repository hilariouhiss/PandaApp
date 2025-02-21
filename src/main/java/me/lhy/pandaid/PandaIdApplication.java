package me.lhy.pandaid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("me.lhy.pandaid.mapper")
public class PandaIdApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandaIdApplication.class, args);
    }

}
