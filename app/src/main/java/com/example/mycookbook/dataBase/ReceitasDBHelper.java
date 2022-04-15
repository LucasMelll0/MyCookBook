package com.example.mycookbook.dataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mycookbook.dao.ReceitaDAO;
import com.example.mycookbook.model.Receita;

import java.util.ArrayList;
import java.util.List;

public class ReceitasDBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DB_RECEITAS";
    private static SQLiteDatabase db;
    private ReceitaDAO dao = new ReceitaDAO();

    public ReceitasDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                "receitas(id INTEGER  PRIMARY KEY AUTOINCREMENT,nome VARCHAR, ingredientes VARCHAR, modoDePreparo VARCHAR, porcao VARCHAR, categoria VARCHAR)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                "ingredientes (id_ingrediente INTEGER " +
                "PRIMARY KEY AUTOINCREMENT, ingrediente VARCHAR,id_receita INTEGER,  CONSTRAINT fk_ingrediente_receita FOREIGN KEY (id_receita) REFERENCES receitas (id) )");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS receitas");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);

    }
    public void insereReceitaDB(Receita receita){
        try {
            db = getWritableDatabase();
            String nome = receita.getNome();
            String ingredientes = receita.getIngredientes();
            String modoDePreparo = receita.getModoDePreparo();
            String porcao = receita.getPorcao();
            String categoria = receita.getCategoria();

            db.execSQL("INSERT " +
                    "INTO receitas(nome, ingredientes, modoDePreparo, porcao, categoria) " +
                    "VALUES ('" + nome + "','" + ingredientes + "','" + modoDePreparo + "','" + porcao + "','" + categoria + "')");




            Log.i("Inseriu no banco de dados?", "SIM");

        }catch (Exception e){
            Log.i("Inseriu no banco de dados?", "NÃO");
            e.printStackTrace();
        }
    }



    public List<Receita> todasReceitas() {
        db = getReadableDatabase();
        List<Receita> receitas = new ArrayList<>();

        try {

            String consulta = "SELECT id, nome, ingredientes, modoDePreparo, porcao, categoria FROM receitas";
            Cursor cursor = db.rawQuery(consulta, null);
            int indexID = cursor.getColumnIndex("id");
            int indexNome = cursor.getColumnIndex("nome");
            int indexIngredientes = cursor.getColumnIndex("ingredientes");
            int indexModoDePreparo = cursor.getColumnIndex("modoDePreparo");
            int indexPorcao = cursor.getColumnIndex("porcao");
            int indexCategoria = cursor.getColumnIndex("categoria");
            cursor.moveToFirst();
            while (cursor != null) {
                int id = cursor.getInt(indexID);
                String nome = cursor.getString(indexNome);
                String ingredientes = cursor.getString(indexIngredientes);
                String modoDePreparo = cursor.getString(indexModoDePreparo);
                String porcao = cursor.getString(indexPorcao);
                String categoria = cursor.getString(indexCategoria);

                Receita receita = new Receita(nome, ingredientes, modoDePreparo, porcao, categoria);
                receita.setId(id);

                receitas.add(receita);
                cursor.moveToNext();

            }
            Log.i("Rodou o Try do insereTodasAsReceitasDoDB?", "SIM");
            return receitas;

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Rodou o Try do insereTodasAsReceitasDoDB?", "NÃO");
            return receitas;
        }

    }

    public Receita getReceitaPorID(int id){
        db = getReadableDatabase();
        try{
            String consulta = "SELECT * FROM receitas WHERE id = " + id;
            Cursor cursor = db.rawQuery(consulta, null);
            int indexID = cursor.getColumnIndex("id");
            int indexNome = cursor.getColumnIndex("nome");
            int indexIngredientes = cursor.getColumnIndex("ingredientes");
            int indexModoDePreparo = cursor.getColumnIndex("modoDePreparo");
            int indexPorcao = cursor.getColumnIndex("porcao");
            int indexCategoria = cursor.getColumnIndex("categoria");
            cursor.moveToFirst();
            int receitaID = cursor.getInt(indexID);
            String nome = cursor.getString(indexNome);
            String ingredientes = cursor.getString(indexIngredientes);
            String modoDePreparo = cursor.getString(indexModoDePreparo);
            String porcao = cursor.getString(indexPorcao);
            String categoria = cursor.getString(indexCategoria);

            Receita receita = new Receita(nome, ingredientes, modoDePreparo, porcao, categoria);
            receita.setId(receitaID);
            Log.i("Pegou a receita?", "Sim");
            return receita;

        }catch (Exception e){
            Log.i("Pegou a receita?", "Não");
            e.printStackTrace();
            return new Receita();
        }
    }

    public void setReceitaPorID(int ID, Receita receita){
        db = getWritableDatabase();
        try {
            String nome = receita.getNome();
            String ingredientes = receita.getIngredientes();
            String modoDePreparo = receita.getModoDePreparo();
            String porcao = receita.getPorcao();
            String categoria = receita.getCategoria();

            db.execSQL("UPDATE receitas SET nome = '" + nome + "', " +
                    "ingredientes = '"+ ingredientes +"', " +
                    "modoDePreparo = '"+ modoDePreparo +"', " +
                    "porcao = '"+ porcao +"', " +
                    "categoria = '"+ categoria +"' WHERE id = " + ID);

            Log.i("Testes", "Editou a receita? SIM");
        }catch (Exception e){
            Log.i("Testes", "Editou a receita? NÃO");
            e.printStackTrace();
        }
    }

    public void deletaReceitaDB(int ID){
    db = getWritableDatabase();
    try{

        db.execSQL("DELETE FROM receitas WHERE id = " + ID);
        Log.i("Testes", "Deletou a receita? SIM");
    }catch (Exception e){
        Log.i("Testes", "Deletou a receita? NÃO");
        e.printStackTrace();
    }

    }
}

