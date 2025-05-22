public abstract class User {
    protected String tc;
    protected String password;

    public User(String tc, String password) {
        this.tc = tc;
        this.password = password;
    }
    public abstract String getRole(); // Her alt sınıf kendine özel rol döndürsün
    public String getTc() {
        return tc;
    }

    public String getPassword() {
        return password;
    }
}

