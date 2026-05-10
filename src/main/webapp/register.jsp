<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>新規登録 | キャット板</title>
<style>
    /* ログイン画面と共通のスタイル */
    body {
        margin: 0; height: 100vh; display: flex; justify-content: center; align-items: center;
        font-family: 'Helvetica Neue', Arial, sans-serif;
        background: radial-gradient(circle at center, #1a2a44 0%, #050a14 100%);
        overflow: hidden;
    }
    body::before {
        content: ""; position: absolute; width: 200%; height: 200%;
        background-image: linear-gradient(rgba(88, 166, 255, 0.05) 1px, transparent 1px), linear-gradient(90deg, rgba(88, 166, 255, 0.05) 1px, transparent 1px);
        background-size: 100px 100px; transform: rotate(15deg); z-index: 0;
    }
    .glass-card {
        position: relative; z-index: 1; width: 380px; padding: 40px;
        background: rgba(255, 255, 255, 0.03); backdrop-filter: blur(25px) saturate(150%);
        border-radius: 20px; border: 1px solid rgba(255, 255, 255, 0.1);
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4); text-align: center;
    }
    h1 { color: #2ecc71; font-size: 1.5rem; font-weight: 400; margin-bottom: 20px; letter-spacing: 2px; }
    .logo-container { margin-bottom: 25px; display: flex; justify-content: center; }
    .cat-logo {
        width: 120px; height: 120px; border-radius: 50%; object-fit: cover;
        box-shadow: 0 0 20px rgba(46, 204, 113, 0.4); border: 2px solid rgba(255, 255, 255, 0.2);
    }
    .input-label { display: block; text-align: left; color: rgba(255, 255, 255, 0.7); font-size: 0.85rem; margin: 15px 0 8px 5px; }
    input[type="text"], input[type="password"] {
        width: 100%; padding: 12px; background: rgba(255, 255, 255, 0.07);
        border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 10px; color: white; outline: none;
    }
    input[type="submit"] {
        width: 100%; padding: 14px; margin-top: 30px;
        background: linear-gradient(to right, #2ecc71, #27ae60);
        border: none; border-radius: 30px; color: white; font-size: 1.1rem; font-weight: bold; cursor: pointer;
    }
    .back-link { display: block; margin-top: 25px; color: rgba(255, 255, 255, 0.6); text-decoration: none; font-size: 0.85rem; }
</style>
</head>
<body>

<div class="glass-card">
    <h1>新規メンバー登録</h1>
    
    <div class="logo-container">
        <img src="${pageContext.request.contextPath}/Copilot_20260403_165901.png" alt="Logo" class="cat-logo">
    </div>
    
    <form action="Register" method="post">
        <span class="input-label">希望ユーザー名：</span>
        <input type="text" name="name" required>
        
        <span class="input-label">パスワード：</span>
        <input type="password" name="pass" required>
        
        <input type="submit" value="アカウントを作成">
    </form>

    <a href="index.jsp" class="back-link">← ログイン画面へ戻る</a>
</div>

</body>
</html>