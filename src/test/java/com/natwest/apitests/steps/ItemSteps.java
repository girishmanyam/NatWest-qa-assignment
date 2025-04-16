package com.natwest.apitests.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.natwest.apitests.apiService.ItemApiService;
import com.natwest.apitests.model.Item;
import com.natwest.apitests.model.ItemData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static com.natwest.apitests.apiAssertions.ApiAssertions.assertForHTTPResponseCode200;
import static com.natwest.apitests.apiAssertions.ApiAssertions.assertForItemDetails;
import static com.natwest.apitests.context.ScenarioContextHolder.threadScenarioContext;
import static org.testng.Assert.assertEquals;

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

    @Given("a new item is created with the following details:")
    public void aNewItemIsCreatedWithTheFollowingDetails(DataTable dataTable) throws JsonProcessingException {
        List<Map<String, String>> rows = dataTable.asMaps();
        Item item = buildItem(rows.get(0));
        Response response = ItemApiService.addItem(item);
        assertForHTTPResponseCode200(response);
        threadScenarioContext().put("itemId", response.jsonPath().getString("id"));
        threadScenarioContext().put("itemDetail", item);
    }

    @When("the user requests to get item details by itemId")
    public void theUserRequestsToGetItemDetailsByItemId() {
        String itemId = threadScenarioContext().getString("itemId");
        Response itemResponse = ItemApiService.getItemById(itemId);
        threadScenarioContext().put("getItem", itemResponse);
    }

    @And("the item id value should match")
    public void theItemIdValueShouldMatch() {
        String actualItemId = threadScenarioContext().getString("itemId");
        Response itemResponse = threadScenarioContext().getResponse("getItem");
        assertEquals(actualItemId, itemResponse.jsonPath().getString("id"));
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

    private static Item buildItem(Map<String, String> row) {
        ItemData itemData = ItemData.builder()
                .year(Integer.parseInt(row.get("year")))
                .price(Double.parseDouble(row.get("price")))
                .cpuModel(row.get("cpuModel"))
                .hardDiskSize(row.get("hardDiskSize"))
                .build();

        Item itemBuilder = Item.builder()
                .name(row.get("name"))
                .data(itemData)
                .build();
        return itemBuilder;
    }



}
