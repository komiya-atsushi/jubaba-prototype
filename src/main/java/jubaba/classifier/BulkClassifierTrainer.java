package jubaba.classifier;

import us.jubat.classifier.TupleStringDatum;

/**
 * 複数のデータを一括して学習させる機能を提供します。
 *
 * @author KOMIYA Atsushi
 */
public class BulkClassifierTrainer extends BaseBulkTask<TupleStringDatum> {
    BulkClassifierTrainer(Classifier classifier) {
        super(classifier);
    }

    @Override
    public ClassificationFeatures newFeatures() {
        ClassificationFeatures result = super.newFeatures();
        data.add(result.features);
        return result;
    }

    public void train() {
        classifier.trainBulk(data);
    }
}
