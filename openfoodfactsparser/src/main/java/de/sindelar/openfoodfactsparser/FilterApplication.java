package de.sindelar.openfoodfactsparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.sindelar.keto.openfoodfacts.OffProduct;
import de.sindelar.keto.openfoodfacts.OffReader;

public class FilterApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterApplication.class);
	private static final AtomicLong countRow = new AtomicLong(0);
	private static final AtomicLong countProduct = new AtomicLong(0);

	private static final Predicate<String> ALL_PRODUCTS = it -> true;
	private static final Predicate<String> GERMAN_PRODUCTS = it -> it.contains("en:germany");

	public static void main(String[] args) throws IOException {

		LOGGER.info("Start");

		try (Stream<OffProduct> products = OffReader.readMongoExport(Paths.get("../products.json"), ALL_PRODUCTS,
				FilterApplication::countRow)) {
			List<OffProduct> filteredProducts = products.filter(OffProduct::isRequiredFieldsSet)
					.peek(FilterApplication::peekProduct)
					.collect(Collectors.toList());

			LOGGER.info("Produkte gefunden:{}", filteredProducts.size());

			List<String> filteredLines = filteredProducts.parallelStream()
					.map(FilterApplication::serializeProduct)
					.collect(Collectors.toList());
			Files.write(Paths.get("../products-all.json"), filteredLines, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
			LOGGER.info("Fertig");
		}
	}

	private static String countRow(String it) {
		countRow.incrementAndGet();
		return it;
	}

	private static void peekProduct(OffProduct product) {
		long count = countProduct.incrementAndGet();
		if (count % 100 == 0) {
			LOGGER.info("Progress: {}|{}", count, countRow.get());
		}
	}

	private static String serializeProduct(OffProduct product) {
		try {
			return OffReader.OBJECTMAPPER.writeValueAsString(product);
		} catch (JsonProcessingException e) {
			LOGGER.error("Fehler beim serialisieren von: {}", product, e);
			return null;
		}
	}

}
