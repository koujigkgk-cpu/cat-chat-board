package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDAO;
import model.User;

@WebServlet("/Register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        
        // JSPの input から取得
        String userId = request.getParameter("name"); // 画面上の「ユーザー名」をIDとして使用
        String pass = request.getParameter("pass");

        // 今回のDB構造に合わせてUserオブジェクトを生成
        // (ID, NAME, PASS) の順。とりあえず名前にも同じ値を入れておきます。
        User user = new User(userId, userId, pass);

        UserDAO dao = new UserDAO();
        boolean isSuccess = dao.create(user);

        if (isSuccess) {
            response.sendRedirect("index.jsp");
        } else {
            // 失敗時はとりあえず登録画面へ
            response.sendRedirect("register.jsp");
        }
    }
}