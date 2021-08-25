package Test1;

import java.time.LocalDate;

public class User {

    private String name, gmail, phone, gender, password,confirmPass, authCode;
    private LocalDate dob;
    private boolean sent;

    User(String name, String gmail, String phone,
         LocalDate dob, String gender, String authCode, String password, String confirmPass, Boolean sent){
        this.name = name;
        this.gmail = gmail;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.authCode = authCode;
        this.password = password;
        this.confirmPass = confirmPass;
        this.sent = sent;
    }

    User(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
