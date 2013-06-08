package jubaba.anomaly;

import us.jubat.anomaly.TupleStringFloat;

public class AnomalyResult {
    private String id;

    private float score;

    AnomalyResult(TupleStringFloat res) {
        id = res.first;
        score = res.second;
    }

    public String id() {
        return id;
    }

    public float score() {
        return score;
    }

    public boolean isAnomaly() {
        return !Float.isInfinite(score) && score != 1.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("{id:").append(id).append(", score:").append(score).toString();
    }
}
