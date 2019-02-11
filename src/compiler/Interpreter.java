package compiler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Interpreter {

    private BufferedReader br;

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
        System.out.println(st);
        return st;
    }

    public static String register_num(String reg){
        if (reg.charAt(1)=='t'){
            if (Integer.parseInt(reg.substring(2))<8)
                return to_binary_string(8+Integer.parseInt(reg.substring(2)),5);
            return to_binary_string(18+Integer.parseInt(reg.substring(2)),5);
        }else if (reg.charAt(1)=='s'){
            return to_binary_string(16+Integer.parseInt(reg.substring(2)),5);
        }else if (reg.charAt(1)=='P'&&reg.charAt(2)=='C'){
            return to_binary_string(32,5);//?
        }else if (reg.charAt(1)=='a'){
            return to_binary_string(4+Integer.parseInt(reg.substring(2)),5);
        }else if (reg.charAt(1)=='v'){
            return to_binary_string(2+Integer.parseInt(reg.substring(2)),5);
        }else if (reg.charAt(1)=='k'){
            return to_binary_string(26+Integer.parseInt(reg.substring(2)),5);
        }else if (reg.substring(0,2).equals("ra")){
            return to_binary_string(31,5);
        }else if (reg.substring(0,2).equals("sp")) {
            return to_binary_string(29, 5);
        }else if (reg.substring(0,2).equals("$z")) {
            return to_binary_string(0, 5);
        }
        return "";
    }
    public static int to_int(String binary){
        if (binary.equals("")) binary = "0";
        int r = Integer.parseInt(binary,2);
        return r;
    }
    public static String to_binary_string(Integer binary,int n){
        if (binary==null) return "null";
        String out = Integer.toBinaryString(binary);
        while (out.length()<n)
            out="0"+out;
        return out;
    }

    public static String toBinary(String code) {
        Map<String,Integer> binary = new HashMap<>();
        if (code.equals(""))
            return "00000000000000000000000000000000";

        String[]str = code.split(" |(|)|,");                    // TODO: "," ham masalast
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
                final_code=final_code+"000000"+register_num(str[2])+register_num(str[3])+register_num(str[1])+"00000"+"100111";
            break;
            case "slt":
                final_code=final_code+"000000"+register_num(str[2])+register_num(str[3])+register_num(str[1])+"00000"+"101010";
            break;
            case "beq":
                final_code=final_code+"000100"+register_num(str[1])+register_num(str[2])+to_binary_string(Integer.parseInt(str[3]),16);
            break;
            case "lw":
                final_code=final_code+"100011"+register_num(str[3])+register_num(str[1])+to_binary_string(Integer.parseInt(str[2]),16);
            break;
            case "sw":
                final_code=final_code+"101011"+register_num(str[3])+register_num(str[1])+to_binary_string(Integer.parseInt(str[2]),16);
            break;
        }
        return final_code;
    }

    public HashMap<Integer,Integer> load(Set<Integer> branch_set){
        String s = "";
        HashMap<Integer,Integer> init = new HashMap<>();
        int l = 0;
        while((s = nextInstruction())!=null) {
            int nbin = Interpreter.to_int(Interpreter.toBinary(s));
            int op = (nbin>>26);
            if (op==4)
                branch_set.add(l);
            init.put(l,nbin);
            l++;
        }
        return init;
    }


}

