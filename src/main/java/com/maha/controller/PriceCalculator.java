package com.maha.controller;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maha.database.WatchesModel;

public class PriceCalculator {

	private static final Logger log = LoggerFactory.getLogger(PriceCalculator.class);

	/**
	 * Get total price from list of watches and
	 */
	public static Long getTotalPrice(Long priceOfAllWatches, Map<Long, Long> distinctWatchesAndCount,
			Map<Long, WatchesModel> watchesById) {
		// Iterate through the list of distinct watches
		Iterator<Map.Entry<Long, Long>> iterator = distinctWatchesAndCount.entrySet().iterator();
		while (iterator.hasNext()) {

			Map.Entry<Long, Long> entryCount = iterator.next();

			if (entryCount != null && entryCount.getValue() >= 1) {

				// Check if the watch has a discount
				Long dicountQuantiy = watchesById.get(entryCount.getKey()).getDiscount_quantity();
				Long unitPrice = watchesById.get(entryCount.getKey()).getUnit_price();
				Long discountPrice = watchesById.get(entryCount.getKey()).getDiscount_price();
				Long numberOfWatchesBought = entryCount.getValue();

				// If there is a discount possible
				if (dicountQuantiy != null) {

					// Check if the amount bought is greater than the discount quantity
					if (numberOfWatchesBought >= dicountQuantiy) {
						// Get the whole integer to calculate number of discounts --> use int quotient =
						// dividend / divisor;
						long numberOfDiscoutedBulk = entryCount.getValue() / dicountQuantiy;

						// Get the total price of watches bought with discount
						priceOfAllWatches = priceOfAllWatches + numberOfDiscoutedBulk * discountPrice;

						// Add the remainder , if no remainder do nothing --> user int remainder =
						// dividend % divisor;
						long remainder = entryCount.getValue() % dicountQuantiy;
						if (remainder > 0) {
							// Assumed that if additional items is bought beyond bulk discounted, this is
							// priced normall
							priceOfAllWatches = priceOfAllWatches + remainder * unitPrice;
						}

					} else {

						// Get the total price of watches bought without discount
						priceOfAllWatches = priceOfAllWatches + numberOfWatchesBought * unitPrice;
					}
				} else {
					// If there is no discount on this watch
					priceOfAllWatches = priceOfAllWatches + numberOfWatchesBought * unitPrice;
				}
			}

			// Prints the watches bought and how much of each
			log.debug(entryCount.getKey() + ":" + entryCount.getValue());
		}
		return priceOfAllWatches;
	}

}
