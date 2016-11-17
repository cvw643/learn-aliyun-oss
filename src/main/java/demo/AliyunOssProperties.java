package demo;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author YQ.Huang
 */
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOssProperties {

    @NotBlank
    private String accessKeyId;

    @NotBlank
    private String accessKeySecret;

    @NotBlank
    private String endpoint;

    @NotBlank
    private String bucket;

    @NotBlank
    private String callbackHost;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public AliyunOssProperties setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        return this;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public AliyunOssProperties setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public AliyunOssProperties setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public AliyunOssProperties setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getCallbackHost() {
        return callbackHost;
    }

    public AliyunOssProperties setCallbackHost(String callbackHost) {
        this.callbackHost = callbackHost;
        return this;
    }
}
