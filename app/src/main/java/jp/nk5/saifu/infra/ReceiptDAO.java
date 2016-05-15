package jp.nk5.saifu.infra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;

/**
 * レシートテーブル関連のDAO．
 * Created by NK5JP on 2016/04/24.
 */
public class ReceiptDAO extends BaseDAO<Receipt> {

    private AssetRepository repository;
    private int year;
    private int month;

    public ReceiptDAO(Context context, AssetRepository assetRepository, int year, int month) {
        super(context);
        this.repository = assetRepository;
        sqlForRead = "select * from Receipt where year = ? and month = ?;";
        setYearAndMonth(year, month);
        tableName = "receipt";
        conditionKey = "id = ?";
    }

    @Override
    protected Receipt transformCursorToEntity(Cursor cursor) throws InfraException {
        try {
            return new Receipt(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("year")),
                    cursor.getInt(cursor.getColumnIndex("month")),
                    cursor.getInt(cursor.getColumnIndex("day")),
                    repository.readAssetById(cursor.getInt(cursor.getColumnIndex("assetId"))),
                    new ArrayList<ReceiptDetail>()
            );
        } catch (Exception e) {
            throw new InfraException("INF:RDA:TCT");
        }
    }

    @Override
    protected ContentValues transformEntityToValues(Receipt entity) {
        ContentValues values = new ContentValues();
        values.put("year", entity.getYear());
        values.put("month", entity.getMonth());
        values.put("day", entity.getDay());
        values.put("assetId", entity.getAsset().getId());
        return values;
    }

    @Override
    protected void updateEntityById(Receipt entity, long rowId) {
        entity.setId((int)rowId);
    }

    @Override
    protected String[] getConditionKey(Receipt entity) {
        return new String[]{Integer.toString(entity.getId())};
    }

    public void setYearAndMonth(int year, int month) {
        this.year = year;
        this.month = month;
        argsForRead = new String[]{Integer.toString(year), Integer.toString(month)};
    }

}
