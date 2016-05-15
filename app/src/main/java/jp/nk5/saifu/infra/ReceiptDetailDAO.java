package jp.nk5.saifu.infra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;

/**
 * レシート明細関連DAO．紐づくレシート毎の処理となる．
 * Created by NK5JP on 2016/04/29.
 */
public class ReceiptDetailDAO extends BaseDAO<ReceiptDetail> {

    private Context context;
    private BudgetRepository repository;
    private Receipt receipt;

    public ReceiptDetailDAO(Context context, Receipt receipt, BudgetRepository repository) {
        super(context);
        this.repository = repository;
        sqlForRead = "select * from ReceiptDetail where receiptId = ?;";
        setTargetReceipt(receipt);
        tableName = "receiptDetail";
        conditionKey = "receiptId = ?";
    }

    @Override
    protected ReceiptDetail transformCursorToEntity(Cursor cursor) throws InfraException {
        try {
            return new ReceiptDetail(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    receipt,
                    repository.readBudgetById(cursor.getInt(cursor.getColumnIndex("budgetId"))),
                    cursor.getInt(cursor.getColumnIndex("amount"))
            );
        } catch (Exception e) {
            throw new InfraException("INF:RDD:TCT");
        }
    }

    @Override
    protected ContentValues transformEntityToValues(ReceiptDetail entity) throws InfraException {
        ContentValues values = new ContentValues();
        values.put("receiptId", entity.getReceipt().getId());
        values.put("budgetId", entity.getBudget().getId());
        values.put("amount", entity.getAmount());
        return values;
    }

    @Override
    protected void updateEntityById(ReceiptDetail entity, long rowId) throws InfraException {
        entity.setId((int)rowId);
    }

    @Override
    protected String[] getConditionKey(ReceiptDetail entity) throws InfraException {
        return new String[]{Integer.toString(receipt.getId())};
    }

    /**
     * 親となるレシートエンティティをセットする．
     * @param receipt 親となるレシートエンティティ．
     */
    public void setTargetReceipt(Receipt receipt) {
        this.receipt = receipt;
        argsForRead = new String[]{Integer.toString(receipt.getId())};
    }

}
