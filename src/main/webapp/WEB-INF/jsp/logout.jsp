<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>ログアウト | キャット板</title>
<style>
    body {
        margin: 0; height: 100vh; display: flex; justify-content: center; align-items: center;
        font-family: sans-serif; background: radial-gradient(circle at center, #1a2a44 0%, #050a14 100%);
        color: white;
    }
    .logout-card {
        width: 350px; padding: 40px; text-align: center;
        background: rgba(255, 255, 255, 0.03); backdrop-filter: blur(20px);
        border-radius: 20px; border: 1px solid rgba(255, 255, 255, 0.1);
    }
    h1 { color: #58a6ff; font-size: 1.4rem; margin-bottom: 20px; }
    p { color: rgba(255,255,255,0.7); margin-bottom: 30px; }
    .btn-home {
        display: block; padding: 12px; background: rgba(255,255,255,0.1);
        color: white; text-decoration: none; border-radius: 30px; font-weight: bold;
    }
</style>
</head>
<body>
<div class="logout-card">
    <div style="font-size: 3rem; margin-bottom: 10px;">👋</div>
    <h1>ログアウト完了</h1>
    <p>ご利用ありがとうございました。<br>またのご利用をお待ちしております。</p>
    <a href="index.jsp" class="btn-home">トップページへ</a>
</div>
</body>
</html>