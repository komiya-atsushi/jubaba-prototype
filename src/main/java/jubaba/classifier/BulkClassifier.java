package jubaba.classifier;

import us.jubat.classifier.Datum;

import java.util.List;

/**
 * 複数のデータを一括して分類する機能を提供します。
 *
 * @author KOMIYA Atsushi
 */
public class BulkClassifier extends BaseBulkTask<Datum> {
    BulkClassifier(Classifier classifier) {
        super(classifier);
    }

    @Override
    public ClassificationFeatures newFeatures() {
        ClassificationFeatures result = super.newFeatures();
        data.add(result.features.second);
        return result;
    }

    public List<ClassificationResult> classify() {
        return classifier.classifyBulk(data);
    }
}
