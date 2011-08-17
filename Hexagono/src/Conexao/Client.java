package Conexao;

//Client.java
//Cliente que lê e exibe as informações enviadas a partir de um Servidor
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client
{
    private ObjectOutputStream output; //gera fluxo de saída para o servidor
    private ObjectInputStream input; //gera o fluxo de entrada a partir do servidor
    private String message = ""; //mensagem do servidor
    private String chatServer; //servidor de host para esse aplicativo
    private int chatPort;
    private Socket client; //socket para comunicação com o servidor

    //inicializa chatServer e configura a GUI
    public Client(String host, int port)
    {
        this.chatServer = host; //configura o servidor ao qual esse cliente se conecta
        this.chatPort  = port;
    }
    //conecta-se ao servidor e processa as mensagens a partir do servidor
    public void runClient()
    {
        try//conecta-se ao servidor, obtém fluxos, processa a conexão
        {
            connectToServer(); //cria um socket para fazer a conexão
            getStreams(); //obtém os fluxos de entrada e saída
            processConnection(); //processa a conexão
        }
        catch(EOFException eofException)
        {
            
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            closeConnection(); //fecha a conexão
        }
    }

    //conecta-se ao servidor
    private void connectToServer() throws IOException
    {

        //cria Socket para fazer a conexão ao servidor
        client = new Socket(InetAddress.getByName(chatServer), this.chatPort);

    }

    //obtém fluxos para enviar e receber dados
    private void getStreams() throws IOException
    {
        //configura o fluxo de saída para objetos
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();//esvazia buffer de saída para enviar as informações de cabeçalho

        //configura o fluxo de entrada para objetos
        input = new ObjectInputStream(client.getInputStream());

    }

    //processa a conexão com o servidor
    private void processConnection() throws IOException
    {

        do//processa as mensagens enviadas do servidor
        {
            try//lê e exibe a mensagem
            {
                message = (String)input.readObject(); //lê uma nova mensagem
                //displayMessage("\n" + message); //exibe a mensagem
            }
            catch(ClassNotFoundException classNotFoundException)
            {
                
            }

        }while(!message.equals("SERVER>>> TERMINATE"));
    }

    //fecha os fluxos e o socket
    private void closeConnection()
    {
        try
        {
            output.close();//fecha o fluxo de saída
            input.close();//fecha o fluxo de entrada
            client.close();//fecha o socket
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    //envia mensagem ao servidor
    private void sendData(String message)
    {
        try//envia o objeto ao servidor
        {
            output.writeObject("CLIENT>>> " + message);
            output.flush(); //esvazia os dados para saída
        }
        catch(IOException ioException)
        {
            
        }
    }

}