package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ProfileDAO;
import model.GetMutterListLogic;
import model.Mutter;
import model.PostMutterLogic;
import model.Profile;
import model.User;

@WebServlet("/Main")
public class Main extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. つぶやきリストの取得 (Logic経由)
        GetMutterListLogic getMutterListLogic = new GetMutterListLogic();
        List<Mutter> mutterList = getMutterListLogic.execute();
        request.setAttribute("mutterList", mutterList);
        
        // 2. ユーザー情報の取得
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        // 3. ログイン済みならプロフィール情報も取得
        if (loginUser != null) {
            ProfileDAO profileDao = new ProfileDAO();
            Profile profile = profileDao.findByUserId(loginUser.getId());
            request.setAttribute("profile", profile);
            
            // フォワード
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/main.jsp");
            dispatcher.forward(request, response);
        } else {
            // 未ログインならインデックスへ
            response.sendRedirect("index.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        // 1. ログインチェック
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 2. パラメータの取得
        String text = request.getParameter("text");
        String replyIdStr = request.getParameter("replyId");
        
        // ★重要: JSがセットしたSupabase上の画像URLを受け取る
        // main.jspの <input type="hidden" name="supabaseImageUrl"> と一致させています
        String imageUrl = request.getParameter("supabaseImageUrl"); 

        int replyId = 0;
        boolean isSuccess = false;

        try {
            // 返信IDのパース
            if(replyIdStr != null && !replyIdStr.isEmpty()) {
                try { 
                    replyId = Integer.parseInt(replyIdStr); 
                } catch (NumberFormatException e) { 
                    replyId = 0; 
                }
            }

            // 3. 投稿処理の実行
            if (text != null && !text.trim().isEmpty()) {
                // Java側では画像保存処理はせず、受け取ったURLをそのままMutterに渡す
                Mutter mutter = new Mutter(loginUser.getName(), text, imageUrl, replyId);
                
                PostMutterLogic postMutterLogic = new PostMutterLogic();
                postMutterLogic.execute(mutter);
                
                isSuccess = true;
            } else {
                request.setAttribute("errorMsg", "つぶやきを入力してください。");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "システムエラーが発生しました。");
        }

        // 4. 処理結果に応じた遷移
        if (isSuccess) {
            // 二重投稿防止のためリダイレクト
            response.sendRedirect("Main");
        } else {
            // 失敗時は現在の情報を再セットして入力画面(JSP)へ
            GetMutterListLogic getMutterListLogic = new GetMutterListLogic();
            List<Mutter> mutterList = getMutterListLogic.execute();
            request.setAttribute("mutterList", mutterList);
            
            ProfileDAO profileDao = new ProfileDAO();
            request.setAttribute("profile", profileDao.findByUserId(loginUser.getId()));

            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/main.jsp");
            dispatcher.forward(request, response);
        }
    }
}
