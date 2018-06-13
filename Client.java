import java.net.*;
import java.io.*;

public class Client extends Thread{
	Socket socket;
	ObjectInputStream input;

	public Client(Socket s){
		try{
			this.input = new ObjectInputStream(s.getInputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String args[]){
		try{
			Chat chat  = new Chat();
			Socket s = new Socket("localhost", 5555);
			String msg;
			ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Digite seu nome: ");
			String user = stdin.readLine();
			chat.username = user;
			System.out.println("Olá! Pedimos seu nome de usuário para que seus amigos possam identificar você :)");
			System.out.println("\t\t\t==========    INSTRUÇÕES    ========== \n");
			System.out.println("I - Caso queira enviar uma mensagem privada faça o seguinte:");
			System.out.println("Ex.: Olá amigo :)@@destinatário\n");
			System.out.println("Caso não adicione o destinatário a mensagem será enviada a todos os usuários");
			System.out.println("\nII - Caso queira visualizar os usuários online digite: @online");
			System.out.println("\nIII - Caso queira sair digite: @exit");
			System.out.println("\n\t\t\t\t      ==========");
			System.out.println("Mensagem >>");
			Thread thread = new Client(s);
			thread.start();
			while(true){
				msg = "";
				msg = stdin.readLine();
				System.out.println("Mensagem >>");
				String[] m = msg.trim().split("@@");
				if(m.length > 1){
					chat.text = "(private) "+m[0];
					chat.dest = m[1];
				}else{
					chat.text = m[0];
					chat.dest = "";
				}
				output.writeObject(chat);
				output.reset();
				if(m[0].equals("@exit")){
					System.exit(0);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public  void run(){
		try{
			Chat c;
			while(true){
				c = (Chat)this.input.readObject();
				if(c.text.equals("@online"))
					System.out.println("Online: "+c.setOnline);
				else if(c.text.equals("@exit"))
					System.exit(0);
				else if(c.text.equals("@equals")){
					System.out.println("Este nome já está em uso, tente novamente com outro nome...");
					System.exit(0);
				}
				System.out.println(c.username+" >> "+c.text);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}