package jp.nk5.saifu.domain;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * レシートのエンティティ．削除は可能であるが，更新は出来ない．
 * Created by NK5JP on 2016/04/11.
 */
@AllArgsConstructor
public class Receipt {
    @Getter @Setter
    private int id;
    @Getter
    private int year;
    @Getter
    private int month;
    @Getter
    private int day;
    @Getter @Setter
    private Asset asset;
    @Getter
    private List<ReceiptDetail> details;

    /**
     * 永続化可能か否かを判定する．
     * 明細が1つ以上紐づいており，それらがすべて有効で，
     * かつ明細とレシートの紐づけが正しくなされていること．
     * @return このエンティティが永続か可能か否か．
     */
    public boolean canPersistent()
    {
        boolean result = true;
        if (details.size() == 0) {
            result =false;
        }
        for (ReceiptDetail detail : details) {
            if (!detail.canPersistent()) {
                result = false;
                break;
            }
            if (detail.getReceipt().getId() != this.id) {
                result = false;
                break;
            }
        }
        return result;
    }
}
