package com.demo.pubsub.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CoffeeDto implements Serializable {
    private String name;
    private int price;
}
