package jp.nk5.saifu.app;

import java.util.ArrayList;
import java.util.List;

import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;
import jp.nk5.saifu.domain.ReceiptRepository;
import jp.nk5.saifu.form.MainForm;
import jp.nk5.saifu.form.PlanActualDTO;

/**
 * MainActivityのアプリケーション層．
 * Created by NK5JP on 2016/05/01.
 */
public class MainApplication {

    protected ReceiptRepository receiptRepository;
    protected BudgetRepository budgetRepository;

    public MainApplication(ReceiptRepository receiptRepository, BudgetRepository budgetRepository) {
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

    public int calcTotal(List<PlanActualDTO> DTOs) {
        int total = 0;
        for (PlanActualDTO dto : DTOs) {
            total = total + dto.getActualAmount();
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

}
