package jp.nk5.saifu.infra;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;
import jp.nk5.saifu.domain.ReceiptRepository;

/**
 * Created by NK5JP on 2016/04/30.
 */
public class ReceiptRepositoryImpl implements ReceiptRepository {

    private Context context;
    private AssetRepository assetRepository;
    private BudgetRepository budgetRepository;
    private int year;
    private int month;
    private List<Receipt> receipts;

    public ReceiptRepositoryImpl(Context context, AssetRepository assetRepository, BudgetRepository budgetRepository, int year, int month) throws InfraException {
        this.context = context;
        this.assetRepository = assetRepository;
        this.budgetRepository = budgetRepository;
        refreshCache(year, month);
    }

    @Override
    public void updateReceipt(Receipt receipt) throws DomainException {
        if (!(this.year == receipt.getYear() && this.month == receipt.getMonth())) {
            refreshCache(receipt.getYear(), receipt.getMonth());
        }
        if (receipt.getId() < 0) {
            ReceiptDAO receiptDAO = new ReceiptDAO(context, assetRepository, year, month);
            receiptDAO.create(receipt);
            ReceiptDetailDAO detailDAO = new ReceiptDetailDAO(context, receipt, budgetRepository);
            for (ReceiptDetail detail : receipt.getDetails()) {
                detailDAO.create(detail);
            }
            receipts.add(receipt);
        } else {
            //仕様としてレシートの更新は認めていない．例外スロー．
            throw new InfraException("INF:RRI:UDR");
        }
    }

    @Override
    public List<Receipt> readReceiptByDate(int year, int month) throws InfraException {
        if (!(this.year == year && this.month == month)) {
            refreshCache(year, month);
        }
        List<Receipt> receiptList = Stream.of(receipts).filter(s -> (s.getYear() == year && s.getMonth() == month)).collect(Collectors.toList());
        if (receiptList == null) {
            return new ArrayList<Receipt>();
        } else {
            return receiptList;
        }
    }

    @Override
    public List<Receipt> readReceiptByDate(int year, int month, int day) throws InfraException {
        if (!(this.year == year && this.month == month)) {
            refreshCache(year, month);
        }
        List<Receipt> receiptList = Stream.of(receipts).filter(s -> (s.getYear() == year && s.getMonth() == month && s.getDay() == day)).collect(Collectors.toList());
        if (receiptList == null) {
            return new ArrayList<Receipt>();
        } else {
            return receiptList;
        }
    }

    @Override
    public Receipt readReceiptById(int id) throws DomainException {
        Receipt receipt = Stream.of(receipts).filter(s -> (s.getId() == id)).findFirst().get();
        if (receipt == null) {
            throw new DomainException("INF:RRI:RRBI");
        } else {
            return receipt;
        }
     }

    @Override
    public void deleteReceipt(Receipt receipt) throws DomainException {
        if (!receipts.contains(receipt)) {
            return;
        }

        if (!(this.year == receipt.getYear() && this.month == receipt.getMonth())) {
            refreshCache(receipt.getYear(), receipt.getMonth());
        }

        ReceiptDetailDAO detailDAO = new ReceiptDetailDAO(context, receipt, budgetRepository);
        if (receipt.getDetails().size() > 0) {
            detailDAO.delete(receipt.getDetails().get(0));
        }
        ReceiptDAO receiptDAO = new ReceiptDAO(context, assetRepository, year, month);
        receiptDAO.delete(receipt);
        receipts.remove(receipt);
    }

    private void refreshCache(int year, int month) throws InfraException {
        this.year = year;
        this.month = month;
        ReceiptDAO receiptDAO = new ReceiptDAO(context, assetRepository,year, month);
        receipts = receiptDAO.read();
        for (Receipt receipt : receipts) {
            ReceiptDetailDAO detailDAO = new ReceiptDetailDAO(context, receipt, budgetRepository);
            List<ReceiptDetail> details = detailDAO.read();
            for (ReceiptDetail detail : details) {
                receipt.getDetails().add(detail);
            }
        }
    }
}
