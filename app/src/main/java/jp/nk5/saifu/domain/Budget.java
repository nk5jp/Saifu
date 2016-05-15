package jp.nk5.saifu.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 予算エンティティ．金額を更新可能．削除は不可．
 * Created by NK5JP on 2016/04/16.
 */
@AllArgsConstructor
public class Budget {
    @Getter @Setter
    private int id;
    @Getter
    private String name;
    @Getter
    private int year;
    @Getter
    private int month;
    @Getter @Setter
    private int amount;
    @Getter @Setter
    private boolean isValid;

    /**
     * 永続化可能か否かを判定する．
     * 名前が空白でないこと，金額が正の整数であること．
     * @return このエンティティが永続か可能か否か．
     */
    public boolean canPersistent()
    {
        return (amount > 0) && (!name.equals("")) && (year > 0) && (month > 0);
    }
}
