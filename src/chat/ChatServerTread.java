package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ChatServerTread extends Thread{
	private String nickname;
	private Socket socket;
	private List<Writer> listWriter;
	BufferedReader bufferedReader = null;
	PrintWriter printWriter = null;

	public ChatServerTread(Socket socket) {
		this.socket = socket;
	}

	public ChatServerTread(Socket socket, List<Writer> listWriter) {
		this.socket = socket;
		this.listWriter = listWriter;
	}

	public void run() {
		// 호스트 연결
		InetSocketAddress remoteAdress = (InetSocketAddress) socket.getRemoteSocketAddress();
		int remoteHostPort = remoteAdress.getPort();
		System.out.println(remoteAdress.getHostName());
		String remoteHostAddress = remoteAdress.getAddress().getHostAddress();
		System.out.println("연결됨" + remoteHostAddress +"."+ remoteHostPort);

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);

			while(true){
				//요쳥
				String request = bufferedReader.readLine();
				if(request == null){
					ChatServer.log("클라이언트로부터 연결 끊김");
					break;
				}

				String[] tokens = request.split( ":" );

				if("join".equals(tokens[0])){
					doJoin( tokens[1], printWriter );
				} else if("message".equals(tokens[0])){
					doMessage(tokens[1]);
				} else if("quit".equals(tokens[0])){
					doQuit(printWriter);
					break;
				} else {
					ChatServer.log( "에러:알수 없는 요청(" + tokens[0] + ")" );
				}

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			doQuit( printWriter );
		} finally {
			try {
				//자원정리
				bufferedReader.close();
				printWriter.close();
				if( socket.isClosed() == false ) {
					socket.close();
				}
			} catch( IOException ex ) {
				ChatServer.log( "error:" + ex );
			}
		}

	}
	private void doQuit(Writer writer) {
		// TODO Auto-generated method stub
		removeWriter(writer);

		String data = nickname+"님이 퇴장 하였습니다.";
		broadcast(data);
	}


	private void removeWriter(Writer writer) {
		// TODO Auto-generated method stub
		synchronized (listWriter) {
			listWriter.remove(writer);
		}

	}

	public void doJoin(String token, PrintWriter writer){
		this.nickname = token;
		addWriter(writer);

		String data = nickname + "님이 참여하였습니다";
		broadcast(data);

		printWriter.println( "join:ok" );
		printWriter.flush();

	}
	public void addWriter(Writer writer){
		synchronized (listWriter) {
			listWriter.add(writer);
		}
	}

	private void broadcast( String data ) {
		synchronized( listWriter ) {
			for( Writer writer : listWriter ) {
				PrintWriter printWriter = (PrintWriter)writer;
				printWriter.println( data );
				printWriter.flush();
			}
		}
	}
	private void doMessage(String message){
		String msg = nickname+">>" +message;
		broadcast(msg);

	}







}
