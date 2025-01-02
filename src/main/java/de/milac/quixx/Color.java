package de.milac.quixx;

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Attribute.*;

public enum Color {
	WHITE("W", WHITE_TEXT()),
	RED("R", RED_TEXT()),
	YELLOW("Y", BRIGHT_YELLOW_TEXT()),
	GREEN("G", BRIGHT_GREEN_TEXT()),
	BLUE("B", BLUE_TEXT());

	private final String id;
	private final AnsiFormat format;

	Color(String id, Attribute textColor) {
		this.id = id;
		this.format = new AnsiFormat(textColor);
	}

	public static Color[] allColored() {
		return new Color[] {RED, YELLOW, GREEN, BLUE};
	}

	public String getId() {
		return id;
	}

	public String dye(String text) {
		return format.format(text);
	}

	public String dyedName() {
		return format.format(name());
	}
}
