package models;

public class User {
    private String userName;
    private String password;
    private String nickname;
    private String email;
    private boolean isMale;
    private SecurityQuestion securityQuestion;

    Player player;

    public User(String userName, String password, String nickname, String email, boolean isMale) {
        this.userName = userName;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.isMale = isMale;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public boolean isMale() {
        return isMale;
    }

    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }
}
