/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Feb 19, 2022
 * @version 1.0
 */

class Point {
    int x, y;
    Point() { System.out.println("default"); }
    Point(int x, int y) { this.x = x; this.y = y; }

    /* A Point instance is explicitly created at class initialization time: */
    static Point origin = new Point(0,0);

    /* A String can be implicitly created by a + operator: */
    public String toString() { return "(" + x + "," + y + ")"; }
}

class Customer {
	public static void main(String[] args) {
        /* A Point is explicitly created using newInstance: */
		Point p1 = null, p2 = null, p3 = null;
        try {
        	p1 = (Point) Class.forName("Point").getDeclaredConstructor().newInstance(); // default
            p2 = (Point) Point.class.getDeclaredConstructor(int.class, int.class).newInstance(2, 2);
            p3 = new Point(3, 3);
            System.out.println(p3.getClass().getName());
            System.out.println(Class.forName("Point").getClassLoader());
        } catch (Exception e) {
            System.out.println(e);
        }

        /* An array is implicitly created by an array constructor: */
        Point a[] = { new Point(0,0), new Point(1,1) };

        /* Strings are implicitly created by + operators: */
        System.out.println("p1: " + p1 + ", p2: " + p2 + ", p3: " + p3); // p1: (0,0)
        System.out.println("a: { " + a[0] + ", " + a[1] + " }");
    
        /* An array is explicitly created by an array creation expression: */
        String sa[] = new String[2];
        sa[0] = "he"; sa[1] = "llo";
        System.out.println(sa[0] + sa[1]);
    }
}
