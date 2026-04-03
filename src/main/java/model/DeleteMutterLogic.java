package model;

import java.io.File;
import dao.MuttersDAO;

public class DeleteMutterLogic {
    public void execute(int id, String image) {
        // 1. データベース(Supabase)からデータを削除
        MuttersDAO dao = new MuttersDAO();
        boolean isSuccess = dao.delete(id); 

        // 2. ファイル削除処理（Render環境に合わせた調整）
        if (isSuccess && image != null && !image.isEmpty()) {
            // パスを環境に合わせて修正（例：Renderのテンポラリフォルダなど）
            // Windows環境でなければ C:/ ではない場所を指定する必要があります
            String path = System.getProperty("os.name").toLowerCase().startsWith("win") 
                          ? "C:/upload/" : "/tmp/"; 
            
            File file = new File(path + image);
            
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
