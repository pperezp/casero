package cl.casero.model;

import android.os.StrictMode;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.mail.smtp.SMTPTransport;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//https://app.mailgun.com/app/dashboard
public class TestMail {
    public static JsonNode sendSimpleMessage() throws UnirestException {

        final String DOMAIN = "sandbox540758d57c6c431b882d9928f31e40f2.mailgun.org";
        final String API_KEY = "947076eb2c7af7e0d97dcf627feb1920-7bce17e5-135e67b6";

        HttpResponse<JsonNode> request =
                Unirest.post("https://api.mailgun.net/v3/" + DOMAIN + "/messages")
                .basicAuth("api", API_KEY)
                .field("from", "Casero <casero@casero.cl>")
                .field("to", "patodeath@gmail.com")
                .field("subject", "Hola!")
                .field("text", "Hola desde android")
                .asJson();

        return request.getBody();
    }

    public static void testSmtp() throws MessagingException {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy =
                new StrictMode
                .ThreadPolicy
                .Builder()
                .permitAll()
                .build();

            StrictMode.setThreadPolicy(policy);

            Properties props = System.getProperties();
            props.put("mail.smtps.host", "smtp.mailgun.org");
            props.put("mail.smtps.auth", "true");

            Session session = Session.getInstance(props, null);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("casero@casero.com"));

            InternetAddress[] addrs = InternetAddress.parse("patodeath@gmail.com", false);
            msg.setRecipients(Message.RecipientType.TO, addrs);

            msg.setSubject("Hello");
            msg.setText("Testing some Mailgun awesomness");
            msg.setSentDate(new Date());

            SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
            t.connect(
                "smtp.mailgun.org",
                "postmaster@sandbox540758d57c6c431b882d9928f31e40f2.mailgun.org",
                "a30da13c07ed50574cb48a2d2d06d5f0-7bce17e5-2169bd40"
            );
            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();
        }


    }

}
