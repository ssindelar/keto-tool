package de.sindelar.keto.openfoodfacts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OffReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(OffReader.class);

	public static final ObjectMapper OBJECTMAPPER = new ObjectMapper();
	private static final Pattern CLEAN_NUMBER = Pattern
			.compile("\\{\"\\$number(?:Int|Long|Double)\":(\"-?\\d+(?:\\.\\d+)?(?:E-?\\d+)?\")\\}");

	static {
		OBJECTMAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static Stream<OffProduct> readMongoExport(Path path, Predicate<String> lineFilter,
			UnaryOperator<String> lineCallback) throws IOException {
		return Files.lines(path)
				.parallel()
				.map(lineCallback)
				.filter(lineFilter)
				.map(OffReader::cleanRow)
				.map(OffReader::mapProduct)
				.filter(Objects::nonNull);
	}

	public static Stream<OffProduct> readExtracted(Path path, UnaryOperator<String> lineCallback) throws IOException {
		return Files.lines(path).map(lineCallback).map(OffReader::mapProduct).filter(Objects::nonNull);
	}

	private static String cleanRow(String row) {
		String cleaned = CLEAN_NUMBER.matcher(row).replaceAll("$1");
		LOGGER.info(cleaned);
		return cleaned;
	}

	private static OffProduct mapProduct(String row) {
		try {
			return OBJECTMAPPER.readValue(row, OffProduct.class);
		} catch (JsonProcessingException e) {
			LOGGER.error("Fehler beim Parsen von: {}", row, e);
			return null;
		}
	}

}
