package com.janisbe.zmieniarka;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ZmieniarkaZnakow extends Application {

	private File selectedFile; // To store the selected file

	public static void main(String[] args) {
		launch(args);
	}

	private void replaceCharacterInFile(File inputFile, Label statusLabel) {
		// Create the new name for the backup file
		File backupFile = new File(inputFile.getParent(), inputFile.getName() + ".old");
		if (inputFile.renameTo(backupFile)) {
			try (BufferedReader reader = new BufferedReader(new FileReader(backupFile));
				OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(inputFile), StandardCharsets.UTF_8)) {

				String line;
				// Read each line from the input file
				while ((line = reader.readLine()) != null) {
					// Replace all occurrences of 'H' with 'R'
					String modifiedLine = line.replace('H', 'R');
					// Write the modified line to the output file
					writer.write(modifiedLine);
					writer.write(System.lineSeparator()); // To
				}
				statusLabel.setText("Zamieniono, output:\n" + inputFile.getAbsolutePath());
			} catch (IOException ignored) {
				statusLabel.setText("Nie można zapisywać do pliku.");
			}
		} else {
			// If renaming fails, notify the user
			statusLabel.setText("Nie można zmienić nazwy pliku.");
		}
	}

	@Override
	public void start(Stage primaryStage) {
		// Set up the stage
		primaryStage.setTitle("Zamieniarka znaków");
		primaryStage.setResizable(false);
		// Label to show file status
		Label statusLabel = new Label("Nie wybrano plików");
		statusLabel.setTextFill(Color.BLUE);
		statusLabel.setUnderline(true);
		statusLabel.setVisible(false);
		// Create buttons
		Button browseButton = new Button("Wybierz plik tekstowy");
		Button replaceButton = new Button("Zamień znaki");

		// Disable replace button initially until a file is selected
		replaceButton.setDisable(true);

		// FileChooser to select a file
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
			.add(new FileChooser.ExtensionFilter("Napisy", "*.txt", "*.sub", "*.srt"));

		// Browse button logic
		browseButton.setOnAction(e -> {
			// Open file chooser and allow user to select a file
			selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				statusLabel.setText("Wybrany plik: \n" + selectedFile.getAbsolutePath());
				statusLabel.setVisible(true);
				replaceButton.setDisable(false);
			} else {
				statusLabel.setText("Nie wybrano pliku");
			}
		});

		// Replace button logic
		replaceButton.setOnAction(e -> {
			if (selectedFile != null) {
				// Perform the replacement operation
				replaceCharacterInFile(selectedFile, statusLabel);
			}
		});
		statusLabel.setOnMouseClicked((MouseEvent e) -> {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().open(selectedFile); // Open the selected file
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		// Set up the layout
		VBox layout = new VBox(10);
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(browseButton, replaceButton, statusLabel);

		// Create a scene and set it on the stage
		Scene scene = new Scene(layout, 600, 200);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}