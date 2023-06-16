package com.ha.facesdk.controller;

import com.ha.facesdk.common.Base64Tools;
import com.ha.facesdk.common.R;
import com.ha.facesdk.service.AuthService;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

/*
 * 人脸数据库
 * */
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*人脸注册*/
    public R userAdd(@RequestParam("photo") MultipartFile file,
                     @RequestParam("user") String user,
                     @RequestParam("userInfo") String userInfo,
                     @RequestParam("groupId") String groupId,
                     @RequestParam("fea") Integer fea,
                     @RequestParam("type") Integer type) throws Exception {

        String photo = Base64.getEncoder().encodeToString(file.getBytes());
        String fileName = user + "_" + userInfo + ".jpg";
        Base64Tools.base64ToImage(photo, fileName);
        Mat mat = Imgcodecs.imread(fileName);
        long matAddr = mat.getNativeObjAddr();
        String result = "";
        switch (fea) {
            case 0:
                result = authService.userAddByMat(matAddr, user, groupId, userInfo);
                break;
            case 1:
                //type 0：表示rgb 人脸检测   1：表示nir人脸检测
                result = authService.userAdd(matAddr, user, groupId, userInfo,type);
                break;
            default:
                break;
        }
        return R.ok(result);
    }

}
