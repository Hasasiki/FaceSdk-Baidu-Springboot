package com.ha.facesdk.controller;

import com.ha.facesdk.common.Base64Tools;
import com.ha.facesdk.service.FaceService;
import com.jni.face.Face;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ha.facesdk.common.R;

import java.util.Base64;

/**
 * 人脸比对
 */
@RestController
@RequestMapping("/face")
public class FaceController {
    private final FaceService faceService;

    public FaceController(FaceService faceService) {
        this.faceService = faceService;
    }

    /**
     * 通过图片帧1:1对比
     *
     * @param file 人脸图片
     * @param type type 0： 表示rgb生活照特征值，1:表示rgb证件照特征值 2：表示nir近红外特征值
     * @param fea  0:使用mat对比，1：使用特征值对比
     * @return R
     * @throws Exception e
     */
    @RequestMapping("/compareByImg")
    public R imageMatchByMat(@RequestParam MultipartFile file, @RequestParam Integer fea, @RequestParam Integer type) throws Exception {
        // 提前加载数据库（和全库比较，所以可先把全部库加载到内存,和全库比较，该句必须先调用）
        Face.loadDbFace();

        String photo = Base64.getEncoder().encodeToString(file.getBytes());
        String fileName = System.currentTimeMillis() + ".jpg";
        Base64Tools.base64ToImage(photo, fileName);
        // 提取第一个人脸特征值
        Mat mat = Imgcodecs.imread(fileName);
        long matAddr = mat.getNativeObjAddr();
        switch (fea) {
            case 0:
                return R.ok(faceService.compareByMat(matAddr, type));
            case 1:
                return R.ok(faceService.compareByFea(matAddr, type));
            default:
                return R.faild("参数错误");
        }
    }
    /**
     *通过摄像头视频检测
     * @param type 0： 表示rgb 人脸检测 1：表示nir人脸检测
     * @return R */
    @GetMapping("/compare")
    public R match(@RequestParam Integer type){
        return R.ok(faceService.match(type));
    }
}
