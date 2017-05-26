package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final int PORT = 6666;
	

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		List<Writer> listWriters = new ArrayList<Writer>();
		
		try {
			serverSocket = new ServerSocket();
			InetAddress hostAddress =  InetAddress.getLocalHost();
			String localhostAddress = hostAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT));
			log( "연결 기다림 " + hostAddress + ":" + PORT );
			
			while(true){
				Socket socket = serverSocket.accept();
				new ChatServerTread(socket,listWriters).start();;
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log("에러 : " + e);
		} finally{
			if(serverSocket != null && serverSocket.isClosed() == false){
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log("에러 : " + e);
				}
			}
		}

	}
	public static void log(String msg){
		System.out.println("[server] : "+msg);
	}

}
