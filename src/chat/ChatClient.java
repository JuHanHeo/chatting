package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class ChatClient {
	private static final String SERVER_IP = "192.168.1.37";
	private static final int PORT = 6666;

	public static void main(String[] args) {
		Socket socket = new Socket();
		Scanner scanner = new Scanner(System.in);
		BufferedReader br = null;
		PrintWriter pw = null;


		try {
			socket.connect(new InetSocketAddress(SERVER_IP, PORT));

			br = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8),true);

			System.out.print("닉네임>>");
			String nickName = scanner.nextLine();
			pw.println("join:" + nickName);
			pw.flush();
//			br.readLine();

			new ChatClientReceivedThread(br).start();

			while( true ) {
				String input = scanner.nextLine();

				if( "quit".equals( input ) == true ) {
					// 8. quit 프로토콜 처리
					pw.println("quit:");
					pw.flush();
					break;
				} else {
					// 9. 메시지 처리
					pw.println("message:"+input);
					pw.flush();
				}
			}



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				if(socket != null&&socket.isClosed()){
					socket.close();
				}
				if(br != null){
					br.close();
				}
				if(pw != null){
					pw.close();
				}
				if(scanner != null){
					scanner.close();
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			scanner.close();
		}


	}

}
