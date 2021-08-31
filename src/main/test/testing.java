package main.test;

public class testing {
    final static String  gmail = "nice@gmail.com ";
    public static void main(String[] args) {
        System.out.println(checkGmail());
    }
    public static boolean checkGmail(){
        StringBuilder checkDomain= new StringBuilder();

        for(int i = 0; i<gmail.length();i++){
            char letter = gmail.charAt(i);
            if (letter=='@') {
                for (int j = i; j < gmail.length(); j++) {
                    if(gmail.charAt(j)!=' ') {
                        checkDomain.append(gmail.charAt(j));
                    }
                    else j++;
                }
                if (checkDomain.toString().equals("@gmail.com")) {
                    return true;
                }
            }

        }
        return false;
    }
}
