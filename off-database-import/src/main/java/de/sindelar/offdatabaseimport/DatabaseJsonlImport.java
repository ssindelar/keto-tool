package de.sindelar.offdatabaseimport;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.sindelar.keto.database.products.Product;
import de.sindelar.keto.database.products.ProductService;
import de.sindelar.keto.openfoodfacts.OffNutriments;
import de.sindelar.keto.openfoodfacts.OffProduct;
import de.sindelar.keto.openfoodfacts.OffReader;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseJsonlImport {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseJsonlImport.class);
	private static final AtomicLong countRow = new AtomicLong(0);

	private final ProductService productService;

	public void start() throws IOException {
		LOGGER.info("Starte Import");

		try (Stream<OffProduct> products = OffReader.readExtracted(Paths.get("../openfoodfacts-products.jsonl"),
				this::countRow)) {
			products.filter(OffProduct::isRequiredFieldsSet).map(this::map).forEach(productService::create);

			LOGGER.info("Fertig");
		}
	}

	private String countRow(String row) {
		long count = countRow.incrementAndGet();
		if (count % 1000 == 0) {
			LOGGER.info("Progress: {}", count);
		}
		return row;
	}

	private Product map(OffProduct offProduct) {
		OffNutriments nutriments = offProduct.getNutriments();
		return Product.builder()
				.id(offProduct.getId())
				.name(offProduct.getName())
				.quantity(offProduct.getQuantity())
				.brands(offProduct.getBrands())
				.stores(offProduct.getStores())
				// .imageUrl(offProduct.getImageUrl())
				// .thumbnailUrl(offProduct.getThumbnailUrl())
				.energy(nutriments.getEnergy())
				.fat(nutriments.getFat())
				.carbohydrate(nutriments.getCarbohydrates())
				.protein(nutriments.getProteins())
				.build();
	}
}
