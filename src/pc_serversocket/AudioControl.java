/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc_serversocket;

/**
 *
 * @author Darkp
 */
public class AudioControl {
    
    private static boolean playing = false;
    private static AudioState curSelection = AudioState.spotify;
    
    public static enum AudioState {
        spotify, java
    }
    
    public static AudioState getCurSelection() {
        return curSelection;
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static void setPlaying(boolean playing) {
        AudioControl.playing = playing;
        if (curSelection.equals(AudioState.spotify)) {
            SpotifyDesktopController.setPlaying(playing);
        } else {
            if (playing) {
                AudioPlayerController.getInstance().play();
            } else {
                AudioPlayerController.getInstance().pause();
            }
        }
    }

    public static void setCurSelection(AudioState curSelection) {
        AudioControl.curSelection = curSelection;
        if (curSelection.equals(AudioState.spotify)) {
            AudioPlayerController.getInstance().pause();
            if (isPlaying()) {
                SpotifyDesktopController.play();
            } else {
                SpotifyDesktopController.pause();
            }
        } else {
            SpotifyDesktopController.pause();
            if (isPlaying()) {
                AudioPlayerController.getInstance().play();
            } else {
                AudioPlayerController.getInstance().pause();
            }
        }
    }
    
    public static void louder() {
        if (curSelection.equals(AudioState.spotify)) {
            SpotifyDesktopController.louder();
        } else {
            AudioPlayerController.getInstance().setVolume(AudioPlayerController.getInstance().getVolume() + 0.05F);
        }
    }
    
    public static void quiter() {
        if (curSelection.equals(AudioState.spotify)) {
            SpotifyDesktopController.quiter();
        } else {
            AudioPlayerController.getInstance().setVolume(AudioPlayerController.getInstance().getVolume() - 0.05F);
        }
    }
    
}
