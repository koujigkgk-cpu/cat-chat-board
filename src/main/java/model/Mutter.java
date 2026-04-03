package model;
import java.io.Serializable;

public class Mutter implements Serializable {
  private int id;
  private String userName;
  private String text;
  private String image;
  private String createdAt;
  private int replyId;
  private int likeCount; // ★追加：肉球の数を保持する変数

  public Mutter() { }

  // 投稿用コンストラクタ
  public Mutter(String userName, String text, String image, int replyId) {
    this.userName = userName;
    this.text = text;
    this.image = image;
    this.replyId = replyId;
  }

  // 表示用コンストラクタ（★likeCountを追加）
  public Mutter(int id, String userName, String text, String image, String createdAt, int replyId, int likeCount) {
    this.id = id;
    this.userName = userName;
    this.text = text;
    this.image = image;
    this.createdAt = createdAt;
    this.replyId = replyId;
    this.likeCount = likeCount; // ★追加
  }

  public int getId() { return id; }
  public String getUserName() { return userName; }
  public String getText() { return text; }
  public String getImage() { return image; }
  public String getCreatedAt() { return createdAt; }
  public int getReplyId() { return replyId; }
  public int getLikeCount() { return likeCount; } // ★追加
}