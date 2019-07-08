package pt.ipg.a.softdigital;

public class User {

    private String UserName; // Nome do utilizador
    private String UserEmail; // Email do utilizador


    public User() {

    }

    public User(String userName, String userEmail) {
        UserName = userName;
        UserEmail = userEmail;
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail(){
        return UserEmail;
    }

    public void setUserEmail(String userEmail){
        UserEmail = userEmail;
    }

}
