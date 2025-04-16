package com.natwest.apitests.model;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ItemData {
    private int year;
    private double price;
    private String cpuModel;
    private String hardDiskSize;
}