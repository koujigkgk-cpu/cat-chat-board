<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>ログイン | キャット板</title>
<style>
    body {
        margin: 0;
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        font-family: 'Helvetica Neue', Arial, sans-serif;
        background: radial-gradient(circle at center, #1a2a44 0%, #050a14 100%);
        overflow: hidden;
    }

    body::before {
        content: "";
        position: absolute;
        width: 200%; height: 200%;
        background-image: 
            linear-gradient(rgba(88, 166, 255, 0.05) 1px, transparent 1px),
            linear-gradient(90deg, rgba(88, 166, 255, 0.05) 1px, transparent 1px);
        background-size: 100px 100px;
        transform: rotate(15deg);
        z-index: 0;
    }

    .glass-card {
        position: relative;
        z-index: 1;
        width: 380px;
        padding: 40px;
        background: rgba(255, 255, 255, 0.03); 
        backdrop-filter: blur(25px) saturate(150%);
        -webkit-backdrop-filter: blur(25px) saturate(150%);
        border-radius: 20px;
        border: 1px solid rgba(255, 255, 255, 0.1);
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4),
                    inset 0 0 0 1px rgba(255, 255, 255, 0.05);
        text-align: center;
    }

    .glass-card::before {
        content: "";
        position: absolute;
        top: 0; left: 15%; right: 15%;
        height: 1px;
        background: linear-gradient(90deg, transparent, rgba(88, 166, 255, 0.8), transparent);
    }

    h1 {
        color: #7abaff;
        font-size: 1.5rem;
        font-weight: 400;
        margin-bottom: 20px;
        letter-spacing: 2px;
    }

    /* --- ロゴ画像用の新しいスタイル --- */
    .logo-container {
        margin-bottom: 25px;
        display: flex;
        justify-content: center;
    }

    .cat-logo {
        width: 120px;         /* ロゴのサイズ */
        height: 120px;
        border-radius: 50%;   /* 正円にする */
        object-fit: cover;
        /* 背景のネオンに合わせた青い光の輪 */
        box-shadow: 0 0 20px rgba(88, 166, 255, 0.6), 
                    0 0 40px rgba(88, 166, 255, 0.2);
        border: 2px solid rgba(255, 255, 255, 0.2);
    }
    /* ------------------------------ */

    .input-label {
        display: block;
        text-align: left;
        color: rgba(255, 255, 255, 0.7);
        font-size: 0.85rem;
        margin: 15px 0 8px 5px;
    }

    input[type="text"],
    input[type="password"] {
        width: 100%;
        padding: 12px;
        background: rgba(255, 255, 255, 0.07);
        border: 1px solid rgba(255, 255, 255, 0.1);
        border-radius: 10px;
        color: white;
        font-size: 1rem;
        box-sizing: border-box;
        outline: none;
        transition: 0.3s;
    }

    input:focus {
        background: rgba(255, 255, 255, 0.12);
        border-color: #58a6ff;
    }

    input[type="submit"] {
        width: 100%;
        padding: 14px;
        margin-top: 30px;
        background: linear-gradient(to right, #4a9eff, #1da1f2);
        border: none;
        border-radius: 30px;
        color: white;
        font-size: 1.1rem;
        font-weight: bold;
        cursor: pointer;
        box-shadow: 0 5px 15px rgba(29, 161, 242, 0.3);
        transition: 0.3s;
    }

    input[type="submit"]:hover {
        transform: scale(1.02);
        box-shadow: 0 8px 20px rgba(29, 161, 242, 0.5);
    }
</style>
</head>
<body>

<div class="glass-card">
    <h1>キャット板へようこそ</h1>
    
    <div class="logo-container">
    <img src="${pageContext.request.contextPath}/Copilot_20260403_165901.png" alt="Logo" class="cat-logo">
</div>
    
    <form action="Login" method="post">
        <span class="input-label">ユーザー名：</span>
        <input type="text" name="name" required>
        
        <span class="input-label">パスワード：</span>
        <input type="password" name="pass" required>
        
        <input type="submit" value="ログイン">
    </form>
</div>

</body>
</html>