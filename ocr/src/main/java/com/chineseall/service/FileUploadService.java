package com.chineseall.service;

import com.chineseall.entity.ImageBaseInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 12:06.
 */
public interface FileUploadService {

    /**
     * save image by ration
     *
     * @param file   file
     * @param type   type
     * @param width  width
     * @param height height
     * @return Map
     */
    Map<String, Object> saveOcrImage(MultipartFile file, String type, double width, double height);

    /**
     * save image with condition
     *
     * @param file      file
     * @param imageInfo imageInfo
     * @return Map
     */
    Map<String, Object> saveOcrImage(MultipartFile file, ImageBaseInfo imageInfo);
}
