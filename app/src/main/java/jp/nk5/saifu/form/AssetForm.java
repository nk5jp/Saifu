package jp.nk5.saifu.form;

import java.util.List;

import jp.nk5.saifu.domain.Asset;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * 資産画面のフォーム
 * Created by NK5JP on 2016/05/08.
 */
@AllArgsConstructor
public class AssetForm {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int total;
    @Getter @Setter
    private List<Asset> assets;
}
