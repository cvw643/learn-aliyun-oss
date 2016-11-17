# learn-aliyun-oss
本项目是阿里云OSS Web直传模式的Java参考实现，前端采用plupload.js

## 启动
* 设置application.yml中的参数
```
aliyun:
  oss:
    access-key-id:
    access-key-secret:
    endpoint:
    bucket:
    callbackHost:
```  
* 运行
```
  gradle bootrun
```  
