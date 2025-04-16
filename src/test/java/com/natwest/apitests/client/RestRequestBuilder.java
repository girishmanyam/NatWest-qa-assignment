package com.natwest.apitests.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

public class RestRequestBuilder {

    private String method;
    private String baseUri;
    private String path;
    private Map<String, String> headers;
    private List<String> queryParams;
    private Object body;
    private ContentType contentType;
    private String paramName;

    private RestRequestBuilder() {
        // private constructor to force use of builder()
    }

    public static RestRequestBuilder builder() {
        return new RestRequestBuilder();
    }

    public RestRequestBuilder method(String method) {
        this.method = method.toUpperCase();
        return this;
    }

    public RestRequestBuilder baseUri(String baseUri) {
        this.baseUri = baseUri;
        return this;
    }

    public RestRequestBuilder path(String path) {
        this.path = path;
        return this;
    }

    public RestRequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RestRequestBuilder queryParams(String paramName, List<String> queryParams) {
        this.queryParams = queryParams;
        this.paramName = paramName;
        return this;
    }

    public RestRequestBuilder body(Object body) {
        this.body = body;
        return this;
    }

    public RestRequestBuilder contentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public Response execute() {
        RequestSpecification request = RestAssured.given();


        if (baseUri != null) request.baseUri(baseUri);
        if (path != null) request.basePath(path);
        if (headers != null) request.headers(headers);
        if (queryParams != null) request.queryParam(paramName, queryParams);
        if (body != null) request.body(body);
        if (contentType != null) request.contentType(contentType);

        return switch (method) {
            case "GET" -> request.get();
            case "POST" -> request.post();
            case "DELETE" -> request.delete();
            default -> throw new IllegalArgumentException("Unsupported method: " + method);
        };
    }
}

