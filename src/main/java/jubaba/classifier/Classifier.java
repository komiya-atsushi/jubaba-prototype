package jubaba.classifier;

import jubaba.Configuration;
import us.jubat.classifier.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Jubatus の多値分類機能を利用するインタフェースを提供しています。
 *
 * @author KOMIYA Atsushi
 */
public class Classifier {
    private ClassifierClient client;

    private String name;

    /**
     * 接続先の Jubatus サーバ情報を指定して、Classifier オブジェクトを生成します。
     *
     * @param conf 接続先の Jubatus サーバ情報を指定します。
     * @param name タスクを識別する ZooKeeperクラスタ内でユニークな名前を指定します
     * @throws Exception
     */
    public Classifier(Configuration conf, String name) throws Exception {
        client = new ClassifierClient(conf.host(), conf.port(), conf.timeoutSeconds());
        this.name = name;
    }

    /**
     * リモートサーバ上にある Jubatus の分類モデルをファイルに保存するように指示を与えます。
     *
     * @param id ファイル名を指定します。
     * @return 分類モデルの保存に成功した場合、true が返却されます。
     */
    public boolean save(String id) {
        return client.save(name, id);
    }

    /**
     * リモートサーバ上にある Jubatus の分類モデルをファイルから読み込むように指示を与えます。
     *
     * @param id フィアル名を指定します。
     * @return 分類モデルの読み込みに成功した場合、true が返却されます。
     */
    public boolean load(String id) {
        return client.load(name, id);
    }

    /**
     * リモートサーバ上で保持している分類モデルを破棄します。
     *
     * @return 分類モデルの破棄に成功した場合、true が返却されます。
     */
    public boolean clear() {
        return client.clear(name);
    }

    /**
     * 分類器の学習もしくは判別処理をするための ClassificationFeatures オブジェクトを生成します。
     * <p>
     * ClassificationFeatures#add() で特徴を追加し、ClassificationFeatures#label() でラベルを設定して
     * ClassificationFeatures#train() を呼び出すと、分類モデルの学習が行われます。
     * ラベルを設定せずに特徴のみ追加して ClassificationFeatures#classify() を呼び出すと、判別が行われます。
     * </p>
     *
     * @return
     */
    public ClassificationFeatures newFeatures() {
        return new ClassificationFeatures(this);
    }

    /**
     * 訓練データによる分類器学習の一括処理を開始します。
     * <p>
     * 訓練データを構築し終えたところで BulkClassifierTrainer#train() を呼び出すと、
     * 内部で ClassifierClient#train() が呼び出され、分類モデルの学習が行われます。
     * </p>
     *
     * @return
     */
    public BulkClassifierTrainer newBulkClassifierTrainer() {
        return new BulkClassifierTrainer(this);
    }

    /**
     * 分類器による一括判別処理を開始します。
     * <p>
     * 判別対象のデータを構築し終えたところで BulkClassifierTrainer#classify() を呼び出すと、
     * 内部で ClassifierClient#classify() が呼び出され、一括での判別処理が行われます。
     * </p>
     *
     * @return
     */
    public BulkClassifier newBulkClassifier() {
        return new BulkClassifier(this);
    }

    void train(TupleStringDatum data) {
        client.train(name, Collections.singletonList(data));
    }

    void trainBulk(List<TupleStringDatum> data) {
        client.train(name, data);
    }

    ClassificationResult classify(Datum datum) {
        List<List<EstimateResult>> res = client.classify(name, Collections.singletonList(datum));
        return new ClassificationResult(res.get(0));
    }

    List<ClassificationResult> classifyBulk(List<Datum> data) {
        List<List<EstimateResult>> res = client.classify(name, data);
        List<ClassificationResult> result = new ArrayList<>(res.size());
        for (List<EstimateResult> est : res) {
            result.add(new ClassificationResult(est));
        }
        return result;
    }
}
