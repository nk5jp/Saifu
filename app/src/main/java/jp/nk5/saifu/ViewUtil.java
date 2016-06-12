package jp.nk5.saifu;

/**
 * Created by NK5JP on 2016/05/01.
 */
public class ViewUtil {
    public static String getMoneyString(int money) {
        if (money < 0) {
            return "-" + getMoneyString(-money);
        }
        if (money < 1000) {
            return Integer.toString(money);
        } else {
            return getMoneyString(money / 1000) + "," + padding(money % 1000);
        }
    }

    private static String padding(int money) {
        if (money >= 100) {
            return Integer.toString(money);
        } else if (money >= 10) {
            return "0" + money;
        } else {
            return "00" + money;
        }
    }
}
