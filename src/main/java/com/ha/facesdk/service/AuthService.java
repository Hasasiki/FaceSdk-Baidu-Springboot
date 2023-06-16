package com.ha.facesdk.service;

import com.ha.facesdk.common.R;
import com.jni.face.Face;
import com.jni.struct.Feature;
import com.jni.struct.FeatureInfo;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {
    public String userAdd(long matAddr, String user, String groupId, String userInfo,int type) {
        FeatureInfo[] feaList = Face.faceFeature(matAddr, type);
        if (feaList == null || feaList.length <= 0) {
            log.warn("get feature fail");
            return "get feature fail";
        }
        Feature fea = feaList[0].feature;
        return Face.userAdd(fea, user, groupId, userInfo);
    }

    public String userAddByMat(long matAddr, String user, String groupId, String userInfo) {
        return Face.userAddByMat(matAddr, user, groupId, userInfo);
    }

    public String userUpdate(long matAddr, String user, String groupId, String userInfo) {
        return Face.userUpdate(matAddr, user, groupId, userInfo);
    }

    public String userDelete(String user, String groupId) {
        return Face.userDelete(user, groupId);
    }

    public String userInfo(String user, String groupId) {
        return Face.getUserInfo(user, groupId);
    }

    public String count(String groupId) {
        return "db face count =" + Face.dbFaceCount(groupId);
    }

    public String userImage(String user, String groupId) {
        Mat outMat = new Mat();
        long outAddr = outMat.getNativeObjAddr();
        int res = Face.getUserImage(outAddr, user, groupId);
        log.info("userImage res = {}", res);
        if (res != 0) {
            return "get user image fail and error =" + res;
        }

        try {
            if (outMat.empty()) {
                return "image empty";
            }
        } catch (Exception e) {
            // 未检测到人脸或其他原因导致sdk无图片返回
            return "outMat empty exception";
        }
        // 抠图完毕可保存到本地
        Imgcodecs.imwrite(user + System.currentTimeMillis()+".jpg", outMat);
        return user + System.currentTimeMillis()+".jpg";
    }

    public String groupAdd(String groupId) {
        return Face.groupAdd(groupId);
    }

    public String groupDelete(String groupId) {
        return Face.groupDelete(groupId);
    }

    public String getUserList(String groupId) {
        return Face.getUserList(groupId, 0, 100);
    }

    public String groupList() {
        return  Face.getGroupList(0, 100);
    }
}
