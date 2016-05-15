package jp.nk5.saifu.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * レシート明細エンティティ．
 * Created by NK5JP on 2016/04/16.
 */
@AllArgsConstructor
public class ReceiptDetail {
    @Getter @Setter
    private int id;
    @Getter
    private Receipt receipt;
    @Getter
    private Budget budget;
    @Getter @Setter
    private int amount;

    /**
     * 永続化可能か否かを判定する．
     * 金額が正の整数であること，および参照先の予算が有効であること，
     * @return このエンティティが永続か可能か否か．
     */
    public boolean canPersistent()
    {
        return amount > 0;
    }
}
