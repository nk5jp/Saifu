package jp.nk5.saifu.form;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * MainActivityの入出力情報を格納するフォーム．
 * Created by NK5JP on 2016/04/30.
 */
@AllArgsConstructor
public class MainForm {
    @Getter @Setter
    private int year;
    @Getter @Setter
    private int month;
    @Getter @Setter
    private int total;
    @Getter @Setter
    private List<PlanActualDTO> DTOs;
 }
