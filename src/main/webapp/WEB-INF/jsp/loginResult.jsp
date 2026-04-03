<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.User" %>
<%
User loginUser = (User) session.getAttribute("loginUser");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>認証結果 | キャット板</title>
<style>
    body {
        margin: 0; height: 100vh; display: flex; justify-content: center; align-items: center;
        font-family: 'Helvetica Neue', Arial, sans-serif;
        background: radial-gradient(circle at center, #1a2a44 0%, #050a14 100%);
        color: white; overflow: hidden;
    }
    .result-card {
        width: 380px; padding: 40px; text-align: center;
        background: rgba(255, 255, 255, 0.03); 
        backdrop-filter: blur(25px) saturate(150%);
        border-radius: 20px; border: 1px solid rgba(255, 255, 255, 0.1);
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
    }
    .result-logo {
        width: 110px; height: 110px; border-radius: 50%; object-fit: cover;
        margin-bottom: 20px; border: 2px solid rgba(255, 255, 255, 0.2);
    }
    .success-glow { box-shadow: 0 0 20px rgba(88, 166, 255, 0.6); }
    h1 { font-size: 1.5rem; margin-bottom: 10px; letter-spacing: 1px; }
    .success-text { color: #58a6ff; }
    .error-text { color: #e0245e; }
    p { color: rgba(255, 255, 255, 0.7); margin-bottom: 30px; }
    .btn-link {
        display: block; padding: 14px; border-radius: 30px; text-decoration: none;
        font-weight: bold; transition: 0.3s;
    }
    .btn-success { background: linear-gradient(to right, #4a9eff, #1da1f2); color: white; }
    .btn-error { background: rgba(255, 255, 255, 0.1); color: white; border: 1px solid var(--border); }
</style>
</head>
<body>
<div class="result-card">
    <% if(loginUser != null) { %>
        <img src="${pageContext.request.contextPath}/Copilot_20260403_165901.png" class="result-logo success-glow">
        <h1 class="success-text">認証成功</h1>
        <p>おかえりなさい、<%= loginUser.getName() %> さん</p>
        <a href="Main" class="btn-link btn-success">タイムラインへ</a>
    <% } else { %>
        <div style="font-size: 4rem; margin-bottom: 15px;">🔒</div>
        <h1 class="error-text">認証失敗</h1>
        <p>ユーザー名またはパスワードが違います</p>
        <a href="index.jsp" class="btn-link btn-error">トップへ戻る</a>
    <% } %>
</div>
</body>
</html>