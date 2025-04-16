package com.natwest.apitests.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.natwest.apitests.apiService.ItemApiService;
import com.natwest.apitests.model.Item;
import com.natwest.apitests.model.ItemData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static com.natwest.apitests.apiAssertions.ApiAssertions.assertForHTTPResponseCode200;
import static com.natwest.apitests.apiAssertions.ApiAssertions.assertForItemDetails;
import static com.natwest.apitests.context.ScenarioContextHolder.threadScenarioContext;

public class ItemSteps {

    private Item item;

    @Given("a {string} item is created")
    public void aItemIsCreated(String name) {
        item = Item.builder()
                .name(name)
                .data(ItemData.builder().build())
                .build();
    }

    @And("is a {string} CPU model")
    public void isACpuModel(String cpuModel) {
        item = updateCpuModel(cpuModel, item);
    }

    @And("has a price of {string}")
    public void hasAPriceOf(String price) {
        item = updateItemPrice(price, item);

    }

    @When("the request to add the item is made")
    public void the_request_to_add_the_item_is_made() throws JsonProcessingException {
        Response response = ItemApiService.addItem(item);
        threadScenarioContext().put("addItem", response);
        threadScenarioContext().put("itemDetail", item);
    }

    @Then("a {string} service returns successful response code")
    public void aServiceReturnsResponseCode(String apiName) {
        Response response = threadScenarioContext().getResponse(apiName);
        assertForHTTPResponseCode200(response);
    }

    @And("the {string} response should contain a item details")
    public void theResponseShouldContainAItemDetails(String apiName) {
        Response itemResponse = threadScenarioContext().getResponse(apiName);
        Item itemDetail = (Item) threadScenarioContext().get("itemDetail");
        assertForItemDetails(itemResponse, itemDetail);
    }


    private Item updateCpuModel(String cpuModel, Item item) {
        return item.toBuilder()
                .data( item.getData().toBuilder()
                        .cpuModel(cpuModel)
                        .build())
                .build();
    }

    private Item updateItemPrice(String price, Item item) {
        return item.toBuilder()
                .data(item.getData().toBuilder()
                        .price(Double.parseDouble(price))
                        .build())
                .build();

    }

}
