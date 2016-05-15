package jp.nk5.saifu.infra;

import jp.nk5.saifu.domain.DomainException;

/**
 * Created by NK5JP on 2016/04/24.
 */
public class InfraException extends DomainException {

    public InfraException (String layerName) {
        super(layerName);
    }

}
