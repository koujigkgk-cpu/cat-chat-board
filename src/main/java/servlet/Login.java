package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest; //requestメソッド
import jakarta.servlet.http.HttpServletResponse;//responseメソッド
import jakarta.servlet.http.HttpSession;

import model.LoginLogic;
import model.User;

@WebServlet("/Login")
public class Login extends HttpServlet {
  private static final long serialVersionUID = 1L;

 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // リクエストパラメータの取得
    request.setCharacterEncoding("UTF-8");
    String id = request.getParameter("name"); // 画面の入力項目が「名前」ならそのままでOK
    String pass = request.getParameter("pass");
    
    // Userインスタンスの生成
    // 第1引数にID、第2引数に名前を入れる設計なら、入力値を適切に割り当てます
    User user = new User(id, id, pass); 
    
    // ログイン処理
    LoginLogic loginLogic = new LoginLogic();
    
    // loginLogic.execute 内で AccountsDAO を呼び出し、
    // DBに一致するユーザーがいれば true が返ってくるはずです
    boolean isLogin = loginLogic.execute(user);

    // ログイン成功時の処理
    if (isLogin) {
      HttpSession session = request.getSession();
      session.setAttribute("loginUser", user);
    }
    
    // ログイン結果画面にフォワード
    RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/loginResult.jsp");
    dispatcher.forward(request, response);
  }
