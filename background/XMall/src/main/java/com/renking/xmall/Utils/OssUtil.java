package com.renking.xmall.Utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OssUtil {

    private final OSS ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    public String uploadImage(MultipartFile file) {
        // 获取当前日期
        LocalDate now = LocalDate.now();

        // 生成带日期路径的文件名：年份/月份/日期/UUID.扩展名
        String datePath = now.getYear() + "/" + String.format("%02d", now.getMonthValue()) + "/" + String.format("%02d", now.getDayOfMonth());

        // 生成唯一文件名
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = datePath + "/" + UUID.randomUUID().toString() + fileExtension;

        // 创建PutObjectRequest对象
        PutObjectRequest putObjectRequest = null;
        try {
            putObjectRequest = new PutObjectRequest(bucketName, fileName, file.getInputStream());
        } catch (IOException e) {
            log.error("上传文件失败", e);
            throw new ServiceException("上传文件失败", StatusCode.FILE_UPLOAD_ERROR);
        }

        // 设置文件元信息
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        putObjectRequest.setMetadata(metadata);

        // 上传文件
        ossClient.putObject(putObjectRequest);

        // 返回文件访问URL
        return "https://" + bucketName + "." + endpoint + "/" + fileName;
    }


    public void deleteImage(String fileName) {
        ossClient.deleteObject(bucketName, fileName);
    }
}
