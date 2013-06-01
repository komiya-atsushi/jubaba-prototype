package jubaba;

/**
 * Jubatus サーバの接続情報を保持します。
 *
 * @author KOMIYA Atsushi
 */
public class Configuration {
    private String host;

    private int port;

    private double timeoutSeconds;

    private Configuration(String host, int port, double timeoutSeconds) {
        this.host = host;
        this.port = port;
        this.timeoutSeconds = timeoutSeconds;
    }

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    public double timeoutSeconds() {
        return timeoutSeconds;
    }

    public static class ConfigurationBuilder {
        private String host;

        private int port;

        private double timeoutSeconds = 1.0;

        /**
         * 接続先 Jubatus サーバのホスト名を指定します。
         *
         * @param host ホスト名
         * @return このオブジェクト自身が返却されます。
         */
        public ConfigurationBuilder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * 接続先 Jubatus サーバのポート番号を指定します。
         *
         * @param port ポート番号
         * @return このオブジェクト自身が返却されます。
         */
        public ConfigurationBuilder port(int port) {
            this.port = port;
            return this;
        }

        /**
         * RPC メソッドの実行からレスポンスまでのタイムアウト時間を指定します。
         * <p>
         * 既定のタイムアウト時間は 1 秒です。
         * </p>
         *
         * @param timeoutSeconds タイムアウト時間 (秒)
         * @return このオブジェクト自身が返却されます。
         */
        public ConfigurationBuilder timeoutSeconds(double timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        public Configuration build() {
            return new Configuration(host, port, timeoutSeconds);
        }
    }
}
