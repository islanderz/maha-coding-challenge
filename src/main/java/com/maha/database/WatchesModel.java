package com.maha.database;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;

@Repository
@Document(collection = "Watches")
public class WatchesModel {

	private Long watch_id;
	private String watch_name;
	private Long unit_price;
	private Long discount_quantity;
	private Long discount_price; 
	
	public WatchesModel () {
		
	}
	 
	public WatchesModel(Long watch_id, String watch_name, Long unit_price, Long discount_quantity,
			Long discount_price) {
		super();
		this.watch_id = watch_id;
		this.watch_name = watch_name;
		this.unit_price = unit_price;
		this.discount_quantity = discount_quantity;
		this.discount_price = discount_price;
	}




	public Long getWatch_id() {
		return watch_id;
	}

	public void setWatch_id(Long watch_id) {
		this.watch_id = watch_id;
	}

	public String getWatch_name() {
		return watch_name;
	}

	public void setWatch_name(String watch_name) {
		this.watch_name = watch_name;
	}

	public Long getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(Long unit_price) {
		this.unit_price = unit_price;
	}

	public Long getDiscount_quantity() {
		return discount_quantity;
	}

	public void setDiscount_quantity(Long discount_quantity) {
		this.discount_quantity = discount_quantity;
	}

	public Long getDiscount_price() {
		return discount_price;
	}

	public void setDiscount_price(Long discount_price) {
		this.discount_price = discount_price;
	}
	

	

}
