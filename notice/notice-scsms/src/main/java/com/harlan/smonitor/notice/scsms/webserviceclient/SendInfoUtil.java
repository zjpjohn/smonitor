package com.harlan.smonitor.notice.scsms.webserviceclient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.harlan.smonitor.monitor.common.Util;
import org.w3c.dom.Document;


public class SendInfoUtil {
	private static Logger logger = Logger.getLogger(SendInfoUtil.class.getName());
	private static final String templateId = "20257151";
	public static Resp sendMobileMsg(String phoneNo,String content){
		logger.info("准备发送短信：发送到："+phoneNo+"，发送的信息为："+content);

		String contentStr = "{\"MSG\":\"" + content  + "\"}";
		SendInfo s = new SendInfo_Service().getSendInfoHttpSoap11Endpoint();
		//拼接字符串参数
		String pin = geneParaString(contentStr, phoneNo);

		String ret = s.callService(pin);

		logger.info("调用webservice的发送短信接口返回结果："+ret);

		return parseXmlResp(ret);

	}

	public static String geneParaString(String contentStr,String phoneNo){

		Random r=new Random();
		String time= Util.date2String(new Date());
		int ran=r.nextInt(899);
		ran=ran+100;
		String seq =time+ran;
		String pin = "<![CDATA[<?xml version=\"1.0\" encoding=\"GBK\"?><ROOT>"+
				"<SYSID type=\"string\">2</SYSID>"+
				"<COMEFROM type=\"string\">999</COMEFROM>"+
				"<SEQ type=\"string\">"+seq+"</SEQ>"+
				"<TEMPLATEID type=\"string\">"+templateId+"</TEMPLATEID>"+
				"<PARAMS type=\"string\">"+contentStr+"</PARAMS>"+
				"<SERVICENO type=\"string\"></SERVICENO>"+
				"<PHONENO type=\"string\">"+phoneNo+"</PHONENO>"+
				"<LOGINNO type=\"string\">admin</LOGINNO>"+
				"<SERVNO type=\"string\"></SERVNO>"+
				"<SERVNAME type=\"string\"></SERVNAME>"+
				"<SUBPHONESEQ type=\"string\"></SUBPHONESEQ>"+
				"<SENDTIME type=\"string\">"+time+"</SENDTIME>"+
				"<HOLD1 type=\"string\"></HOLD1>"+
				"<HOLD2 type=\"string\">1000</HOLD2>"+
				"<HOLD3 type=\"string\"></HOLD3>"+
				"<HOLD4 type=\"string\"></HOLD4></ROOT>]]>";
		return pin;
	}

	private static Resp parseXmlResp(String xml){
		if(xml == null || "".equals(xml.trim())){
			return null;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream in = new ByteArrayInputStream(xml.getBytes());
			Document doc = builder.parse(in);
			String code = doc.getElementsByTagName("RETURN_CODE").item(0).getTextContent();
			String msg = doc.getElementsByTagName("RETURN_MSG").item(0).getTextContent();
			Resp resp = new Resp();
			resp.setCode(code);
			resp.setMsg(msg);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class Resp{
		private String code;
		private String msg;
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		@Override
		public String toString() {
			return "Resp [code=" + code + ", msg=" + msg + "]";
		}
	}

}
