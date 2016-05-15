package jp.nk5.saifu.domain;

import android.content.Context;

import java.util.List;

/**
 * 資産エンティティのリポジトリインタフェース．実装はinfra層が担務する．
 * Created by NK5JP on 2016/04/17.
 */
public interface AssetRepository {

    /**
     * 引数で指定した資産エンティティを永続化する．初期永続化時はidも付与する．
     * @param asset 永続化対象の資産エンティティ．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public void updateAsset(Asset asset) throws DomainException;

    /**
     * 全ての資産エンティティを返却する．振替画面で使用想定．．
     * @return 全ての資産エンティティのリスト．何もない場合は空リストを返却．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public List<Asset> readAllAsset() throws DomainException;

    /**
     * 全ての有効な資産エンティティを返却する．買い物画面で使用想定．
     * @return 全ての有効な資産エンティティのリスト．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public List<Asset> readAllValidAsset() throws DomainException;

    /**
     * 指定したIDの資産エンティティを返却する．資産編集画面で使用想定．
     * @param id 取得対象の資産ID．
     * @return 取得した資産エンティティ．
     * @throws DomainException DBアクセス失敗時にスローされる．
     */
    public Asset readAssetById(int id) throws DomainException;

}
