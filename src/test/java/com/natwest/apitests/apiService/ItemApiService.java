package com.natwest.apitests.apiService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.natwest.apitests.client.RestRequestBuilder;
import com.natwest.apitests.model.Item;
import config.ConfigUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class ItemApiService {

    private static final String BASE_URI = ConfigUtil.get("baseUri");

    private static final String API_PATH = "/objects";

    private ItemApiService() {}

    public static Response addItem(Item item) {
        return RestRequestBuilder.builder().method("POST")
                .baseUri(BASE_URI)
                .path(API_PATH)
                .headers(Map.of("Accept", "application/json"))
                .contentType(ContentType.JSON)
                .body(item)
                .execute();

    }

    public static Response getItemById(String id) {
        String endpoint = API_PATH + "/" + id;
        return RestRequestBuilder.builder()
                .method("GET")
                .baseUri(BASE_URI)
                .path(endpoint)
                .headers(Map.of("Accept", "application/json"))
                .contentType(ContentType.JSON)
                .execute();
    }

    public static Response listAllItems(List<String> ids) {
        return RestRequestBuilder.builder()
                .method("GET")
                .baseUri(BASE_URI)
                .path(API_PATH)
                .queryParams("id", ids)
                .headers(Map.of("Accept", "application/json"))
                .contentType(ContentType.JSON)
                .execute();
    }

    public static Response deleteItemById(String id) {
        String endpoint = API_PATH + "/" + id;
        return RestRequestBuilder.builder()
                .method("DELETE")
                .baseUri(BASE_URI)
                .path(endpoint)
                .headers(Map.of("Accept", "application/json"))
                .contentType(ContentType.JSON)
                .execute();
    }
}

