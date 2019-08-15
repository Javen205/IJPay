package com.ijpay.demo.controller.unionpay;

import com.ijpay.unionpay.SDKConfig;
import com.ijpay.unionpay.SDKConstants;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * 名称： demo中用到的方法<br>
 * 日期： 2015-09<br>
 
 * 版权： 中国银联<br>
 * 声明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障<br>
 */
public class DemoBase {

	//默认配置的是UTF-8
	public static String encoding = "UTF-8";
	
	//全渠道固定值
	public static String version = SDKConfig.getConfig().getVersion();
	
	//后台服务对应的写法参照 FrontRcvResponse.java
	public static String frontUrl = SDKConfig.getConfig().getFrontUrl();

	//后台服务对应的写法参照 BackRcvResponse.java
	public static String backUrl = SDKConfig.getConfig().getBackUrl();//受理方和发卡方自选填写的域[O]--后台通知地址

	// 商户发送交易时间 格式:YYYYMMDDhhmmss
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	// AN8..40 商户订单号，不能含"-"或"_"
	public static String getOrderId() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}
	
   /**
	 * 组装请求，返回报文字符串用于显示
	 * @param data
	 * @return
	 */
    public static String genHtmlResult(Map<String, String> data){

    	TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			String key = en.getKey(); 
			String value =  en.getValue();
			if("respCode".equals(key)){
				sf.append("<b>"+key + SDKConstants.EQUAL + value+"</br></b>");
			}else
				sf.append(key + SDKConstants.EQUAL + value+"</br>");
		}
		return sf.toString();
    }
    /**
	 * 功能：解析全渠道商户对账文件中的ZM文件并以List<Map>方式返回
	 * 适用交易：对账文件下载后对文件的查看
	 * @param filePath ZM文件全路径
	 * @return 包含每一笔交易中 序列号 和 值 的map序列
	 */
	public static List<Map<Integer,String>> parseZMFile(String filePath){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,32,98};
		return parseFile(filePath,lengthArray);
	}
	
	/**
	 * 功能：解析全渠道商户对账文件中的ZME文件并以List<Map>方式返回
	 * 适用交易：对账文件下载后对文件的查看
	 * @param filePath ZME文件全路径
	 * @return 包含每一笔交易中 序列号 和 值 的map序列
	 */
	public static List<Map<Integer,String>> parseZMEFile(String filePath){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,2,6,10,4,12,13,13,15,15,1,12,2,135};
		return parseFile(filePath,lengthArray);
	}
	
	/**
	 * 功能：解析全渠道商户 ZM,ZME对账文件
	 * @param filePath
	 * @param lengthArray 参照《全渠道平台接入接口规范 第3部分 文件接口》 全渠道商户对账文件 6.1 ZM文件和6.2 ZME 文件 格式的类型长度组成int型数组
	 * @return
	 */
	private static List<Map<Integer,String>> parseFile(String filePath,int lengthArray[]){
	 	List<Map<Integer,String>> ZmDataList = new ArrayList<Map<Integer,String>>();
	 	try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	//解析的结果MAP，key为对账文件列序号，value为解析的值
        		 	Map<Integer,String> ZmDataMap = new LinkedHashMap<Integer,String>();
                    //左侧游标
                    int leftIndex = 0;
                    //右侧游标
                    int rightIndex = 0;
                    for(int i=0;i<lengthArray.length;i++){
                    	rightIndex = leftIndex + lengthArray[i];
                    	String filed = lineTxt.substring(leftIndex,rightIndex);
                    	leftIndex = rightIndex+1;
                    	ZmDataMap.put(i, filed);
                    }
                    ZmDataList.add(ZmDataMap);
                }
                read.close();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
	 	
		return ZmDataList;	
	}
	 
    public static String getFileContentTable(List<Map<Integer,String>> dataList,String file){
    	StringBuffer  tableSb = new StringBuffer("对账文件的规范参考 https://open.unionpay.com/ajweb/help/file/ 产品接口规范->平台接口规范:文件接口</br> 文件【"+file + "】解析后内容如下：");
    	tableSb.append("<table border=\"1\">");
    	if(dataList.size() > 0){
    		Map<Integer,String> dataMapTmp = dataList.get(0);
    		tableSb.append("<tr>");
	 		for(Iterator<Integer> it = dataMapTmp.keySet().iterator();it.hasNext();){
	 			Integer key = it.next();
	 			String value = dataMapTmp.get(key);
		 		System.out.println("序号："+ (key+1) + " 值: '"+ value +"'");
		 		tableSb.append("<td>序号"+(key+1)+"</td>");
		 	}
	 		tableSb.append("</tr>");
    	}
    	
    	for(int i=0;i<dataList.size();i++){
	 		System.out.println("行数: "+ (i+1));
	 		Map<Integer,String> dataMapTmp = dataList.get(i);
	 		tableSb.append("<tr>");
	 		for(Iterator<Integer> it = dataMapTmp.keySet().iterator();it.hasNext();){
	 			Integer key = it.next();
	 			String value = dataMapTmp.get(key);
		 		System.out.println("序号："+ (key+1) + " 值: '"+ value +"'");
		 		tableSb.append("<td>"+value+"</td>");
		 	}
	 		tableSb.append("</tr>");
	 	}
    	tableSb.append("</table>");
    	return tableSb.toString();
    }

	
	public static List<String> unzip(String zipFilePath,String outPutDirectory){
		List<String> fileList = new ArrayList<String>();
		try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFilePath));//输入源zip路径  
            BufferedInputStream bin = new BufferedInputStream(zin);
            BufferedOutputStream bout = null;
            File file=null;  
            ZipEntry entry;
            try {
                while((entry = zin.getNextEntry())!=null && !entry.isDirectory()){
                	file = new File(outPutDirectory,entry.getName());  
                    if(!file.exists()){  
                        (new File(file.getParent())).mkdirs();  
                    }
                    bout = new BufferedOutputStream(new FileOutputStream(file));  
                    int b;
                    while((b=bin.read())!=-1){  
                    	bout.write(b);  
                    }
                    bout.flush();
                    fileList.add(file.getAbsolutePath());
                    System.out.println(file+"解压成功");
                }
            } catch (IOException e) {  
                e.printStackTrace();  
            }finally{
                try {
					bin.close();
					zin.close();
					if(bout!=null){
						bout.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}  
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
		return fileList;
	}

}