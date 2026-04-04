<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>キャット板 - タイムライン</title>

<script src="https://cdn.jsdelivr.net/npm/@supabase/supabase-js@2"></script>

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
    
    .side-column { width: 240px; flex-shrink: 0; position: sticky; top: 20px; height: fit-content; margin-top: 80px; display: block !important; }
    .center-column { flex: 1; max-width: 600px; min-height: 100vh; background: rgba(13, 17, 23, 0.5); border-left: 1px solid var(--border); border-right: 1px solid var(--border); }

    h1 { padding: 20px; color: var(--accent); text-align: center; position: sticky; top: 0; background: rgba(13, 17, 23, 0.85); backdrop-filter: blur(10px); z-index: 10; border-bottom: 1px solid var(--border); margin: 0; letter-spacing: 2px; }

    .glass-card { background: var(--glass); backdrop-filter: blur(10px); border: 1px solid var(--border); border-radius: 16px; padding: 20px; margin-bottom: 20px; }
    .profile-card { text-align: center; }
    .profile-avatar { width: 80px; height: 80px; border-radius: 50%; border: 2px solid var(--accent); margin-bottom: 10px; }
    .stats { display: flex; justify-content: center; gap: 20px; margin-top: 15px; border-top: 1px solid var(--border); padding-top: 10px; }
    .btn-side { display: block; margin-top: 15px; padding: 8px; border: 1px solid var(--accent); border-radius: 20px; color: var(--accent); text-decoration: none; font-size: 0.85rem; transition: 0.3s; }
    .btn-side:hover { background: rgba(88, 166, 255, 0.1); }

    form { padding: 20px; border-bottom: 8px solid rgba(0,0,0,0.3); }
    input[type="text"] { width: 100%; background: rgba(255,255,255,0.07); border: 1px solid var(--border); border-radius: 12px; padding: 15px; color: white; margin-bottom: 10px; box-sizing: border-box; outline: none; }
    input[type="button"], input[type="submit"] { background: var(--accent); color: white; border: none; border-radius: 25px; padding: 10px 25px; font-weight: bold; cursor: pointer; }
    input[type="button"]:disabled { background: #30363d; color: #8b949e; cursor: not-allowed; }

    .thread-group { border-bottom: 5px solid rgba(0, 0, 0, 0.5); padding-bottom: 10px; background: rgba(255,255,255,0.01); }
    .mutter-card { padding: 20px; display: flex; border-left: 3px solid transparent; }
    .mutter-card:hover { border-left-color: var(--accent); background: rgba(255,255,255,0.02); }
    
    .mutter-icon { width: 48px; height: 48px; border-radius: 50%; margin-right: 12px; flex-shrink: 0; border: 1px solid var(--border); }
    .reply-card { margin: 0 20px 10px 60px; padding: 12px; background: rgba(88, 166, 255, 0.05); border-radius: 12px; display: flex; border: 1px solid var(--border); border-left: 2px solid var(--accent); }
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

    @media (max-width: 300px) {
        .side-column { display: none !important; }
    }
</style>

<script>
    // ★Supabaseの設定：修正完了済み！
    const SUPABASE_URL = 'https://arpakswzlfpntdwrrghy.supabase.co';
    const SUPABASE_KEY = 'sb_publishable_vH_hoR1yyE8hOcRsQ0XJQw_LlSHbr3E'; 
    const _supabase = supabase.createClient(SUPABASE_URL, SUPABASE_KEY);

    // ★投稿処理（画像アップロードを含む）
    async function handlePost() {
        const form = document.getElementById('postForm');
        const fileInput = document.getElementById('imageInput');
        const submitBtn = document.getElementById('submitBtn');
        const textInput = form.querySelector('input[name="text"]');
        const file = fileInput.files[0];

        // バリデーション
        if (!textInput.value.trim()) {
            alert("つぶやきを入力してください");
            return;
        }

        submitBtn.disabled = true;
        submitBtn.value = "送信中...";

        try {
            if (file) {
                // ファイル名の作成（重複回避）
                const fileExt = file.name.split('.').pop();
                const fileName = Date.now() + "." + fileExt;
                
                // 1. Supabase Storageへアップロード
                const { data, error } = await _supabase.storage
                    .from('images') 
                    .upload(fileName, file);

                if (error) throw error;

                // 2. 公開URLを取得
                const { data: publicData } = _supabase.storage
                    .from('images')
                    .getPublicUrl(fileName);

                // 3. URLを隠しフィールドにセット
                document.getElementById('supabaseImageUrl').value = publicData.publicUrl;
            }

            // 4. サーバーへ送信（JavaのMain.javaへ飛ばす）
            form.submit();

        } catch (error) {
            console.error('Upload error:', error);
            alert('画像の送信に失敗しました。コンソールを確認してください。');
            submitBtn.disabled = false;
            submitBtn.value = "つぶやく";
        }
    }

    // --- 既存の機能スクリプト ---
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
    "猫の鼻紋は一匹ずつ違います。",
    "猫は一生の約3分の2を寝て過ごします。",
    "猫は時速約48キロメートルで走ることができます。",
    "猫の耳は180度回転させることができます。",
    "猫がゴロゴロ鳴らすのは怪我を治すためという説もあります。",
    "猫は甘みを感じることができません。",
    "猫は自分の体高の約5倍から6倍の高さをジャンプできます。",
    "猫の利き手は、オスは左、メスは右が多い傾向にあります。",
    "猫には鎖骨がないため、頭が通る隙間なら通り抜けられます。",
    "猫は足の裏（肉球）でしか汗をかきません。",
    "猫の前足の指は5本、後ろ足の指は4本です。",
    "猫の血液型はA型、B型、AB型の3種類で、A型が圧倒的に多いです。",
    "猫は「立ち直り反射」により、空中で体勢を立て直して着地できます。",
    "すべての子猫は生まれた時、目が青い（キトンブルー）です。",
    "猫には「瞬膜」という第3のまぶたがあります。",
    "猫の平熱は38度から39度で、人間より少し高いです。",
    "猫の心拍数は1分間に120回から140回で、人間の約2倍です。",
    "猫の視野は約200度あり、人間よりも広範囲を見渡せます。",
    "猫は人間の約6分の1の光があれば暗闇でも物が見えます。",
    "猫のヒゲの向きで、その時の感情を読み取ることができます。",
    "猫の手首の裏側にも、獲物を察知するためのヒゲが生えています。",
    "猫の舌のザラザラは、骨から肉を削ぎ落とす役割があります。",
    "猫の腎臓は非常に優秀で、海水を飲んでも水分補給が可能です。",
    "猫の骨の数は約230個で、人間（約200個）よりも多いです。",
    "エジプシャン・マウという猫種は、時速約48kmで走ると言われています。",
    "猫が口を半開きにする「フレーメン反応」は、臭いを味わっています。",
    "片方の耳だけで32個の筋肉があり、耳を別々に動かせます。",
    "猫は犬よりも高い音（ネズミの超音波など）を聞き取れます。",
    "猫は苦味と酸味には非常に敏感ですが、甘みはわかりません。",
    "猫が最も美味しく感じる食べ物の温度は、獲物の体温に近い35度前後です。",
    "猫は溜まった水よりも、蛇口などの流れている水を好むことが多いです。",
    "すべての猫がマタタビに反応するわけではなく、遺伝で決まります。",
    "夜に猫が集まる「猫の集会」は、縄張りの確認などの親睦会です。",
    "砂をかけるのは、自分の臭いを消して敵から身を守る野生の名残です。",
    "「ふみふみ」する動作は、母猫の乳を出すための名残で甘えています。",
    "猫同士が鼻をくっつけるのは、身元を確認し合う挨拶です。",
    "家具などに体をこすりつけるのは、自分の臭いをつけるマーキングです。",
    "飼い主に獲物を持ってくるのは、狩りを教えようとしている説があります。",
    "猫が狭いところを好むのは、外敵に見つからず安心できるからです。",
    "猫がゆっくりまばたきをするのは、相手を信頼している証拠です。",
    "猫が高い場所を好むのは、獲物を見つけやすく安全だからです。",
    "成猫が「ニャー」と鳴くのは、主に人間に対しての要求です。",
    "約9500年前の遺跡から、人間と猫が一緒に埋葬された骨が見つかっています。",
    "1963年、フランスの猫「フェリセット」が宇宙飛行に成功しました。",
    "ギネス記録に残る最も長生きした猫は、38歳と3日です。",
    "作家ヘミングウェイは、指が6本ある多指症の猫を愛しました。",
    "英語で猫の集団（群れ）のことを「Clowder（クラウダー）」と呼びます。",
    "アラスカのある町では、猫の「スタッブス」が20年間市長を務めました。",
    "船のネズミを駆除するため、昔から猫は「船乗り猫」として重宝されました。",
    "物理学者ニュートンが、猫専用の出入り口を考案したという説があります。"
];
    function updateTrivia() {
        const randomIndex = Math.floor(Math.random() * trivias.length);
        document.getElementById('cat-trivia').innerText = trivias[randomIndex];
    }

    document.addEventListener('input', (e) => {
        if (e.target.id === 'searchInput') {
            const word = e.target.value.toLowerCase();
            const threads = document.querySelectorAll('.thread-group');
            document.getElementById('clearBtn').style.display = word.length > 0 ? "block" : "none";
            threads.forEach(thread => {
                thread.style.display = thread.innerText.toLowerCase().includes(word) ? "" : "none";
            });
        }
    });

    function clearSearch() {
        document.getElementById('searchInput').value = "";
        document.querySelectorAll('.thread-group').forEach(t => t.style.display = "");
        document.getElementById('clearBtn').style.display = "none";
    }

    function pressNiku(btn, mutterId) {
        const params = new URLSearchParams();
        params.append('mutterId', mutterId);

        fetch('LikeServlet', { 
            method: 'POST', 
            body: params,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        })
        .then(res => {
            if (!res.ok) throw new Error('Network response was not ok');
            return res.text();
        })
        .then(count => {
            const countSpan = btn.querySelector('.niku-count');
            if (countSpan) countSpan.innerText = count;
            btn.style.color = "#ff7eb9";
            btn.style.transition = "0.2s";
            btn.style.transform = "scale(1.3)";
            setTimeout(() => { btn.style.transform = "scale(1)"; }, 200);
        })
        .catch(error => console.error('Error:', error));
    }
</script>
</head>
<body onload="updateTrivia()">

<div class="app-container">

    <aside class="side-column">
        <div class="glass-card profile-card">
            <img src="${pageContext.request.contextPath}/Copilot_20260403_165901.png" class="profile-avatar">
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
            <button type="button" id="clearBtn" onclick="clearSearch()" style="display: none; background: var(--glass); border: 1px solid var(--border); color: var(--text-sub); border-radius: 20px; padding: 8px 15px; cursor: pointer;">✕ 解除</button>
        </div>

        <c:if test="${not empty errorMsg}">
            <div class="error-banner"><c:out value="${errorMsg}" /></div>
        </c:if>

        <form action="Main" method="post" id="postForm">
            <input type="hidden" name="replyId" id="replyIdField" value="0">
            <input type="hidden" name="supabaseImageUrl" id="supabaseImageUrl" value="">
            
            <div id="reply-label">
                <span id="reply-target-name"></span>
                <span style="cursor:pointer;" onclick="cancelReply()">✕ 止める</span>
            </div>
            <input type="text" name="text" placeholder="いまどうしてる？" required>
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <input type="file" id="imageInput" style="font-size: 0.7rem; color: var(--text-sub);">
                <input type="button" id="submitBtn" value="つぶやく" onclick="handlePost()">
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
                                <img src="${mutter.image}" style="max-width:100%; border-radius:12px; margin-top:10px; border: 1px solid var(--border);">
                            </c:if>
                            
                            <div style="margin-top: 10px; display: flex; gap: 15px; align-items: center;">
                                <button type="button" style="background:none; border:none; color:var(--text-sub); cursor:pointer; font-size:0.8rem;" onclick="setReply('${mutter.id}', '${mutter.userName}')">💬 返信する</button>
                                <button type="button" onclick="pressNiku(this, '${mutter.id}')" style="background:none; border:none; color:var(--text-sub); cursor:pointer; font-size:0.8rem; display: flex; align-items: center; gap: 4px;">
                                    🐾 <span class="niku-count">${mutter.likeCount}</span>
                                </button>
                            </div>
                        </div>
                    </div>
                    
                    <c:forEach var="reply" items="${mutterList}">
                        <c:if test="${reply.replyId == mutter.id && reply.id != mutter.id}">
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
                                        <img src="${reply.image}" style="max-width:150px; border-radius:8px; margin-top:8px;">
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
