package logging;
import config.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

/** a class for logging erors and debugging statements.
 *
 * Author: Tyler Wahl
 * Date: February 15, 2022
 * Course:CS-622
 * */
public class Logger {
    /**function for writing to a log */
    public static  void writeToLog(int level, String message){
        String logDir = getLogDir();
        String fileName = "error_log";
        switch(level){
            case 200:
               fileName = "debug_log.txt";
               break;
            default:
                fileName= "error_log.txt";
                break;
       }
        File logFile = new File(logDir + fileName);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            PrintWriter pw = new PrintWriter(new  FileWriter(logFile, true));
            pw.format( "{Log Entry:{ Date: %s,  message: %s }}" , (new Date()).toString(), message );
            pw.println();
            pw.close();

        }
        catch(Exception ex){
            System.out.println("Error logging to file");
        }
        return;

    }

    /**function that takes in an error and writes it to the appropriate log */
    public static void writeToLog(Exception ex){
        writeToLog(400, ex.getMessage());

    }

    /**gets the appropriate directory for logging to */
    private static String getLogDir(){
        String log_dir = Properties.getProperty("logDir");
       // System.out.println(Properties.getProperty("environment"));
        if(Properties.getProperty("environment") == "PROD"){
           //change this to stream to an aws lambda endpoint?
        }
        return log_dir;

    }


}
