package model;
import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String pass;

    public User() {}

    // ★ここをチェック！ 引数が2つのものと3つのもの、両方用意しておくと安全です
    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public User(String id, String name, String pass) {
        this.id = id;
        this.name = name;
        this.pass = pass;
    }

    // Getterメソッド
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPass() { return pass; }
}