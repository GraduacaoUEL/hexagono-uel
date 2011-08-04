package Conexao;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;


public class Server
{
    private ObjectOutputStream output; //gera fluxo de saída para o cliente
    private ObjectInputStream input; //gera fluxo de saída a partir do cliente
    private ServerSocket server; //socket de servidor
    private Socket connection; // conexão com o cliente
    private int porta;
    
    public Server(int porta)
    {
        this.porta = porta;
    }
    //configura e executa o servidor
    public void runServer()
    {
        try//configura o servidor para receber conexões; processa as conexões
        {
            server = new ServerSocket(this.porta, 2); //cria ServerSocket

            while(true)
            {
               try
               {
                   waitForConnection(); //espera uma conexão
                   getStreams(); //obtém fluxos de entrada e saída
                   processConnection(); //processa a conexão
               }
               catch(EOFException eofException)
               {
                   
               }
               finally
               {
                   closeConnection(); //fecha a conexão
               }
            }
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    //espera que a conexão chegue e então exibe informações sobre a conexão
    private void waitForConnection() throws IOException
    {
        connection = server.accept();//permite que o servidor aceite conexões
        
       // JOptionPane.showMessageDialog("Connection received from: " + connection.getInetAddress().getHostName(), "Status da conexão");
    }

    //obtém fluxos para enviar e receber dados
    private void getStreams() throws IOException
    {
        //configura fluxo de sáida para objetos
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();//esvaziar buffer de saída para enviar as informações de cabeçalho

        //configura o fluxo de entrada para objetos
        input = new ObjectInputStream(connection.getInputStream());
    }

    //processa a conexão com o cliente
    private void processConnection() throws IOException
    {
        String message = "Connection successful";
        sendData(message); //envia uma mensagem de conexão bem-sucedida

        do//processa as mensagens enviadas pelo cliente
        {
            try//lê e exibe a mensagem
            {
                message = (String) input.readObject();//lê uma nova mensagem
            }
            catch(ClassNotFoundException classNotFoundException)
            {
                
            }

        }while(!message.equals("CLIENT>>> TERMINATE"));
    }

    //fecha os fluxos e o socket
    private void closeConnection()
    {
        displayMessage("\nTerminating connection\n");
        setTextFieldEditable(false); //desativa enterField

        try
        {
            output.close();//fecha o fluxo de saída
            input.close(); //fecha o fluxo de entrada
            connection.close();//fecha o socket
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    //envia a mensagem ao cliente
    private void sendData(String message)
    {
        try//envia o objeto ao cliente
        {
            output.writeObject("SERVER>>> " + message);
            output.flush();//esvazia a saída para o cliente
            displayMessage("\nSERVER>>> " + message);
        }
        catch(IOException iOException)
        {
            
        }
    }

}