package jubaba.anomaly;

import jubaba.Configuration;
import us.jubat.anomaly.AnomalyClient;
import us.jubat.anomaly.Datum;
import us.jubat.anomaly.TupleStringFloat;

public class Anomaly {
    private AnomalyClient client;

    private String name;

    public Anomaly(Configuration conf, String name) throws Exception {
        client = new AnomalyClient(conf.host(), conf.port(), conf.timeoutSeconds());
        this.name = name;
    }

    public AnomalyFeatures newFeatures() {
        return new AnomalyFeatures(this);
    }

    TupleStringFloat apply(Datum feature) {
        return client.add(name, feature);
    }
}
