package demo;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YQ.Huang
 */
@RestController
public class UploadController {
    private Logger logger = LoggerFactory.getLogger(UploadController.class);
    private final AliyunOssProperties ossProperties;
    private final OSSClient ossClient;

    public UploadController(AliyunOssProperties ossProperties, OSSClient ossClient) {
        this.ossProperties = ossProperties;
        this.ossClient = ossClient;
    }

    /**
     * 前端请求获取上传凭证
     */
    @GetMapping("/upload/credentials")
    public ResponseEntity<Object> getCredentials() throws JsonProcessingException {
        // 链接有效期60秒
        Date expiration = Date.from(Instant.now().plusSeconds(60));
        PolicyConditions policyConditions = new PolicyConditions();
        // 文件大小不超过5MB
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 5242880);
        // 上传路径必需以upload/开头
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, "upload/");
        String policy = ossClient.generatePostPolicy(expiration, policyConditions);
        String signature = ossClient.calculatePostSignature(policy);
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("url", "https://" + ossProperties.getBucket() + "." + ossProperties.getEndpoint());
        credentials.put("OSSAccessKeyId", ossProperties.getAccessKeyId());
        credentials.put("policy", Base64.getEncoder().encodeToString(policy.getBytes()));
        credentials.put("signature", signature);
        credentials.put("successActionStatus", "200");
        credentials.put("callback", Base64.getEncoder().encodeToString(buildCallback().getBytes()));
        credentials.put("dir", "upload/");
        logger.info("上传凭证：\n" + credentials.toString());
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    /**
     * OSS上传回调
     */
    @PostMapping("/upload/callback")
    public ResponseEntity<Object> onUploaded(String filename, String size, String mimeType) {
        Map<String, Object> result = new HashMap<>();
        result.put("filename", filename);
        result.put("size", size);
        result.put("mimeType", mimeType);
        logger.info("返回参数：\n" + result.toString());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 构建Callback参数
     */
    private String buildCallback() throws JsonProcessingException {
        Callback callback = new Callback();
        callback.setCallbackUrl("http://" + ossProperties.getCallbackHost() + "/upload/callback");
        callback.setCallbackHost(ossProperties.getCallbackHost());
        callback.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}");
        callback.setCalbackBodyType(Callback.CalbackBodyType.URL);
        return new ObjectMapper().writeValueAsString(callback);
    }
}
