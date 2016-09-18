package clientserver;

import java.io.BufferedReader; 
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	private static final int PORT = 8888;
	private ArrayList<WorkThread> listThread;

	

	private class WorkThread extends Thread {

		private Socket socket;
		private int id;

		public WorkThread(Socket socket, int id) {
			this.socket = socket;
			this.id = id;
		}
		
		public void start() {
			listThread = new ArrayList<WorkThread>();
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(PORT);
				System.out.println("Started:" + serverSocket);
				while (true) {
					Socket socket = serverSocket.accept();
					WorkThread newThread = new WorkThread(socket, listThread.size());
					newThread.start();
					System.out.println("New Thread Started.(" + listThread.size() + ")");
					listThread.add(newThread);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		@Override
		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				PrintStream ps = new PrintStream(socket.getOutputStream());
				boolean done = false;
				while (!done) {
					String receiveStr = br.readLine();
					if (receiveStr == null) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					System.out.println("Received from client " + id + ": " + receiveStr);
					String reverseStr = reverse(receiveStr);
					ps.println(reverseStr);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//reserve倒序
		private String reverse(String str) {
			int length = str.length();
			char[] result = new char[length];
			for (int i = 0; i < length; i++) {
				result[i] = str.charAt(length-i-1);
			}
			String resultStr = new String(result);
			return resultStr;
	
		}
		
	}

	
	public void start() {
		listThread = new ArrayList<WorkThread>();
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Started:" + serverSocket);
			while (true) {
				Socket socket = serverSocket.accept();
				WorkThread newThread = new WorkThread(socket, listThread.size());
				newThread.start();
				System.out.println("New Thread Started.(" + listThread.size() + ")");
				listThread.add(newThread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



}
