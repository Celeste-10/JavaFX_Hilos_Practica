package model;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ProcesoModel implements Runnable{
    private final String nombreProceso;
    private final int totalRegistros;
    private final int tiempoMilis;
    private final BiConsumer<Integer, Double> enProgreso;
    private final Runnable enFinalizado;
    private final Consumer<String> enEstadoCambiado;

    public ProcesoModel(String nombreProceso,int totalRegistros, int tiempoMilis, BiConsumer<Integer, Double> enProgreso, Runnable enFinalizado, Consumer<String> enEstadoCambiado) {
        this.nombreProceso = nombreProceso;
        this.totalRegistros = totalRegistros;
        this.tiempoMilis = tiempoMilis;
        this.enProgreso = enProgreso;
        this.enFinalizado = enFinalizado;
        this.enEstadoCambiado = enEstadoCambiado;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= totalRegistros; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                Thread.sleep(tiempoMilis);

                int registrosProcesados = i;
                double porcentaje = (double) i / totalRegistros;

                if (enProgreso != null) {
                    enProgreso.accept(registrosProcesados, porcentaje);
                }
            }

            if (enEstadoCambiado != null) {
                enEstadoCambiado.accept("Finalizado");
            }

            if (enFinalizado != null) {
                enFinalizado.run();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (enEstadoCambiado != null) {
                enEstadoCambiado.accept("Detenido");
            }
            if (enFinalizado != null) {
                enFinalizado.run();
            }
        }
    }

    public String getNombreProceso() {
        return nombreProceso;
    }
}
