package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.DeleteMutterLogic;

@WebServlet("/Delete")
public class DeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // JSPのリンクから送られてきた id と image を取得
        String idStr = request.getParameter("id");
        String image = request.getParameter("image");

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);

                // ★修正箇所: Mutterオブジェクトを作らず、値をそのままロジックに渡す
                DeleteMutterLogic logic = new DeleteMutterLogic();
                logic.execute(id, image); 
                
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // 削除が終わったらメイン画面にリダイレクト
        response.sendRedirect("Main");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}