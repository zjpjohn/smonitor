package com.harlan.smonitor.monitor.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class GetHostPassword {
	protected final static Logger logger = LoggerFactory.getLogger("detail");
	private static Map<String, Map<String, String>> passwordMap;
	private static final String DEC_ENC_SPLIT = " ";
	private static GetHostPassword instance;
	public static final String echnToolHome = Constants.ECHN_TOOL_HOME;
	private static final String EPT_FILE = echnToolHome + File.separator;
	
	public static synchronized GetHostPassword getInstance(String fileName) {
		if (instance == null) {
			instance = new GetHostPassword();
			File file = new File(EPT_FILE + fileName);
			if(!file.exists()){
				throw new RuntimeException("指定加密文件不存在:"+file.getAbsolutePath());
			}else{
				readDecryptFile(EPT_FILE + fileName);
			}
		}
		return instance;
	}

	private static void readDecryptFile(String writeFileUrl) {
		BufferedReader reader = null;
		passwordMap = new HashMap<String, Map<String, String>>();
		try {
			File file = new File(writeFileUrl);
			if (file.canRead()) {
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				DESDecryptNew des = new DESDecryptNew();

				while ((tempString = reader.readLine()) != null) {
					if (!"".equals(tempString.trim())) {
						String descryptText = des.decrypt(tempString.trim());
						String[] decryAry = descryptText.split(DEC_ENC_SPLIT);

						if (decryAry.length >= 4){
							String key = decryAry[2];
							Map<String, String> user = passwordMap.get(key);
							if(user == null){
								user = new HashMap<String,String>();
							}
							user.put(decryAry[3], decryAry[4]);
							passwordMap.put(key, user);
						}
					}
				}
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();

			if (reader != null)
				try {
					reader.close();
				} catch (Exception localException1) {
				}
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (Exception localException2) {
				}
		}
	}

	public String getPassword(String ip,String username) {
		String password = null;
		password = passwordMap.get(ip).get(username);
		if(password == null){
			throw new RuntimeException("not found this ip or user infomation:{"+ip+":"+username+"}");
		}
		return password;
	}
	public Map<String, Map<String, String>> getPasswordMap() {
		return passwordMap;
	}
	private GetHostPassword(){}
}