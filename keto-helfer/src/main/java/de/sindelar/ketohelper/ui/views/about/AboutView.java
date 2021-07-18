package de.sindelar.ketohelper.ui.views.about;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.sindelar.ketohelper.ui.views.MainView;

@Route(value = "about", layout = MainView.class)
@PageTitle("About")
@CssImport("./views/about-view.css")
public class AboutView extends Div {

	public AboutView() {
		addClassName("about-view");
		add(new Text("Content placeholder"));
	}

}
