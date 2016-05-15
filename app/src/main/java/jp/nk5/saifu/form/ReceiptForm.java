package jp.nk5.saifu.form;

import java.util.List;

import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * レシート画面の入出力フォーム
 * Created by NK5JP on 2016/05/05.
 */
@AllArgsConstructor
public class ReceiptForm {
    @Getter @Setter
    private boolean isCreateMode;
    @Getter @Setter
    private List<Asset> assets;
    @Getter @Setter
    private List<Budget> budgets;
    @Getter @Setter
    private Receipt receipt;
    @Getter @Setter
    private int total;
}
