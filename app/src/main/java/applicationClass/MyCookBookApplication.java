package applicationClass;

import android.app.Application;

import com.example.mycookbook.activity.model.Receita;

import dao.ReceitaDAO;

public class MyCookBookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ReceitaDAO dao = new ReceitaDAO();
        dao.salva(new Receita("Bolo", "ovo e leite", "bata tudo no liquidificador", "2",4));
        dao.salva(new Receita("Torta", "ovo e leite", "bata tudo no liquidificador", "2",3));
    }
}
