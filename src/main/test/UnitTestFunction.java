package main.test;

import main.java.registration.MailVerify;
import main.java.registration.SceneOneController;
import main.java.registration.SceneTwoController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <h2>Unit testing on Fuctions</h2>
 */
@SuppressWarnings("ALL")
public class UnitTestFunction {
    SceneOneController sceneOneController;
    SceneTwoController sceneTwoController;
    @Before
    public void init(){
         sceneOneController = new SceneOneController();
         sceneTwoController = new SceneTwoController();

    }

    @Test
    public void passwordStrengthCheck(){
        String PASSWORD_WITH_NUMBER_AND_STRING = "abcd1234";
        assertTrue(sceneTwoController.checkPasswordStrength(PASSWORD_WITH_NUMBER_AND_STRING));
    }

    @Test
    public void gmailDomainCheck(){
        String ANY_GMAIL_ADDRESS = "anuragbharati26@gmail.com";
        assertTrue(sceneOneController.checkGmail(ANY_GMAIL_ADDRESS));
    }

    @Test
    public void gmailValidityCheck() throws Exception {
        String YOUR_NAME = "Anurag";
        String YOUR_GMAIL_ACCOUNT = "anruagbharati26@gmail.com";
        MailVerify.sendMail(YOUR_NAME,YOUR_GMAIL_ACCOUNT);
        assertNotNull(MailVerify.OTP);
    }
    @After
    public void cleanUp(){
        sceneOneController = null;
        sceneTwoController = null;
        Runtime.getRuntime().gc();
    }
}
