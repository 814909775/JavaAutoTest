package Agent;

import org.apache.commons.beanutils.PropertyUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static Agent.MyFunctions.loadConfig;
import static net.masterthought.cucumber.ReportResult.getCurrentTime;

public class EmailUtils {
    public static Map<String,String> getEmailInfo(){

        HashMap<String, Object> map = loadConfig();
        Map<String, String> emailInfoMap = (Map<String, String>) map.get("EmailReport");
        if (emailInfoMap == null) {
            throw new RuntimeException("未配置邮件发送信息");
        }
        return emailInfoMap;
    }

    public static void sendEmail() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        // 获取邮件发送信息
        Map<String, String> emailConfig = EmailUtils.getEmailInfo();
        String username = PropertyUtils.getProperty(emailConfig, "from").toString();
        String password = PropertyUtils.getProperty(emailConfig, "password").toString();
        String to = PropertyUtils.getProperty(emailConfig, "to").toString();
        String host = PropertyUtils.getProperty(emailConfig, "host").toString();
        String port = PropertyUtils.getProperty(emailConfig, "port").toString();
        String reportPath = System.getProperty ("user.dir")+"/"+PropertyUtils.getProperty(emailConfig, "attachment").toString();
        Properties props = new Properties();

        // 添加超时配置
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        /*// 163邮箱可能需要额外配置
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.163.com");
        // 针对163邮箱465端口的SSL配置
        if ("587".equals(port)) {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
        }*/


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println("Auth Username: " + username);     // 调试输出
                System.out.println("Auth Password: " + (password != null));
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        // 测试连接
       /* try {
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.163.com", 25, username, password);
            System.out.println("SMTP连接测试成功");
            transport.close();
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }*/
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Cucumber自动化测试报告"+getCurrentTime());

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("自动化测试已完成，请查看附件中的详细报告。");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart attachPart = new MimeBodyPart();
            attachPart.attachFile(reportPath);
            multipart.addBodyPart(attachPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("测试报告邮件已发送!");
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }




    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        EmailUtils.sendEmail();
    }

}
