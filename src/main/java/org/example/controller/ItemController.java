package org.example.controller;

import org.example.model.Item;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "ItemsTable";

    public ItemController(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        String id = UUID.randomUUID().toString();
        item.setId(id);

        Map<String, AttributeValue> itemMap = new HashMap<>();
        itemMap.put("id", AttributeValue.builder().s(id).build());
        itemMap.put("name", AttributeValue.builder().s(item.getName()).build());
        itemMap.put("description", AttributeValue.builder().s(item.getDescription()).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemMap)
                .build();

        dynamoDbClient.putItem(putItemRequest);
        return item;
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        Map<String, AttributeValue> returnedItem = dynamoDbClient.getItem(getItemRequest).item();

        if (returnedItem == null || returnedItem.isEmpty()) {
            return null;
        }

        return Item.builder()
                .id(returnedItem.get("id").s())
                .name(returnedItem.get("name").s())
                .description(returnedItem.get("description").s())
                .build();
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from AWS Lambda Spring Boot!";
    }
}
