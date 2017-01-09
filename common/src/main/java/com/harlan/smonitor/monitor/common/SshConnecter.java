package com.harlan.smonitor.monitor.common;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * ssh连接器
 *
 * @author zhaohl
 */
public class SshConnecter implements Cloneable{
    private final Logger logger = LoggerFactory.getLogger(SshConnecter.class);

    private ChannelSftp sftpChannel = null;
    private Session sshSession = null;
    private boolean localhost = false;

    /**
     * 建立 ssh 连接
     *
     * @param host     如果host为 127.0.0.1 或者 127.1 ，则使用本地连接
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @throws Exception
     */
    public SshConnecter(String host, int port, String username, String password) throws Exception {
        if ("127.0.0.1".equals(host) || "127.1".equals(host)) {
            logger.info("localhost oprate ...");
            localhost = true;
        } else {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            logger.debug("Connected to {} , port:{}, username:{} ,passwd:{}", host, port, username, password);
        }
    }

    /***
     * 断开连接
     */
    public void disconnect() {
        if (sftpChannel != null) {
            sftpChannel.disconnect();
            sftpChannel.exit();
            logger.debug("sftpChannel disconnect...");
        }
        if (sshSession != null) {
            sshSession.disconnect();
            logger.debug("sshSession disconnect...");
        }

    }

    /**
     * 上传文件到指定服务器（目录没有创建会新建目录，处理异常后一定要关闭连接）
     *
     * @param localFilePath  待上传的本地文件路径
     * @param remoteFilePath 上传后的文件路径
     * @throws Exception 抛出异常的情况：a、远程未连接  b、文件超过2G  c、本地文件未找到
     */
    public void upload(String localFilePath, String remoteFilePath) throws Exception {
        File inputFile = new File(localFilePath);
        if (inputFile.length() > Integer.MAX_VALUE) {
            throw new RuntimeException("file too big...");
        }
        if (inputFile.isFile()) {
            byte b[] = new byte[(int) inputFile.length()];
            InputStream inputStream = new FileInputStream(inputFile);
            inputStream.read(b);
            upload(b, remoteFilePath);
            inputStream.close();
        } else {
            throw new RuntimeException("file not found...");
        }
    }

    /**
     * 上传文件到指定服务器（目录没有创建会新建目录，处理异常后一定要关闭连接）
     *
     * @param bytes          待上传的文件byte[]
     * @param remoteFilePath 上传后的文件路径（要是绝对路径）
     * @throws Exception
     */
    public void upload(byte[] bytes, String remoteFilePath) throws Exception {
        if (localhost) {
            String dir = remoteFilePath.substring(0, remoteFilePath.lastIndexOf("/"));
            logger.debug("(本地目录是)" + dir);
            File dir_file = new File(dir);
            if (!dir_file.exists()) {
                dir_file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(remoteFilePath);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } else {
            createSftpChannel();
            String dir = remoteFilePath.substring(0, remoteFilePath.lastIndexOf("/"));
            logger.debug("(远程目录是)" + dir);
            String[] folders = dir.split("/");
            StringBuilder absolute_path = new StringBuilder();
            for (String folder : folders) {
                if (folder.length() > 0) {
                    absolute_path.append("/").append(folder);
                    try {
                        sftpChannel.cd(absolute_path.toString());
                    } catch (SftpException e) {
                        logger.debug("新建远程文件夹: {}", absolute_path.toString());
                        sftpChannel.mkdir(absolute_path.toString());
                        sftpChannel.cd(absolute_path.toString());
                    }
                }
            }
            sftpChannel.put(new ByteArrayInputStream(bytes), remoteFilePath);
        }
        logger.info("(上传成功,是否为本地文件:)" + localhost + "(文件路径:)" + remoteFilePath);
    }

    /**
     * 使用sftp上传下载时，先创建 sftp channel
     *
     * @throws Exception
     */
    private void createSftpChannel() throws Exception {
        if (sftpChannel == null) {
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
        }
    }

    /**
     * 下载文件
     *
     * @param remoteFilePath 待下载的文件路径
     * @param localFilePath  下载后的文件路径
     * @throws Exception
     */
    public void download(String remoteFilePath, String localFilePath) throws Exception {
        createSftpChannel();
        sftpChannel.get(remoteFilePath, new FileOutputStream(new File(localFilePath)));
    }

    /**
     * 删除文件
     *
     * @param filePath 文件绝对路径
     * @throws Exception 抛出异常的情况：1、未连接 2、文件不存在
     */
    public void delete(String filePath) throws Exception {
        if (localhost) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            } else {
                throw new RuntimeException("file not found...");
            }
        } else {
            createSftpChannel();
            sftpChannel.rm(filePath);
        }
        logger.info("(删除成功,是否为本地文件:)" + localhost + "(文件路径:)" + filePath);
    }

    /**
     * 执行 命令行（处理异常后一定要关闭连接）
     *
     * @param command 要执行的命令（控制台输入的命令行，如 ls -ll）
     * @return 命令产生的结果
     * @throws Exception
     */
    public List<String> command(String command) throws Exception {
        List<String> response = new ArrayList<String>();
        ChannelExec channel = (ChannelExec) sshSession.openChannel("exec");
        channel.setCommand(command);//每次执行命令需要新打开一个channel
        channel.setInputStream(null);
        channel.setErrStream(System.err);
        channel.connect();
        logger.debug("需要执行的命令是:{}", command);
        InputStream in = channel.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String buf;
        while ((buf = reader.readLine()) != null) {
            response.add(buf);
        }
        channel.disconnect();
        return response;
    }
    @Override
    public SshConnecter clone() throws CloneNotSupportedException {
    	return (SshConnecter) super.clone();
    }
}