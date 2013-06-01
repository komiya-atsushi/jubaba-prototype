package jubaba.demo;

import jubaba.Configuration;
import jubaba.classifier.ClassificationResult;
import jubaba.classifier.Classifier;
import twitter4j.*;
import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.Datum;
import us.jubat.classifier.EstimateResult;
import us.jubat.classifier.TupleStringString;

import java.util.ArrayList;
import java.util.List;

/**
 * Twitter の Streaming API から受信したツイートを言語判定するデモアプリです。
 * <p>
 * <a href="https://github.com/jubatus/jubatus-example">jubatus-example</a> の Python サンプル
 * <a hrefhttps://github.com/jubatus/jubatus-example/tree/master/twitter_streaming_lang"></a>twitter_streaming_lang</a>
 * を参考にしています。
 * </p>
 */
public class LanguageDetectionDemo {
    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 9199;

        TwitterStream stream = TwitterStreamFactory.getSingleton();
        stream.addListener(newStatusListener(true, host, port));
        stream.sample();

        Thread.sleep(20 * 1000);
        System.exit(0);
    }

    static StatusListener newStatusListener(boolean usesJubaba, String host, int port) throws Exception {
        if (usesJubaba) {
            return new LanguageDetectorJubaba(host, port);

        } else {
            return new LanguageDetectorJubatusJavaClient(host, port);
        }
    }

    /**
     * Jubatus Java クライアントを直に利用した場合の実装です。
     */
    static class LanguageDetectorJubatusJavaClient extends AbstractStatusListener {
        ClassifierClient client;

        LanguageDetectorJubatusJavaClient(String host, int port) throws Exception {
            client = new ClassifierClient(host, port, 1.0);
        }

        @Override
        public void onStatus(Status status) {
            String text = status.getText();

            TupleStringString feature = new TupleStringString();
            feature.first = "text";
            feature.second = text;

            List<TupleStringString> stringFeatures = new ArrayList<>();
            stringFeatures.add(feature);

            Datum datum = new Datum();
            datum.num_values = new ArrayList<>();
            datum.string_values = stringFeatures;

            List<Datum> data = new ArrayList<>();
            data.add(datum);

            List<List<EstimateResult>> res = client.classify("", data);

            List<EstimateResult> classificationResult = res.get(0);

            double maxScore = Double.MIN_VALUE;
            String label = null;
            for (EstimateResult er : classificationResult) {
                if (er.score > maxScore) {
                    maxScore = er.score;
                    label = er.label;
                }
            }

            System.out.println(String.format("[%s] %s", label, text));
        }
    }

    /**
     * Jubaba を利用した場合の実装です。
     */
    static class LanguageDetectorJubaba extends AbstractStatusListener {
        Classifier classifier;

        LanguageDetectorJubaba(String host, int port) throws Exception {
            classifier = new Classifier(new Configuration.ConfigurationBuilder().host(host).port(port).build(), "");
        }

        @Override
        public void onStatus(Status status) {
            String text = status.getText();

            ClassificationResult res = classifier
                    .newFeatures()
                    .add("text", text)
                    .classify();

            System.out.println(String.format("[%s] %s", res.maximumScoredLabel(), text));
        }
    }

    static abstract class AbstractStatusListener implements StatusListener {
        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        }

        @Override
        public void onTrackLimitationNotice(int i) {
        }

        @Override
        public void onScrubGeo(long l, long l2) {
        }

        @Override
        public void onStallWarning(StallWarning stallWarning) {
        }

        @Override
        public void onException(Exception e) {
        }
    }
}
