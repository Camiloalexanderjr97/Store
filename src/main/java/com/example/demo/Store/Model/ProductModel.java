package com.example.demo.Store.Model;

import com.example.demo.Store.Entity.Product;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProductModel {
	  private int id;
	  private  String name;
	  private  String description;
	  private Double price;
	  private Product category;
	  private String imageUrl;	  
	  private Boolean stock;
	  private int amount;



}
