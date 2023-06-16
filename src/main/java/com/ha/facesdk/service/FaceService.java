package com.ha.facesdk.service;

import com.jni.face.Face;
import com.jni.face.FaceDraw;
import com.jni.face.ImageGUI;
import com.jni.face.ShowVideo;
import com.jni.struct.*;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
@Slf4j
public class FaceService {

    public String compareByMat(long matAddr, Integer type) {
        return Face.identifyWithAllByMat(matAddr, type);
    }

    public String compareByFea(long matAddr, Integer type) {
        // 提取要比对的图片特征值
        FeatureInfo[] feaList1 = Face.faceFeature(matAddr, type);
        Feature fea = feaList1[0].feature;
        return Face.identifyWithAll(fea, type);
    }

    public String match(Integer type) {
        // 提前加载数据库（和全库比较，所以可先把全部库加载到内存,和全库比较，该句必须先调用）
        Face.loadDbFace();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // 打开摄像头或者视频文件
        // device为0默认打开笔记本电脑自带摄像头，若为0打不开外置usb摄像头
        // 请把device修改为1或2再重试，1，或2为usb插上电脑后，电脑认可的usb设备id
        VideoCapture capture = new VideoCapture();
        // 打开device 0
        capture.open(0);
        if (!capture.isOpened()) {
            log.warn("could not open camera...");
            return "could not open camera...";
        }

        Scalar color = new Scalar(0, 255, 255);
        int radius = 2;
        int frameWidth = (int) capture.get(3);
        int frameHeight = (int) capture.get(4);
        ImageGUI gui = new ImageGUI();
        gui.createWin("video", new Dimension(frameWidth, frameHeight));
        Mat frame = new Mat();

        // 设置循环结束条件
        int maxCount = 100000;
        int index = 0;
        while (index <= maxCount) {
            index++;
            boolean have = capture.read(frame);
            // Core.flip(frame, frame, 1); // Win上摄像头
            if (!have) {
                continue;
            }
            if (!frame.empty()) {
                long matAddr = frame.getNativeObjAddr();
                LiveFeatureInfo[] infos = Face.livenessFeature(matAddr, type);

                if (infos == null || infos.length <= 0) {
                    System.out.printf("detect no face");
                    continue;
                }

                // 检测到人脸
                if (infos != null && infos.length > 0) {
                    for (int j = 0; j < infos.length; j++) {
                        FaceBox box = infos[j].box;
                        // 活体分值
                        float liveScore = infos[j].score;
                        log.info("liveness score is:" + liveScore);
                        if (liveScore > 80) {
                            log.info("活体检测通过");
                            // 特征值
                            Feature fea = infos[j].feature;
                            // 人脸比对
                            String res = Face.identifyWithAll(fea, type);
                            log.info("compare result is:" + res);
                            //TODO 人脸比对结果处理

                            // 画人脸框
                            FaceDraw.drawRects(frame, box);
                            // 画活体分值
                            String sScore = "score:" + liveScore;
                            double fontScale = 2;
                            org.opencv.core.Point pos = new Point(20, 100);
                            Imgproc.putText(frame, sScore, pos, radius, fontScale, color);
                        } else {
                            log.info("活体检测不通过");
                        }
                    }
                }
                gui.imshow(ShowVideo.conver2Image(frame));
                gui.repaint();
                frame.release();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
