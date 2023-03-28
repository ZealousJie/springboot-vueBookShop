package com.example.demo.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public class OSSUtil {
    //阿里域名
    public static final String ALI_DOMAIN = "https://zjbucket-02.oss-cn-guangzhou.aliyuncs.com/";

    public static String uploadImage(MultipartFile file) throws IOException {
        //生成文件名
        String originalFileName = file.getOriginalFilename();
        String ext = "." + FilenameUtils.getExtension(originalFileName);
        String uuid = UUID.randomUUID().toString().replace("-","");
        String filename = uuid + ext;

        //地域节点
        String endPoint = "http://oss-cn-guangzhou.aliyuncs.com";
        String accessKeyId = "LTAI5tFLbynscbJDLmzxPPQU";
        String accessKeySecret= "7MXtbgJz5T1Hrr1kWDNXpoTBwvTk4w";

        //OSS客户端对象
        OSS ossClient = new OSSClientBuilder().build(endPoint,accessKeyId,accessKeySecret);
        ossClient.putObject("zjbucket-02",filename,file.getInputStream());
        ossClient.shutdown();
        return ALI_DOMAIN + filename;
    }
}
