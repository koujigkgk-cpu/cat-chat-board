package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; // MultipartConfigは不要になるため削除
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
        // つぶやきリストの取得
        GetMutterListLogic getMutterListLogic = new GetMutterListLogic();
        List<Mutter> mutterList = getMutterListLogic.execute();
        request.setAttribute("mutterList", mutterList);
        
        // ユーザー情報の取得
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        // プロフィール情報の取得
        if (loginUser != null) {
            ProfileDAO profileDao = new ProfileDAO();
            Profile profile = profileDao.findByUserId(loginUser.getId());
            request.setAttribute("profile", profile);
        }

        // フォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/main.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        // セッションとログインユーザーのチェック
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String text = request.getParameter("text");
        String replyIdStr = request.getParameter("replyId");
        
        // ★重要: JSPのJavaScriptから送られてくる「Supabase上の画像URL」を取得
        String imageUrl = request.getParameter("supabaseImageUrl"); 

        int replyId = 0;
        boolean isSuccess = false;

        try {
            // 1. 返信IDの解析
            if(replyIdStr != null && !replyIdStr.isEmpty()) {
                try { 
                    replyId = Integer.parseInt(replyIdStr); 
                } catch (NumberFormatException e) { 
                    replyId = 0; 
                }
            }

            // 2. 投稿処理
            if (text != null && !text.trim().isEmpty()) {
                // ファイル保存処理（part.writeなど）はすべてJavaScript側に移行したため
                // ここでは受け取ったimageUrlをそのままMutterオブジェクトにセットします
                
                Mutter mutter = new Mutter(loginUser.getName(), text, imageUrl, replyId);
                PostMutterLogic postMutterLogic = new PostMutterLogic();
                postMutterLogic.execute(mutter);
                
                isSuccess = true;
            } else {
                request.setAttribute("errorMsg", "つぶやきを入力してください。");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "投稿処理中にエラーが発生しました。");
        }

        // 3. 結果に応じた画面遷移
        if (isSuccess) {
            response.sendRedirect("Main");
        } else {
            // 失敗時はリストを再取得してフォワード
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
