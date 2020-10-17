package com.maha.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

		try {
			watchesModelList = watchesRepository.findAll();

			// Circuit breaker - If any issue with reading from DB , read from json file
			if (watchesModelList == null) {
				watchesModelList = getListOfWatchesFromJson();
			}

		} catch (Exception e) {
			// Circuit breaker - IF DB is down get from json file
			watchesModelList = getListOfWatchesFromJson();

			// Log and but carry on processing
			log.error(e.getLocalizedMessage());

		}

		// IMPROVEMENT: A more performant way would have been to create a findByIds in
		// WatchesRepository, which will allow to only get the watches for the watch IDs
		// that have been entered, instead of all watches

		if (!watchesModelList.isEmpty() && watch_ids != null) {

			// Use Java 8 feature to get a map of distinct elements and their counts.
			Map<Long, Long> distinctWatchesAndCount = watch_ids.stream()
					.collect(Collectors.groupingBy(e -> e, Collectors.counting()));

			// Get a Map of watch ID and watches
			Map<Long, WatchesModel> watchesById = mapOfWatchesWithIdasKey(watchesModelList);

			priceOfAllWatches = PriceCalculator.getTotalPrice(priceOfAllWatches, distinctWatchesAndCount, watchesById);

		}

		return new Price(priceOfAllWatches);
	}

	/**
	 * Converts a list of watch objects into a MAP of Watch ID: Watch Object
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

	/**
	 * Get list of watches from a JSON file
	 * 
	 * @return
	 */
	private List<WatchesModel> getListOfWatchesFromJson() {

		List<WatchesModel> listWatches = null;
		;
		try {

			URL res = getClass().getClassLoader().getResource("watches.json");

			listWatches = new ObjectMapper().readValue(Paths.get(res.toURI()).toFile(),
					new TypeReference<List<WatchesModel>>() {
					});
		} catch (JsonParseException e) {
			log.error(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			log.error(e.getLocalizedMessage());
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		} catch (URISyntaxException e) {
			log.error(e.getLocalizedMessage());
		}

		return listWatches;
	}

	@GetMapping("/allWatches")
	public ResponseEntity<List<WatchesModel>> getAllWatches() {

		watchesModelList = watchesRepository.findAll();
		if (watchesModelList.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		return ResponseEntity.status(HttpStatus.CREATED).body(watchesModelList);
	}

}
