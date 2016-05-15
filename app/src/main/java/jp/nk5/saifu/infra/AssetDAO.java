package jp.nk5.saifu.infra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import jp.nk5.saifu.domain.Asset;

/**
 * 資産テーブル周りのDAO．
 * Created by NK5JP on 2016/04/23.
 */
public class AssetDAO extends BaseDAO<Asset> {

    public AssetDAO(Context context) {
        super(context);
        sqlForRead = "select * from asset;";
        argsForRead = null;
        tableName = "asset";
        conditionKey = "id = ?";
    }

    @Override
    protected Asset transformCursorToEntity(Cursor cursor) {
        return new Asset(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getInt(cursor.getColumnIndex("amount")),
                DAOUtil.returnBoolFromNum(cursor.getInt(cursor.getColumnIndex("isValid")))
        );
    }

    @Override
    protected ContentValues transformEntityToValues(Asset entity) {
        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("amount", entity.getAmount());
        values.put("isValid", DAOUtil.returnNumFromBool(entity.isValid()));
        return values;
    }

    @Override
    protected void updateEntityById(Asset entity, long rowId) {
        entity.setId((int)rowId);
    }

    @Override
    protected String[] getConditionKey(Asset entity) {
        return new String[]{Integer.toString(entity.getId())};
    }


}
