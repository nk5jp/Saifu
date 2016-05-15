package jp.nk5.saifu.app;

import java.util.List;

import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptRepository;

/**
 * カレンダー画面のアプリケーション層．
 * Created by NK5JP on 2016/05/04.
 */
public class CalendarApplication {

    private ReceiptRepository repository;

    public CalendarApplication (ReceiptRepository repository) {
        this.repository = repository;
    }

    public List<Receipt> createReceiptList(int year, int month, int day) throws DomainException {
        List<Receipt> receipts = repository.readReceiptByDate(year, month, day);
        return receipts;
    }

}
