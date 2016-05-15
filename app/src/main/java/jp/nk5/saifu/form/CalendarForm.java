package jp.nk5.saifu.form;

import java.util.List;

import jp.nk5.saifu.domain.Receipt;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * カレンダー画面の入出力情報を格納するフォーム．
 * Created by NK5JP on 2016/05/04.
 */
@AllArgsConstructor
public class CalendarForm {
    @Getter @Setter
    private int year;
    @Getter @Setter
    private int month;
    @Getter @Setter
    private int day;
    @Getter @Setter
    private List<Receipt> receipts;
}
