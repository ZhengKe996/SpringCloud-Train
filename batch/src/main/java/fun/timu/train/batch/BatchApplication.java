package fun.timu.train.batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("fun.timu")
@MapperScan("fun.timu.train.*.mapper")
@SpringBootApplication
@EnableFeignClients("fun.timu.train.batch.feign")
public class BatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}
