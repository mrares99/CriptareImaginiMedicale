package main.java;

public class StringException extends Exception {

    private String string;

    StringException(String string){
        this.string=string;
    }

    public String toString(){
        return ("Exceptia: "+string);
    }
}
