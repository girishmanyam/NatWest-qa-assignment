package com.natwest.apitests.steps;

import com.natwest.apitests.apiService.ItemApiService;
import com.natwest.apitests.model.Item;
import com.natwest.apitests.model.ItemData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.natwest.apitests.apiAssertions.ApiAssertions.assertForHTTPResponseCode200;
import static com.natwest.apitests.apiAssertions.ApiAssertions.assertForItemDetails;
import static com.natwest.apitests.context.ScenarioContextHolder.threadScenarioContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;

public class ItemSteps {

    private Item item;

    private static Item buildItem(Map<String, String> row) {
        ItemData itemData = ItemData.builder()
                .year(Integer.parseInt(row.get("year")))
                .price(Double.parseDouble(row.get("price")))
                .cpuModel(row.get("cpuModel"))
                .hardDiskSize(row.get("hardDiskSize"))
                .build();

        return Item.builder()
                .name(row.get("name"))
                .data(itemData)
                .build();
    }

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
    public void the_request_to_add_the_item_is_made() {
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
    public void aNewItemIsCreatedWithTheFollowingDetails(DataTable dataTable) {
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

    @Given("a multiple items are created with the following details:")
    public void aMultipleItemsAreCreatedWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Response> responses = new ArrayList<>();
        for (Map<String, String> row : rows) {
            Item item = buildItem(row);
            Response response = ItemApiService.addItem(item);
            assertForHTTPResponseCode200(response);
            items.add(item);
            responses.add(response);
        }
        threadScenarioContext().put("itemResponses", responses);
        threadScenarioContext().put("items", items);
    }

    @Given("the request to list all objects is made")
    public void theRequestToListAllObjectsIsMade() {
        List<Response> itemResponses = threadScenarioContext().getResponses("itemResponses");
        List<String> ids = itemResponses.stream()
                .map(response -> response.jsonPath().get("id").toString())
                .collect(Collectors.toList());
        Response listResponse = ItemApiService.listAllItems(ids);
        threadScenarioContext().put("allItemsResponse", listResponse);
    }

    @And("the response should contain more or equal to {int} items")
    public void theResponseShouldContainMoreOrEqualToItems(int noOfItems) {
        Response response = threadScenarioContext().getResponse("allItemsResponse");
        assertForHTTPResponseCode200(response);
        Assert.assertEquals(response.jsonPath().getList("$").size(), noOfItems);
    }

    @When("the user requests to delete item by itemId")
    public void theUserRequestsToDeleteItemByItemId() {
        String itemId = threadScenarioContext().getString("itemId");
        Response itemResponse = ItemApiService.deleteItemById(itemId);
        threadScenarioContext().put("deleteItem", itemResponse);
    }

    @Then("item is deleted successfully")
    public void itemIsDeletedSuccessfully() {
        Response deleteItemResponse = threadScenarioContext().getResponse("deleteItem");
        assertForHTTPResponseCode200(deleteItemResponse);
        String itemId = threadScenarioContext().getString("itemId");
        String actualMessage = deleteItemResponse.jsonPath().getString("message");
        String expectedMessage = String.format("Object with id = %s has been deleted.", itemId);
        assertThat(actualMessage, is(equalTo(expectedMessage)));
    }

    @Given("I have a non-existing item")
    public void iHaveANonExistingItem() {
        String invalidItemId = UUID.randomUUID().toString();
        threadScenarioContext().put("itemId", invalidItemId);
    }

    @Then("a {string} response should be {int} with error message")
    public void deleteItemResponseShouldBeWithMessage(String apiName, int statusCode) {
        Response response = threadScenarioContext().getResponse(apiName);
        String itemId = threadScenarioContext().getString("itemId");
        assertThat(response.getStatusCode(), is(equalTo(statusCode)));
        String actualMessage = response.jsonPath().getString("error");
        String expectedMessage = String.format("Object with id = %s doesn't exist.", itemId);
        assertThat(actualMessage, is(equalTo(expectedMessage)));
    }

    private Item updateCpuModel(String cpuModel, Item item) {
        return item.toBuilder()
                .data(item.getData().toBuilder()
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
