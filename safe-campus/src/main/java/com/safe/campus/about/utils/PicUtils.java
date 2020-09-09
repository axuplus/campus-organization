package com.safe.campus.about.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.util.StringUtils;

import net.coobird.thumbnailator.Thumbnails;

public class PicUtils {

    /**
     * 根据指定大小和指定精度压缩图片
     *
     * @param srcPath
     *            源图片地址
     * @param desPath
     *            目标图片地址
     * @param desFilesize
     *            指定图片大小，单位kb
     * @param accuracy
     *            精度，递归压缩的比率，建议小于0.9
     * @return
     */
    public static String commpressPicForSize(String srcPath, String desPath, long desFileSize, double accuracy) {
        if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(srcPath)) {
            return null;
        }
        if (!new File(srcPath).exists()) {
            return null;
        }
        try {
            File srcFile = new File(srcPath);
            // long srcFileSize = srcFile.length();
            // System.out.println("源图片：" + srcPath + "，大小：" + srcFileSize / 1024
            // + "kb");

            // 1、先转换成jpg
            // Thumbnails.of(srcPath).scale(1f).toFile(desPath);

            try {
                BufferedImage bufferedImage = ImageIO.read(srcFile);
                BufferedImage newBufferedImage =
                    new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
                ImageIO.write(newBufferedImage, "jpg", new File(desPath));
                Thumbnails.of(new File(desPath)).height(300).asBufferedImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 递归压缩，直到目标文件大小小于desFileSize
            commpressPicCycle(desPath, desFileSize, accuracy);

            // File desFile = new File(desPath);
            // System.out.println("目标图片：" + desPath + "，大小" + desFile.length()
            // / 1024 + "kb");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return desPath;
    }

    /**
     * 图片压缩:按指定大小把图片进行缩放（会遵循原图高宽比例） 并设置图片文件大小
     */
    private static void commpressPicCycle(String desPath, long desFileSize, double accuracy) throws IOException {
        File srcFileJPG = new File(desPath);
        long srcFileSizeJPG = srcFileJPG.length();
        // 2、判断大小，如果小于指定大小，不压缩；如果大于等于指定大小，压缩
        if (srcFileSizeJPG <= desFileSize * 1024) {
            return;
        }
        // 计算宽高
        BufferedImage bim = ImageIO.read(srcFileJPG);
        int srcWdith = bim.getWidth();
        int srcHeigth = bim.getHeight();
        int desWidth = new BigDecimal(srcWdith).multiply(new BigDecimal(accuracy)).intValue();
        int desHeight = new BigDecimal(srcHeigth).multiply(new BigDecimal(accuracy)).intValue();

        Thumbnails.of(desPath).size(desWidth, desHeight).outputQuality(accuracy).toFile(desPath);
        commpressPicCycle(desPath, desFileSize, accuracy);
    }

    // 链接URL下载图片
    public static boolean downloadPicture(String urlList, String path) {
        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
