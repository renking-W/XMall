package com.renking.xmall.Service.Impl;

import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Service.FileService;
import com.renking.xmall.Utils.OssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final OssUtil ossUtil;

    @Override
    public String uploadImage(MultipartFile image) {
        if(image.isEmpty()){
            log.warn("上传图片为空");
            throw new ServiceException("上传图片为空", StatusCode.FILE_EMPTY_ERROR);
        }
        //校验文件大小
        if(image.getSize()>1024*1024*10){
            log.warn("上传图片大小超过10M");
            throw new ServiceException("上传图片大小超过10M", StatusCode.FILE_SIZE_ERROR);
        }
        String path = ossUtil.uploadImage(image);
        return path;
    }
}
