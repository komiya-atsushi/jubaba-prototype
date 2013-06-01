package jubaba.classifier;

import us.jubat.classifier.TupleStringDatum;

/**
 * 分類器に与えるデータを表します。
 *
 * @author KOMIYA Atsushi
 */
public class ClassificationFeatures {
    private final Classifier classifier;

    TupleStringDatum features = ClassifierHelper.newEmptyTupleStringDatum();

    private boolean executesBulk = false;

    ClassificationFeatures(Classifier classifier) {
        this.classifier = classifier;
    }

    /**
     * 数値で表される特徴を追加します。
     *
     * @param key
     * @param value
     * @return
     */
    public ClassificationFeatures add(String key, double value) {
        throwExceptionIfAlreadyUsed();
        features.second.num_values.add(ClassifierHelper.newTupleStringDouble(key, value));
        return this;
    }

    /**
     * 文字列で表される特徴を追加します。
     *
     * @param key
     * @param value
     * @return
     */
    public ClassificationFeatures add(String key, String value) {
        throwExceptionIfAlreadyUsed();
        features.second.string_values.add(ClassifierHelper.newTupleStringString(key, value));
        return this;
    }

    /**
     * このオブジェクトを訓練データとする場合、正解となるラベルを設定します。
     *
     * @param label
     * @return
     */
    public ClassificationFeatures label(String label) {
        features.first = label;
        return this;
    }

    /**
     * このデータを用いて分類モデルを学習させます。
     * <p>
     * あらかじめ ClassificationFeatures#label() メソッドを呼び出して、正解ラベルを設定しておく必要があります。
     * </p>
     */
    public void train() {
        throwExceptionIfAlreadyUsed();
        if (features.first == null) {
            throw new IllegalStateException("この訓練データにはラベルが設定されていません。");
        }
        classifier.train(features);
        features = null;
    }

    public ClassificationResult classify() {
        throwExceptionIfAlreadyUsed();
        ClassificationResult result = classifier.classify(features.second);
        features = null;
        return result;
    }

    void throwExceptionIfAlreadyUsed() {
        if (features == null) {
            String message = "このオブジェクトは利用済みです。再利用することはできません。";
            throw new IllegalStateException(message);
        }
    }
}
