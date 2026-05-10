package model;

import dao.UserDAO;

public class LoginLogic {
  public boolean execute(User user) {
    // データベース操作用のDAOを準備
    UserDAO dao = new UserDAO();
    
    // 修正ポイント：DBから一致するユーザーを探す
    User result = dao.findByLogin(user);
    
    // 結果がnullでなければ「ユーザーが存在する＝ログイン成功」
    return result != null;
  }
}