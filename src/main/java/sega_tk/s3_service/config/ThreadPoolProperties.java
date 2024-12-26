package sega_tk.s3_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("executor")
public class ThreadPoolProperties {

    private int capacity;
    private int corePoolSize;
    private int maximumPoolSize;
    private long keepAliveTime;

}