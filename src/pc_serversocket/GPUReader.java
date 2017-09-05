/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc_serversocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Darkp
 */
public class GPUReader extends Thread {
    
    public static float GPUMemUsage;    //in MB
    public static float GPUMaxMem;      //in MB
    public static float GPULoad;        //absolute (0-1)
    public static boolean destroy = false;
    
    private static Process GPUTool = null;
    
    private static final String command = "\"C:\\Program Files\\NVIDIA Corporation\\NVSMI\\nvidia-smi.exe\" --query-gpu=utilization.gpu,memory.used,memory.total --format=csv --loop-ms=200";
    
    //refreshing every 50ms, with info message at begin; format: utilization.gpu [%], memory.used [MiB], memory.total [MiB]
    
    @Override
    public void run() {
        try {
            System.out.println("starting GPU_reading Thread");
            GPUTool = Runtime.getRuntime().exec("cmd /C " + command);
            
            BufferedReader input = new BufferedReader(new InputStreamReader(GPUTool.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                if (destroy) {
                    GPUTool.destroyForcibly();
                    this.interrupt();
                    this.destroy();
                }
                if (line.contains("utilization.gpu [%], memory.used [MiB], memory.total [MiB]")) {
                    continue;
                }
                //System.out.println(line);
                String Parts[] = line.replace(" ", "").replace("%", "").replace("MiB", "").split(",");
                GPUMemUsage = Float.valueOf(Parts[1]);
                GPUMaxMem = Float.valueOf(Parts[2]);
                GPULoad = Float.valueOf(Parts[0]) / 100;
                //System.out.println("GPU values: GPUMemUsage=" + GPUMemUsage  + " GPUMaxMem=" + GPUMaxMem + " GPULoad=" + GPULoad);
            }
        } catch (IOException ex) {
            Logger.getLogger(GPUReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
