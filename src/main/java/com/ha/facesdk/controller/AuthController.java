package com.ha.facesdk.controller;

import com.ha.facesdk.common.Base64Tools;
import com.ha.facesdk.common.R;
import com.ha.facesdk.service.AuthService;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

/**
 * 人脸数据库
 * 备注（人脸数据库管理说明）：
 * 人脸数据库为采用sqlite3的数据库，会自动生成一个db目录夹，下面有数据库face.db文件保存数据库
 * 可用SQLite Expert之类的可视化工具打开查看,亦可用命令行，方法请自行百度。
 * 该数据库仅仅可适应于5w人左右的人脸库，且设计表格等属于小型通用化。
 * 若不能满足客户个性化需求，客户可自行设计数据库保存数据。宗旨就是每个人脸图片提取一个特征值保存。
 * 人脸1:1,1:N比对及识别实际就是特征值的比对。1:1只要提取2张不同的图片特征值调用compareFeature比对。
 * 1：N是提取一个特征值和数据库中已保存的N个特征值一一比对(比对速度很快，不用担心效率问题)，
 * 最终取分数高的值为最高相似度。
 * 相似度识别的分数可自行测试根据实验结果拟定，一般推荐相似度大于80分为同一人。
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*人脸注册*/
    @PostMapping("/userAdd")
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
                result = authService.userAdd(matAddr, user, groupId, userInfo, type);
                break;
            default:
                break;
        }
        return R.ok("user add result is:" + result);
    }

    // 人脸更新
    @PostMapping("/userUpdate")
    public R userUpdate(@RequestParam("photo") MultipartFile file,
                        @RequestParam("user") String user,
                        @RequestParam("userInfo") String userInfo,
                        @RequestParam("groupId") String groupId) throws Exception {
        String photo = Base64.getEncoder().encodeToString(file.getBytes());
        String fileName = user + "_" + userInfo + ".jpg";
        Base64Tools.base64ToImage(photo, fileName);
        Mat mat = Imgcodecs.imread(fileName);
        long matAddr = mat.getNativeObjAddr();
        return R.ok("user update result is:" + authService.userUpdate(matAddr, user, groupId, userInfo));
    }

    // 删除
    @DeleteMapping("/userDelete")
    public R userDelete(@RequestParam("user") String user,
                        @RequestParam("groupId") String groupId) {
        return R.ok("userDelete res is:" + authService.userDelete(user, groupId));
    }

    // 查询用户信息
    @GetMapping("/userInfo")
    public R userInfo(@RequestParam("user") String user,
                      @RequestParam("groupId") String groupId) {
        return R.ok("userInfo res is:" + authService.userInfo(user, groupId));
    }

    // 查数据库人脸数量
    @GetMapping("/count")
    public R count(@RequestParam("groupId") String groupId) {
        return R.ok("count res is:" + authService.count(groupId));
    }

    // 查用户图片信息
    @GetMapping("/userImage")
    public R userImage(@RequestParam("user") String user,
                       @RequestParam("groupId") String groupId) {
        return R.ok("userImage is load as:" + authService.userImage(user, groupId));
    }

    @PostMapping("/groupAdd")
    public R groupAdd(@RequestParam("groupId") String groupId) {
        return R.ok("groupAdd res is:" + authService.groupAdd(groupId));
    }

    @DeleteMapping("/groupDelete")
    public R groupDelete(@RequestParam("groupId") String groupId) {
        return R.ok("groupDelete res is:" + authService.groupDelete(groupId));
    }

    @GetMapping("/groupList")
    public R groupList() {
        return R.ok("groupList res is:" + authService.groupList());
    }

    @GetMapping("/userList")
    public R groupList(@RequestParam("groupId") String groupId  ) {
        return R.ok("userList res is:" + authService.getUserList(groupId));
    }
}
