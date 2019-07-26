package pt.ipg.a.softdigital;

public class User {

    private String UserName; // Nome do utilizador
    private String UserEmail; // Email do utilizador
    private String UserRegime; // Regime em que se encontra o utilizador


    public User() {

    }

    public User(String userName, String userEmail, String userRegime) {
        UserName = userName;
        UserEmail = userEmail;
        UserRegime = userRegime;
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

    public String getUserRegime() {
        return UserRegime;
    }

    public void setUserRegime(String userRegime) {
        UserRegime = userRegime;
    }
}
