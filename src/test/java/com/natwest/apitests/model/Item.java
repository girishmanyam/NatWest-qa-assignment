package com.natwest.apitests.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder(toBuilder = true)
public class Item {
    private String name;
    private ItemData data;
}
