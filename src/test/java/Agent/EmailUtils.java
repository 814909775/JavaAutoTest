package Agent;

import io.cucumber.java.Scenario;
import org.apache.commons.beanutils.PropertyUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static Agent.MyFunctions.loadConfig;
import static Glue.Hook.currentTime;
import static Glue.Hook.scenarioResults;
import static net.masterthought.cucumber.ReportResult.getCurrentTime;

public class EmailUtils {
    @SuppressWarnings("unchecked")
    public static Map<String,String> getEmailInfo(){

        HashMap<String, Object> map = loadConfig();
        Map<String, String> emailInfoMap = (Map<String, String>) map.get("EmailReport");
        if (emailInfoMap == null) {
            throw new RuntimeException("未配置邮件发送信息");
        }
        return emailInfoMap;
    }

    public static void sendEmail(Map<String, String> emailConfig) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        // 获取邮件发送信息
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
        //邮件debug
        session.setDebug(false);
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
            message.setSubject("Cucumber自动化测试报告"+currentTime); //getCurrentTime()

            //添加测试结果

            String htmlContent = addScenarioResultsToEmail(scenarioResults);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            // 设置HTML内容类型
            messageBodyPart.setContent(htmlContent, "text/html; charset=utf-8");
           // messageBodyPart.setText("自动化测试已完成，请查看附件中的详细报告。\n"+scenarioTable);
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
    public static String addScenarioResultsToEmail(List<Scenario> scenarios) {
        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append("<html><body>");
        tableBuilder.append("<h2>自动化测试报告</h2>");
        tableBuilder.append("<p>自动化测试已完成，请查看附件中的详细报告。</p>");
        tableBuilder.append("<table border='1' style='border-collapse: collapse;'>");

        // 统计信息
        long passedCount = scenarios.stream().filter(s -> !s.isFailed()).count();
        long failedCount = scenarios.stream().filter(Scenario::isFailed).count();

        tableBuilder.append("<p><strong>执行统计：</strong>通过 ").append(passedCount)
                .append("，失败 ").append(failedCount).append("</p>");

        // 场景结果表格
        tableBuilder.append("<p><strong>概览</strong>");
        tableBuilder.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
        tableBuilder.append("<tr style='background-color: #f2f2f2;'><th>场景名称</th><th>执行结果</th></tr>");

        for (Scenario scenario : scenarios) {
            String name = scenario.getName();
            String result = scenario.isFailed() ? "失败" : "通过";
            String bgColor = scenario.isFailed() ? "#ffcccc" : "#ccffcc";

            tableBuilder.append("<tr style='background-color: ").append(bgColor).append(";'>");
            tableBuilder.append("<td>").append(name).append("</td>");
            tableBuilder.append("<td>").append(result).append("</td>");
            tableBuilder.append("</tr>");
        }

        tableBuilder.append("</table>");
        tableBuilder.append("</body></html>");

        // 将表格添加到邮件内容中
        String emailContent = tableBuilder.toString();
        return emailContent;
    }
}
