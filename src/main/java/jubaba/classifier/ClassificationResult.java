package jubaba.classifier;

import us.jubat.classifier.EstimateResult;

import java.util.*;

/**
 * 分類結果を表します。
 * <ul>
 * <li>分類スコアが最も高いクラスのラベルを取得する場合は、ClassificationResult#maximumScoredLabel() メソッドを呼び出します</li>
 * <li>ラベル名から分類スコアを参照したい場合は、ClassificationResult#asMap() メソッドで Map オブジェクトに変換して利用します。</li>
 * <li>ひと通りの分類結果を確認したい場合は、ClassificationResult#iterator() メソッドから Iterator オブジェクトを取得して利用します</li>
 * </ul>
 *
 * @author KOMIYA Atsushi
 */
public class ClassificationResult implements Iterable<EstimateResult> {
    private List<EstimateResult> classifications;

    private String maximumScoredLabel;

    ClassificationResult(List<EstimateResult> classifications) {
        this.classifications = classifications;
    }

    /**
     * 分類スコアが最大となったクラスのラベルを返却します。
     *
     * @return ラベルを表す文字列が返却されます。
     */
    public String maximumScoredLabel() {
        if (maximumScoredLabel != null) {
            return maximumScoredLabel;
        }

        double maxScore = Double.MIN_VALUE;

        for (EstimateResult er : classifications) {
            if (er.score > maxScore) {
                maxScore = er.score;
                maximumScoredLabel = er.label;
            }
        }

        return maximumScoredLabel;
    }

    /**
     * 分類結果を Map オブジェクトに変換して返却します。
     *
     * @return キーがラベル名、値が分類スコアの Map オブジェクトが返却されます。
     */
    public Map<String, Double> asMap() {
        Map<String, Double> result = new HashMap<>();

        for (EstimateResult res : classifications) {
            result.put(res.label, res.score);
        }

        return result;
    }

    public String label(int classIndex) {
        return classifications.get(classIndex).label;
    }

    public double score(int classIndex) {
        return classifications.get(classIndex).score;
    }

    @Override
    public Iterator<EstimateResult> iterator() {
        return Collections.unmodifiableList(classifications).iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (EstimateResult er : classifications) {
            if (sb.length() > 0) {
                sb.append(",\n");
            }
            sb.append("{label:").append(er.label).append(", score:").append(er.score).append('}');
        }
        return sb.append("\n").toString();
    }
}
