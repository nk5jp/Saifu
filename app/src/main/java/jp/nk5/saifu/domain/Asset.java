package jp.nk5.saifu.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 資産エンティティ．
 * Created by NK5JP on 2016/04/16.
 */
@AllArgsConstructor
public class Asset {
    @Getter @Setter
    private int id;
    @Getter
    private String name;
    @Getter @Setter
    private int amount;
    @Getter @Setter
    private boolean isValid;

    /**
     * 永続化可能か否かを判定する．
     * 名前が空白でないこと，
     * @return このエンティティが永続か可能か否か．
     */
    public boolean canPersistent()
    {
        return (!name.equals(""));
    }
}
