package main.java.registration;

import com.sun.mail.smtp.SMTPAddressFailedException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <h1>MAILING SYSTEM</h1>
 * <p>This class is responsible for sending mail to the end users</p>
 */
public class MailVerify {
    public static int OTP;
    public static void sendMail(String name, String recepient) throws Exception{

        OTP = (int) (Math.random()*90000)+10000;


        System.out.println("Now sending the mail to "+recepient);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        String myAccountEmail = "noreply.thisisforaproject@gmail.com";
        String myAccountPassword = Pass.getPass();

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail,myAccountPassword);
            }
        });

        Message message = prepareMsg(session, myAccountEmail, recepient, OTP, name);

        assert message != null;
        Transport.send(message);
        System.out.println("A message has been Sent and the code is " + OTP);

    }

    /**
     * <h2>Message Packer</h2>
     * <p>This method packages the message to be sent to end users</p>
     * @param session is mailing session
     * @param myAccountEmail is the senders gmail account
     * @param recepient is the recepient gmail account
     * @param OTP is the 5-digit auth code
     * @param name is the recepient name
     * @return Message
     */
    private static Message prepareMsg(Session session, String myAccountEmail, String recepient, int OTP, String name){

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(recepient));
            message.setSubject("Verification Code | RuthlessRaccoons - RaccoonsInnRooms");
            message.setText("Hello "+name+ "!\n" +OTP+ " is your verification code.\n" + "Thanks for using our " +
                    "service.\n-RuthlessRaccoons");
            return message;

        } catch (SMTPAddressFailedException e){
            e.printStackTrace();
        } catch (MessagingException e) {
            Logger.getLogger(MailVerify.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}