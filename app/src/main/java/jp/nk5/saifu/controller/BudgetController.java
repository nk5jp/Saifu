package jp.nk5.saifu.controller;

import jp.nk5.saifu.app.BudgetApplication;
import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.form.BudgetForm;
import jp.nk5.saifu.form.UpdateViewListener;

/**
 * 予算画面のコントローラ．
 * Created by NK5JP on 2016/05/08.
 */
public class BudgetController {

    private BudgetApplication application;
    private BudgetForm form;
    private UpdateViewListener listener;

    public BudgetController(BudgetApplication application, BudgetForm form, UpdateViewListener listener) {
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
            form.setYear(year);
            form.setMonth(month);
            clearBudget(-1, "", 0, true);
        } catch (DomainException e) {
            setErrorMessage(e);
        }
    }

    /**
     * 予算を作成もしくは更新する．
     * @param year 対象年
     * @param month 対象月
     * @param name 予算名
     * @param amount 予算
     * @param isValid 有効か
     */
    public void updateBudget(int year, int month, String name, int amount, boolean isValid) {
        try {
            application.updateBudget(form.getSelectedId(), name, year, month, amount, isValid);
            updatePlanActualList(year, month);
        } catch (DomainException e) {
            setErrorMessage(e);
        }
    }

    /**
     * 予算情報のUIを更新する．
     * @param selectedId 選択している予算
     * @param name 予算名
     * @param amount 予算金額
     * @param isValid 有効か
     */
    public void clearBudget(int selectedId, String name, int amount, boolean isValid) {
        form.setSelectedId(selectedId);
        form.setName(name);
        form.setAmount(amount);
        form.setValid(isValid);
        updateUI();
    }

    private void updateUI() {
        listener.updateUI();
    }

    public void setErrorMessage(DomainException e) {
        listener.showError(e.getLayerName());
    }



}