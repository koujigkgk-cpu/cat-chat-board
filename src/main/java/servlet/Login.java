package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.LoginLogic;
import model.User;

@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. パラメータの取得
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String pass = request.getParameter("pass");

        // 2. 入力情報をUserインスタンスにまとめる
        User user = new User(name, name, pass);

        // 3. ログイン処理（DB検索）の実行
        LoginLogic loginLogic = new LoginLogic();
        boolean isLogin = loginLogic.execute(user);

        // 4. 結果に応じた処理
        if (isLogin) {
            // 成功：セッションに保存
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);
        }

        // 5. 結果画面へフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/loginResult.jsp");
        dispatcher.forward(request, response);
    }
}