package model;

import java.io.File;

import dao.MuttersDAO;

public class DeleteMutterLogic {
    // 引数を (int id, String image) に変更
    public void execute(int id, String image) {
        // 1. データベース(MySQL)からデータを削除
        MuttersDAO dao = new MuttersDAO();
        boolean isSuccess = dao.delete(id); // 直接 id を渡す

        // 2. データベースの削除に成功し、かつ画像ファイル名がある場合は、フォルダからも消す
        if (isSuccess && image != null && !image.isEmpty()) {
            // 保存先フォルダにあるファイルを指定
            File file = new File("C:/upload/" + image);
            
            // ファイルが実際に存在すれば削除する
            if (file.exists()) {
                file.delete();
            }
        }
    }
}