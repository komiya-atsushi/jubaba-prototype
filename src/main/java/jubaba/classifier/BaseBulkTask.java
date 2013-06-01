package jubaba.classifier;

import java.util.ArrayList;
import java.util.List;

/**
 * 複数データを一括処理する機能の基底クラスです。
 *
 * @author KOMIYA Atsushi
 */
public class BaseBulkTask<D> {
    Classifier classifier;

    public List<D> data = new ArrayList<>();

    BaseBulkTask(Classifier classifier) {
        this.classifier = classifier;
    }

    /**
     * 分類器に与える訓練 or 判別用データをこれより構築します。
     *
     * @return
     */
    public ClassificationFeatures newFeatures() {
        return new ClassificationFeatures(classifier);
    }
}
