package compiler;

import java.io.*;

public class Interpreter {

    BufferedReader br;

    public Interpreter(String fileDir) {
        File file = new File(fileDir);
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String nextInstruction(){
        String st = null;
        try {
            st = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return st;
    }

    public int toBinary(String code) {
        return 0; // azoon kara
        //chhe cool
    }


}
