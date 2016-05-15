package jp.nk5.saifu.form;

/**
 * Activityが継承する．Formクラス側で画面更新のトリガを引けるように
 * するためのインタフェース．
 * Created by NK5JP on 2016/04/30.
 */
public interface UpdateViewListener {
    public void updateUI();
    public void showError(String errorMsg);
}
