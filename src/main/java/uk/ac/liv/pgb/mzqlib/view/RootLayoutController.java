package uk.ac.liv.pgb.mzqlib.view;

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;

import uk.ac.liv.pgb.mzqlib.MainApp;

/**
 * FXML Controller class
 *
 * @author Da Qi
 */
public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    // private Stage newStage;
    private File     file;    // mzq file
    @FXML
    private Menu     statistics;
    @FXML
    private MenuItem closeFile;

    /**
     * Disable specified menus.
     */
    public final void disbbleMenus() {
        statistics.setDisable(true);
        closeFile.setDisable(true);
    }

    /**
     * Enable specified menus.
     */
    public final void enableMenus() {
        statistics.setDisable(false);
        closeFile.setDisable(false);
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleClose() {
        mainApp.closeMzqInfo();
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleCurve() throws JAXBException {
        mainApp.showCurve();
    }

    /**
     * Closes the application.
     */
    @FXML
    @SuppressWarnings("unused")
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleHeatMapPdf() {
        mainApp.showHeatMapPdfWindow();
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleHeatMapinR() {
        mainApp.showHeatMapinR();
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleInstallRequiredPackages() {
        mainApp.installRequiredPackages();
    }

//  public Stage getStage() {
//      return newStage;
//  }
    @FXML
    @SuppressWarnings("unused")
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        if (mainApp.getLastFilePath() != null && mainApp.getLastFilePath().exists()) {
            fileChooser.setInitialDirectory(mainApp.getLastFilePath());
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        // Set extension filter
        ExtensionFilter extFilter = new ExtensionFilter("mzQuantML file (*.mzq)", "*.mzq");

        fileChooser.getExtensionFilters().add(extFilter);

        // Set the title for file dialog
        fileChooser.setTitle("Select an mzQuantML file");

        // Show open file dialog
        file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadMzqFile(file);
            mainApp.setLastFilePath(file);
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void handlePCA() {
        mainApp.showPCAPlot();
    }

    @FXML
    @SuppressWarnings("unused")
    private void showAbout() {
        mainApp.showAbout();
    }

    @FXML
    @SuppressWarnings("unused")
    private void showGui() {
        mainApp.showGui();
    }

    @FXML
    @SuppressWarnings("unused")
    private void showProgenesisConverterWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(RootLayoutController.class.getClassLoader().getResource("ProgenesisConverter.fxml"));

            AnchorPane progenConvt = (AnchorPane) loader.load();

            // ProgenesisConverterController controller = loader.getController();
            Scene scene     = new Scene(progenConvt);
            Stage progStage = new Stage();

            progStage.initModality(Modality.APPLICATION_MODAL);
            progStage.setTitle("Progenesis to MzQuantML Converter");
            progStage.setScene(scene);
            progStage.show();
        } catch (IOException ex) {
            Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Set main app.
     *
     * @param mainApp main app.
     */
    public final void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
//~ Formatted by Jindent --- http://www.jindent.com
