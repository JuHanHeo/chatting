package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatClientReceivedThread extends Thread{
	private BufferedReader bufferedReader;
	
	public ChatClientReceivedThread(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
		
	}
	public void run() {
		
		try {
			while(true){
				String data = bufferedReader.readLine();
				if(data == null){
					break;
				}
				System.out.println(data);
				
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
