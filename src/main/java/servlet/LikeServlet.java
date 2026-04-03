package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.LikeDAO;
import model.User;

@WebServlet("/LikeServlet")
public class LikeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 肉球ボタン押下時の非同期リクエストを処理します
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. パラメータの取得（どの投稿へのアクションか）
        String mutterIdStr = request.getParameter("mutterId");
        
        // 2. セッションからログインユーザー情報を取得
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        // 3. バリデーション（IDが存在し、かつログインしている場合のみ実行）
        if (mutterIdStr != null && loginUser != null) {
            try {
                int mutterId = Integer.parseInt(mutterIdStr);
                String userName = loginUser.getName();

                // 4. LikeDAOを呼び出してDB更新処理
                // toggleLikeメソッドは「更新後の最新カウント数」を返すように設計します
                LikeDAO dao = new LikeDAO();
                int newCount = dao.toggleLike(mutterId, userName);

                // 5. レスポンスの設定
                // 画面全体をリロードしないため、HTMLではなく「数字のみ」を返します
                response.setContentType("text/plain; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(newCount);
                
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}