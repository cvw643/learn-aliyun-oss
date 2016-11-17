# learn-aliyun-oss
本项目是阿里云OSS Web直传模式的Java参考实现

## 启动
* 设置环境变量
```
  ALIYUN_OSS_ACCESS_KEY_ID=<accessKeyId> （必需）
  ALIYUN_OSS_ACCESS_KEY_SECRET=<accessKeySecret> （必需）
  ALIYUN_OSS_ENDPOINT=<accessKeyId> （默认：oss-cn-beijing.aliyuncs.com）
  ALIYUN_OSS_BUCKET=<accessKeyId> （默认：payingcloud-resources）
```  
* 构建
```
  gradle build
```  
* 运行
```
  gradle bootrun
```  
