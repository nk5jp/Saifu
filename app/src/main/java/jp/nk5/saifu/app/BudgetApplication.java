package jp.nk5.saifu.app;

import java.util.ArrayList;
import java.util.List;

import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;
import jp.nk5.saifu.domain.ReceiptRepository;
import jp.nk5.saifu.form.PlanActualDTO;

/**
 * 予算画面のアプリケーション層．
 * Created by NK5JP on 2016/05/08.
 */
public class BudgetApplication {

    private ReceiptRepository receiptRepository;
    private BudgetRepository budgetRepository;

    public BudgetApplication (ReceiptRepository receiptRepository, BudgetRepository budgetRepository) {
        this.receiptRepository = receiptRepository;
        this.budgetRepository = budgetRepository;
    }

    /**
     * 予実情報をリスト化して返却する．
     */
    public List<PlanActualDTO> createPlanActualList(int year, int month) throws DomainException {
        List<PlanActualDTO> dtos = new ArrayList<PlanActualDTO>();
        List<Budget> budgets = budgetRepository.readBudgetByDate(year, month);
        for (Budget budget : budgets) {
            dtos.add(new PlanActualDTO(budget, 0));
        }
        List<Receipt> receipts = receiptRepository.readReceiptByDate(year, month);
        calculateAndSetActualValue(dtos, receipts);
        return dtos;
    }

    public int calcActualTotal(List<PlanActualDTO> DTOs) {
        int total = 0;
        for (PlanActualDTO dto : DTOs) {
            total = total + dto.getActualAmount();
        }
        return total;
    }

    public int calcPlanTotal(List<PlanActualDTO> DTOs) {
        int total = 0;
        for (PlanActualDTO dto : DTOs) {
            total = total + dto.getBudget().getAmount();
        }
        return total;
    }

    /**
     * レシートの各明細の値を元に，予算の実績を加算していく．
     * @param dtos 格納先となるDTOコレクション
     * @param receipts 算出元となるレシートコレクション
     */
    private void calculateAndSetActualValue(List<PlanActualDTO> dtos, List<Receipt> receipts) {
        for (Receipt receipt : receipts) {
            for (ReceiptDetail detail : receipt.getDetails()) {
                for (PlanActualDTO dto : dtos) {
                    if (dto.getBudget().getId() == detail.getBudget().getId()) {
                        dto.setActualAmount(dto.getActualAmount() + detail.getAmount());
                        break;
                    }
                }
            }
        }
    }

    public void updateBudget(int id, String name, int year, int month, int amount, boolean isValid) throws DomainException {

        Budget budget;
        int tempAmount = 0;
        boolean tempValid = true;
        if (id < 0) {
            budget = new Budget(id, name, year, month, amount, isValid);
        } else {
            budget = budgetRepository.readBudgetById(id);
            tempAmount = budget.getAmount();
            tempValid = budget.isValid();
            budget.setAmount(amount);
            budget.setValid(isValid);
        }
        if (budget.canPersistent()) {
            budgetRepository.updateBudget(budget);
        } else {
            budget.setAmount(tempAmount);
            budget.setValid(tempValid);
            throw new DomainException("DOM:BA:UB");
        }
    }

}
