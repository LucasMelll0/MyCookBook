package com.example.mycookbook.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mycookbook.model.Receita;

import java.util.ArrayList;
import java.util.List;

public class ReceitasDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DB_RECEITAS";
    private static SQLiteDatabase db;

    public ReceitasDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS receitas");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ingredientes");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                "receitas(id_receita INTEGER  PRIMARY KEY AUTOINCREMENT,nome VARCHAR,imagem BLOB, modoDePreparo VARCHAR, porcao VARCHAR, categoria VARCHAR)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                "ingredientes (id_ingrediente INTEGER " +
                "PRIMARY KEY AUTOINCREMENT, ingrediente VARCHAR,id_receita INTEGER,  CONSTRAINT fk_ingrediente_receita FOREIGN KEY (id_receita) REFERENCES receitas (id_receita) )");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS receitas");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);

    }

    public void insereReceitaDB(Receita receita) {
        try {
            db = getWritableDatabase();

            ContentValues dados = new ContentValues();
            dados.put("nome", receita.getNome());
            dados.put("imagem", receita.getImagemReceita());
            dados.put("modoDePreparo", receita.getModoDePreparo());
            dados.put("porcao", receita.getPorcao());
            dados.put("categoria", receita.getCategoria());
            db.insert("receitas", null, dados);
            ArrayList<String> ingredientes = receita.getIngredientes();


            String consultaUltimo = "SELECT id_receita FROM receitas WHERE id_receita = (SELECT MAX(id_receita) FROM receitas)";
            Cursor cursor = db.rawQuery(consultaUltimo, null);
            int indexID = cursor.getColumnIndex("id_receita");
            cursor.moveToFirst();

            int idUltimaReceita = cursor.getInt(indexID);

            for (int i = 0; i < ingredientes.size(); i++) {
                ContentValues ingredientesReceita = new ContentValues();
                ingredientesReceita.put("ingrediente", ingredientes.get(i));
                ingredientesReceita.put("id_receita", idUltimaReceita);
                db.insert("ingredientes", null, ingredientesReceita);
            }

            db.close();
            Log.i("Inseriu no banco de dados?", "SIM");

        } catch (Exception e) {
            Log.i("Inseriu no banco de dados?", "N??O");
            e.printStackTrace();
        }
    }


    public List<Receita> todasReceitas() {

        List<Receita> receitas = new ArrayList<>();

        try {
            db = getReadableDatabase();
            String consulta = "SELECT id_receita, nome, imagem, modoDePreparo, porcao, categoria FROM receitas";
            Cursor cursor = db.rawQuery(consulta, null);
            int indexID = cursor.getColumnIndex("id_receita");
            int indexNome = cursor.getColumnIndex("nome");
            int indexImagem = cursor.getColumnIndex("imagem");
            int indexModoDePreparo = cursor.getColumnIndex("modoDePreparo");
            int indexPorcao = cursor.getColumnIndex("porcao");
            int indexCategoria = cursor.getColumnIndex("categoria");
            cursor.moveToFirst();
            while (cursor != null) {

                int id = cursor.getInt(indexID);
                String nome = cursor.getString(indexNome);
                String modoDePreparo = cursor.getString(indexModoDePreparo);
                byte[] imagem = cursor.getBlob(indexImagem);
                String porcao = cursor.getString(indexPorcao);
                String categoria = cursor.getString(indexCategoria);
                ArrayList<String> ingredientes = getIngredientesDaReceitaPorID(id);

                Receita receita = new Receita(nome, ingredientes, modoDePreparo, porcao, categoria);
                receita.setId(id);
                receita.setImagemReceita(imagem);

                receitas.add(receita);
                cursor.moveToNext();


            }
            Log.i("Testes", "todasReceitas: RODOU");
            db.close();
            return receitas;

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Testes", "todasReceitas: N??O RODOU");
            db.close();
            return receitas;
        }

    }

    private ArrayList<String> getIngredientesDaReceitaPorID(int id) {
        db = getReadableDatabase();
        ArrayList<String> ingredientes = new ArrayList<>();
        try {
            String consulta2 = "SELECT ingrediente FROM ingredientes WHERE id_receita = "+ id ;
            Cursor cursor2 = db.rawQuery(consulta2, null);
            int indexIngrediente = cursor2.getColumnIndex("ingrediente");
            cursor2.moveToFirst();
            while (cursor2 != null) {
                String ingrediente = cursor2.getString(indexIngrediente);
                ingredientes.add(ingrediente);
                cursor2.moveToNext();
            }
            Log.i("Testes", "pegaIngredientesDaReceitaPorID: PEGOU");
            return ingredientes;
        } catch (Exception e) {
            Log.i("Testes", "pegaIngredientesDaReceitaPorID: N??O PEGOU");
            e.printStackTrace();
            return ingredientes;

        }
    }

    public Receita getReceitaPorID(int id) {
        db = getReadableDatabase();
        try {
            String consulta = "SELECT * FROM receitas WHERE id_receita = " + id;
            Cursor cursor = db.rawQuery(consulta, null);
            int indexID = cursor.getColumnIndex("id_receita");
            int indexNome = cursor.getColumnIndex("nome");
            int indexIngredientes = cursor.getColumnIndex("ingredientes");
            int indexModoDePreparo = cursor.getColumnIndex("modoDePreparo");
            int indexPorcao = cursor.getColumnIndex("porcao");
            int indexCategoria = cursor.getColumnIndex("categoria");
            cursor.moveToFirst();
            int receitaID = cursor.getInt(indexID);
            String nome = cursor.getString(indexNome);
            ArrayList<String> ingredientes = getIngredientesDaReceitaPorID(receitaID);
            String modoDePreparo = cursor.getString(indexModoDePreparo);
            String porcao = cursor.getString(indexPorcao);
            String categoria = cursor.getString(indexCategoria);

            Receita receita = new Receita(nome, ingredientes, modoDePreparo, porcao, categoria);
            receita.setId(receitaID);
            Log.i("Testes", "getReceitaPorID: RODOU");

            return receita;
        } catch (Exception e) {
            Log.i("Testes", "getReceitaPorID: N??O RODOU");
            e.printStackTrace();
            return new Receita();
        }
    }

    public void setReceitaPorID(int ID, Receita receita) {
        db = getWritableDatabase();
        try {
            ContentValues update = new ContentValues();
            update.put("nome", receita.getNome());
            update.put("imagem", receita.getImagemReceita());
            update.put("modoDePreparo", receita.getModoDePreparo());
            update.put("porcao", receita.getPorcao());
            update.put("categoria", receita.getCategoria());

            db.update("receitas", update, "id_receita = ?", new String[]{String.valueOf(ID)});

            setIngredientesPorID(ID, receita);
            db.close();

            Log.i("Testes", "Editou a receita? SIM");
        } catch (Exception e) {
            Log.i("Testes", "Editou a receita? N??O");
            e.printStackTrace();
        }
    }

    private void setIngredientesPorID(int ID, Receita receita) {
        db = getWritableDatabase();
        db.execSQL("DELETE FROM ingredientes WHERE id_receita = " + ID);

        ArrayList<String> ingredientes = receita.getIngredientes();

        for (int i = 0; i < ingredientes.size(); i++) {
            ContentValues ingrediente = new ContentValues();
            ingrediente.put("ingrediente", ingredientes.get(i));
            ingrediente.put("id_receita", ID);
            db.insert("ingredientes",null, ingrediente);
        }

    }

    public void deletaReceitaDB(int ID) {
        db = getWritableDatabase();
        try {

            db.execSQL("DELETE FROM receitas WHERE id_receita = " + ID);
            db.execSQL("DELETE FROM ingredientes WHERE id_receita = " + ID);
            Log.i("Testes", "Deletou a receita? SIM");
        } catch (Exception e) {
            Log.i("Testes", "Deletou a receita? N??O");
            e.printStackTrace();
        }

    }
}

