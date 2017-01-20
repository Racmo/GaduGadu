/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gadugadu;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author Monika
 */
public class MySocketReader {
    private BufferedReader bufferedReader;
    
    MySocketReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }
    
    public String read() throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        
        int ch;
        
        while( (ch = this.bufferedReader.read()) != -1){
            if(ch == 10){ 
                //znak nowej linii
                break;
            }
            else{
                stringBuilder.append((char)ch);
            }
        }
        return stringBuilder.toString();
    }
}
