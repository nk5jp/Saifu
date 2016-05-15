package jp.nk5.saifu.form;

import java.util.List;

import jp.nk5.saifu.domain.Asset;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
/**
 * Created by NK5JP on 2016/05/13.
 */
@AllArgsConstructor
public class TransferForm {
    @Getter @Setter
    private Asset fromAsset;
    @Getter @Setter
    private Asset toAsset;
    @Getter @Setter
    private int amount;
    @Getter @Setter
    private List<Asset> assets;
}
