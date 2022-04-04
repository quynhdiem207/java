/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Mar 30, 2022
 * @version 1.0
 */
package example;

class Point {
    int x = 2;
}

public class StaticField extends Point {
	
    void printBoth() {
        System.out.println(x + " " + super.x);
    }
    
    public static void main(String[] args) {
    	StaticField sample = new StaticField();
        sample.printBoth();
        System.out.println(sample.x + " " +	((Point)sample).x);
    }
}