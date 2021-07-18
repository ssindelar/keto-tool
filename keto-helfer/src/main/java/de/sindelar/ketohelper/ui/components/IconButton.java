package de.sindelar.ketohelper.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class IconButton extends Button {

	private static final long serialVersionUID = 574646447811946928L;

	public IconButton(VaadinIcon icon) {
		this(new Icon(icon));
	}

	public IconButton(Icon icon) {
		super(icon);
		addThemeVariants(ButtonVariant.LUMO_ICON);
		addClassName("iconbutton");
	}

}
