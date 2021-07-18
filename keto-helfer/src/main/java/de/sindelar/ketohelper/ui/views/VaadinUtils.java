package de.sindelar.ketohelper.ui.views;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;

public class VaadinUtils {

	public static boolean isMobile() {
		VaadinSession session = VaadinSession.getCurrent();
		WebBrowser browser = session.getBrowser();
		return browser.isAndroid() || browser.isIPhone() || browser.isWindowsPhone();
	}

}
