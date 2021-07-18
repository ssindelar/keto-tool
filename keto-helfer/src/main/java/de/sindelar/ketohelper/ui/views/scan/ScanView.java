package de.sindelar.ketohelper.ui.views.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wontlost.zxing.Constants;
import com.wontlost.zxing.ZXingVaadinReader;

import de.sindelar.ketohelper.ui.views.MainView;
import de.sindelar.ketohelper.ui.views.product.ProductView;

@Route(value = "scan", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Scan")
@CssImport("./views/scan-view.css")
@NpmPackage(value = "@zxing/library", version = "^0.18.4")
public class ScanView extends VerticalLayout {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScanView.class);

	private static final long serialVersionUID = -3861812863664981610L;

	private final ZXingVaadinReader zXingVaadin = new ZXingVaadinReader();

	private final TextField input = new TextField();
	private final Button send = new Button(new Icon(VaadinIcon.MAGIC));
	private final HorizontalLayout lowerLayout = new HorizontalLayout(input, send);

	public ScanView() {
		addClassName("scan-view");

		zXingVaadin.setFrom(Constants.From.camera);
		zXingVaadin.setId("video"); // id needs to be 'video' if From.camera.
		zXingVaadin.setWidthFull();
		zXingVaadin.setHeight("300px");
		zXingVaadin.addValueChangeListener(event -> {
			LOGGER.info("Barcodescan : {}", event.getValue());
			openProduct(event.getValue());
			zXingVaadin.clear();

		});
		addAndExpand(zXingVaadin);

		input.setLabel("Barcode selbst eingeben");
		input.setPlaceholder("Barcodeingabe");
		send.addClickListener(event -> openProduct(input.getValue()));
		lowerLayout.expand(input);
		lowerLayout.setWidthFull();
		lowerLayout.setAlignItems(Alignment.END);
		add(lowerLayout);
		setSizeFull();
	}

	private void openProduct(String barcode) {
		if (Strings.isNullOrEmpty(barcode)) {
			return;
		}
		if ("test".equalsIgnoreCase(barcode)) {
			barcode = "4000539669804";
		}
		input.clear();
		String finalBarcode = barcode;
		getUI().ifPresent(ui -> ui.navigate(ProductView.class, finalBarcode));
	}

}
