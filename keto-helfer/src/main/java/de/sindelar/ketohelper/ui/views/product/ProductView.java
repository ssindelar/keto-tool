package de.sindelar.ketohelper.ui.views.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.sindelar.keto.database.products.Product;
import de.sindelar.keto.database.products.ProductService;
import de.sindelar.ketohelper.ui.components.IconButton;
import de.sindelar.ketohelper.ui.components.TextDiv;
import de.sindelar.ketohelper.ui.views.MainView;
import de.sindelar.ketohelper.ui.views.scan.ScanView;

@Route(value = "product", layout = MainView.class)
@PageTitle("Produkt")
@CssImport("./views/product-view.css")
public class ProductView extends VerticalLayout implements HasUrlParameter<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductView.class);

	private static final long serialVersionUID = -3861812863664981610L;

	private final TextDiv title = new TextDiv();
	private final FormLayout nutritentsLayout = new FormLayout();
	private final FormLayout rightSide = new FormLayout();
	private final HorizontalLayout productInfoLayout = new HorizontalLayout(nutritentsLayout, rightSide);

	private final Button saveButton = new IconButton(VaadinIcon.FILE_ADD);
	private final Button calcButton = new IconButton(VaadinIcon.SCALE);
	private final HorizontalLayout actionLayout = new HorizontalLayout(saveButton, calcButton);

	private final transient ProductService productService;

	public ProductView(ProductService productService) {
		this.productService = productService;
		addClassName("product-view");

		title.addClassName("title");

		rightSide.setHeight(null);

		actionLayout.setWidthFull();
		actionLayout.setJustifyContentMode(JustifyContentMode.CENTER);

		setSpacing(false);
		addAndExpand(productInfoLayout);
		add(actionLayout);
		setSizeFull();
	}

	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		Product product = productService.getById(parameter);
		if (product == null) {
			LOGGER.warn("Nicht gefunden: {}", parameter);
			getUI().ifPresent(ui -> ui.navigate(ScanView.class));
			return;
		}

		title.setText(product.getName());

		nutritentsLayout.addFormItem(renderNutrientValue(product.getFat()), "Fett");
		nutritentsLayout.addFormItem(renderNutrientValue(product.getCarbohydrate()), "Kohlenhydrate");
		nutritentsLayout.addFormItem(renderNutrientValue(product.getProtein()), "Eiweiß");

		String ketoQuotientValue = String.format("%.1f:1", product.getKetonicQuotient());
		rightSide.addFormItem(new TextDiv(ketoQuotientValue), "Verhältnis");

	}

	private Component renderNutrientValue(double value) {
		String textValue = String.format("%.1fg", value);
		return new TextDiv(textValue);
	}

}
