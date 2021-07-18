package de.sindelar.ketohelper.ui.components;

import com.google.common.base.Strings;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;

public class TextDiv extends Div {

	private static final long serialVersionUID = 3417199744739482779L;

	private final Text text = new Text("");

	public TextDiv() {
		this("");
	}

	public TextDiv(String text) {
		super();
		setText(Strings.nullToEmpty(text));
		add(this.text);
		addClassName("textdiv");
	}

	@Override
	public void setText(String text) {
		this.text.setText(text);
	}

	@Override
	public String getText() {
		return text.getText();
	}

}
