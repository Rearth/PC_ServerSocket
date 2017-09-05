/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc_serversocket;

import java.io.Serializable;

/**
 *
 * @author Darkp
 */
public class RaspberryInfo implements Serializable {

    static final long serialVersionUID = 6644;
    private final boolean Playing;
    private final float Volume;
    private final boolean spotify;
    private final boolean nextTrack;
    private final boolean previousTrack;
    
    public RaspberryInfo(boolean Playing, float Volume, boolean spotify, boolean nextTrack, boolean previousTrack) {
        this.Playing = Playing;
        this.Volume = Volume;
        this.spotify = spotify;
        this.nextTrack = nextTrack;
        this.previousTrack = previousTrack;
        
    }

    public boolean isPlaying() {
        return Playing;
    }

    public float getVolume() {
        return Volume;
    }

    public boolean isSpotify() {
        return spotify;
    }

    public boolean isNextTrack() {
        return nextTrack;
    }

    public boolean isPreviousTrack() {
        return previousTrack;
    }

    @Override
    public String toString() {
        return "RaspberryInfo{" + "Playing=" + Playing + ", Volume=" + Volume + ", spotify=" + spotify + '}';
    }
    
}
