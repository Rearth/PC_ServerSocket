/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc_serversocket;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Darkp
 */
public class AudioPlayerController {
    
    private static final AudioPlayerController instance = new AudioPlayerController();
    private final ArrayList<String> audioFiles = new ArrayList<>();
    private final String FolderPath = "C:\\Users\\Darkp\\Desktop\\RaspberryPi\\Networking\\musik\\";
    private MediaPlayer mediaPlayer;
    private float Volume = 0.35F;
    private States state = States.unconfigured;
    private String title = "";
    
    public static enum States {
        unconfigured, playing, paused
    }
    
    public static AudioPlayerController getInstance() {
        return instance;
    }
    
    private AudioPlayerController() {
        
    }
    
    public void start() {
        Runnable runnable = () -> {
            getInstance().init();
        };
        com.sun.javafx.application.PlatformImpl.startup(runnable);
    }
    
    public void init() {
        
        System.out.println("Starting Musik...");
        
        File folder = new File(FolderPath);
        File[] listOfFiles = folder.listFiles();
        
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                System.out.println("File " + listOfFile.getName());
                audioFiles.add(listOfFile.toString().replace("\\", "/"));
            }
        }
        
        System.out.println("Musik done");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(AudioPlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        initPlayer();
    }
    
    public void initPlayer() {
        
        System.out.println("Started playing audio...");
        
        String path = new File(audioFiles.get(0)).toURI().toString();
        title = new File(audioFiles.get(0)).getName();
        Media media = new Media(path);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(Volume);
        mediaPlayer.setOnEndOfMedia(this::onEnd);
        
        state = States.paused;
        
    }
    
    private void onEnd() {
        Collections.shuffle(audioFiles);
        initPlayer();
    }
    
    public void pause() {
        try {
            mediaPlayer.pause();
            state = States.paused;
        } catch (NullPointerException ex) {
            
        }
    }
    
    public void play() {
        if (!state.equals(States.unconfigured)) {
            mediaPlayer.play();
            state = States.playing;
        }
    }
    
    public void nextTrack() {
        mediaPlayer.stop();
        onEnd();
    }
    
    public void playFile(String file) {
        
        String path = new File(file).toURI().toString();
        title = new File(file).getName();
        Media media = new Media(path);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(Volume);
        mediaPlayer.setOnEndOfMedia(this::onEnd);
        play();
    }
    
    public boolean isplaying() {
        return state.equals(States.playing);
    }

    public float getVolume() {
        return Volume;
    }

    public void setVolume(float Volume) {
        this.Volume = Volume;
        if (!state.equals(States.unconfigured)) {
            mediaPlayer.setVolume(Volume);
        }
    }

    public States getState() {
        return state;
    }
    
    public String getTitle() {
        return title;
    }
    
    @Override
    public String toString() {
        return "MUSIC: " + state + " volume=" + Volume + "% Title=" + title;
    }
}
