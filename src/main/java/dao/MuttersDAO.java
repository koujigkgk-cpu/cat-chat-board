package dao;
import java.util.ArrayList;
import java.util.List;

import model.Mutter;

public class MuttersDAO {
    // サーバー起動中、メモリ上にデータを保持する「リスト」
    private static List<Mutter> mutterList = new ArrayList<>();

    public List<Mutter> findAll() {
        return mutterList;
    }

    public void create(Mutter mutter) {
        // 新しいつぶやきをリストの先頭に追加
        mutterList.add(0, mutter);
    }

    // ★このメソッドを追加することで DeleteMutterLogic のエラーが消えます
    public boolean delete(int id) {
        // リストの中から id が一致する「つぶやき」を探して削除する
        // 削除できたら true、見つからなければ false を返す
        return mutterList.removeIf(mutter -> mutter.getId() == id);
    }
}