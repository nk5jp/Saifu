package jp.nk5.saifu.infra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import jp.nk5.saifu.domain.Budget;

/**
 * 予算テーブル周りのDAO．
 * Created by NK5JP on 2016/04/24.
 */
public class BudgetDAO extends BaseDAO<Budget> {

    public BudgetDAO(Context context) {
        super(context);
        sqlForRead = "select * from budget;";
        argsForRead = null;
        tableName = "budget";
        conditionKey = "id = ?";
    }

    @Override
    protected Budget transformCursorToEntity(Cursor cursor) {
        return new Budget(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getInt(cursor.getColumnIndex("year")),
                cursor.getInt(cursor.getColumnIndex("month")),
                cursor.getInt(cursor.getColumnIndex("amount")),
                DAOUtil.returnBoolFromNum(cursor.getInt(cursor.getColumnIndex("isValid")))
        );
    }

    @Override
    protected ContentValues transformEntityToValues(Budget entity) {
        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("year", entity.getYear());
        values.put("month", entity.getMonth());
        values.put("amount", entity.getAmount());
        values.put("isValid", DAOUtil.returnNumFromBool(entity.isValid()));
        return values;
    }

    @Override
    protected void updateEntityById(Budget entity, long rowId) {
        entity.setId((int)rowId);
    }

    @Override
    protected String[] getConditionKey(Budget entity) {
        return new String[]{Integer.toString(entity.getId())};
    }
}
