package com.harlan.smonitor.monitor.core.connection;

import com.jcraft.jsch.JSchException;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.HostMonitorItem;
import com.harlan.smonitor.monitor.common.Constants;
import com.harlan.smonitor.monitor.common.EncryptUtil;
import com.harlan.smonitor.monitor.common.GetHostPassword;
import com.harlan.smonitor.monitor.common.SshConnecter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;

public class SSHSource {
	private final static Logger logger = LoggerFactory.getLogger("detail");
	
	private static Map<String,List<SshConnecter>> hostSshMap = new HashMap<String,List<SshConnecter>>();
	
	/**
	 * 从连接池中获取ssh链接
	 * @param ip
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public static SshConnecter  obtainSSHConn(String ip,String username) throws CloneNotSupportedException{
		List<SshConnecter> sshList = hostSshMap.get(ip+"_"+username);
		if(sshList.size() <= 1){
			logger.info("指定IP:{}下的用户：{}链接没有可用数量，开始创建此链接的复制",ip,username);
			for(int i = 0; i < 5; i++){
				SshConnecter ssh = sshList.get(0).clone();
				sshList.add(ssh);
			}
		}
        //printSizeInfo(ip, username);
		SshConnecter ssh = sshList.remove(0);
		//printSizeInfo(ip, username);
		return ssh;
	}
	
	/**
	 * 把使用结束的链接重新回收到链接池中
	 * @param ip
	 * @param ssh
	 */
	public static void recoverSSHConn(String ip,String username,SshConnecter ssh){
		//printSizeInfo(ip, username);
		hostSshMap.get(ip+"_"+username).add(ssh);
		//printSizeInfo(ip, username);
	}
	
	/**
	 * 初始化连接池的ssh链接资源
	 * @param monitorItemList
	 */
	public static void initSSHSource(List<MonitorItem> monitorItemList){
		String passwdSwitch = ResourceBundle.getBundle("config").getString("xml_password");
		if("0".equals(passwdSwitch)){
			initByPwdFile();
		}else{
			initByItemXml(monitorItemList);
		}
	}
	
	/**
	 * 通过读取item的xml配置文件方式初始化连接池
	 * @param monitorItemList
	 */
	private static void initByItemXml(List<MonitorItem> monitorItemList){
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		//遍历每个主机配置
		for (MonitorItem monitorItem : monitorItemList) {
			if(monitorItem instanceof HostMonitorItem){}
			HostMonitorItem hostItem = (HostMonitorItem) monitorItem;
			String key = hostItem.getIp();
			Map<String, String> value = new HashMap<String,String>();
			value.put(hostItem.getUser(), hostItem.getPasswd());
			map.put(key, value);
		}
		if(map.size() == 0){
			logger.error("没有可用的SSH链接信息，请检查item项的xml文件host配置是否正确");
			return;
		}
		logger.info("初始化链接池-----by item xml");
		String ip = null;
		String username = null;
		String password = null;
		try {
			Integer port = 22;
			
			Set<Entry<String, Map<String, String>>> ipSet = map.entrySet();
			for (Entry<String, Map<String, String>> entry : ipSet) {
				ip = entry.getKey();
				Map<String, String> userMap = entry.getValue();
				Set<Entry<String, String>> userSet = userMap.entrySet();
				List<SshConnecter> sshList = new ArrayList<SshConnecter>();
				for (Entry<String, String> userEntry : userSet) {
					username = userEntry.getKey();
					password = userEntry.getValue();
					SshConnecter ssh = new SshConnecter(ip,port,username, password);
					sshList.add(ssh);
					sshList.add(ssh.clone());
					hostSshMap.put(ip+"_"+username, sshList);
				}
			}
			logger.info("连接池初始化完成："+hostSshMap);
		}catch(JSchException jse){
			logger.error("创建ip地址为：{}所使用的用户名:{}或者密码:{}不正确",ip,username,password);
		} catch (Exception e) {
			logger.error("初始化ssh连接池出现异常:",e.getMessage());
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 通过读取统一密码文件的方式初始化连接池
	 */
	private static void initByPwdFile(){
		logger.info("初始化链接池-----by pwd file");
		String username = null;
		String password = null;
		String ip = null;
		try {
			Integer port = 22;
			String fileName = Constants.SOURCE_HOST_PWD_FILE_NAME;
			
			File sourceFile = new File(Constants.ECHN_TOOL_HOME+File.separator+fileName);
			if(sourceFile.exists()){
				EncryptUtil.encryptFile(fileName);
			}else{
				File encryptFile = new File(GetHostPassword.echnToolHome+File.separator+fileName+"_e");
				if(!encryptFile.exists()){
					throw new FileNotFoundException("指定的密码文件不存在-->“"+sourceFile.getAbsolutePath()+",或者"+encryptFile.getAbsolutePath()+"”,请查看文件是否存在");
				}
			}
			GetHostPassword ga = GetHostPassword.getInstance(fileName+"_e");
			Map<String, Map<String, String>> map = ga.getPasswordMap();
			Set<Entry<String, Map<String, String>>> ipSet = map.entrySet();
			for (Entry<String, Map<String, String>> entry : ipSet) {
				ip = entry.getKey();
				Map<String, String> userMap = entry.getValue();
				Set<Entry<String, String>> userSet = userMap.entrySet();
				List<SshConnecter> sshList = new ArrayList<SshConnecter>();
				for (Entry<String, String> userEntry : userSet) {
					username = userEntry.getKey();
					password = userEntry.getValue();
					SshConnecter ssh = new SshConnecter(ip,port,username, password);
					sshList.add(ssh);
					sshList.add(ssh.clone());
					hostSshMap.put(ip+"_"+username, sshList);
				}
			}
			logger.info("连接池初始化完成："+hostSshMap);
		}catch(JSchException jse){
			logger.error("创建ip地址为：{}所使用的用户名:{}或者密码:{}不正确",ip,username,password);
		} catch (Exception e) {
			logger.error("初始化ssh连接池出现异常:",e.getMessage());
			e.printStackTrace();
		}
	} 
	
	public static int getSSHConnSize(){
		return hostSshMap.size();
	}
	
	/**
	 * 日志记录当前链接数量信息
	 * @param ip
	 * @param username
	 */
	private static void printSizeInfo(String ip ,String username){
		int size = hostSshMap.get(ip+"_"+username).size();
		logger.info("当前ip：[{}]下的用户：[{}]可使用的SSH链接数：[{}]",ip,username,size);
	}
	private SSHSource(){}
}
