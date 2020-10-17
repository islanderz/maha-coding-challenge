package com.maha.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maha.database.WatchesModel;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PriceCalculatorTest {

	@Test
	void savedUserHasRegistrationDate()
			throws JsonParseException, JsonMappingException, IOException, URISyntaxException {

		URL res = getClass().getClassLoader().getResource("watches.json");

		List listWatches = new ObjectMapper().readValue(Paths.get(res.toURI()).toFile(),
				new TypeReference<List<WatchesModel>>() {
				});

		Long priceOfAllWatches = 0L;

		Map<Long, Long> distinctWatchesAndCount = new HashMap<Long, Long>();
		distinctWatchesAndCount.put(001L, 3L);
		distinctWatchesAndCount.put(002L, 3L);
		distinctWatchesAndCount.put(003L, 1L);

		Map<Long, WatchesModel> watchesById = new HashMap<Long, WatchesModel>();

		watchesById.put(001L, (WatchesModel) listWatches.get(0));
		watchesById.put(002L, (WatchesModel) listWatches.get(1));
		watchesById.put(003L, (WatchesModel) listWatches.get(2));

		assertEquals(PriceCalculator.getTotalPrice(priceOfAllWatches, distinctWatchesAndCount, watchesById), 450L);
	}
}
