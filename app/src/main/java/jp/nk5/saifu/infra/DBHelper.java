package jp.nk5.saifu.infra;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * データベース管理のヘルパークラス．自動テストの対象外とする．
 * Singletonパターンにより，単一インスタンスであることを保障する．
 * Created by NK5JP on 2016/04/18.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance = null;

    static final String DB_NAME = "nk5_saifu.db";
    static final int DB_VERSION = 1;
    static final String CREATE_ASSET_TABLE = "create table asset ( " +
            "id integer primary key autoincrement, " +
            "name text not null, " +
            "amount integer not null, " +
            "isValid integer not null );";
    static final String CREATE_BUDGET_TABLE = "create table budget ( " +
            "id integer primary key autoincrement, " +
            "name text not null, " +
            "year integer not null, " +
            "month integer not null, " +
            "amount integer not null, " +
            "isValid integer not null );";
    static final String CREATE_RECEIPT_TABLE = "create table receipt ( " +
            "id integer primary key autoincrement, " +
            "year integer not null, " +
            "month integer not null, " +
            "day integer not null, " +
            "assetId integer not null );";
    static final String CREATE_RECEIPT_DETAIL_TABLE = "create table receiptDetail ( " +
            "id integer primary key autoincrement, " +
            "receiptId integer not null, " +
            "budgetId integer not null, " +
            "amount integer not null );";
    static final String DROP_ASSET_TABLE = "drop table asset;";
    static final String DROP_RECEIPT_TABLE = "drop table receipt;";
    static final String DROP_RECEIPT_DETAIL_TABLE = "drop table receiptDetail;";
    static final String DROP_BUDGET_TABLE = "drop table budget;";

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_ASSET_TABLE);
        db.execSQL(CREATE_RECEIPT_TABLE);
        db.execSQL(CREATE_RECEIPT_DETAIL_TABLE);
        db.execSQL(CREATE_BUDGET_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DROP_ASSET_TABLE);
        db.execSQL(DROP_RECEIPT_TABLE);
        db.execSQL(DROP_RECEIPT_DETAIL_TABLE);
        db.execSQL(DROP_BUDGET_TABLE);
        onCreate(db);
    }

    public int getVersion(){
        return DB_VERSION;
    }

}
