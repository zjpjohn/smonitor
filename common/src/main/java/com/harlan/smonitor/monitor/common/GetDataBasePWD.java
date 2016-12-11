package com.harlan.smonitor.monitor.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class GetDataBasePWD {
	protected final static Logger logger = LoggerFactory.getLogger("detail");
	private static Map<String, String> passwordMap;
	private static final String DEC_ENC_SPLIT = " ";
	private static GetDataBasePWD instance;
	public static final String echnToolHome = Constants.ECHN_TOOL_HOME;
	private static final String EPT_FILE = echnToolHome + File.separator;
	
	public static synchronized GetDataBasePWD getInstance(String fileName) {
		if (instance == null) {
			instance = new GetDataBasePWD();
			File file = new File(EPT_FILE + fileName);
			if(!file.exists()){
				String sourceFileName = EPT_FILE+Constants.SOURCE_DATABASED_PWD_FILE_NAME; 
				File source = new File(sourceFileName);
				if(source.exists()){
					EncryptUtil.encryptFile(fileName);
				}else{
					/*
					String host = "10.112.102.72";
					int port = 21;
					String username = "pwdmgr";
					String password = "Qazpl_23";
					try {
						SshConnecter ssh = new SshConnecter(host, port, username, password);
						String remoteFilePath = "/home/pwdmgr/des/*";
						ssh.download(remoteFilePath , echnToolHome);
						if(source.exists()){
							EncryptUtil.encryptFile(fileName);
						}else{
							throw new RuntimeException("指定加密文件不存在:"+sourceFileName);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					*/
					throw new RuntimeException("指定加密文件不存在:"+sourceFileName);
				}
			}else{
				readDecryptFile(EPT_FILE + fileName);
			}
		}
		return instance;
	}

	private static void readDecryptFile(String writeFileUrl) {
		BufferedReader reader = null;
		passwordMap = new HashMap<String, String>();
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
							String key = decryAry[1];
							passwordMap.put(key, decryAry[4]);
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

	public String getDBPassword(String key) {
		String password = null;
		password = passwordMap.get(key);
		if(password == null){
			throw new RuntimeException("not found this key:{"+key+"}");
		}
		return password;
	}
	private GetDataBasePWD(){}
}