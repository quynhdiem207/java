/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Mar 18, 2022
 * @version 1.0
 */
package example;

class Seq<T> { 
    T      head;
    Seq<T> tail;

    Seq() { 
        this(null, null); 
    } 

    Seq(T head, Seq<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    boolean isEmpty() { 
        return tail == null; 
    }
    
    public String toString() {
    	return "(" + head + ", " + tail + ")";
    }

    class Zipper<S> { 
        Seq<Pair<T,S>> zip(Seq<S> that) { 
            if (isEmpty() || that.isEmpty()) {
                return new Seq<Pair<T,S>>(); 
            } else {
                Seq<T>.Zipper<S> tailZipper = tail.new Zipper<S>();
                return new Seq<Pair<T,S>>(
                    new Pair<T,S>(head, that.head),
                    tailZipper.zip(that.tail)
                );
            }
        }
    }
}

class Pair<T, S> {
    T fst; 
    S snd;

    Pair(T f, S s) { 
        fst = f; 
        snd = s; 
    }
    
    public String toString() {
    	return fst + " - " + snd;
    }
}

public class Generic {
    public static void main(String[] args) {
        Seq<String> strs = new Seq<String>(
            "a",
            new Seq<String>("b", new Seq<String>())
        );

        Seq<Number> nums = new Seq<Number>(
            new Integer(1),
            new Seq<Number>(new Double(1.5), new Seq<Number>())
        );

        Seq<String>.Zipper<Number> zipper = strs.new Zipper<Number>();

        Seq<Pair<String,Number>> combined = zipper.zip(nums);
        
        System.out.println(combined);
    }
}