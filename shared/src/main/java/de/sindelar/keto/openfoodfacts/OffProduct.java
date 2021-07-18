package de.sindelar.keto.openfoodfacts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

import lombok.Data;

@Data
public class OffProduct {

	@JsonProperty("_id")
	private String id;
	@JsonProperty("product_name")
	private String name;

	@JsonProperty("product_name_de")
	private String nameDe;

	@JsonProperty("quantity")
	private String quantity;

	@JsonProperty("brands")
	private String brands;

	@JsonProperty("stores")
	private String stores;

	@JsonProperty("countries_hierarchy")
	private List<String> countries;

	@JsonProperty("image_thumb_url")
	private String thumbnailUrl;

	@JsonProperty("image_url")
	private String imageUrl;

	// Nährwerte
	@JsonProperty("nutriments")
	private OffNutriments nutriments;

	@JsonIgnore
	public boolean isRequiredFieldsSet() {
		return nutriments != null && nutriments.isRequiredFieldsSet() && !Strings.isNullOrEmpty(name);
	}

	@JsonIgnore
	public String getName() {
		if (Strings.isNullOrEmpty(nameDe)) {
			return name;
		}
		return nameDe;
	}

}
