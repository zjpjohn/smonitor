package com.harlan.smonitor.notice.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by harlan on 2017/2/7.
 */
public class SendEmail {
    private final static Logger logger = LoggerFactory.getLogger(SendEmail.class);

    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    public static String emailAccount =null;
    public static String emailPassword = null;

    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    public static String emailSMTPHost = null;

    // 收件人邮箱（替换为自己知道的有效邮箱）
    public  String receiveMailAccount = null;

    public SendEmail(String receiveMailAccount) {
        this.receiveMailAccount = receiveMailAccount;
    }

    static{
        ResourceBundle rb = ResourceBundle.getBundle("notice-email");
        emailAccount=rb .getString("emailAccount");
        emailPassword=rb .getString("emailPassword");
        emailSMTPHost=rb .getString("emailSMTPHost");
        logger.info("emailAccount:{}",emailAccount);
        logger.info("emailPassword:{}",emailPassword);
        logger.info("emailSMTPHost:{}",emailSMTPHost);
    }


    public boolean sendMail(String title, String content){
        try {
            // 1. 创建参数配置, 用于连接邮件服务器的参数配置
            Properties props = new Properties();                    // 参数配置
            props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.host", emailSMTPHost);        // 发件人的邮箱的 SMTP 服务器地址
            props.setProperty("mail.smtp.auth", "true");            // 请求认证，参数名称与具体实现有关

            // 2. 根据配置创建会话对象, 用于和邮件服务器交互
            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);                                 // 设置为debug模式, 可以查看详细的发送 log

            // 3. 创建一封邮件
            MimeMessage message = createMimeMessage(session, emailAccount, receiveMailAccount,title,content);

            // 4. 根据 Session 获取邮件传输对象
            Transport transport = session.getTransport();

            // 5. 使用 邮箱账号 和 密码 连接邮件服务器
            //    这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
            transport.connect(emailAccount, emailPassword);

            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());

            // 7. 关闭连接

            transport.close();
        } catch (Exception e) {
            logger.error("发送邮件错误：",e);
        }

        return true;
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @param title 邮件标题
     * @param content 邮件内容
     * @return
     * @throws Exception
     */
    private static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,String title,String content) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, "Smonitor", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, receiveMail, "UTF-8"));

        // 4. Subject: 邮件主题
        message.setSubject(title, "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(content, "text/html;charset=UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }
}
