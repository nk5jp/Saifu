package jp.nk5.saifu.infra;

/**
 * 各種DAOで共通的に使用する処理．
 * Created by NK5JP on 2016/04/24.
 */
public class DAOUtil {

    /**
     * 本アプリのDB上でtrue/falseの意味を持つ整数値を返す．
     * @return true/falseに対応する．
     */
    public static int returnNumFromBool(boolean bool) {
        if(bool) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * DBの格納整数をboolに変換する．
     * @return DB格納整数に対応するbool値
     */
    public static boolean returnBoolFromNum(int num) {
        if(num > 0) {
            return true;
        } else {
            return false;
        }
    }

}
