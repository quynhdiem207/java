/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Mar 30, 2022
 * @version 1.0
 */
package example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class Book implements Serializable {
    private static final long serialVersionUID = -2936687026040726549L;
    private String bookName;
    private transient String description;
    private transient int copies;

    /**
	 * @param bookName the bookName to set
	 */
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param copies the copies to set
	 */
	public void setCopies(int copies) {
		this.copies = copies;
	}

	/**
	 * @return the bookName
	 */
	public String getBookName() {
		return bookName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the copies
	 */
	public int getCopies() {
		return copies;
	}

    /**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void serialize(Book book) throws Exception {
        FileOutputStream file = new FileOutputStream("D:\\Transient");
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(book);
        out.close();
        file.close();
    }

	public static Book deserialize() throws Exception {
        FileInputStream file = new FileInputStream("D:\\Transient");
        ObjectInputStream in = new ObjectInputStream(file);
        Book book = (Book) in.readObject();
        in.close();
        file.close();
        return book;
    }
}

public class TransientField {
	public static void main(String[] args) {
		Book book = new Book();
        book.setBookName("Java Reference");
        book.setDescription("will not be saved");
        book.setCopies(25);
        
        try {
        	Book.serialize(book);
        	Book book2 = Book.deserialize();
        	
        	System.out.println(book2.getBookName());
        	System.out.println(book2.getDescription());
        	System.out.println(book2.getCopies());
        	System.out.println(book2.getSerialversionuid());
        } catch(Exception e) {
        	System.out.println("Error");
        }
        
        Z z = new Z(4);
        z.m();
        z.<String>testG("Hi");
        
//        Point1[] p = new Point1[100];
//        for (int i = 0; i < p.length; i++) {
//            p[i] = new Point1();
//            p[i].move(i, p.length-1-i);
//        }
//        for(Point1 pi: p) {
//        	System.out.println(pi);
//        }
        
        RealPoint rp = new RealPoint();
        Point2 p = rp;
        rp.move(1.71828f, 4.14159f);
        p.move(1, -1);
        show(p.x, p.y);
        show(rp.x, rp.y);
        show(p.getX(), p.getY());
        show(rp.getX(), rp.getY());
	}
	
	static void show(int x, int y) {
        System.out.println("(" + x + ", " + y + ")");
    }
    static void show(float x, float y) {
        System.out.println("(" + x + ", " + y + ")");
    }
}

class Z {
    static int peek() { return j; }
    static int i = peek();
    static int j = 1;
    int k;
    
    Z(int k) {
    	this.k = k;
    }
    
    void m(Z this) {
    	System.out.println(this);
    }
    
    public <T> void testG(T t) {
    	System.out.println(t);
    }
}

final class Point1 {
    int x, y;
    void move(int dx, int dy) { x += dx; y += dy; }
    public String toString() {
    	return x + ", " + y;
    }
}

class Point2 {
    int x = 0, y = 0;
    void move(int dx, int dy) { x += dx; y += dy; }
    int getX() { return x; }
    int getY() { return y; }
    int color;
}

class RealPoint extends Point2 {
    float x = 0.0f, y = 0.0f;
    void move(int dx, int dy) { move((float)dx, (float)dy); }
    void move(float dx, float dy) { x += dx; y += dy; }
    int getX() { return (int)Math.floor(x); }
    int getY() { return (int)Math.floor(y); }
}
