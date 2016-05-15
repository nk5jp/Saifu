package jp.nk5.saifu.form;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * Created by NK5JP on 2016/05/08.
 */
@AllArgsConstructor
public class BudgetForm {
    @Getter @Setter
    private int selectedId;
    @Getter @Setter
    private int year;
    @Getter @Setter
    private int month;
    @Getter @Setter
    private List<PlanActualDTO> DTOs;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int amount;
    @Getter @Setter
    private boolean isValid;
}
