package org.nikita.loadtesting;

public class V1Call extends CommonCall{

    private static final String URL = "http://localhost:8080/insurance/travel/api/v1/";
    private static final String REQUEST_RESOURCE = "rest/request/validRequest.json";
    private static final String RESPONSE_RESOURCE = "rest/response/validResponse.json";

    public V1Call(LoadTestingStatistic statistic) {
        super(statistic);
    }

    @Override
    protected String getUrl() {
        return URL;
    }

    @Override
    protected String getRequestResource() {
        return REQUEST_RESOURCE;
    }

    @Override
    protected String getExpectedResponseResource() {
        return RESPONSE_RESOURCE;
    }
}
