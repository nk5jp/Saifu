package jp.nk5.saifu.controller;

import jp.nk5.saifu.app.BudgetApplication;
import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.form.MainForm;
import jp.nk5.saifu.form.UpdateViewListener;

/**
 * メイン画面におけるコントローラ．
 * Created by NK5JP on 2016/05/01.
 */
public class MainController {

    private BudgetApplication application;
    private MainForm form;
    private UpdateViewListener listener;

    public MainController (BudgetApplication application, MainForm form, UpdateViewListener listener) {
        this.application = application;
        this.form = form;
        this.listener = listener;
    }

    /**
     * 予実表を更新する．
     * @param year 指定年
     * @param month 指定月
     */
    public void updatePlanActualList(int year, int month) {
        try {
            form.setDTOs(application.createPlanActualList(year, month));
            form.setTotal(application.calcTotal(form.getDTOs()));
            form.setYear(year);
            form.setMonth(month);
            updateUI();
        } catch (DomainException e) {
            setErrorMessage(e);
        }
    }

    private void updateUI() {
        listener.updateUI();
    }

    public void setErrorMessage(DomainException e) {
        listener.showError(e.getLayerName());
    }

}
