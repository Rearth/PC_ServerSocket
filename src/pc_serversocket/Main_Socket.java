/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc_serversocket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Darkp
 */
public class Main_Socket {
    
    private static GPUReader GPUReaderThread;
    private static ServerSocket serverSocket;
    private static Socket server = null;
    private static final AudioPlayerController audio = AudioPlayerController.getInstance();
    
    private static final int port = 230;
    
    public static void main(String[] Args) {
        System.out.println("Starting Server socket");
                
        GPUReaderThread = new GPUReader();
        GPUReaderThread.start();
        
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(5000);
            audio.start();
            Thread.sleep(100);
            restartUpdate();
            
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(Main_Socket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void restartUpdate() throws InterruptedException {
        try {
            while (true) {
                update();
                Thread.sleep(50);
            }
        } catch (InterruptedException | SocketException | ClassNotFoundException ex) {
            Thread.sleep(100);
            restartUpdate();
        } catch (IOException ex) {
            Logger.getLogger(Main_Socket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //called every 50 ms
    //PC sends info first, raspberry then processes that, then sends back information while PC waits for input, then processes input and updates again
    private static void update() throws InterruptedException, IOException, ClassNotFoundException {
        
        if (server == null) {
            server = connect();
        }
        
        if (server == null) {
            return;
        }
        
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        
        PCInfo sendInfo = PCInfo.gather();
        System.out.println(sendInfo);
        String sendMessage = toString(sendInfo);
        
        out.writeUTF(sendMessage);
        
        System.out.println("waiting for response from client");
        
        DataInputStream in = new DataInputStream(server.getInputStream());
        
        String input;
        try {
            input = in.readUTF();
        } catch (EOFException ex) {
            System.err.println("unable to read input");
            server = connect();
            return;
        }
        RaspberryInfo recievedInfo = (RaspberryInfo) fromString(input);
        System.out.println(recievedInfo);

        handleInfo(recievedInfo, sendInfo);
    }
    
    private static Object fromString(String s) throws IOException ,
                                                       ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream( 
                                        new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
   }

    /** Write the object to a Base64 string. */
    private static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
    }

    private static Socket connect(){
        try {
            System.out.println("connecting with raspberry socket...");
            
            Socket serverHere = serverSocket.accept();
            System.out.println("succesfully connected!");
            return serverHere;
        } catch (Exception ex) {
            System.err.println("error while connecting with raspberry: " + ex + " trying again...");
        }
        return null;
    }

    private static void handleInfo(RaspberryInfo recievedInfo, PCInfo ownInfo) {
        
        if (recievedInfo.isSpotify() != ownInfo.getAudioDat().isSpotify()) {
            if (recievedInfo.isSpotify()) {
                AudioControl.setCurSelection(AudioControl.AudioState.spotify);
            } else {
                AudioControl.setCurSelection(AudioControl.AudioState.java);
            }
        }
        
        if (recievedInfo.isPlaying() != ownInfo.getAudioDat().isPlaying()) {
            AudioControl.setPlaying(recievedInfo.isPlaying());
        }
        
        float volumeNow = AudioPlayerController.getInstance().getVolume();
        float targetVolume = recievedInfo.getVolume();
        if (volumeNow != targetVolume) {
            if (targetVolume > volumeNow) {
                AudioControl.louder();
            } else {
                AudioControl.quiter();
            }
            AudioPlayerController.getInstance().setVolume(targetVolume);
        }
        
        if (recievedInfo.isNextTrack()) {
            if (recievedInfo.isSpotify()) {
                SpotifyDesktopController.nextTrack();
            } else {
                AudioPlayerController.getInstance().nextTrack();
            }
        } else if (recievedInfo.isPreviousTrack()) {
            if (recievedInfo.isSpotify()) {
                SpotifyDesktopController.previousTrack();
            } else {
                AudioPlayerController.getInstance().nextTrack();
            }
        }
        
    }
    
    
}
