package jp.nk5.saifu.infra;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.DomainException;

/**
 * 資産リポジトリの実装クラス．SQLiteを使用して永続化する．
 * selectはコンストラクタで1度のみ行い，以後，コレクションの変更に併せて
 * DBに対してupdateやdeleteをかけて同期をとる．
 * Created by NK5JP on 2016/04/17.
 */
public class AssetRepositoryImpl implements AssetRepository {
    private List<Asset> assets;
    private Context context;

    public AssetRepositoryImpl(Context context) throws InfraException {
        this.context = context;
        AssetDAO dao = new AssetDAO(context);
        assets = dao.read();
    }

    @Override
    public void updateAsset(Asset asset) throws InfraException {
        AssetDAO dao = new AssetDAO(context);
        if (asset.getId() < 0) {
            dao.create(asset);
            assets.add(asset);
        } else {
            dao.update(asset);
        }
    }

    @Override
    public List<Asset> readAllAsset() throws InfraException {
        return assets;
    }

    @Override
    public List<Asset> readAllValidAsset() {
        List<Asset> assetList = Stream.of(assets).filter(s -> s.isValid()).collect(Collectors.toList());
        if (assetList == null) {
            return new ArrayList<Asset>();
        } else {
            return assetList;
        }
    }

    @Override
    public Asset readAssetById(int id) throws InfraException
    {
        Asset asset = Stream.of(assets).filter(s -> (s.getId() == id)).findFirst().get();
        if (asset == null) {
            throw new InfraException("INF:ARI:RAB");
        } else {
            return asset;
        }
    }
}
