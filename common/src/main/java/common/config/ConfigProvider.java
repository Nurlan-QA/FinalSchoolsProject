package common.config;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigProvider {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigProvider.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties не найден в classpath!");
            }
            // Явно читаем UTF-8, чтобы русские тексты не бились
            properties.load(new java.io.InputStreamReader(input, java.nio.charset.StandardCharsets.UTF_8));

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке config.properties", e);
        }
    }

    // --- Геттеры для каждого параметра ---

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getApiUrl() {
        return properties.getProperty("api.url");
    }

    public static long getTimeout() {
        return Long.parseLong(properties.getProperty("timeout"));
    }

    public static String getLogMode() {
        return properties.getProperty("log.mode");
    }

    public static String getAdminLogin() {
        return properties.getProperty("admin.login");
    }

    public static String getAdminPassword() {
        return properties.getProperty("admin.password");
    }

    public static String getInvalidLogin() {
        return properties.getProperty("invalid.login");
    }

    public static String getInvalidPassword() {
        return properties.getProperty("invalid.password");
    }

    public static String getProductName() {
        return properties.getProperty("product.name");
    }

    public static String getProductPrice() {
        return properties.getProperty("product.price");
    }

    public static String getProductBigPrice() {
        return properties.getProperty("product.bigPrice");
    }

    public static String getProductUpdatePrice() {
        return properties.getProperty("product.update.price");
    }

    public static String getProductAssertjPrice() {
        return properties.getProperty("product.assertjPrice");
    }

    public static Set<Long> getProtectedIds() {
        return Arrays.stream(properties.getProperty("protected.ids").split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    public static int getApiListPage() {
        return Integer.parseInt(properties.getProperty("api.list.page"));
    }

    public static int getApiListSize() {
        return Integer.parseInt(properties.getProperty("api.list.size"));
    }

    public static String getToastGoodAdded() {
        return properties.getProperty("toast.good.added");
    }

    public static String getToastOrderAccepted() {
        return properties.getProperty("toast.order.accepted");
    }

    public static String getErrorInvalidCredentials() {
        return properties.getProperty("error.invalid.credentials");
    }

    public static String getBrowser() {
        return properties.getProperty("browser");
    }

    public static String getBrowserSize() {
        return properties.getProperty("browser.size");
    }

    public static int getCartMaxSmallOrder() {
        return Integer.parseInt(properties.getProperty("cart.max.small.order"));
    }
}