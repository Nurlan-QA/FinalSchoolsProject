package api.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

public class ReqSpec {

    public static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(ApiConfig.BASE_URL)
            .addHeader("Content-Type", "application/json")
            .addFilter(new AllureRestAssured())
            .log(LogDetail.ALL)
            .build();
}