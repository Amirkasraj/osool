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

    public int register_num(String reg){

        return 0;
    }
    public int to_int(String binary){
        return Integer.parseInt(binary,2);
    }
    public String to_binary_string(int binary){
        return Integer.toBinaryString(binary);
    }
    public String toBinary(String code) {
        String[]str = code.split(" ");
        String final_code="";
        switch(str[0]){
            case "add":
                final_code=final_code+"000000"+register_num(str[2])+register_num(str[3])+register_num(str[1])+"00000"+"100000";
            break;
            case "sub":
                final_code=final_code+"000000"+register_num(str[2])+register_num(str[3])+register_num(str[1])+"00000"+"100010";
            break;
            case "and":
                final_code=final_code+"000000"+register_num(str[2])+register_num(str[3])+register_num(str[1])+"00000"+"100100";
            break;
            case "or":
                final_code=final_code+"000000"+register_num(str[2])+register_num(str[3])+register_num(str[1])+"00000"+"100101";
            break;
            case "nor":
                //?
            break;
            case "slt":
                final_code=final_code+"000000"+register_num(str[2])+register_num(str[3])+register_num(str[1])+"00000"+"101010";
            break;
            case "beq":
                final_code=final_code+"000100"+register_num(str[1])+register_num(str[2])+str[3];//????
            break;
            case "lw":
                final_code=final_code+"100011";///???
            break;
            case "sw":
                final_code=final_code+"101011";///?
            break;
        }


       return final_code;
    }


}

