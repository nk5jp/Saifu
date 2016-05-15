package jp.nk5.saifu.domain;


import java.util.List;

/**
 * 予算エンティティのリポジトリインタフェース．実装はinfra層が担務する．
 * Created by NK5JP on 2016/04/29.
 */
public interface BudgetRepository {

    /**
     * 引数で指定した予算エンティティを永続化する．初期永続化時はidも付与する．
     * @param budget 永続化対象の予算エンティティ．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public void updateBudget(Budget budget) throws DomainException;

    /**
     * 指定した年月の全ての予算エンティティを返却する．メイン画面で使用想定．．
     * @param year 指定年
     * @param month 指定月
     * @return 全ての予算エンティティのリスト．何もない場合は空リストを返却．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public List<Budget> readBudgetByDate(int year, int month) throws DomainException;

    /**
     * 全ての有効な予算エンティティを返却する．買い物画面で使用想定．
     * @return 全ての有効な予算エンティティのリスト．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public List<Budget> readValidBudgetByDate(int year, int month) throws DomainException;

    /**
     * 指定したIDの資産エンティティを返却する．予算作成画面で使用想定．
     * @param id 取得対象の予算ID．
     * @return 取得した予算エンティティ．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public Budget readBudgetById(int id) throws DomainException;


}
