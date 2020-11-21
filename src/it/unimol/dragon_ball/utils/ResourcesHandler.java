package it.unimol.dragon_ball.utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static it.unimol.dragon_ball.utils.Resources.*;


/**
 * Singleton che si occupa di gestire le risorse video e audio del programma.
 * Presenta le seguenti funzionalità:
 * - ottenimento dell'immagine tramite filepath;
 * - avvio di un contenuto acustico;
 * - chiusura di un contenuto acustico;
 *
 * @author Alessandro
 */
public class ResourcesHandler {

    private static ResourcesHandler instance = new ResourcesHandler();

    private ResourcesHandler() {

    }

    public static ResourcesHandler getInstance() {
        return instance;
    }

    /**
     * Restituisce l'immagine passata come input tramite filepath. Lancia un ResourceException qualora il filepath
     * non è valido.
     *
     * @param path filepath dell'immagine desiderata
     * @return un'istanza di BufferedImage
     * @throws ResourceException eccezione lanciata quando il pathname preso come parametro sia nullo.
     */
    public BufferedImage getImage(String path){
        return Resources.getImage(path);
    }


    /**
     * Avvia la riproduzione musicale della risorsa presa come parametro. Lancia un ResourceException quando il filepath
     * inserito non è valido.
     *
     * @param path      filepath della risorsa musicale;
     * @param loopCount intero che rappresenta il numero di volte in cui deve essere riprodotta integralmente la risorsa.
     *                  Si inserisca Clip.LOOP_CONTINUOUSLY per una riproduzione in loop.
     * @return un'istanza di Clip che rappresenta la risorsa musicale riprodotta.
     * @throws ResourceException eccezione lanciata quando il filepath preso come parametro è nullo.
     */
    public Clip playMusic(String path, int loopCount) throws ResourceException {
        if (path == null)
            throw new ResourceException("Path not inserted");

        Clip clip = null;

        try {
            Sound sound = Resources.getSound(path);
            clip = sound.play(loopCount);

        } catch (Exception ignored) {

        }

        return clip;
    }


    /**
     * Avvia la riproduzione musicale della risorsa presa come parametro. Lancia un ResourceException quando il filepath
     * inserito non è valido. La riproduzione si stopperà quando si conclude l'animazione
     *
     * @param path              filepath della risorsa musicale;
     * @param loopCount         intero che rappresenta il numero di volte in cui deve essere riprodotta integralmente la risorsa.
     *                          Si inserisca Clip.LOOP_CONTINUOUSLY per una riproduzione in loop.
     * @param isAnimationActive istanza di AtomicBoolean che rappresenta lo stato dell'animazione.
     *                          Se l'animazione non è attiva, la riproduzione musicale termina.
     * @return un'istanza di Clip che rappresenta la risorsa musicale riprodotta.
     * @throws ResourceException eccezione lanciata quando il filepath preso come parametro è nullo.
     */
    public Clip playAnimationMusic(String path, int loopCount, AtomicBoolean isAnimationActive) throws ResourceException {
        if (path == null)
            throw new ResourceException("Path not inserted");

        Clip clip = null;
        final boolean[] isSoundActive = new boolean[1];

        try {
            Sound sound = Resources.getSound(path);
            clip = sound.play(loopCount);
            isSoundActive[0] = true;

            Clip finalClip = clip;
            Thread thread = new Thread(() -> {

                while (isSoundActive[0]) {
                    if (!isAnimationActive.get()) {
                        try {
                            this.stopMusic(finalClip);
                        } catch (SoundException e) {
                            e.printStackTrace();
                        }
                        isSoundActive[0] = false;
                    }
                }
            });
            thread.start();

        } catch (Exception ignored) {

        }

        return clip;
    }

    /**
     * Ferma la riproduzione della risorsa musicale già attiva, presa come parametro.
     * Lancia un SoundException quando la Clip presa in input non è valida o è inattiva.
     *
     * @param clip Clip della risorsa musicale di cui si desidera interrompere la riproduzione.
     * @throws SoundException quando il filepath della Clip vale null oppure se la risorsa musicale non è in riproduzione.
     */
    public void stopMusic(Clip clip) throws SoundException {
        if (clip == null)
            throw new SoundException("Clip not inserted");

        if (clip.isOpen()) {
            clip.close();
            clip.stop();
        } else
            throw new SoundException("Clip not active");
    }

}


/**
 * Resources è un gestore delle risorse interne di un'applicazione. La classe
 * fornisce diversi metodi per leggere immagini, suoni o testo
 *
 * @author Oneiros
 */
class Resources {

    //La classe di riferimento dalla quale leggere le risorse
    static Class source = Resources.class;

    /**
     * Costruttore privato (Resources è una classe statica)
     */
    private Resources() {
    }

    /**
     * Imposta la classe di riferimento dalla quale leggere le risorse. La
     * classe di riferimento di default è Resources stessa.
     *
     * @param source la classe di riferimento dalla quale leggere le risorse
     */
    public static void setSourceClass(Class source) {
        Resources.source = source;
    }

    /**
     * Restituisce un'immagine presente nelle risorse dell'applicazione
     *
     * @param path Il path relativo dell'immagine (ad esempio:
     *             "/path/myImage.png")
     * @return L'immagine corrispondente al path
     */
    public static BufferedImage getImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getResource(path));
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
        return image;
    }


    /**
     * Restituisce un suono WAVE presente nelle risorse dell'applicazione
     *
     * @param path Il path relativo del suono (ad esempio: "/path/mySound.wav")
     * @return Il suono corrispondente al path
     */
    public static Sound getSound(String path) {
        Sound sound = null;
        try {
            //System.out.println("\n\n\n\n" + getResource(path).toURI() + "\n\n\n\n");
            //File file = new File(getResource(path).toURI());
            sound = new Sound(getResource(path));
        } catch (NullPointerException | SoundException | IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
        return sound;
    }


    /**
     * Restituisce il testo contenuto in un file di testo (.txt) presente nelle
     * risorse dell'applicazione
     *
     * @param path Il path relativo del file di testo (ad esempio:
     *             "/path/myText.txt")
     * @return Il testo contenuto nel file
     */
    public static String getText(String path) {

        StringBuilder builder = new StringBuilder();

        try (InputStream byteStream = getResourceAsStream(path);
             InputStreamReader txtStream = new InputStreamReader(byteStream, "ISO-8859-1");
             BufferedReader reader = new BufferedReader(txtStream)) {

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }

        return builder.toString();
    }

    /**
     * Estrae un file interno al jar nella stessa cartella del jar e ne
     * restituisce il path. Nel caso in cui il programma risulti non essere
     * eseguito da un jar ma da un file .class, il metodo ritorna direttamente
     * il path del file presente nelle risorse del file .class
     *
     * @param path Il path del file interno al jar
     * @return Il path del file estratto
     */
    public static String extract(String path) throws IOException {
        return extract(path, null);
    }

    /**
     * Estrae un file interno al jar nella cartella specificata e ne restituisce
     * il path. Nel caso in cui il programma risulti non essere eseguito da un
     * jar ma da un file .class, il metodo ritorna direttamente il path del file
     * presente nelle risorse del file .class
     *
     * @param path            Il path del file interno al jar
     * @param destinationPath Il path della cartella di destinazione
     * @return Il path del file estratto
     */
    public static String extract(String path, String destinationPath) throws IOException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String runningPath = getRunningPath();
        if (isRunningFromJar()) {
            if (destinationPath == null || destinationPath.isEmpty()) {
                destinationPath = runningPath.substring(0, runningPath.lastIndexOf("/"));
            }
            JarFile jar = new JarFile(runningPath);
            JarEntry file = jar.getJarEntry(path);
            String fileName = file.getName();
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
            File destination = new File(destinationPath + File.separator + fileName);
            if (!destination.exists()) {
                if (file.isDirectory()) {
                    destination.mkdir();
                }
                try (
                        InputStream input = jar.getInputStream(file);
                        FileOutputStream output = new FileOutputStream(destination)) {
                    while (input.available() > 0) {
                        output.write(input.read());
                    }
                }
            }
            return destination.getAbsolutePath();
        } else {
            return runningPath + path;
        }
    }

    /**
     * Restituisce il path dal quale si sta eseguendo l'applicazione
     */
    public static String getRunningPath() {
        String runningPath = Resources.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            return new URI(runningPath).getPath();
        } catch (URISyntaxException ex) {
            return runningPath;
        }
    }

    /**
     * @return True se si sta eseguendo l'applicazione da un file .jar, False
     * altrimenti
     */
    public static boolean isRunningFromJar() {
        String className = Resources.class.getName().replace('.', '/');
        String classJar = Resources.class.getResource("/" + className + ".class").toString();
        return classJar.startsWith("jar:");
    }

    /**
     * Chiama il metodo getResource() sulla classe di riferimento
     *
     * @see Class#getResource(String)
     */
    public static URL getResource(String resource) {
        return Resources.source.getResource(resource);
    }

    /**
     * Chiama il metodo getResourceAsStream() sulla classe di riferimento
     *
     * @see Class#getResourceAsStream(String)
     */
    public static InputStream getResourceAsStream(String resource) {
        return Resources.source.getResourceAsStream(resource);
    }
}



/**
 * La classe Sound modella, riproduce e gestisce un suono in formato WAVE.
 *
 * @author Lorenzo
 */
class Sound {

    private static final Line.Info INFO = new Line.Info(Clip.class);
    private static HashMap<String, Sound> map;
    private URL soundUrl;
    public Clip readyClip;

    /**
     * Crea un nuovo suono a partire dal path assoluto di un file WAVE.
     *
     * @param path Il path assoluto di un file WAVE.
     * @throws SoundException Se il path non risulta puntare ad un corretto file
     * WAVE
     */
    public Sound(String path) throws SoundException, MalformedURLException {
        this(new URL(path));
    }

    /**
     * Crea un nuovo suono a partire da un file WAVE
     *
     * @param url Il file WAVE
     * @throws SoundException Se il file non risulta essere un corretto file
     * WAVE
     */
    public Sound(URL url) throws SoundException {
        if (url == null) {
            throw new SoundException("Cannot read " + url.getPath());
        }
        this.soundUrl = url;
        this.readyClip = this.getNewClip();
    }

    /**
     * Riproduce una volta il suono
     *
     * @return Un oggetto di tipo Clip che permette di gestire il suono
     */
    public Clip play() {
        return play(1);
    }

    /**
     * Riproduce all'infinito il suono
     *
     * @return Un oggetto di tipo Clip che permette di gestire il suono
     */
    public Clip loop() {
        return play(-1);
    }

    /**
     * Riproduce n volte il suono
     *
     * @param times Il numero di volte da riprodurre il suono (un numero
     * negativo provoca una riproduzione in loop)
     * @return Un oggetto di tipo Clip che permette di gestire il suono
     */
    public Clip play(int times) {
        Clip clip = null;
        try {
            clip = getNewClip();
        } catch (SoundException ex) {
            throw new RuntimeException(ex);
        }
        if (clip != null) {
            clip.loop(times);
        }
        return clip;
    }

    /**
     * Crea e restituisce un nuovo Clip del suono, pronto per essere riprodotto
     *
     * @return Un oggetto di tipo Clip che permette di gestire il suono
     * @throws SoundException Se non è possibile ottenere un Clip dal suono
     */
    public final Clip getNewClip() throws SoundException {
        try {
            if (this.readyClip == null) {
                this.readyClip = Sound.getNewClip(this.soundUrl);
            }
            Clip c = this.readyClip;
            this.readyClip = Sound.getNewClip(this.soundUrl);
            return c;
        } catch (SoundException ex) {
            this.readyClip = null;
            throw ex;
        }
    }

    /**
     * Restituisce una HashMap (sempre la stessa istanza) utilizzabile per
     * gestire con comodità una collezione di suoni. Ad ogni nuovo suono
     * aggiunto a tale struttura dati è necessario associare un nome mnemonico
     * per poter richiamare successivamente il suono stesso
     *
     * @return Una struttura dati di tipo HashMap per la gestione multipla di
     * suoni
     */
    public static HashMap<String, Sound> getMap() {
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }


    public Clip getReadyClip() {
        return readyClip;
    }

    /**
     * Crea e restituisce un nuovo Clip di un file WAVE, pronto per essere
     * riprodotto
     *
     * @param clipURL Il file WAVE dal quale ottenere un Clip
     * @return Un oggetto di tipo Clip che permette di gestire il suono
     * @throws SoundException Se non è possibile ottenere un Clip dal file
     */
    public static Clip getNewClip(URL clipURL) throws SoundException {
        Clip clip = null;
        try {
            clip = (Clip) AudioSystem.getLine(INFO);
            clip.open(AudioSystem.getAudioInputStream(clipURL));
        } catch (IOException | UnsupportedAudioFileException ex) {
            throw new SoundException(clipURL.getFile(), ex);
        } catch (LineUnavailableException ex) {
            throw new RuntimeException(ex);
        }
        return clip;
    }
}




