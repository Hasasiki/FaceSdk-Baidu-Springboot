package com.ha.facesdk.service;

import com.jni.face.Face;
import com.jni.struct.Feature;
import com.jni.struct.FeatureInfo;
import lombok.extern.slf4j.Slf4j;
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
}
