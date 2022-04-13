package applicationClass;

import android.app.Application;

import com.example.mycookbook.DataBase.ReceitasDBHelper;
import com.example.mycookbook.dao.ReceitaDAO;

public class MyCookBookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ReceitasDBHelper db = new ReceitasDBHelper(this);
        ReceitaDAO dao = new ReceitaDAO();
        dao.insereTodasAsReceitasDoDB(db);
    }



}
