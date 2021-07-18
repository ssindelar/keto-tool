package de.sindelar.ketohelper.ui.views;

import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

import de.sindelar.ketohelper.ui.views.about.AboutView;
import de.sindelar.ketohelper.ui.views.myproducts.MyProductsView;
import de.sindelar.ketohelper.ui.views.scan.ScanView;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "Keto Helfer", shortName = "Keto Helfer", enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
@CssImport("./views/main-view.css")
public class MainView extends AppLayout {

	private static final long serialVersionUID = -7272054941504189236L;
	private final Tabs menu;

	public MainView() {
		HorizontalLayout headerBar = createHeader();
		boolean mobile = VaadinUtils.isMobile();
		menu = createMenuTabs();
		VerticalLayout tabBar = createTopBar(menu);
		if (mobile) {
			addToNavbar(headerBar);
			addToNavbar(true, tabBar);
		} else {
			VerticalLayout verticalLayout = new VerticalLayout(headerBar, tabBar);
			verticalLayout.setSpacing(false);
			verticalLayout.setPadding(false);
			addToNavbar(verticalLayout);
		}
	}

	private VerticalLayout createTopBar(Tabs menu) {
		VerticalLayout layout = new VerticalLayout();
		layout.getThemeList().add("dark");
		layout.setWidthFull();
		layout.setSpacing(false);
		layout.setPadding(false);
		layout.setAlignItems(FlexComponent.Alignment.CENTER);
		layout.add(menu);
		return layout;
	}

	private HorizontalLayout createHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.getThemeList().add("dark");
		header.setPadding(false);
		header.setSpacing(false);
		header.setWidthFull();
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		header.setId("header");
		Image logo = new Image("images/logo.png", "Keto Helfer logo");
		logo.setId("logo");
		header.add(logo);
		Avatar avatar = new Avatar();
		avatar.setId("avatar");
		header.add(new H1("Keto Helfer"));
		header.add(avatar);
		return header;
	}

	private static Tabs createMenuTabs() {
		final Tabs tabs = new Tabs();
		tabs.getStyle().set("max-width", "100%");
		tabs.add(getAvailableTabs());
		return tabs;
	}

	private static Tab[] getAvailableTabs() {
		return new Tab[] { createTab("Scan", ScanView.class), createTab("Meine Produkte", MyProductsView.class),
				createTab("About", AboutView.class) };
	}

	private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
		final Tab tab = new Tab();
		tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
		tab.add(new RouterLink(text, navigationTarget));
		ComponentUtil.setData(tab, Class.class, navigationTarget);
		return tab;
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();
		getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
	}

	private Optional<Tab> getTabForComponent(Component component) {
		return menu.getChildren()
				.filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
				.findFirst()
				.map(Tab.class::cast);
	}
}
