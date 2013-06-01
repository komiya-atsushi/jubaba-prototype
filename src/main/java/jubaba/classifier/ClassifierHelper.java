package jubaba.classifier;

import us.jubat.classifier.Datum;
import us.jubat.classifier.TupleStringDatum;
import us.jubat.classifier.TupleStringDouble;
import us.jubat.classifier.TupleStringString;

import java.util.ArrayList;

public class ClassifierHelper {
    static TupleStringString newTupleStringString(String key, String value) {
        TupleStringString result = new TupleStringString();
        result.first = key;
        result.second = value;
        return result;
    }

    static TupleStringDouble newTupleStringDouble(String key, double value) {
        TupleStringDouble result = new TupleStringDouble();
        result.first = key;
        result.second = value;
        return result;
    }

    static TupleStringDatum newEmptyTupleStringDatum() {
        TupleStringDatum result = new TupleStringDatum();
        result.second = new Datum();
        result.second.num_values = new ArrayList<>();
        result.second.string_values = new ArrayList<>();
        return result;
    }
}
