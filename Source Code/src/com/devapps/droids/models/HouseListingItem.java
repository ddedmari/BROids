package com.devapps.droids.models;

/**
 * A POJO class for HouseListing Entity.
 * 
 * Created by mahendraliya on 26/04/15.
 */
public class HouseListingItem {
	private int id;
	private String name;
	private String location;
	private String price;
	private String imageURL;
	
	public HouseListingItem() {}
	
	/**
	 * 
	 * @param id The unique id for the house entity
	 * @param name The name as to be displayed / shown
	 * @param location The location of the house entity
	 * @param price The price of the house entity
	 * @param imageURL The image url for the initial picture to be shown in the house listing
	 */
	public HouseListingItem(int id, String name, String location, String price, String imageURL) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.price = price;
		this.imageURL = imageURL;
	}

	/**
	 * @return The unique id for the current house listing item.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique id for the model.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the name of the house listing as displayed in the UI.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the house listing as to be displayed in the UI.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The location of the listed house.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location of the house which is to be listed.
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return Returns the price of the listed house.
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * Sets the price of the house which is to be listed.
	 * @param price
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return The image url of the initial photo to be shown when the listed house is shown.
	 */
	public String getImageURL() {
		return imageURL;
	}

	/**
	 * Sets the image url for the photo of the house to be listed.
	 * @param imageURL
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
}
