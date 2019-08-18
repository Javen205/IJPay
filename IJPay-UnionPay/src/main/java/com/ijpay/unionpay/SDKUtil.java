
package com.ijpay.unionpay;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static com.ijpay.unionpay.SDKConstants.*;

/**
 * @author UnionPay
 */
public class SDKUtil {

    public static boolean sign(Map<String, String> data, String encoding) {

        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        String signMethod = data.get(param_signMethod);
        String version = data.get(SDKConstants.param_version);
        if (!VERSION_1_0_0.equals(version) && !VERSION_5_0_1.equals(version) && isEmpty(signMethod)) {
            LogUtil.writeErrorLog("signMethod must Not null");
            return false;
        }

        if (isEmpty(version)) {
            LogUtil.writeErrorLog("version must Not null");
            return false;
        }
        if (SIGNMETHOD_RSA.equals(signMethod) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
            if (VERSION_5_0_0.equals(version) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
                // 设置签名证书序列号
                data.put(SDKConstants.param_certId, CertUtil.getSignCertId());
                // 将Map信息转换成key1=value1&key2=value2的形式
                String stringData = coverMap2String(data);
                LogUtil.writeLog("打印排序后待签名请求报文串（交易返回11验证签名失败时可以用来同正确的进行比对）:[" + stringData + "]");
                byte[] byteSign = null;
                String stringSign = null;
                try {
                    // 通过SHA1进行摘要并转16进制
                    byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
                    LogUtil.writeLog("打印摘要（交易返回11验证签名失败可以用来同正确的进行比对）:[" + new String(signDigest) + "]");
                    byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(CertUtil.getSignCertPrivateKey(), signDigest));
                    stringSign = new String(byteSign);
                    // 设置签名域值
                    data.put(SDKConstants.param_signature, stringSign);
                    return true;
                } catch (Exception e) {
                    LogUtil.writeErrorLog("Sign Error", e);
                    return false;
                }
            } else if (VERSION_5_1_0.equals(version)) {
                // 设置签名证书序列号
                data.put(SDKConstants.param_certId, CertUtil.getSignCertId());
                // 将Map信息转换成key1=value1&key2=value2的形式
                String stringData = coverMap2String(data);
                LogUtil.writeLog("打印待签名请求报文串（交易返回11验证签名失败时可以用来同正确的进行比对）:[" + stringData + "]");
                byte[] byteSign = null;
                String stringSign = null;
                try {
                    // 通过SHA256进行摘要并转16进制
                    byte[] signDigest = SecureUtil.sha256X16(stringData, encoding);
                    LogUtil.writeLog("打印摘要（交易返回11验证签名失败可以用来同正确的进行比对）:[" + new String(signDigest) + "]");
                    byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft256(CertUtil.getSignCertPrivateKey(), signDigest));
                    stringSign = new String(byteSign);
                    // 设置签名域值
                    data.put(SDKConstants.param_signature, stringSign);
                    return true;
                } catch (Exception e) {
                    LogUtil.writeErrorLog("Sign Error", e);
                    return false;
                }
            }
        } else if (SIGNMETHOD_SHA256.equals(signMethod)) {
            return signBySecureKey(data, SDKConfig.getConfig().getSecureKey(), encoding);
        } else if (SIGNMETHOD_SM3.equals(signMethod)) {
            return signBySecureKey(data, SDKConfig.getConfig().getSecureKey(), encoding);
        }
        return false;
    }

    public static boolean signBySecureKey(Map<String, String> data, String secureKey, String encoding) {

        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        if (isEmpty(secureKey)) {
            LogUtil.writeErrorLog("secureKey is empty");
            return false;
        }
        String signMethod = data.get(param_signMethod);
        if (isEmpty(signMethod)) {
            LogUtil.writeErrorLog("signMethod must Not null");
            return false;
        }

        if (SIGNMETHOD_SHA256.equals(signMethod)) {
            // 将Map信息转换成key1=value1&key2=value2的形式
            String stringData = coverMap2String(data);
            LogUtil.writeLog("待签名请求报文串:[" + stringData + "]");
            String strBeforeSha256 = stringData + SDKConstants.AMPERSAND + SecureUtil.sha256X16Str(secureKey, encoding);
            String strAfterSha256 = SecureUtil.sha256X16Str(strBeforeSha256, encoding);
            // 设置签名域值
            data.put(SDKConstants.param_signature, strAfterSha256);
            return true;
        } else if (SIGNMETHOD_SM3.equals(signMethod)) {
            String stringData = coverMap2String(data);
            LogUtil.writeLog("待签名请求报文串:[" + stringData + "]");
            String strBeforeSM3 = stringData + SDKConstants.AMPERSAND + SecureUtil.sm3X16Str(secureKey, encoding);
            String strAfterSM3 = SecureUtil.sm3X16Str(strBeforeSM3, encoding);
            // 设置签名域值
            data.put(SDKConstants.param_signature, strAfterSM3);
            return true;
        }
        return false;
    }

    public static boolean signByCertInfo(Map<String, String> data, String certPath, String certPwd, String encoding) {

        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        if (isEmpty(certPath) || isEmpty(certPwd)) {
            LogUtil.writeErrorLog("CertPath or CertPwd is empty");
            return false;
        }
        String signMethod = data.get(param_signMethod);
        String version = data.get(SDKConstants.param_version);
        if (!VERSION_1_0_0.equals(version) && !VERSION_5_0_1.equals(version) && isEmpty(signMethod)) {
            LogUtil.writeErrorLog("signMethod must Not null");
            return false;
        }
        if (isEmpty(version)) {
            LogUtil.writeErrorLog("version must Not null");
            return false;
        }

        if (SIGNMETHOD_RSA.equals(signMethod) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
            if (VERSION_5_0_0.equals(version) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
                // 设置签名证书序列号
                data.put(SDKConstants.param_certId, CertUtil.getCertIdByKeyStoreMap(certPath, certPwd));
                // 将Map信息转换成key1=value1&key2=value2的形式
                String stringData = coverMap2String(data);
                LogUtil.writeLog("待签名请求报文串:[" + stringData + "]");
                byte[] byteSign = null;
                String stringSign = null;
                try {
                    // 通过SHA1进行摘要并转16进制
                    byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
                    byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(CertUtil.getSignCertPrivateKeyByStoreMap(certPath, certPwd), signDigest));
                    stringSign = new String(byteSign);
                    // 设置签名域值
                    data.put(SDKConstants.param_signature, stringSign);
                    return true;
                } catch (Exception e) {
                    LogUtil.writeErrorLog("Sign Error", e);
                    return false;
                }
            } else if (VERSION_5_1_0.equals(version)) {
                // 设置签名证书序列号
                data.put(SDKConstants.param_certId, CertUtil.getCertIdByKeyStoreMap(certPath, certPwd));
                // 将Map信息转换成key1=value1&key2=value2的形式
                String stringData = coverMap2String(data);
                LogUtil.writeLog("待签名请求报文串:[" + stringData + "]");
                byte[] byteSign = null;
                String stringSign = null;
                try {
                    // 通过SHA256进行摘要并转16进制
                    byte[] signDigest = SecureUtil.sha256X16(stringData, encoding);
                    byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft256(CertUtil.getSignCertPrivateKeyByStoreMap(certPath, certPwd), signDigest));
                    stringSign = new String(byteSign);
                    // 设置签名域值
                    data.put(SDKConstants.param_signature, stringSign);
                    return true;
                } catch (Exception e) {
                    LogUtil.writeErrorLog("Sign Error", e);
                    return false;
                }
            }

        }
        return false;
    }

    public static boolean validateBySecureKey(Map<String, String> resData, String secureKey, String encoding) {
        LogUtil.writeLog("验签处理开始");
        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        String signMethod = resData.get(param_signMethod);
        if (SIGNMETHOD_SHA256.equals(signMethod)) {
            // 1.进行SHA256验证
            String stringSign = resData.get(SDKConstants.param_signature);
            LogUtil.writeLog("签名原文：[" + stringSign + "]");
            // 将Map信息转换成key1=value1&key2=value2的形式
            String stringData = coverMap2String(resData);
            LogUtil.writeLog("待验签返回报文串：[" + stringData + "]");
            String strBeforeSha256 = stringData + SDKConstants.AMPERSAND + SecureUtil.sha256X16Str(secureKey, encoding);
            String strAfterSha256 = SecureUtil.sha256X16Str(strBeforeSha256, encoding);
            return stringSign.equals(strAfterSha256);
        } else if (SIGNMETHOD_SM3.equals(signMethod)) {
            // 1.进行SM3验证
            String stringSign = resData.get(SDKConstants.param_signature);
            LogUtil.writeLog("签名原文：[" + stringSign + "]");
            // 将Map信息转换成key1=value1&key2=value2的形式
            String stringData = coverMap2String(resData);
            LogUtil.writeLog("待验签返回报文串：[" + stringData + "]");
            String strBeforeSM3 = stringData + SDKConstants.AMPERSAND + SecureUtil.sm3X16Str(secureKey, encoding);
            String strAfterSM3 = SecureUtil.sm3X16Str(strBeforeSM3, encoding);
            return stringSign.equals(strAfterSM3);
        }
        return false;
    }

    public static boolean validate(Map<String, String> resData, String encoding) {
        LogUtil.writeLog("验签处理开始");
        if (isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        String signMethod = resData.get(param_signMethod);
        String version = resData.get(SDKConstants.param_version);
        if (SIGNMETHOD_RSA.equals(signMethod) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
            // 获取返回报文的版本号
            if (VERSION_5_0_0.equals(version) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
                String stringSign = resData.get(SDKConstants.param_signature);
                LogUtil.writeLog("签名原文：[" + stringSign + "]");
                // 从返回报文中获取certId ，然后去证书静态Map中查询对应验签证书对象
                String certId = resData.get(SDKConstants.param_certId);
                LogUtil.writeLog("对返回报文串验签使用的验签公钥序列号：[" + certId + "]");
                // 将Map信息转换成key1=value1&key2=value2的形式
                String stringData = coverMap2String(resData);
                LogUtil.writeLog("待验签返回报文串：[" + stringData + "]");
                try {
                    // 验证签名需要用银联发给商户的公钥证书.
                    return SecureUtil.validateSignBySoft(CertUtil
                                    .getValidatePublicKey(certId), SecureUtil
                                    .base64Decode(stringSign.getBytes(encoding)),
                            SecureUtil.sha1X16(stringData, encoding));
                } catch (UnsupportedEncodingException e) {
                    LogUtil.writeErrorLog(e.getMessage(), e);
                } catch (Exception e) {
                    LogUtil.writeErrorLog(e.getMessage(), e);
                }
            } else if (VERSION_5_1_0.equals(version)) {
                // 1.从返回报文中获取公钥信息转换成公钥对象
                String strCert = resData.get(SDKConstants.param_signPubKeyCert);
//				LogUtil.writeLog("验签公钥证书：["+strCert+"]");
                X509Certificate x509Cert = CertUtil.genCertificateByStr(strCert);
                if (x509Cert == null) {
                    LogUtil.writeErrorLog("convert signPubKeyCert failed");
                    return false;
                }
                // 2.验证证书链
                if (!CertUtil.verifyCertificate(x509Cert)) {
                    LogUtil.writeErrorLog("验证公钥证书失败，证书信息：[" + strCert + "]");
                    return false;
                }

                // 3.验签
                String stringSign = resData.get(SDKConstants.param_signature);
                LogUtil.writeLog("签名原文：[" + stringSign + "]");
                // 将Map信息转换成key1=value1&key2=value2的形式
                String stringData = coverMap2String(resData);
                LogUtil.writeLog("待验签返回报文串：[" + stringData + "]");
                try {
                    // 验证签名需要用银联发给商户的公钥证书.
                    boolean result = SecureUtil.validateSignBySoft256(x509Cert
                            .getPublicKey(), SecureUtil.base64Decode(stringSign
                            .getBytes(encoding)), SecureUtil.sha256X16(
                            stringData, encoding));
                    LogUtil.writeLog("验证签名" + (result ? "成功" : "失败"));
                    return result;
                } catch (UnsupportedEncodingException e) {
                    LogUtil.writeErrorLog(e.getMessage(), e);
                } catch (Exception e) {
                    LogUtil.writeErrorLog(e.getMessage(), e);
                }
            }

        } else if (SIGNMETHOD_SHA256.equals(signMethod)) {
            // 1.进行SHA256验证
            String stringSign = resData.get(SDKConstants.param_signature);
            LogUtil.writeLog("签名原文：[" + stringSign + "]");
            // 将Map信息转换成key1=value1&key2=value2的形式
            String stringData = coverMap2String(resData);
            LogUtil.writeLog("待验签返回报文串：[" + stringData + "]");
            String strBeforeSha256 = stringData
                    + SDKConstants.AMPERSAND
                    + SecureUtil.sha256X16Str(SDKConfig.getConfig()
                    .getSecureKey(), encoding);
            String strAfterSha256 = SecureUtil.sha256X16Str(strBeforeSha256,
                    encoding);
            boolean result = stringSign.equals(strAfterSha256);
            LogUtil.writeLog("验证签名" + (result ? "成功" : "失败"));
            return result;
        } else if (SIGNMETHOD_SM3.equals(signMethod)) {
            // 1.进行SM3验证
            String stringSign = resData.get(SDKConstants.param_signature);
            LogUtil.writeLog("签名原文：[" + stringSign + "]");
            // 将Map信息转换成key1=value1&key2=value2的形式
            String stringData = coverMap2String(resData);
            LogUtil.writeLog("待验签返回报文串：[" + stringData + "]");
            String strBeforeSM3 = stringData
                    + SDKConstants.AMPERSAND
                    + SecureUtil.sm3X16Str(SDKConfig.getConfig()
                    .getSecureKey(), encoding);
            String strAfterSM3 = SecureUtil
                    .sm3X16Str(strBeforeSM3, encoding);
            boolean result = stringSign.equals(strAfterSM3);
            LogUtil.writeLog("验证签名" + (result ? "成功" : "失败"));
            return result;
        }
        return false;
    }

    public static String coverMap2String(Map<String, String> data) {
        TreeMap<String, String> tree = new TreeMap<String, String>();
        Iterator<Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            if (SDKConstants.param_signature.equals(en.getKey().trim())) {
                continue;
            }
            tree.put(en.getKey(), en.getValue());
        }
        it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            sf.append(en.getKey() + SDKConstants.EQUAL + en.getValue()
                    + SDKConstants.AMPERSAND);
        }
        return sf.substring(0, sf.length() - 1);
    }


    public static Map<String, String> coverResultString2Map(String result) {
        return convertResultStringToMap(result);
    }

    public static Map<String, String> convertResultStringToMap(String result) {
        Map<String, String> map = null;

        if (result != null && !"".equals(result.trim())) {
            if (result.startsWith("{") && result.endsWith("}")) {
                result = result.substring(1, result.length() - 1);
            }
            map = parseQString(result);
        }

        return map;
    }


    public static Map<String, String> parseQString(String str) {

        Map<String, String> map = new HashMap<String, String>();
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        char curChar;
        String key = null;
        boolean isKey = true;
        boolean isOpen = false;//值里有嵌套
        char openName = 0;
        if (len > 0) {
            for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
                curChar = str.charAt(i);// 取当前字符
                if (isKey) {// 如果当前生成的是key

                    if (curChar == '=') {// 如果读取到=分隔符
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else {// 如果当前生成的是value
                    if (isOpen) {
                        if (curChar == openName) {
                            isOpen = false;
                        }

                    } else {//如果没开启嵌套
                        if (curChar == '{') {//如果碰到，就开启嵌套
                            isOpen = true;
                            openName = '}';
                        }
                        if (curChar == '[') {
                            isOpen = true;
                            openName = ']';
                        }
                    }

                    if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    } else {
                        temp.append(curChar);
                    }
                }

            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }

    private static void putKeyValueToMap(StringBuilder temp, boolean isKey,
                                         String key, Map<String, String> map) {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, temp.toString());
        }
    }

    public static int getEncryptCert(Map<String, String> resData, String encoding) {
        String strCert = resData.get(SDKConstants.param_encryptPubKeyCert);
        String certType = resData.get(SDKConstants.param_certType);
        if (isEmpty(strCert) || isEmpty(certType))
            return -1;
        X509Certificate x509Cert = CertUtil.genCertificateByStr(strCert);
        if (CERTTYPE_01.equals(certType)) {
            // 更新敏感信息加密公钥
            if (!CertUtil.getEncryptCertId().equals(
                    x509Cert.getSerialNumber().toString())) {
                // ID不同时进行本地证书更新操作
                String localCertPath = SDKConfig.getConfig().getEncryptCertPath();
                String newLocalCertPath = genBackupName(localCertPath);
                // 1.将本地证书进行备份存储
                if (!copyFile(localCertPath, newLocalCertPath))
                    return -1;
                // 2.备份成功,进行新证书的存储
                if (!writeFile(localCertPath, strCert, encoding))
                    return -1;
                LogUtil.writeLog("save new encryptPubKeyCert success");
                CertUtil.resetEncryptCertPublicKey();
                return 1;
            } else {
                return 0;
            }

        } else if (CERTTYPE_02.equals(certType)) {
            return 0;
        } else {
            LogUtil.writeLog("unknown cerType:" + certType);
            return -1;
        }
    }

    public static boolean copyFile(String srcFile, String destFile) {
        boolean flag = false;
        FileInputStream fin = null;
        FileOutputStream fout = null;
        FileChannel fcin = null;
        FileChannel fcout = null;
        try {
            // 获取源文件和目标文件的输入输出流
            fin = new FileInputStream(srcFile);
            fout = new FileOutputStream(destFile);
            // 获取输入输出通道
            fcin = fin.getChannel();
            fcout = fout.getChannel();
            // 创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                // clear方法重设缓冲区，使它可以接受读入的数据
                buffer.clear();
                // 从输入通道中将数据读到缓冲区
                int r = fcin.read(buffer);
                // read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
                if (r == -1) {
                    flag = true;
                    break;
                }
                // flip方法让缓冲区可以将新读入的数据写入另一个通道
                buffer.flip();
                // 从输出通道中将数据写入缓冲区
                fcout.write(buffer);
            }
            fout.flush();
        } catch (IOException e) {
            LogUtil.writeErrorLog("CopyFile fail", e);
        } finally {
            try {
                if (null != fin)
                    fin.close();
                if (null != fout)
                    fout.close();
                if (null != fcin)
                    fcin.close();
                if (null != fcout)
                    fcout.close();
            } catch (IOException ex) {
                LogUtil.writeErrorLog("Releases any system resources fail", ex);
            }
        }
        return flag;
    }

    public static boolean writeFile(String filePath, String fileContent, String encoding) {
        FileOutputStream fout = null;
        FileChannel fcout = null;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        try {
            fout = new FileOutputStream(filePath);
            // 获取输出通道
            fcout = fout.getChannel();
            // 创建缓冲区
            // ByteBuffer buffer = ByteBuffer.allocate(1024);
            ByteBuffer buffer = ByteBuffer.wrap(fileContent.getBytes(encoding));
            fcout.write(buffer);
            fout.flush();
        } catch (FileNotFoundException e) {
            LogUtil.writeErrorLog("WriteFile fail", e);
            return false;
        } catch (IOException ex) {
            LogUtil.writeErrorLog("WriteFile fail", ex);
            return false;
        } finally {
            try {
                if (null != fout)
                    fout.close();
                if (null != fcout)
                    fcout.close();
            } catch (IOException ex) {
                LogUtil.writeErrorLog("Releases any system resources fail", ex);
                return false;
            }
        }
        return true;
    }

    public static String genBackupName(String fileName) {
        if (isEmpty(fileName))
            return "";
        int i = fileName.lastIndexOf(POINT);
        String leftFileName = fileName.substring(0, i);
        String rightFileName = fileName.substring(i + 1);
        String newFileName = leftFileName + "_backup" + POINT + rightFileName;
        return newFileName;
    }


    public static byte[] readFileByNIO(String filePath) {
        FileInputStream in = null;
        FileChannel fc = null;
        ByteBuffer bf = null;
        try {
            in = new FileInputStream(filePath);
            fc = in.getChannel();
            bf = ByteBuffer.allocate((int) fc.size());
            fc.read(bf);
            return bf.array();
        } catch (Exception e) {
            LogUtil.writeErrorLog(e.getMessage());
            return null;
        } finally {
            try {
                if (null != fc) {
                    fc.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                LogUtil.writeErrorLog(e.getMessage());
                return null;
            }
        }
    }

    public static Map<String, String> filterBlank(Map<String, String> contentData) {
        LogUtil.writeLog("打印请求报文域 :");
        Map<String, String> submitFromData = new HashMap<String, String>();
        Set<String> keySet = contentData.keySet();

        for (String key : keySet) {
            String value = contentData.get(key);
            if (value != null && !"".equals(value.trim())) {
                // 对value值进行去除前后空处理
                submitFromData.put(key, value.trim());
                LogUtil.writeLog(key + "-->" + String.valueOf(value));
            }
        }
        return submitFromData;
    }

    public static byte[] inflater(final byte[] inputByte) throws IOException {
        int compressedDataLength = 0;
        Inflater compresser = new Inflater(false);
        compresser.setInput(inputByte, 0, inputByte.length);
        ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
        byte[] result = new byte[1024];
        try {
            while (!compresser.finished()) {
                compressedDataLength = compresser.inflate(result);
                if (compressedDataLength == 0) {
                    break;
                }
                o.write(result, 0, compressedDataLength);
            }
        } catch (Exception ex) {
            System.err.println("Data format error!\n");
            ex.printStackTrace();
        } finally {
            o.close();
        }
        compresser.end();
        return o.toByteArray();
    }


    public static byte[] deflater(final byte[] inputByte) throws IOException {
        int compressedDataLength = 0;
        Deflater compresser = new Deflater();
        compresser.setInput(inputByte);
        compresser.finish();
        ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
        byte[] result = new byte[1024];
        try {
            while (!compresser.finished()) {
                compressedDataLength = compresser.deflate(result);
                o.write(result, 0, compressedDataLength);
            }
        } finally {
            o.close();
        }
        compresser.end();
        return o.toByteArray();
    }


    public static boolean isEmpty(String s) {
        return null == s || "".equals(s.trim());
    }

}
