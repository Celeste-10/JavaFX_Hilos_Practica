package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import model.ProcesoModel;

public class ProcesadorController {

    @FXML
    private Label lblEstado;
    @FXML
    private Label lblPorcentaje;
    @FXML
    private ProgressBar barraProgreso;

    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnDetener;
    @FXML
    private Button btnReiniciar;

    private Thread hiloProceso;

    @FXML
    private void iniciarProceso() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            return;
        }

        barraProgreso.setProgress(0);
        lblPorcentaje.setText("0%");
        lblEstado.setText("Procesando registros...");

        btnIniciar.setDisable(true);
        btnDetener.setDisable(false);
        btnReiniciar.setDisable(true);

        ProcesoModel tarea = new ProcesoModel(
                "Clientes",
                100,
                100,
                (registros, porcentaje) -> Platform.runLater(() -> {
                    barraProgreso.setProgress(porcentaje);
                    lblPorcentaje.setText(Math.round(porcentaje * 100) + "%");
                }),
                () -> Platform.runLater(this::habilitarBotones),
                estado -> Platform.runLater(() -> lblEstado.setText("Estado: " + estado))
        );

        hiloProceso = new Thread(tarea, "Hilo-Procesamiento-Clientes");
        hiloProceso.setDaemon(true);
        hiloProceso.start();
    }

    @FXML
    private void detenerProceso() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            btnDetener.setDisable(true);
            lblEstado.setText("Solicitando detención...");
            hiloProceso.interrupt();
        }
    }

    @FXML
    private void reiniciarProceso() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            return;
        }

        barraProgreso.setProgress(0);
        lblPorcentaje.setText("0%");
        lblEstado.setText("Esperando inicio");

        habilitarBotones();
    }

    private void habilitarBotones() {
        btnIniciar.setDisable(false);
        btnDetener.setDisable(true);
        btnReiniciar.setDisable(false);
    }

    public void detenerAlCerrar() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            hiloProceso.interrupt();
        }
    }
}
