package jp.nk5.saifu.controller;

import java.util.List;

import jp.nk5.saifu.app.CalendarApplication;
import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.form.CalendarForm;
import jp.nk5.saifu.form.UpdateViewListener;

/**
 * カレンダー画面のコントローラ．
 * Created by NK5JP on 2016/05/04.
 */
public class CalendarController {
    private CalendarApplication application;
    private CalendarForm form;
    private UpdateViewListener listener;

    public CalendarController(CalendarApplication application, CalendarForm form, UpdateViewListener listener) {
        this.application = application;
        this.form = form;
        this.listener = listener;
    }

    public void updateReceiptList(int year, int month, int day) {
        try {
            List<Receipt> receipts = application.createReceiptList(year, month, day);
            form.setYear(year);
            form.setMonth(month);
            form.setDay(day);
            form.setReceipts(receipts);
            updateUI();
        } catch (DomainException e) {
            showErrorMessage(e);
        }
    }

    private void updateUI() {
        listener.updateUI();
    }

    private void showErrorMessage(DomainException e) {
        listener.showError(e.getLayerName());
    }

}
