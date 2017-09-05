/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc_serversocket;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Darkp
 */
public class SpotifyDesktopController {
    
    private static final String command = "C:\\Users\\Darkp\\Desktop\\RaspberryPi\\Networking\\spotify\\CLMControl.exe spotify ";
    private static boolean playing = false;
    
    public static void play() {
        System.out.println("setting spotify to: play");
        playing = true;
        try {
            Runtime.getRuntime().exec("cmd /C " + command + "-p");
            //exec.waitFor(50, TimeUnit.MILLISECONDS);
            //exec.destroyForcibly();
        } catch (IOException ex) {
            Logger.getLogger(SpotifyDesktopController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void pause() {
        System.out.println("setting spotify to: pause");
        playing = false;
        try {
            Process exec = Runtime.getRuntime().exec("cmd /C " + command + "-pa");
            exec.waitFor(50, TimeUnit.MILLISECONDS);
            exec.destroyForcibly();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SpotifyDesktopController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void louder() {
        System.out.println("setting spotify to: louder");
        try {
            Process exec = Runtime.getRuntime().exec("cmd /C " + command + "-vu");
            exec.waitFor(50, TimeUnit.MILLISECONDS);
            exec.destroyForcibly();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SpotifyDesktopController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void quiter() {
        System.out.println("setting spotify to: quiter");
        try {
            Process exec = Runtime.getRuntime().exec("cmd /C " + command + "-vd");
            exec.waitFor(50, TimeUnit.MILLISECONDS);
            exec.destroyForcibly();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SpotifyDesktopController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static void setPlaying(boolean playing) {
        if (playing) {
            play();
        } else {
            pause();
        }
        SpotifyDesktopController.playing = playing;
    }
    
    public static void nextTrack() {
        try {
            Process exec = Runtime.getRuntime().exec("cmd /C " + command + "-nt");
            exec.waitFor(50, TimeUnit.MILLISECONDS);
            exec.destroyForcibly();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SpotifyDesktopController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void previousTrack() {
        try {
            Process exec = Runtime.getRuntime().exec("cmd /C " + command + "-pt");
            exec.waitFor(50, TimeUnit.MILLISECONDS);
            exec.destroyForcibly();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SpotifyDesktopController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
