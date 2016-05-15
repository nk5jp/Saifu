package jp.nk5.saifu.domain;

import lombok.Getter;

/**
 * ドメイン層で発生した例外をラップする．
 *
 * Created by NK5JP on 2016/04/17.
 */
public class DomainException extends Exception {
    @Getter
    private String layerName;

    public DomainException(String layerName) {
        super();
        this.layerName = layerName;
    }

}
