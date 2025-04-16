package com.natwest.apitests.apiAssertions;

import com.natwest.apitests.model.Item;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;

import static org.testng.AssertJUnit.assertEquals;

public class ApiAssertions {
    public static void assertForHTTPResponseCode200(Response getResponse) {
        assertEquals("mismatch in Http Status code", HttpStatus.SC_OK, getResponse.getStatusCode());
    }

    public static void assertForItemDetails(Response getResponse, Item item) {
        SoftAssertions softly = new SoftAssertions();
        JsonPath jsonPath = getResponse.jsonPath();
        softly.assertThat(jsonPath.getString("name")).isEqualTo(item.getName());
        softly.assertThat(jsonPath.getString("data.cpuModel")).isEqualTo(item.getData().getCpuModel());
        softly.assertThat(Double.valueOf(jsonPath.getString("data.price"))).isEqualTo(item.getData().getPrice());
        softly.assertAll();
    }

}
