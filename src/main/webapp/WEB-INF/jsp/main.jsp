<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>キャット板 - タイムライン</title>
<style>
    :root {
        --accent: #58a6ff; 
        --text-sub: #8b949e; 
        --border: rgba(255, 255, 255, 0.1);
        --danger: #ff7b72;
        --glass: rgba(255, 255, 255, 0.03);
    }
    body {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
        color: #e6edf3; 
        margin: 0;
        background: radial-gradient(circle at center, #1a2a44 0%, #050a14 100%) fixed;
    }

    .app-container { display: flex; justify-content: center; gap: 24px; max-width: 1250px; margin: 0 auto; padding: 0 20px; }
    .side-column { width: 260px; flex-shrink: 0; position: sticky; top: 20px; height: fit-content; margin-top: 80px; }
    .center-column { flex: 1; max-width: 650px; min-height: 100vh; background: rgba(13, 17, 23, 0.5); border-left: 1px solid var(--border); border-right: 1px solid var(--border); }

    h1 { padding: 20px; color: var(--accent); text-align: center; position: sticky; top: 0; background: rgba(13, 17, 23, 0.85); backdrop-filter: blur(10px); z-index: 10; border-bottom: 1px solid var(--border); margin: 0; letter-spacing: 2px; }

    .glass-card { background: var(--glass); backdrop-filter: blur(10px); border: 1px solid var(--border); border-radius: 16px; padding: 20px; margin-bottom: 20px; }
    .profile-card { text-align: center; }
    .profile-avatar { width: 80px; height: 80px; border-radius: 50%; border: 2px solid var(--accent); margin-bottom: 10px; }
    .stats { display: flex; justify-content: center; gap: 20px; margin-top: 15px; border-top: 1px solid var(--border); padding-top: 10px; }
    .btn-side { display: block; margin-top: 15px; padding: 8px; border: 1px solid var(--accent); border-radius: 20px; color: var(--accent); text-decoration: none; font-size: 0.85rem; transition: 0.3s; }
    .btn-side:hover { background: rgba(88, 166, 255, 0.1); }

    form { padding: 20px; border-bottom: 8px solid rgba(0,0,0,0.3); }
    input[type="text"] { width: 100%; background: rgba(255,255,255,0.07); border: 1px solid var(--border); border-radius: 12px; padding: 15px; color: white; margin-bottom: 10px; box-sizing: border-box; outline: none; }
    input[type="submit"] { background: var(--accent); color: white; border: none; border-radius: 25px; padding: 10px 25px; font-weight: bold; cursor: pointer; }

    /* ★修正: 投稿ごとの区切りを強調 */
    .thread-group { border-bottom: 5px solid rgba(0, 0, 0, 0.5); padding-bottom: 10px; background: rgba(255,255,255,0.01); }
    .mutter-card { padding: 20px; display: flex; border-left: 3px solid transparent; transition: 0.3s; }
    .mutter-card:hover { border-left-color: var(--accent); background: rgba(255,255,255,0.02); }
    
    .mutter-icon { width: 48px; height: 48px; border-radius: 50%; margin-right: 12px; flex-shrink: 0; border: 1px solid var(--border); }
    .reply-card { margin: 0 20px 10px 60px; padding: 12px; background: rgba(255,255,255,0.03); border-radius: 12px; display: flex; border: 1px solid var(--border); }
    .reply-icon { width: 32px; height: 32px; border-radius: 50%; margin-right: 10px; flex-shrink: 0; }

    .user-name-link { color: white; font-weight: bold; text-decoration: none; }
    .date { color: var(--text-sub); font-size: 0.75rem; margin-left: 10px; }
    .content { margin-top: 5px; white-space: pre-wrap; word-break: break-all; }
    .delete-link { margin-left: auto; color: var(--text-sub); text-decoration: none; }

    .error-banner { color: var(--danger); text-align: center; background: rgba(255,123,114,0.1); padding: 10px; border-radius: 8px; margin: 10px; border: 1px solid rgba(255,123,114,0.2); }
    #reply-label { display: none; background: rgba(88, 166, 255, 0.1); padding: 8px 15px; border-radius: 10px; margin-bottom: 10px; color: var(--accent); justify-content: space-between; font-size: 0.85rem; }

    .trivia-title { font-size: 0.9rem; color: var(--accent); margin-top: 0; display: flex; align-items: center; gap: 5px; }
    #cat-trivia { font-size: 0.85rem; line-height: 1.6; min-height: 50px; }
    .btn-refresh { background: none; border: none; color: var(--text-sub); cursor: pointer; font-size: 0.75rem; padding: 0; text-decoration: underline; }

    /* ★修正: 1050pxでの非表示を削除 */
    @media (max-width: 800px) {
        .side-column { display: none; }
        .center-column { border: none; }
    }
</style>

<script>
    function setReply(id, name) {
        document.getElementById('replyIdField').value = id;
        document.getElementById('reply-target-name').innerText = name + " さんに返信中";
        document.getElementById('reply-label').style.display = 'flex';
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
    function cancelReply() {
        document.getElementById('replyIdField').value = '0';
        document.getElementById('reply-label').style.display = 'none';
    }
    
    const trivias = [
        "猫の鼻紋は、人間でいう「指紋」と同じで、一匹ずつ違います。",
        "猫は一生の約3分の2を寝て過ごします。",
        "猫は時速約48キロメートルで走ることができます。",
        "猫の耳は180度回転させることができ、32個の筋肉で制御されています。",
        "猫がゴロゴロ鳴らすのは、怪我を治すためという説もあります。",
        "猫は甘みを感じることができません。"
    ];
    function updateTrivia() {
        const randomIndex = Math.floor(Math.random() * trivias.length);
        document.getElementById('cat-trivia').innerText = trivias[randomIndex];
    }

    document.addEventListener('input', (e) => {
        if (e.target.id === 'searchInput') {
            const word = e.target.value.toLowerCase();
            const threads = document.querySelectorAll('.thread-group');
            const clearBtn = document.getElementById('clearBtn');
            clearBtn.style.display = word.length > 0 ? "block" : "none";
            threads.forEach(thread => {
                const text = thread.innerText.toLowerCase();
                thread.style.display = text.includes(word) ? "" : "none";
            });
        }
    });

    function clearSearch() {
        const input = document.getElementById('searchInput');
        input.value = "";
        const threads = document.querySelectorAll('.thread-group');
        threads.forEach(thread => thread.style.display = "");
        document.getElementById('clearBtn').style.display = "none";
    }

    function pressNiku(btn, mutterId) {
        const params = new URLSearchParams();
        params.append('mutterId', mutterId);
        fetch('LikeServlet', { method: 'POST', body: params })
        .then(res => res.text())
        .then(count => {
            const countSpan = btn.querySelector('.niku-count');
            countSpan.innerText = count;
            btn.style.color = "#ff7eb9";
            btn.style.transform = "scale(1.3) rotate(-10deg)";
            setTimeout(() => { btn.style.transform = "scale(1) rotate(0deg)"; }, 200);
        });
    }
</script>
</head>
<body onload="updateTrivia()">

<div class="app-container">

    <aside class="side-column">
        <div class="glass-card profile-card">
            <img src="${pageContext.request.contextPath}/Copilot_20260403_165901.png" class="profile-avatar">
            
            <%-- ★修正: loginUserがnullの場合の対策 --%>
            <c:choose>
                <c:when test="${not empty loginUser}">
                    <div style="font-weight: bold; font-size: 1.1rem;"><c:out value="${loginUser.name}" /></div>
                    <div style="color: var(--text-sub); font-size: 0.8rem;">@${loginUser.name}</div>
                    <div class="stats">
                        <div><small>投稿</small><br><strong>${mutterList.size()}</strong></div>
                    </div>
                    <a href="ProfileServlet" class="btn-side">Profile</a>
                    <a href="Logout" style="color: var(--text-sub); font-size: 0.75rem; text-decoration: none; display: block; margin-top: 15px;">Logout</a>
                </c:when>
                <c:otherwise>
                    <div style="font-weight: bold; font-size: 1.1rem;">ゲスト</div>
                    <a href="index.jsp" class="btn-side">Login</a>
                </c:otherwise>
            </c:choose>
        </div>
    </aside>

    <main class="center-column">
        <h1>キャット板</h1>

        <div style="padding: 10px 20px; border-bottom: 1px solid var(--border); display: flex; align-items: center; gap: 10px;">
            <input type="text" id="searchInput" placeholder="🐾 キーワードで検索..." 
                   style="flex: 1; padding: 10px 15px; border-radius: 20px; border: 1px solid var(--border); background: var(--glass); color: white; outline: none; font-size: 0.9rem;">
            <button type="button" id="clearBtn" onclick="clearSearch()" 
                    style="display: none; background: var(--glass); border: 1px solid var(--border); color: var(--text-sub); border-radius: 20px; padding: 8px 15px; cursor: pointer; font-size: 0.8rem; white-space: nowrap;">
                ✕ 解除
            </button>
        </div>

        <c:if test="${not empty errorMsg}">
            <div class="error-banner"><c:out value="${errorMsg}" /></div>
        </c:if>

        <form action="Main" method="post" enctype="multipart/form-data">
            <input type="hidden" name="replyId" id="replyIdField" value="0">
            <div id="reply-label">
                <span id="reply-target-name"></span>
                <span style="cursor:pointer;" onclick="cancelReply()">✕ 止める</span>
            </div>
            <input type="text" name="text" placeholder="いまどうしてる？" required>
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <input type="file" name="image" style="font-size: 0.7rem; color: var(--text-sub);">
                <input type="submit" value="つぶやく">
            </div>
        </form>

        <div class="mutter-list">
        <c:forEach var="mutter" items="${mutterList}">
            <c:if test="${mutter.replyId == 0}">
                <div class="thread-group">
                    <div class="mutter-card">
                        <img src="${pageContext.request.contextPath}/Copilot_20260403_165901.png" class="mutter-icon">
                        <div style="flex: 1;">
                            <div style="display:flex; align-items: center;">
                                <a href="ProfileServlet?userId=${mutter.userName}" class="user-name-link"><c:out value="${mutter.userName}" /></a>
                                <span class="date"><c:out value="${mutter.createdAt}" /></span>
                                <a href="Delete?id=${mutter.id}" onclick="return confirm('削除しますか？')" class="delete-link">×</a>
                            </div>
                            <div class="content"><c:out value="${mutter.text}" /></div>
                            <c:if test="${not empty mutter.image}">
                                <img src="images/${mutter.image}" style="max-width:100%; border-radius:12px; margin-top:10px; border: 1px solid var(--border);">
                            </c:if>
                            
                            <div style="margin-top: 10px; display: flex; gap: 15px; align-items: center;">
                                <button type="button" style="background:none; border:none; color:var(--text-sub); cursor:pointer; font-size:0.8rem;" onclick="setReply('${mutter.id}', '${mutter.userName}')">💬 返信する</button>
                                <button type="button" onclick="pressNiku(this, '${mutter.id}')" 
                                        style="background:none; border:none; color:var(--text-sub); cursor:pointer; font-size:0.8rem; display: flex; align-items: center; gap: 4px; transition: 0.2s;">
                                    🐾 <span class="niku-count">${mutter.likeCount}</span>
                                </button>
                            </div>
                        </div>
                    </div>
                    <c:forEach var="reply" items="${mutterList}">
                        <c:if test="${reply.replyId == mutter.id}">
                            <div class="reply-card">
                                <img src="${pageContext.request.contextPath}/Copilot_20260403_165901.png" class="reply-icon">
                                <div style="flex: 1;">
                                    <div style="display:flex; align-items: center;">
                                        <span class="user-name-link" style="font-size:0.85rem;"><c:out value="${reply.userName}" /></span>
                                        <span class="date"><c:out value="${reply.createdAt}" /></span>
                                        <a href="Delete?id=${reply.id}" onclick="return confirm('削除しますか？')" class="delete-link">×</a>
                                    </div>
                                    <div class="content" style="font-size: 0.9rem;"><c:out value="${reply.text}" /></div>
                                    <c:if test="${not empty reply.image}">
                                        <img src="images/${reply.image}" style="max-width:150px; border-radius:8px; margin-top:8px;">
                                    </c:if>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:if>
        </c:forEach>
        </div>
    </main>

    <aside class="side-column">
        <div class="glass-card">
            <h4 class="trivia-title">🐾 猫の豆知識</h4>
            <p id="cat-trivia"></p>
            <button class="btn-refresh" onclick="updateTrivia()">別のを見る</button>
        </div>
        <div class="glass-card">
            <h4 style="margin:0 0 10px 0; font-size:0.9rem;">注目のハッシュタグ</h4>
            <ul style="list-style:none; padding:0; margin:0; font-size:0.85rem; color:var(--accent); line-height:2;">
                <li>#猫のいる暮らし</li>
                <li>#Java学習中</li>
                <li>#今日のランチ</li>
            </ul>
        </div>
    </aside>

</div>

</body>
</html>
