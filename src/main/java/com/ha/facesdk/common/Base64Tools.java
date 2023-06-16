package com.ha.facesdk.common;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class Base64Tools {
    public static void base64ToImage(String base64String, String savePath) throws IOException {
        // 去掉头部信息（data:image/png;base64,）
        int commaIndex = base64String.indexOf(",");
        base64String = base64String.substring(commaIndex + 1);

        // 根据base64字符串生成字节数组
        byte[] data = Base64.getDecoder().decode(base64String);

        // 根据字节数组生成图片文件
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage image = ImageIO.read(bis);
        File output = new File(savePath);
        ImageIO.write(image, "png", output);
    }
}
