package com.devapps.droids.utils;

import java.util.ArrayList;

import com.devapps.droids.models.HouseListingItem;

/**
 * Created by mahendraliya on 26/04/15.
 */
public class HouseDataSouce {
	
	/**
	 * Generic DataSource for displaying the data inside app
	 * @return Returns an array of houses
	 */
	public static ArrayList<HouseListingItem> getHouseDataSource() {
		HouseListingItem house1 = new HouseListingItem(1, "3BHK Villa", "Indiranagar", "3.0C", "");
		HouseListingItem house2 = new HouseListingItem(2, "3BHK Villa", "Indiranagar", "3.1C", "");
		HouseListingItem house3 = new HouseListingItem(3, "Pent House Villa", "Indiranagar", "3.2C", "");
		HouseListingItem house4 = new HouseListingItem(4, "4BHK Apartment", "Indiranagar", "3.3C", "");
		HouseListingItem house5 = new HouseListingItem(5, "3BHK Apartment", "Indiranagar", "3.4C", "");
		HouseListingItem house6 = new HouseListingItem(6, "3.5 BHK Apartment", "Indiranagar", "3.5C", "");
		HouseListingItem house7 = new HouseListingItem(7, "Pent House", "Indiranagar", "3.6C", "");
		HouseListingItem house8 = new HouseListingItem(8, "4BHK Villa", "Indiranagar", "3.7C", "");
		
		ArrayList<HouseListingItem> arrHouses = new ArrayList<>();
		arrHouses.add(house1);
		arrHouses.add(house2);
		arrHouses.add(house3);
		arrHouses.add(house4);
		arrHouses.add(house5);
		arrHouses.add(house6);
		arrHouses.add(house7);
		arrHouses.add(house8);
		
		return arrHouses;
	}
}
