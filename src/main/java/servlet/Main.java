package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import dao.ProfileDAO;
import model.GetMutterListLogic;
import model.Mutter;
import model.PostMutterLogic;
import model.Profile;
import model.User;

@WebServlet("/Main")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,     // 1ファイル最大10MB
    maxRequestSize = 1024 * 1024 * 50   // リクエスト全体最大50MB
)
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
        
        // ★追加: セッションとログインユーザーのチェック (ヌルポ対策)
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            // ログインしていない場合はログイン画面へ戻す
            response.sendRedirect("index.jsp");
            return;
        }

        String text = null;
        String filename = null;
        int replyId = 0;
        boolean isSuccess = false;

        try {
            // 1. 返信IDの取得
            String replyIdStr = request.getParameter("replyId");
            if(replyIdStr != null && !replyIdStr.isEmpty()) {
                try { 
                    replyId = Integer.parseInt(replyIdStr); 
                } catch (NumberFormatException e) { 
                    replyId = 0; 
                }
            }

            // 2. テキストと画像の取得
            text = request.getParameter("text");
            Part part = request.getPart("image");
            filename = (part != null) ? part.getSubmittedFileName() : null;

            // 3. 投稿処理
            if (text != null && !text.trim().isEmpty()) {
                // 画像がある場合のみ保存処理
                if (filename != null && !filename.isEmpty()) {
                    // ★修正: PC用パスではなく、サーバーが書き込み可能な場所を自動選択
                    String path = request.getServletContext().getRealPath("/images/");
                    
                    java.io.File uploadDir = new java.io.File(path);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs(); // フォルダがなければ作成
                    }
                    
                    // ファイルを物理保存
                    part.write(path + java.io.File.separator + filename);
                }
                
                // Mutterインスタンスを生成して保存
                // ここで loginUser.getName() を呼んでも、上で null チェック済みなので安全です
                Mutter mutter = new Mutter(loginUser.getName(), text, filename, replyId);
                PostMutterLogic postMutterLogic = new PostMutterLogic();
                postMutterLogic.execute(mutter);
                
                isSuccess = true;
            } else {
                request.setAttribute("errorMsg", "つぶやきを入力してください。");
            }

        } catch (IllegalStateException | ServletException e) {
            request.setAttribute("errorMsg", "アップロードに失敗しました。");
        }

        // 4. 結果に応じた画面遷移
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
