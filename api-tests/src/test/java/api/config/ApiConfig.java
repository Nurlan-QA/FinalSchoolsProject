package api.config;

import common.config.ConfigProvider;
import io.restassured.RestAssured;

public class ApiConfig {
    public static final String BASE_URL = ConfigProvider.getApiUrl();

    static {
        RestAssured.baseURI = BASE_URL;
    }
}