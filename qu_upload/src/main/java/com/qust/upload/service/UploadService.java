package com.qust.upload.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import qu_Item.common.Exception.quException;
import qu_Item.common.enume.ExceptionEnum;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UploadService {

    //把传入的类型转化为list<String>;
    private final List<String> UPLOADMESS = Arrays.asList("image/jpeg","image/png");

    public String unloadImage(MultipartFile file) {
        try {
            //校验
            //1） 图片的后缀校验;
            String contentType = file.getContentType();
            if (!UPLOADMESS.contains(contentType)){
                throw new quException(ExceptionEnum.FILE_TYPR_ERROR);
            }
            //2) 图片的内容校验;
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null){
                throw new quException(ExceptionEnum.FILE_TYPR_ERROR);
            }
            //保存文件到本地;
            File dest = new File("C:\\Users\\dell\\Desktop\\upload",file.getOriginalFilename());
            //文件的保存;
            file.transferTo(dest);
            //返回一个路径;
            return "http://localhost:8083/"+file.getOriginalFilename();
        } catch (IOException e) {
            log.error("文件保存失败！");
            throw  new quException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
    }
}
