package main;

import compiler.CPU;
import compiler.Interpreter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter("mips.mips");

        Scanner sc = new Scanner(System.in);
        int clock = 0;
        while ((clock=sc.nextInt())!=0) {
            CPU cpu = new CPU(interpreter);
            for (int c = 0; c<clock; c++)
                cpu.clock();
            System.out.println(cpu.log());
        }

    }
}
