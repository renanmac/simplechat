import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread{
	static Map<String, ObjectOutputStream> online;
	Socket connection;
	String client;
	static Set<String> clients = new HashSet<String>();

	public Server(Socket s){
		this.connection = s;
	}

	public boolean addClient(String name){
		return clients.add(name);
	}

	public boolean removeClient(String name){
		return clients.remove(name);
	}

	public static void main(String args[]){
		online = new HashMap<String, ObjectOutputStream>();
		try{
			ServerSocket serversocket= new ServerSocket(5555);
			System.out.println("O Server rodando na porta 5555!");
			while(true){
				Socket s = serversocket.accept();
				Thread thread = new Thread(new Server(s));
				thread.start();
			} 
		}catch(Exception e ){
			e.printStackTrace();
		}
	}

	public void run(){
		try{
			Chat chat = new Chat();
			ObjectOutputStream output = new ObjectOutputStream(this.connection.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(this.connection.getInputStream());
			chat = (Chat)input.readObject();
			if(!addClient(chat.username)){
				System.out.println("Este nome já existe!");
				chat.text = "@equals";
				output.writeObject(chat);
				output.reset();
				return;
			}else{
				System.out.println(chat.username+" conectado ao servidor!");
				chat.setOnline = clients;
				System.out.println("online: "+chat.setOnline);
				online.put(chat.username,output);
			}
			
			while(chat.text != null && !(chat.text.equals(""))){
				if(chat.text.equals("@online")){
					chat.setOnline = clients;
					send(chat, chat.username);
				}else if(chat.text.equals("@exit")){
					send(chat, chat.username);
					removeClient(chat.username);
					online.remove(chat.username);
					chat.text = chat.username+" saiu!";
					sendAll(chat, online);
					return;
				}else{
					if(chat.dest.equals("")){
						sendAll(chat, online);
					}else{
						send(chat, chat.dest);
					}
				}
				chat = (Chat)input.readObject();
			}
		}catch(Exception e){
			//Exception
		}
	}
	public void send(Chat chat, String name){
		try{
			ObjectOutputStream output = online.get(name);
			output.writeObject(chat);
			output.reset();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void sendAll(Chat chat, Map<String, ObjectOutputStream> on){
		for(Map.Entry<String, ObjectOutputStream> c : on.entrySet()){
			ObjectOutputStream out = c.getValue();
			if(!chat.username.equals(c.getKey())){
				try{
					out.writeObject(chat);
					out.reset();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}