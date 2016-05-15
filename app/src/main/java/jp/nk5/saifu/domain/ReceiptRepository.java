package jp.nk5.saifu.domain;

import java.util.List;

/**
 * レシートエンティティのリポジトリインタフェース．実装はInfra層が担う．
 * 全レシートを扱うとデータが膨大になり得るので，コンストラクタで指定した年月を
 * 対象として振る舞う．
 * Created by NK5JP on 2016/04/29.
 */
public interface ReceiptRepository {

    /**
     * 引数で指定した資産エンティティを明細含め永続化する．初期永続化時はidも付与する．
     * @param receipt 永続化対象のレシートエンティティ．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public void updateReceipt(Receipt receipt) throws DomainException;

    /**
     * 指定した年月のレシートエンティティを返却する．メイン画面で使用想定．
     * @param year 指定年
     * @param month 指定月
     * @return レシートエンティティのリスト
     * @throws DomainException
     */
    public List<Receipt> readReceiptByDate(int year, int month) throws DomainException;

    /**
     * 指定した日のレシートエンティティを返却する．カレンダー画面で使用想定．
     * @param year 指定年
     * @param month 指定月
     * @param day 指定日
     * @return レシートエンティティのリスト．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public List<Receipt> readReceiptByDate(int year, int month, int day) throws DomainException;

    /**
     * 指定したIDのレシートエンティティを返却する．レシート画面（参照モード）で使用想定．
     * @param id 取得対象レシートのID
     * @return レシート
     * @throws DomainException
     */
    public Receipt readReceiptById(int id) throws DomainException;

    /**
     * 指定したレシートエンティティを削除する．カレンダー画面で使用想定．
     * @param receipt 削除対象レシート．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public void deleteReceipt(Receipt receipt) throws DomainException;

}
