package model;
import java.io.Serializable;

public class User implements Serializable {
    private String id;    // DBのUSER_IDに対応
    private String name;  // DBのNAMEに対応
    private String pass;  // DBのPASSに対応

    public User() {}

    // ログイン・登録時に使うコンストラクタ
    public User(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    // 全データ用
    public User(String id, String name, String pass) {
        this.id = id;
        this.name = name;
        this.pass = pass;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPass() { return pass; }
}