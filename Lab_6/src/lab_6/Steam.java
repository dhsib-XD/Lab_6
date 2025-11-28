import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Steam {
    
    
    private static final String ROOT_DIR = "steam";
    private static final String CODES_FILE = ROOT_DIR + File.separator + "codes.stm";
    
    
    private RandomAccessFile codesRAF;
    
    

    public Steam() {
        try {
            
            File rootDir = new File(ROOT_DIR);
            if (!rootDir.exists()) {
                rootDir.mkdirs();
                System.out.println("Directorio 'steam/' creado.");
            }

           
            codesRAF = new RandomAccessFile(CODES_FILE, "rw");

           
            if (codesRAF.length() == 0) {
                
                
                codesRAF.writeInt(1); 
                
                
                codesRAF.writeInt(1); 
                
                
                codesRAF.writeInt(1); 
                
                System.out.println("Archivo codes.stm inicializado con contadores (1, 1, 1).");
            }
            
        } catch (IOException e) {
           
            System.err.println("Error FATAL al inicializar los archivos del sistema: " + e.getMessage());
            
        }
    }
    
   
}