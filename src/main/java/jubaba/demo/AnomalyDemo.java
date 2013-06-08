package jubaba.demo;

import jubaba.Configuration;
import jubaba.Configuration.ConfigurationBuilder;
import jubaba.anomaly.Anomaly;
import jubaba.anomaly.AnomalyFeatures;
import jubaba.anomaly.AnomalyResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Anomaly のデモアプリです。
 * <p>
 * <a href="http://jubat.us/ja/tutorial/anomaly_java.html">http://jubat.us/ja/tutorial/anomaly_java.html</a> の
 * サンプルを参考にしています。
 * </p>
 */
public class AnomalyDemo {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 9199;
    public static final String NAME = "anom_kddcup";
    public static final String FILE_PATH = "./src/main/resources/";
    public static final String TEXT_NAME = "kddcup.data_10_percent.txt";

    // TEXTのカラム名定義
    public static String[] TEXT_COLUMN = {
            "duration",
            "protocol_type",
            "service",
            "flag",
            "src_bytes",
            "dst_bytes",
            "land",
            "wrong_fragment",
            "urgent",
            "hot",
            "num_failed_logins",
            "logged_in",
            "num_compromised",
            "root_shell",
            "su_attempted",
            "num_root",
            "num_file_creations",
            "num_shells",
            "num_access_files",
            "num_outbound_cmds",
            "is_host_login",
            "is_guest_login",
            "count",
            "srv_count",
            "serror_rate",
            "srv_serror_rate",
            "rerror_rate",
            "srv_rerror_rate",
            "same_srv_rate",
            "diff_srv_rate",
            "srv_diff_host_rate",
            "dst_host_count",
            "dst_host_srv_count",
            "dst_host_same_srv_rate",
            "dst_host_diff_srv_rate",
            "dst_host_same_src_port_rate",
            "dst_host_srv_diff_host_rate",
            "dst_host_serror_rate",
            "dst_host_srv_serror_rate",
            "dst_host_rerror_rate",
            "dst_host_srv_rerror_rate",
            "label"
    };

    // String型の項目
    public static String[] STRING_COLUMN = {
            "protocol_type",
            "service",
            "flag",
            "land",
            "logged_in",
            "is_host_login",
            "is_guest_login"
    };

    // Double型の項目
    public static String[] DOUBLE_COLUMN = {
            "duration",
            "src_bytes",
            "dst_bytes",
            "wrong_fragment",
            "urgent",
            "hot",
            "num_failed_logins",
            "num_compromised",
            "root_shell",
            "su_attempted",
            "num_root",
            "num_file_creations",
            "num_shells",
            "num_access_files",
            "num_outbound_cmds",
            "count",
            "srv_count",
            "serror_rate",
            "srv_serror_rate",
            "rerror_rate",
            "srv_rerror_rate",
            "same_srv_rate",
            "diff_srv_rate",
            "srv_diff_host_rate",
            "dst_host_count",
            "dst_host_srv_count",
            "dst_host_same_srv_rate",
            "dst_host_same_src_port_rate",
            "dst_host_diff_srv_rate",
            "dst_host_srv_diff_host_rate",
            "dst_host_serror_rate",
            "dst_host_srv_serror_rate",
            "dst_host_rerror_rate",
            "dst_host_srv_rerror_rate"
    };

    static enum Type {
        DOUBLE() {
            @Override
            void set(AnomalyFeatures features, String key, String value) {
                features.add(key, Double.parseDouble(value));
            }
        },
        STRING() {
            @Override
            void set(AnomalyFeatures features, String key, String value) {
                features.add(key, value);
            }
        };

        abstract void set(AnomalyFeatures features, String key, String value);
    }

    private static Type[] types;

    static {
        Set<String> stringColumns = new HashSet<>(Arrays.asList(STRING_COLUMN));

        types = new Type[TEXT_COLUMN.length];
        for (int i = 0; i < TEXT_COLUMN.length; i++) {
            if (stringColumns.contains(TEXT_COLUMN[i])) {
                types[i] = Type.STRING;
            } else {
                types[i] = Type.DOUBLE;
            }
        }
    }

    public void execute() throws Exception {
        Configuration conf = new ConfigurationBuilder()
                .host(HOST)
                .port(PORT)
                .timeoutSeconds(10)
                .build();
        Anomaly anomaly = new Anomaly(conf, NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH + TEXT_NAME))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] elems = line.split(",");
                AnomalyFeatures features = anomaly.newFeatures();
                for (int i = 0; i < elems.length - 1; i++) {
                    types[i].set(features, TEXT_COLUMN[i], elems[i]);
                }

                AnomalyResult res = features.apply();
                if (res.isAnomaly()) {
                    System.out.println(res.toString() + ", " + elems[elems.length - 1]);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new AnomalyDemo().execute();
        System.exit(0);
    }
}