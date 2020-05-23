package springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lcp
 * @date 2020/5/21 10:24
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerOrderZkApp84 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerOrderZkApp84.class, args);
    }
}
