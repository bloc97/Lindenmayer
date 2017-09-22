/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Collection;

/**
 *
 * @author bowen
 */
public class Utils {
    
    public static String readStringFile(String path) throws IOException {
        return unwrap(Files.readAllLines(FileSystems.getDefault().getPath(path)));
    }
    
    public static String unwrap(Collection<String> coll) {
        String string = "";
        for (String str : coll) {
            string += str + "\n";
        }
        return string.substring(0, string.length() - 1);
    }
}
