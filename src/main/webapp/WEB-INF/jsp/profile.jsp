<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>プロフィール - キャット板</title>
<style>
    :root {
        --bg-color: #0d1117;
        --card-bg: rgba(255, 255, 255, 0.05);
        --text-main: #e6edf3;
        --text-sub: #8b949e;
        --accent: #58a6ff;
        --border: rgba(255, 255, 255, 0.1);
        --input-bg: rgba(255, 255, 255, 0.07);
    }

    body {
        margin: 0;
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
        background: radial-gradient(circle at center, #1a2a44 0%, #050a14 100%);
        background-attachment: fixed;
        color: var(--text-main);
        padding: 20px;
        box-sizing: border-box;
    }

    .profile-container {
        width: 100%;
        max-width: 500px;
        background: var(--card-bg);
        backdrop-filter: blur(20px) saturate(150%);
        -webkit-backdrop-filter: blur(20px) saturate(150%);
        border-radius: 20px;
        padding: 40px;
        border: 1px solid var(--border);
        box-shadow: 0 20px 50px rgba(0, 0, 0, 0.5);
    }

    h2 {
        color: var(--accent);
        font-size: 1.5rem;
        border-bottom: 1px solid var(--border);
        padding-bottom: 15px;
        margin-top: 0;
        display: flex;
        align-items: center;
        justify-content: space-between;
        letter-spacing: 1px;
    }

    .icon-area {
        text-align: center;
        margin-bottom: 30px;
    }

    /* プロフィールアイコンの装飾 */
    .profile-icon {
        width: 130px;
        height: 130px;
        border-radius: 50%;
        object-fit: cover;
        border: 2px solid var(--accent);
        box-shadow: 0 0 20px rgba(88, 166, 255, 0.4);
        background: #161b22;
    }

    .field-group {
        margin-bottom: 25px;
    }

    label {
        display: block;
        color: var(--text-sub);
        font-size: 0.85rem;
        margin-bottom: 8px;
        margin-left: 5px;
    }

    .value-display {
        font-size: 1.1rem;
        padding: 10px 5px;
        border-bottom: 1px solid var(--border);
        color: var(--text-main);
    }

    /* 入力フォームのスタイル */
    input[type="text"], textarea {
        width: 100%;
        padding: 12px 15px;
        background: var(--input-bg);
        border: 1px solid var(--border);
        border-radius: 10px;
        color: white;
        box-sizing: border-box;
        font-size: 1rem;
        outline: none;
        transition: 0.3s;
    }

    input[type="text"]:focus, textarea:focus {
        border-color: var(--accent);
        background: rgba(255, 255, 255, 0.12);
    }

    textarea {
        height: 100px;
        resize: vertical;
    }

    /* ボタン群 */
    .button-group {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 40px;
    }

    .btn-save {
        background: linear-gradient(to right, #4a9eff, #1da1f2);
        color: white;
        border: none;
        padding: 12px 30px;
        border-radius: 30px;
        font-weight: bold;
        cursor: pointer;
        transition: 0.3s;
        box-shadow: 0 5px 15px rgba(29, 161, 242, 0.3);
    }

    .btn-save:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba(29, 161, 242, 0.5);
    }

    .btn-back {
        color: var(--text-sub);
        text-decoration: none;
        font-size: 0.9rem;
        transition: 0.3s;
    }

    .btn-back:hover {
        color: var(--text-main);
    }

    .badge-owner {
        background: rgba(88, 166, 255, 0.15);
        color: var(--accent);
        font-size: 0.75rem;
        padding: 4px 12px;
        border-radius: 20px;
        border: 1px solid rgba(88, 166, 255, 0.3);
    }
</style>
</head>
<body>

<div class="profile-container">
    <h2>
        プロフィール
        <c:if test="${isMine}"><span class="badge-owner">マイページ</span></c:if>
    </h2>

    <form action="ProfileServlet" method="post" enctype="multipart/form-data">
        
        <div class="icon-area">
            <img src="${pageContext.request.contextPath}/Copilot_20260403_165901.png" class="profile-icon">
            <c:if test="${isMine}">
                <div style="margin-top:15px;">
                    <label style="cursor:pointer; color:var(--accent); font-weight: bold;">📸 写真を変更する</label>
                    <input type="file" name="icon" style="font-size: 0.75rem; color: var(--text-sub); margin-top:5px;">
                </div>
            </c:if>
        </div>

        <div class="field-group">
            <label>ユーザーID</label>
            <div class="value-display" style="color: var(--accent); font-weight: bold;">@<c:out value="${profile.userId}" /></div>
        </div>

        <div class="field-group">
            <label>名前</label>
            <c:choose>
                <c:when test="${isMine}">
                    <input type="text" name="name" value="${profile.name}" required placeholder="あなたの名前">
                </c:when>
                <c:otherwise>
                    <div class="value-display"><c:out value="${profile.name}" /></div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="field-group">
            <label>住所</label>
            <c:choose>
                <c:when test="${isMine}">
                    <input type="text" name="address" value="${profile.address}" placeholder="例：広島県広島市">
                </c:when>
                <c:otherwise>
                    <div class="value-display"><c:out value="${profile.address}" /></div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="field-group">
            <label>性格・モットー</label>
            <c:choose>
                <c:when test="${isMine}">
                    <input type="text" name="personality" value="${profile.personality}" placeholder="例：元気いっぱい">
                </c:when>
                <c:otherwise>
                    <div class="value-display"><c:out value="${profile.personality}" /></div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="field-group">
            <label>自己紹介</label>
            <c:choose>
                <c:when test="${isMine}">
                    <textarea name="introduction" placeholder="よろしくお願いします！"><c:out value="${profile.introduction}" /></textarea>
                </c:when>
                <c:otherwise>
                    <div class="value-display" style="white-space: pre-wrap;"><c:out value="${profile.introduction}" /></div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="button-group">
            <a href="Main" class="btn-back">← タイムラインへ戻る</a>
            <c:if test="${isMine}">
                <input type="submit" value="保存して更新" class="btn-save">
            </c:if>
        </div>
    </form>
</div>

</body>
</html>