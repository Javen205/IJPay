package com.ijpay.unionpay;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author UnionPay
 */
public class AcpService {

    public static Map<String, String> sign(Map<String, String> reqData, String encoding) {
        reqData = SDKUtil.filterBlank(reqData);
        SDKUtil.sign(reqData, encoding);
        return reqData;
    }
    public static Map<String, String> signByCertInfo(Map<String, String> reqData, String certPath, String certPwd, String encoding) {
        reqData = SDKUtil.filterBlank(reqData);
        SDKUtil.signByCertInfo(reqData, certPath, certPwd, encoding);
        return reqData;
    }

    public static Map<String, String> signBySecureKey(Map<String, String> reqData, String secureKey, String encoding) {
        reqData = SDKUtil.filterBlank(reqData);
        SDKUtil.signBySecureKey(reqData, secureKey, encoding);
        return reqData;
    }

    public static boolean validate(Map<String, String> rspData, String encoding) {
        return SDKUtil.validate(rspData, encoding);
    }

    public static boolean validateBySecureKey(Map<String, String> rspData, String secureKey, String encoding) {
        return SDKUtil.validateBySecureKey(rspData, secureKey, encoding);
    }

    public static String createAutoFormHtml(String reqUrl, Map<String, String> hiddenMap, String encoding) {
        StringBuffer sf = new StringBuffer();
        sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + encoding + "\"/></head><body>");
        sf.append("<form id = \"pay_form\" action=\"" + reqUrl
                + "\" method=\"post\">");
        if (null != hiddenMap && 0 != hiddenMap.size()) {
            Set<Entry<String, String>> set = hiddenMap.entrySet();
            Iterator<Entry<String, String>> it = set.iterator();
            while (it.hasNext()) {
                Entry<String, String> ey = it.next();
                String key = ey.getKey();
                String value = ey.getValue();
                sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\""
                        + key + "\" value=\"" + value + "\"/>");
            }
        }
        sf.append("</form>");
        sf.append("</body>");
        sf.append("<script type=\"text/javascript\">");
        sf.append("document.all.pay_form.submit();");
        sf.append("</script>");
        sf.append("</html>");
        return sf.toString();
    }


    public static String enCodeFileContent(String filePath, String encoding) {
        String baseFileContent = "";

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtil.writeErrorLog(e.getMessage(), e);
            }
        }
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            int fl = in.available();
            if (null != in) {
                byte[] s = new byte[fl];
                in.read(s, 0, fl);
                // 压缩编码.
                baseFileContent = new String(SecureUtil.base64Encode(SDKUtil.deflater(s)), encoding);
            }
        } catch (Exception e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.writeErrorLog(e.getMessage(), e);
                }
            }
        }
        return baseFileContent;
    }

    public static String deCodeFileContent(Map<String, String> resData, String fileDirectory, String encoding) {
        // 解析返回文件
        String filePath = null;
        String fileContent = resData.get(SDKConstants.param_fileContent);
        if (null != fileContent && !"".equals(fileContent)) {
            FileOutputStream out = null;
            try {
                byte[] fileArray = SDKUtil.inflater(SecureUtil
                        .base64Decode(fileContent.getBytes(encoding)));
                if (SDKUtil.isEmpty(resData.get("fileName"))) {
                    filePath = fileDirectory + File.separator + resData.get("merId")
                            + "_" + resData.get("batchNo") + "_"
                            + resData.get("txnTime") + ".txt";
                } else {
                    filePath = fileDirectory + File.separator + resData.get("fileName");
                }
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                out = new FileOutputStream(file);
                out.write(fileArray, 0, fileArray.length);
                out.flush();
            } catch (UnsupportedEncodingException e) {
                LogUtil.writeErrorLog(e.getMessage(), e);
            } catch (IOException e) {
                LogUtil.writeErrorLog(e.getMessage(), e);
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

    public static String getFileContent(String fileContent, String encoding) {
        String fc = "";
        try {
            fc = new String(SDKUtil.inflater(SecureUtil.base64Decode(fileContent.getBytes())), encoding);
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        } catch (IOException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        }
        return fc;
    }


    public static String getCustomerInfo(Map<String, String> customerInfoMap, String accNo, String encoding) {

        if (customerInfoMap.isEmpty()) {
            return "{}";
        }
        StringBuffer sf = new StringBuffer("{");
        for (Iterator<String> it = customerInfoMap.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            String value = customerInfoMap.get(key);
            if ("pin".equals(key)) {
                if (null == accNo || "".equals(accNo.trim())) {
                    LogUtil.writeLog("送了密码（PIN），必须在getCustomerInfo参数中上传卡号");
                    throw new RuntimeException("加密PIN没送卡号无法后续处理");
                } else {
                    value = encryptPin(accNo, value, encoding);
                }
            }
            sf.append(key).append(SDKConstants.EQUAL).append(value);
            if (it.hasNext()) {
                sf.append(SDKConstants.AMPERSAND);
            }
        }
        String customerInfo = sf.append("}").toString();
        LogUtil.writeLog("组装的customerInfo明文：" + customerInfo);
        try {
            return new String(SecureUtil.base64Encode(sf.toString().getBytes(
                    encoding)), encoding);
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        } catch (IOException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        }
        return customerInfo;
    }

    public static String getCustomerInfoWithEncrypt(Map<String, String> customerInfoMap, String accNo, String encoding) {
        if (customerInfoMap.isEmpty()) {
            return "{}";
        }
        StringBuffer sf = new StringBuffer("{");
        //敏感信息加密域
        StringBuffer encryptedInfoSb = new StringBuffer("");

        for (Iterator<String> it = customerInfoMap.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            String value = customerInfoMap.get(key);
            if ("phoneNo".equals(key) || "cvn2".equals(key) || "expired".equals(key)) {
                encryptedInfoSb.append(key).append(SDKConstants.EQUAL).append(value).append(SDKConstants.AMPERSAND);
            } else {
                if ("pin".equals(key)) {
                    if (null == accNo || "".equals(accNo.trim())) {
                        LogUtil.writeLog("送了密码（PIN），必须在getCustomerInfoWithEncrypt参数中上传卡号");
                        throw new RuntimeException("加密PIN没送卡号无法后续处理");
                    } else {
                        value = encryptPin(accNo, value, encoding);
                    }
                }
                sf.append(key).append(SDKConstants.EQUAL).append(value).append(SDKConstants.AMPERSAND);
            }
        }

        if (!"".equals(encryptedInfoSb.toString())) {
            //去掉最后一个&符号
            encryptedInfoSb.setLength(encryptedInfoSb.length() - 1);
            LogUtil.writeLog("组装的customerInfo encryptedInfo明文：" + encryptedInfoSb.toString());
            sf.append("encryptedInfo").append(SDKConstants.EQUAL).append(encryptData(encryptedInfoSb.toString(), encoding));
        } else {
            sf.setLength(sf.length() - 1);
        }

        String customerInfo = sf.append("}").toString();
        LogUtil.writeLog("组装的customerInfo明文：" + customerInfo);
        try {
            return new String(SecureUtil.base64Encode(sf.toString().getBytes(encoding)), encoding);
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        } catch (IOException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        }
        return customerInfo;
    }

    public static Map<String, String> parseCustomerInfo(String customerInfo, String encoding) {
        Map<String, String> customerInfoMap = null;
        try {
            byte[] b = SecureUtil.base64Decode(customerInfo.getBytes(encoding));
            String customerInfoNoBase64 = new String(b, encoding);
            LogUtil.writeLog("解base64后===>" + customerInfoNoBase64);
            //去掉前后的{}
            customerInfoNoBase64 = customerInfoNoBase64.substring(1, customerInfoNoBase64.length() - 1);
            customerInfoMap = SDKUtil.parseQString(customerInfoNoBase64);
            if (customerInfoMap.containsKey("encryptedInfo")) {
                String encInfoStr = customerInfoMap.get("encryptedInfo");
                customerInfoMap.remove("encryptedInfo");
                String encryptedInfoStr = decryptData(encInfoStr, encoding);
                Map<String, String> encryptedInfoMap = SDKUtil.parseQString(encryptedInfoStr);
                customerInfoMap.putAll(encryptedInfoMap);
            }
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        } catch (IOException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        }
        return customerInfoMap;
    }

    public static Map<String, String> parseCustomerInfo(String customerInfo, String certPath, String certPwd, String encoding) {
        Map<String, String> customerInfoMap = null;
        try {
            byte[] b = SecureUtil.base64Decode(customerInfo.getBytes(encoding));
            String customerInfoNoBase64 = new String(b, encoding);
            LogUtil.writeLog("解base64后===>" + customerInfoNoBase64);
            //去掉前后的{}
            customerInfoNoBase64 = customerInfoNoBase64.substring(1, customerInfoNoBase64.length() - 1);
            customerInfoMap = SDKUtil.parseQString(customerInfoNoBase64);
            if (customerInfoMap.containsKey("encryptedInfo")) {
                String encInfoStr = customerInfoMap.get("encryptedInfo");
                customerInfoMap.remove("encryptedInfo");
                String encryptedInfoStr = decryptData(encInfoStr, certPath, certPwd, encoding);
                Map<String, String> encryptedInfoMap = SDKUtil.parseQString(encryptedInfoStr);
                customerInfoMap.putAll(encryptedInfoMap);
            }
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        } catch (IOException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        }
        return customerInfoMap;
    }

    public static String encryptPin(String accNo, String pin, String encoding) {
        return SecureUtil.encryptPin(accNo, pin, encoding, CertUtil
                .getEncryptCertPublicKey());
    }

    public static String encryptData(String data, String encoding) {
        return SecureUtil.encryptData(data, encoding, CertUtil
                .getEncryptCertPublicKey());
    }

    /**
     * 敏感信息解密，使用配置文件acp_sdk.properties解密
     *
     * @param base64EncryptedInfo 加密信息
     * @param encoding            编码格式
     * @return 解密后的明文
     */
    public static String decryptData(String base64EncryptedInfo, String encoding) {
        return SecureUtil.decryptData(base64EncryptedInfo, encoding, CertUtil.getSignCertPrivateKey());
    }

    public static String decryptData(String base64EncryptedInfo, String certPath, String certPwd, String encoding) {
        return SecureUtil.decryptData(base64EncryptedInfo, encoding, CertUtil.getSignCertPrivateKeyByStoreMap(certPath, certPwd));
    }

    public static String getEncryptCertId() {
        return CertUtil.getEncryptCertId();
    }

    public static String base64Encode(String rawStr, String encoding) throws IOException {
        byte[] rawByte = rawStr.getBytes(encoding);
        return new String(SecureUtil.base64Encode(rawByte), encoding);
    }

    public static String base64Decode(String base64Str, String encoding) throws IOException {
        byte[] rawByte = base64Str.getBytes(encoding);
        return new String(SecureUtil.base64Decode(rawByte), encoding);
    }


    public static String getCardTransData(Map<String, String> cardTransDataMap, Map<String, String> requestData, String encoding) {
        StringBuffer cardTransDataBuffer = new StringBuffer();

        if (cardTransDataMap.containsKey("track2Data")) {
            StringBuffer track2Buffer = new StringBuffer();
            track2Buffer.append(requestData.get("merId"))
                    .append(SDKConstants.COLON).append(requestData.get("orderId"))
                    .append(SDKConstants.COLON).append(requestData.get("txnTime"))
                    .append(SDKConstants.COLON).append(requestData.get("txnAmt") == null ? 0 : requestData.get("txnAmt"))
                    .append(SDKConstants.COLON).append(cardTransDataMap.get("track2Data"));
            cardTransDataMap.put("track2Data",
                    AcpService.encryptData(track2Buffer.toString(), encoding));
        }

        if (cardTransDataMap.containsKey("track3Data")) {
            StringBuffer track3Buffer = new StringBuffer();
            track3Buffer.append(requestData.get("merId"))
                    .append(SDKConstants.COLON).append(requestData.get("orderId"))
                    .append(SDKConstants.COLON).append(requestData.get("txnTime"))
                    .append(SDKConstants.COLON).append(requestData.get("txnAmt") == null ? 0 : requestData.get("txnAmt"))
                    .append(SDKConstants.COLON).append(cardTransDataMap.get("track3Data"));
            cardTransDataMap.put("track3Data",
                    AcpService.encryptData(track3Buffer.toString(), encoding));
        }

        return cardTransDataBuffer.append(SDKConstants.LEFT_BRACE)
                .append(SDKUtil.coverMap2String(cardTransDataMap))
                .append(SDKConstants.RIGHT_BRACE).toString();

    }

    public static int updateEncryptCert(Map<String, String> resData, String encoding) {
        return SDKUtil.getEncryptCert(resData, encoding);
    }

    public static int genLuHn(String number) {
        return SecureUtil.genLuHn(number);
    }
}
