package application;

import java.util.LinkedHashMap;

import javafx.scene.control.Button;

public class ButtonList {
	private LinkedHashMap<Integer, Button> buttonList = new LinkedHashMap<Integer, Button>();

	public LinkedHashMap<Integer, Button> getButtonList() {
		return buttonList;
	}
}
