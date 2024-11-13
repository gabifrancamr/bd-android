package database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NOME = "produtos.bd";
    private static Integer DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NOME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("DB", "Executando o oncreate do database");
        try {
            String sql = "CREATE TABLE IF NOT EXISTS produtos(" +
                    "id_usuario INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "nome VARCHAR(100) NOT NULL," +
                    "preco REAL NOT NULL" +
                    ");";
            sqLiteDatabase.execSQL(sql, null);
            Log.i("DB", "tabela de produtos criada com sucesso");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "erro ao criar tabela de produtos");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
