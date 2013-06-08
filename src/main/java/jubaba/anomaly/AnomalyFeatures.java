package jubaba.anomaly;

import us.jubat.anomaly.Datum;
import us.jubat.anomaly.TupleStringDouble;
import us.jubat.anomaly.TupleStringString;

import java.util.ArrayList;

public class AnomalyFeatures {
    private Anomaly anomaly;

    private Datum features;

    AnomalyFeatures(Anomaly anomaly) {
        this.anomaly = anomaly;
        this.features = new Datum();
        this.features.num_values = new ArrayList<>();
        this.features.string_values = new ArrayList<>();
    }

    public AnomalyFeatures add(String key, String value) {
        TupleStringString v = new TupleStringString();
        v.first = key;
        v.second = value;
        features.string_values.add(v);
        return this;
    }

    public AnomalyFeatures add(String key, double value) {
        TupleStringDouble v = new TupleStringDouble();
        v.first = key;
        v.second = value;
        features.num_values.add(v);
        return this;
    }

    public AnomalyResult apply() {
        return new AnomalyResult(anomaly.apply(features));
    }
}