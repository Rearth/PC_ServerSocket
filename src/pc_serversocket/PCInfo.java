/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc_serversocket;

import com.sun.management.OperatingSystemMXBean;
import java.io.Serializable;
import java.lang.management.ManagementFactory;

/**
 *
 * @author Darkp
 */
public class PCInfo implements Serializable{
    
    static final long serialVersionUID = 66449;
    private static final int cValue = 1048576;

    private final PerformanceData PCdata;
    private final AudioData audioDat;
    
    public PCInfo(PerformanceData PCdata, AudioData audioDat) {
        this.PCdata = PCdata;
        this.audioDat = audioDat;
        
    }
    
    public static PCInfo gather() {
        
        AudioData data = new AudioData();
        data.spotify = AudioControl.getCurSelection().equals(AudioControl.AudioState.spotify);
        data.playing = AudioControl.isPlaying();
        data.volume = AudioPlayerController.getInstance().getVolume();
        
        PCInfo info = new PCInfo(PerformanceData.getNow(), data);
        
        return info;
    }
    
    public static class AudioData implements Serializable {
        
        static final long serialVersionUID = 6644934;
        private boolean spotify;
        private boolean playing;
        private float volume;
        
        public AudioData() {
            
        }

        public boolean isSpotify() {
            return spotify;
        }
        public boolean isPlaying() {
            return playing;
        }
        public float getVolume() {
            return volume;
        }

        @Override
        public String toString() {
            return "AudioData{" + "spotify=" + spotify + ", playing=" + playing + ", volume=" + volume + '}';
        }
        
    }

    public PerformanceData getPCdata() {
        return PCdata;
    }

    public AudioData getAudioDat() {
        return audioDat;
    }

    @Override
    public String toString() {
        return "PCInfo{" + "PCdata=" + PCdata + ", audioDat=" + audioDat + '}';
    }
    
    public static class PerformanceData implements Serializable {
        static final long serialVersionUID = 66449343;
        private final float CPUusage;   //prozentual (0-1)
        private final float MaxRam;     //in MB
        private final float UsedRam;    //in MB
        private final float GPUMemUsage;//in MB
        private final float GPUMaxMem;  //in MB
        private final float GPULoad;    //prozentual (0-1)
        
        private PerformanceData(float CPUusage, float MaxRam, float UsedRam, float GPUMemUsage, float GPUMaxMem, float GPULoad) {
            this.CPUusage = CPUusage;
            this.MaxRam = MaxRam;
            this.UsedRam = UsedRam;
            this.GPUMemUsage = GPUMemUsage;
            this.GPUMaxMem = GPUMaxMem;
            this.GPULoad = GPULoad;
            
        }
        
        public static PerformanceData getNow() {
            
            OperatingSystemMXBean system = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            
            float usedRam = (system.getTotalPhysicalMemorySize() - system.getFreePhysicalMemorySize()) / cValue;
            
            PerformanceData data = new PerformanceData((float) system.getSystemCpuLoad(), (float) system.getTotalPhysicalMemorySize() / cValue, usedRam, GPUReader.GPUMemUsage, GPUReader.GPUMaxMem, GPUReader.GPULoad);
            
            return data;
        }
        
        private float runden(double number) {
            return (float) ((int) (number * 10)) / 10;
        }

        @Override
        public String toString() {
            return "PerformanceData{" + "CPUusage=" + CPUusage + ", MaxRam=" + MaxRam + ", UsedRam=" + UsedRam + ", GPUMemUsage=" + GPUMemUsage + ", GPUMaxMem=" + GPUMaxMem + ", GPULoad=" + GPULoad + '}';
        }

        public float getCPUusage() {
            return CPUusage;
        }

        public float getMaxRam() {
            return MaxRam;
        }

        public float getUsedRam() {
            return UsedRam;
        }

        public float getGPUMemUsage() {
            return GPUMemUsage;
        }

        public float getGPUMaxMem() {
            return GPUMaxMem;
        }

        public float getGPULoad() {
            return GPULoad;
        }
        
    }
    
}
