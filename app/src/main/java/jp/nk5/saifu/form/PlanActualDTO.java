package jp.nk5.saifu.form;

import jp.nk5.saifu.domain.Budget;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * MainFormで使う，予実リスト用のDTO．
 * Created by NK5JP on 2016/04/30.
 */
@AllArgsConstructor
public class PlanActualDTO {
    @Getter @Setter
    private Budget budget;
    @Getter @Setter
    private int actualAmount;
}
