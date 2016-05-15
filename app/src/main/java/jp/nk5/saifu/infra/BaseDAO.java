package jp.nk5.saifu.infra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * DAOの共通的な処理を記述するための親クラス．
 * Created by NK5JP on 2016/04/23.
 */
public abstract class BaseDAO <T> {

    protected Context context;
    protected String sqlForRead;
    protected String[] argsForRead;
    protected String tableName;
    protected String conditionKey;

    public BaseDAO (Context context) {
        this.context = context;
    }

    /**
     * テーブルからデータを読み込み，リストコレクションとして返却．
     * 条件はargsForReadで指定．
     * @return 取得したデータコレクション．
     * @throws InfraException
     */
    public List<T> read() throws InfraException {
        List<T> list = new ArrayList<>();

        try (
            SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
            Cursor cursor = db.rawQuery(sqlForRead, argsForRead)
        ) {
            db.beginTransaction();
            if (cursor.moveToFirst()) {
                do {
                    T entity = transformCursorToEntity(cursor);
                    list.add(entity);
                } while (cursor.moveToNext());
                db.setTransactionSuccessful();
            }
            db.endTransaction();
            return list;
        } catch (Exception e) {
            throw new InfraException("INF:BAS:RED");
        }
    }

    /**
     * 引数のエンティティをテーブルに格納し，エンティティにidを割り振る．
     * @param entity 格納対象エンティティ．
     * @throws InfraException
     */
    public void create (T entity) throws InfraException
    {
        ContentValues contentValues = transformEntityToValues(entity);

        try (SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase()) {
            db.beginTransaction();
            long rowId = db.insert(tableName, null, contentValues);
            if (rowId == -1) {
                throw new Exception();
            } else {
                updateEntityById(entity, rowId);
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        } catch (Exception e) {
            throw new InfraException("INF:BAS:CRE");
        }
    }

    /**
     * 引数のエンティティに対応するレコードを更新する．
     * R/OマッピングはconditionKeyとgetConditionKey()で実施する．
     * @param entity 更新対象エンティティ．
     * @throws InfraException
     */
    public void update(T entity) throws InfraException {
        ContentValues contentValues = transformEntityToValues(entity);
        try (SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase()) {
            db.beginTransaction();
            long updateRaw = db.update(tableName, contentValues, conditionKey, getConditionKey(entity));
            if (updateRaw == -1) {
                throw new Exception();
            } else {
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        } catch (Exception e) {
            throw new InfraException("INF:BAS:UPD");
        }
    }

    /**
     * 引数のエンティティに対応するレコードを削除する．
     * R/OマッピングはconditionKeyとgetConditionKey()で実施する．
     * @param entity 削除対象エンティティ．
     * @throws InfraException
     */
    public void delete(T entity) throws InfraException {
        try (SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase()) {
            db.beginTransaction();
            long deleteRaw = db.delete(tableName, conditionKey, getConditionKey(entity));
            if (deleteRaw == -1) {
                throw new Exception();
            } else {
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        } catch (Exception e) {
            throw new InfraException("INF:BAS:DEL");
        }
    }

    /**
     * 引数のカーソルに対応するレコードをエンティティに変換する．
     * @param cursor 変換対象カーソル．
     * @return 変換後のエンティティ．
     */
    protected abstract T transformCursorToEntity(Cursor cursor) throws InfraException;

    /**
     * 引数のエンティティをテーブルに格納できる形に変換する．
     * @param entity 変換対象エンティティ
     * @return 変換後のレコード情報
     */
    protected abstract ContentValues transformEntityToValues(T entity) throws InfraException;

    /**
     * 新しいidが割り振られた場合に，そのidを対応するオブジェクトに割り振る．
     * @param entity 割り振り先となるエンティティ．
     * @param rowId 新しいID．
     */
    protected abstract void updateEntityById(T entity, long rowId) throws InfraException;

    /**
     * 更新や削除を行うための条件（多くの場合id）を取得して返却する．
     * @param entity 更新/削除対象エンティティ
     * @return 更新/削除条件文（conditionKeyの?に埋め込まれる文言）
     */
    protected abstract String[] getConditionKey(T entity) throws InfraException;

}
