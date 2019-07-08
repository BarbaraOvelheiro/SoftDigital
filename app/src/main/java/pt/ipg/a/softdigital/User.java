package pt.ipg.a.softdigital;

public class User {

    private String UserName; // Nome do utilizador

    public User() {

    }

    public User(String userName) {
        UserName = userName;
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

}
