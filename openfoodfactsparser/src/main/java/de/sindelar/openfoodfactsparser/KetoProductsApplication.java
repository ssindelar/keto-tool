package de.sindelar.openfoodfactsparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.sindelar.keto.openfoodfacts.OffNutriments;
import de.sindelar.keto.openfoodfacts.OffProduct;

public class KetoProductsApplication  {

	private static final Logger LOGGER = LoggerFactory.getLogger(KetoProductsApplication.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) throws IOException {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		LOGGER.info("Start");
		List<OffProduct> products = Files.lines(Paths.get("products-de.json")).parallel().map(KetoProductsApplication::mapProduct)
				.sorted(Comparator.comparing(OffProduct::getStores, Comparator.nullsLast(Comparator.naturalOrder()))
						.thenComparing(Comparator.comparing(OffProduct::getName))
						)
				.collect(Collectors.toList());

		LOGGER.info("Produkte gefunden:{}", products.size());
		checkKeto(products, 1.5);

		LOGGER.info("Fertig");
	}


	private static OffProduct mapProduct(String row) {
		try {
			OffProduct product = objectMapper.readValue(row, OffProduct.class);
			return product;
		} catch (JsonProcessingException e) {
			LOGGER.error("Fehler beim Parsen von: {}", row, e);
			return null;
		}
	}

	private static void checkKeto(List<OffProduct> products, double ketoValue) {
		List<OffProduct> ketoProducts = products.stream().filter(it -> {
			OffNutriments nutriments = it.getNutriments();
			double ketonicValue = nutriments.getKetonicValue();
			return ketonicValue >= ketoValue-0.1 && ketonicValue <= ketoValue+0.1;
		}).collect(Collectors.toList());

		LOGGER.info("{} Produkte:", ketoProducts.size());
		ketoProducts.forEach(it -> {
			OffNutriments nutriments = it.getNutriments();
			double ketonicValue = nutriments.getKetonicValue();
			LOGGER.info("{} von {} \t Laden: {} \t Wert: {} \t N�hrwerte: {}g Fett, {}g Kohlenhydrate, {}g Eiwei�", it.getName(), it.getBrands(), it.getStores(),  String.format("%.02f", ketonicValue), nutriments.getFat(), nutriments.getCarbohydrates(), nutriments.getProteins());
		});
	}

}
