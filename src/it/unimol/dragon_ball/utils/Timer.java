package it.unimol.dragon_ball.utils;


/**
 * Thread che consente di eseguire un {@link Runnable} al termine di un'intervallo di tempo, definito nel momento
 * dell'inizializzazione.
 * Il thread <code>Timer</code> viene avviato automaticamente in seguito alla creazione dell'oggetto.
 */
public class Timer extends Thread {

    private double secondsDelay;
    private Runnable action;

    /**
     * Genera un nuovo <code>Timer</code> e inizializza il tempo di attesa e il {@link Runnable} da eseguire.
     * Avvia automaticamente l'esecuzione del thread appena istanziato.
     * @param secondsDelay tempo di attesa espresso in secondi.
     * @param action evento che verrà eseguito al termine dell'attesa.
     */
    public Timer(double secondsDelay, Runnable action) {
        super();
        this.secondsDelay = secondsDelay;
        this.action = action;

        this.start();
    }

    /**
     * Blocca l'avanzamento delle operazioni per un periodo di tempo pari alla durata del Timer.
     * Al termine dell'attesa, viene eseguito il {@link Runnable} che gli è stato assegnato.
     */
    @Override
    public void run() {
        try {
            Thread.sleep((long)(secondsDelay * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        action.run();
    }

}
