package com.ijpay.core.kit;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p> google 开源图形码工具类</p>
 *
 * @author Javen
 */
public class QrCodeKit {
    /**
     * 图形码生成工具
     *
     * @param contents        内容
     * @param barcodeFormat   BarcodeFormat对象
     * @param format          图片格式，可选[png,jpg,bmp]
     * @param width           宽
     * @param height          高
     * @param margin          边框间距px
     * @param saveImgFilePath 存储图片的完整位置，包含文件名
     * @return {boolean}
     */
    public static boolean encode(String contents, BarcodeFormat barcodeFormat, Integer margin,
                                 ErrorCorrectionLevel errorLevel, String format, int width, int height, String saveImgFilePath) {
        Boolean bool = false;
        BufferedImage bufImg;
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>(3);
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, errorLevel);
        hints.put(EncodeHintType.MARGIN, margin);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, barcodeFormat, width, height, hints);
            MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
            bufImg = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
            bool = writeToFile(bufImg, format, saveImgFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * @param outputStream  可以来自response，也可以来自文件
     * @param contents      内容
     * @param barcodeFormat BarcodeFormat对象
     * @param margin        图片格式，可选[png,jpg,bmp]
     * @param errorLevel    纠错级别 一般为：ErrorCorrectionLevel.H
     * @param format        图片格式，可选[png,jpg,bmp]
     * @param width         宽
     * @param height        高
     */
    public static void encodeOutPutSteam(OutputStream outputStream, String contents, BarcodeFormat barcodeFormat, Integer margin, ErrorCorrectionLevel errorLevel, String format, int width, int height) throws IOException {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>(3);
        hints.put(EncodeHintType.ERROR_CORRECTION, errorLevel);
        hints.put(EncodeHintType.MARGIN, margin);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = (new MultiFormatWriter()).encode(contents, barcodeFormat, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

    }

    /**
     * @param srcImgFilePath 要解码的图片地址
     * @return {Result}
     */
    public static Result decode(String srcImgFilePath) {
        Result result = null;
        BufferedImage image;
        try {
            File srcFile = new File(srcImgFilePath);
            image = ImageIO.read(srcFile);
            if (null != image) {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
                hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
                result = new MultiFormatReader().decode(bitmap, hints);
            } else {
                throw new IllegalArgumentException("Could not decode image.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将BufferedImage对象写入文件
     *
     * @param bufImg          BufferedImage对象
     * @param format          图片格式，可选[png,jpg,bmp]
     * @param saveImgFilePath 存储图片的完整位置，包含文件名
     * @return {boolean}
     */
    public static boolean writeToFile(BufferedImage bufImg, String format, String saveImgFilePath) {
        Boolean bool = false;
        try {
            bool = ImageIO.write(bufImg, format, new File(saveImgFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    public static void main(String[] args) {
        String saveImgFilePath = "/Users/Javen/Documents/dev/IJPay/qrCode.png";
        Boolean encode = encode("https://gitee.com/javen205/IJPay", BarcodeFormat.QR_CODE, 3,
                ErrorCorrectionLevel.H, "png", 200, 200, saveImgFilePath);
        if (encode) {
            Result result = decode(saveImgFilePath);
            String text = result.getText();
            System.out.println(text);
        }
    }
}