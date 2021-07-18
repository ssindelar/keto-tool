package de.sindelar.keto.openfoodfacts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OffNutriments {
	
	@JsonProperty("energy_100g")
	private Double energy;
	
	@JsonProperty("fat_100g")
	private Double fat;
	
	@JsonProperty("carbohydrates_100g")
	private Double carbohydrates;
	
	@JsonProperty("proteins_100g")
	private Double proteins;

	
	@JsonIgnore
	public boolean isRequiredFieldsSet() {
		return energy != null && fat != null && carbohydrates != null && proteins != null;
	}
	
	@JsonIgnore
	public double getKetonicValue() {
		return fat/(carbohydrates + proteins);
	}
	
	
	
	
	

}
