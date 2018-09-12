package client;

import javafx.scene.control.Alert;

public class GuiUtil {
	
	public static void ShowInformation(String info) {
		new Alert(Alert.AlertType.INFORMATION, info).showAndWait();
	}
	
	
	public static void ShowError(String msg) {
		new Alert(Alert.AlertType.ERROR, msg).showAndWait();
	}

}
