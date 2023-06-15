package com.jni.face;

import java.util.Scanner;

/**
 * 
 * @ 多线程示例
 *
 */
public class MultiThread {
    
    public class RunnableA implements Runnable{
        @Override
        public void run(){
            // 取特征值
            FaceFeature facefea = new FaceFeature();
            for (int i = 0; i < 10000; i++) {
                facefea.imageFaceFeature(); 
                System.out.println("i is:" + i);
            }
            
        }
    }
    
    public class RunnableB implements Runnable{
        @Override
        public void run(){
            // 人脸检测
            FaceDetect detect = new FaceDetect();
            for (int i = 0; i < 10000; i++) {
                detect.imageDetect();
            }            
        }
    }
    
    public class RunnableC implements Runnable{
        @Override
        public void run(){
            // 人脸活体检测
            FaceLiveness liveness = new FaceLiveness();
            for (int i = 0; i < 10000; i++) {
                liveness.faceLivenessByRgbImage();
            }            
        }
    }
    
    // 多线程示例
    public int multiThreadDemo() {
        // opencv的mat并不是线程安全的，用完需要释放，同一个图片若同时imread可能出错
        RunnableA runa = new RunnableA();
        Thread t1=new Thread(runa);
        t1.start();
        
        RunnableB runb = new RunnableB();
        Thread t2=new Thread(runb);
        t2.start();
        
        RunnableC runc = new RunnableC();
        Thread t3=new Thread(runc);
        t3.start();
        // 回车后继续执行
        Scanner input = new Scanner(System.in);
        input.next();
        return 0;
    }
}
