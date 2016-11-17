package demo;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PolicyConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YQ.Huang
 */
@RestController
public class DemoController {
    private Logger logger = LoggerFactory.getLogger(DemoController.class);
    private final AliyunOssProperties ossProperties;
    private final OSSClient ossClient;

    public DemoController(AliyunOssProperties ossProperties, OSSClient ossClient) {
        this.ossProperties = ossProperties;
        this.ossClient = ossClient;
    }

    @GetMapping("/getUploadCredentials")
    public ResponseEntity<Object> getUploadCredentials() {
        // 链接有效期60秒
        Date expiration = Date.from(Instant.now().plusSeconds(60));
        PolicyConditions policyConditions = new PolicyConditions();
        // 文件大小不超过5MB
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 5242880);
        String policy = ossClient.generatePostPolicy(expiration, policyConditions);
        String signature = ossClient.calculatePostSignature(policy);
        Map<String, Object> result = new HashMap<>();
        result.put("url", "https://" + ossProperties.getBucket() + "." + ossProperties.getEndpoint());
        result.put("OSSAccessKeyId", ossProperties.getAccessKeyId());
        result.put("policy", Base64.getEncoder().encodeToString(policy.getBytes()));
        result.put("signature", signature);
        result.put("successActionStatus", "200");
        result.put("dir", "upload/");
        logger.info("返回参数：\n" + result.toString());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
//
//    @GetMapping("/getUploadCredentials")
//    public ResponseEntity<Object> getUploadCredentials() {
//        // 链接有效期30秒
//        Date expiration = Date.from(Instant.now().plusSeconds(30));
//        PolicyConditions policyConditions = new PolicyConditions();
//        // 文件大小不超过5MB
//        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 5242880);
//        String policy = ossClient.generatePostPolicy(expiration, policyConditions);
//        String signature = ossClient.calculatePostSignature(policy);
//        Map<String, String> result = new HashMap<>();
//        result.put("action", "https://" + ossProperties.getBucket() + "." + ossProperties.getEndpoint());
//        result.put("method", "post");
//        result.put("enctype", "multipart/form-data");
//        result.put("OSSAccessKeyId", ossProperties.getAccessKeyId());
//        result.put("policy", Base64.getEncoder().encodeToString(policy.getBytes()));
//        result.put("signature", signature);
//        result.put("keyPrefix", "upload/");
//        result.put("success_action_redirect", "http://payingcloud.cn");
//        result.put("success_action_status", "201");
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

    @GetMapping("/downloadUrl")
    public ResponseEntity<Object> getDownloadUrl() {
        Date expiration = Date.from(Instant.now().plusSeconds(30));
        URL url = ossClient.generatePresignedUrl("payingcloud-resources", "note.txt", expiration, HttpMethod.GET);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }
}
