package senac.com.br.controledegastos.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import senac.com.br.controledegastos.model.Gasto;
import senac.com.br.controledegastos.model.Mes;
import senac.com.br.controledegastos.model.Orcamento;

//Created by Carlos Lohmeyer on 26/09/2017.

public class MyORMLiteHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "simplifica";
    private static final int DATABASE_VERSION = 1;
    private static MyORMLiteHelper mInstance = null;
    private Dao<Mes, Integer> mesDao = null;
    private Dao<Orcamento, Integer> orcamentoDao = null;
    private Dao<Gasto, Integer> gastoDao = null;

    public MyORMLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MyORMLiteHelper getInstance(Context ctx){
        if (mInstance == null)
            mInstance = new MyORMLiteHelper(ctx);
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        //Criar as tabelas no Banco de Dados
        try {
            TableUtils.createTable(connectionSource, Mes.class);
            TableUtils.createTable(connectionSource, Orcamento.class);
            TableUtils.createTable(connectionSource, Gasto.class);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try{
            TableUtils.dropTable(connectionSource, Mes.class, true);
            TableUtils.dropTable(connectionSource, Orcamento.class, true);
            TableUtils.dropTable(connectionSource, Gasto.class, true);
        }catch (SQLException e){
            e.printStackTrace();
        }
        onCreate(sqLiteDatabase, connectionSource);
    }

    //Para cada tabela criada, deve ser criado o seu Dao
    public Dao<Mes, Integer> getMesDao() throws SQLException{
        if (mesDao == null){
            mesDao = getDao(Mes.class);
        }
        return mesDao;
    }

    public Dao<Orcamento, Integer> getOrcamentoDao() throws SQLException{
        if (orcamentoDao == null){
            orcamentoDao = getDao(Orcamento.class);
        }
        return  orcamentoDao;
    }

    public Dao<Gasto, Integer> getGastoDao() throws SQLException{
        if (gastoDao == null){
            gastoDao = getDao(Gasto.class);
        }
        return  gastoDao;
    }
}