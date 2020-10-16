package com.maha.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maha.database.WatchesModel;
import com.maha.database.WatchesRepository;

import springfox.documentation.annotations.ApiIgnore;

@RestController
public class WatchesController {

	private static final Logger log = LoggerFactory.getLogger(WatchesController.class);

	@Autowired
	private List<WatchesModel> watchesModelList;

	@Autowired
	private WatchesRepository watchesRepository;

	@ApiIgnore
	@RequestMapping(value = "/")
	public void redirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui.html");
	}

	@GetMapping("/checkout")
	public Price checkout(@RequestParam(value = "watch_ids") List<Long> watch_ids) {

		Long priceOfAllWatches = 0L;

		// Get the list of all watches from the repository
		watchesModelList = watchesRepository.findAll();

		// IMPROVEMENT: A more performant way would have been to create a findByIds in
		// WatchesRepository, which will allow to only get the watches for the watch IDs
		// that have been entered, instead of all watches

		if (!watchesModelList.isEmpty() && watch_ids != null) {

			// Use Java 8 feature to get a map of distinct elements and their counts.
			Map<Long, Long> distinctWatchesAndCount = watch_ids.stream()
					.collect(Collectors.groupingBy(e -> e, Collectors.counting()));

			// Get a Map of watch ID and watches
			Map<Long, WatchesModel> watchesById = mapOfWatchesWithIdasKey(watchesModelList);

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

		}

		return new Price(priceOfAllWatches);
	}

	/**
	 * 
	 * @param models
	 * @return
	 */
	private Map<Long, WatchesModel> mapOfWatchesWithIdasKey(List<WatchesModel> models) {
		final Map<Long, WatchesModel> hashMap = new HashMap<>();
		for (final WatchesModel model : models) {
			hashMap.put(model.getWatch_id(), model);
		}
		return hashMap;
	}

	@GetMapping("/allWatches")
	public ResponseEntity<List<WatchesModel>> getAllWatches() {

		watchesModelList = watchesRepository.findAll();
		if (watchesModelList.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		return ResponseEntity.status(HttpStatus.CREATED).body(watchesModelList);
	}

}
