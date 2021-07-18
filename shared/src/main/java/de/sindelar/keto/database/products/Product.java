package de.sindelar.keto.database.products;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Product {

	@Id
	private String id;

	private String name;
	private String quantity;
	private String brands;
	private String stores;

	// @Column(name = "imageurl")
	// private String imageUrl;
	// @Column(name = "imageurl")
	// private String thumbnailUrl;

	@Column(name = "energy_100g")
	private Double energy;
	@Column(name = "fat_100g")
	private double fat;
	@Column(name = "carbohydrate_100g")
	private double carbohydrate;
	@Column(name = "protein_100g")
	private double protein;

	public double getKetonicQuotient() {
		return fat / (carbohydrate + protein);
	}
}
