package demo;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author YQ.Huang
 */
@Configuration
public class AliyunOssConfiguration {

    @Bean
    AliyunOssProperties ossProperties() {
        return new AliyunOssProperties();
    }

    @Bean
    OSSClient ossClient() {
        // 创建ClientConfiguration实例，按照您的需要修改默认参数
        ClientConfiguration conf = new ClientConfiguration();
        // 开启支持CNAME选项
        conf.setSupportCname(true);
        return new OSSClient("https://" + ossProperties().getEndpoint(),
                ossProperties().getAccessKeyId(),
                ossProperties().getAccessKeySecret());
    }

}
