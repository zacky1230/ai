package com.chineseall.controller;

import com.alibaba.fastjson.JSONArray;
import com.chineseall.entity.ImageBaseInfo;
import com.chineseall.entity.UploadFileContext;
import com.chineseall.service.OcrHandleService;
import com.chineseall.util.base.string.StringUtils;
import com.chineseall.util.model.MessageCode;
import com.chineseall.util.model.RetMsg;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 13:47.
 */
@Controller
public class OcrHandleController extends BaseController {

    @Resource
    private OcrHandleService ocrHandleService;

    /**
     * 上传图片带选择识别方式
     */
    @RequestMapping(value = "/ocr/upload/{type}")
    @ResponseBody
    public ResponseEntity imageUpload(@RequestParam("imgName") MultipartFile file, HttpServletRequest request,
                                      @PathVariable String type) {

        double width = super.parseHttpDoubleVauleByKey(request, "width");
        double height = super.parseHttpDoubleVauleByKey(request, "height");
        RetMsg retMsg = ocrHandleService.imageUpload(file, type, width, height);
        return ResponseEntity.ok(retMsg);
    }


    /**
     * 图片识别
     */
    @RequestMapping(value = "/ocr/image/{type}")
    @ResponseBody
    public ResponseEntity imageRecognition(@RequestBody Map<String, Object> map) {
        RetMsg retMsg = ocrHandleService.imageRecognition(map);
        return ResponseEntity.ok(retMsg);
    }


    /**
     * 获取处理好的图片
     */
    @RequestMapping(value = "/ocr/handler/{imageId}")
    @ResponseBody
    public ResponseEntity getHandlerImage(@PathVariable String imageId) {
        RetMsg retMsg = ocrHandleService.getHandlerImage(imageId);
        return ResponseEntity.ok(retMsg);
    }

    /**
     * 保存图片
     */
    @RequestMapping(value = "/ocr/save/{imageId}")
    @ResponseBody
    public ResponseEntity saveImageInfo(@PathVariable String imageId, @RequestBody Map<String, Object> map) {
        UploadFileContext info = new UploadFileContext();
        String context = (String) map.get("context");
        LinkedHashMap coordinates = (LinkedHashMap) map.get("coordinate");
        String coordinate = JSONArray.toJSONString(coordinates);
        RetMsg retMsg = new RetMsg();
        if (StringUtils.isEmpty(context) || StringUtils.isEmpty(coordinate) || StringUtils.isEmpty(imageId)) {
            retMsg.setMsg(MessageCode.ParamIsError.getDescription());
            retMsg.setCode(MessageCode.ParamIsError.getCode());
            return ResponseEntity.ok(retMsg);
        } else {
            info.setFileId(imageId);
            info.setContext(context);
            info.setCoordinate(coordinate);
        }
        retMsg = ocrHandleService.saveImageInfo(imageId, info);
        return ResponseEntity.ok(retMsg);
    }

    /**
     * 演示
     */
    @RequestMapping(value = "/ocr/query/{id}")
    @ResponseBody
    public ResponseEntity queryImageInfo(@PathVariable int id) {
        RetMsg retMsg = ocrHandleService.queryImageInfo(id);
        return ResponseEntity.ok(retMsg);
    }

    /**
     * 测试版本
     */
    @RequestMapping(value = "/ocr/demo")
    @ResponseBody
    public ResponseEntity demo(@RequestParam("imgName") MultipartFile file, HttpServletRequest request) throws IOException {
        String coordinates = super.parseHttpStringVauleByKey(request, "coordinate");
        double width = super.parseHttpDoubleVauleByKey(request, "width");
        double height = super.parseHttpDoubleVauleByKey(request, "height");

        ImageBaseInfo imageInfo = new ImageBaseInfo(width, height, coordinates);

        RetMsg retMsg = ocrHandleService.imageDemo(file, imageInfo);
        return ResponseEntity.ok(retMsg);
    }
}
