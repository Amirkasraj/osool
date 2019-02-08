package main;

import compiler.CPU;
import compiler.Interpreter;

import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int clock = 0;
        while ((clock=sc.nextInt())!=0) {
            Interpreter interpreter = new Interpreter("mips.mips");
            CPU cpu = new CPU(interpreter);
            for (int c = 0; c<clock; c++)
                cpu.clock();
        }
    }
}
