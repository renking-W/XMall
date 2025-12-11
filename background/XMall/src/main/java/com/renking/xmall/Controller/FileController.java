package com.renking.xmall.Controller;


import com.renking.xmall.Entity.Result;
import com.renking.xmall.Service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 给指定商品上传图片
     * 前端传参： 图片文件
     * @param image
     * @return
     */
    @PostMapping("/upload/image")
    public Result<String> uploadImage(@RequestBody MultipartFile image) {
        String filePath = fileService.uploadImage(image);
        return Result.success(filePath);
    }

}
