package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import dao.ProfileDAO;
import model.Profile;
import model.User;

@WebServlet("/ProfileServlet")
// locationを指定しないと一時ファイルが作れず失敗することがあるため空文字を指定
@MultipartConfig(location="", maxFileSize=1048576) 
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) { response.sendRedirect("index.jsp"); return; }

        String userId = request.getParameter("userId");
        if (userId == null || userId.isEmpty()) { userId = loginUser.getId(); }

        ProfileDAO dao = new ProfileDAO();
        Profile profile = dao.findByUserId(userId);

        if (profile == null) {
            // 初期表示用のデフォルト値
            profile = new Profile(userId, loginUser.getName(), "未設定", "", "", "default_icon.png");
        }
        request.setAttribute("profile", profile);
        request.setAttribute("isMine", userId.equals(loginUser.getId()));
        request.getRequestDispatcher("WEB-INF/jsp/profile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 実行確認ログ
        System.out.println("--- ProfileServlet doPost 開始 ---");
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            System.out.println("エラー：セッションが切れています");
            response.sendRedirect("index.jsp");
            return;
        }

        // 2. 値の受け取り
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String personality = request.getParameter("personality");
        String introduction = request.getParameter("introduction");

        // デバッグログ：受け取った値をコンソールに表示
        System.out.println("受取データ: Name=" + name + ", Address=" + address);

        // 3. アイコン画像の処理
        String iconFilename = "default_icon.png";
        try {
            Part part = request.getPart("icon");
            if (part != null && part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
                iconFilename = part.getSubmittedFileName();
                System.out.println("画像あり: " + iconFilename);
            } else {
                // 画像がない場合は既存の値を維持
                ProfileDAO dao = new ProfileDAO();
                Profile oldProfile = dao.findByUserId(loginUser.getId());
                if (oldProfile != null) {
                    iconFilename = oldProfile.getIconFilename();
                }
            }
        } catch (Exception e) {
            System.out.println("画像処理でエラー（続行します）: " + e.getMessage());
        }

        // 4. オブジェクト作成
        Profile profile = new Profile(
            loginUser.getId(),
            name,
            address,
            personality,
            introduction,
            iconFilename
        );
        
        // 5. データベース更新
        ProfileDAO dao = new ProfileDAO();
        System.out.println("データベース更新を実行します...");
        
        if (dao.update(profile)) {
            System.out.println("更新成功！Mainへリダイレクトします");
            response.sendRedirect("Main"); 
        } else {
            System.out.println("更新失敗：DAOでエラーが発生しました");
            request.setAttribute("errorMsg", "プロフィールの更新に失敗しました");
            request.setAttribute("profile", profile);
            request.setAttribute("isMine", true);
            request.getRequestDispatcher("WEB-INF/jsp/profile.jsp").forward(request, response);
        }
    }
}