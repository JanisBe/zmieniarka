module com.janisbe.zmieniarka {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.desktop;

	opens com.janisbe.zmieniarka to javafx.fxml;
	exports com.janisbe.zmieniarka;
}