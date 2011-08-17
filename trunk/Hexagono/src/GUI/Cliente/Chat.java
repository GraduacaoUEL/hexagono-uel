package GUI.Cliente;


// Server.java
//Configura uma classe Server que receberá uma conexão de um cliente, envia
// uma string ao cliente e fecha a conexão.
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class Chat extends JPanel implements Runnable
{
    private JTextField enterField; //insere a mensagem do usuário
    private JTextArea displayArea; //exibe informações para o usuário
    private ObjectOutputStream output; //gera fluxo de saída para o cliente
    private ObjectInputStream input; //gera fluxo de saída a partir do cliente
    private ServerSocket server; //socket de servidor
    private Socket connection; // conexão com o cliente
    private String chatServer; //servidor de host para esse aplicativo
    private int counter = 1; //contador de número de conexões
    private Socket client; //socket para comunicação com o servidor
    private String message = ""; //mensagem do servidor
    private JScrollPane scroll;

    //Configura a GUI
    public Chat(String host)
    {
        setLayout(null);

        chatServer = host; //configura o servidor ao qual esse cliente se conecta

        
        displayArea = new JTextArea(); //cria displayArea
        displayArea.setRows(10);
        displayArea.setColumns(80);
        displayArea.setLineWrap(true);
        
        scroll = new JScrollPane(displayArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(0, 0, 500, 160);
        
        add(scroll);
        
        enterField = new JTextField(); //cria enterField
        enterField.requestFocus();
        enterField.setEditable(false);
        enterField.setBounds(0, 170, 300, 25);
        
        enterField.addActionListener(
                new ActionListener()
                {
                    //envia a mensagem ao cliente
                    public void actionPerformed(ActionEvent event)
                    {
                        sendData(event.getActionCommand());
                        enterField.setText("");
                    }
                }
            );

            add(enterField);
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
            displayMessage("\nClient terminated connection");
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
        displayMessage("Attempting connection\n");

        //cria Socket para fazer a conexão ao servidor
        client = new Socket(InetAddress.getByName(chatServer), 12345);

        //exibe informações sobre a conexão
        displayMessage("Connected to: " +
                client.getInetAddress().getHostName());
    }

    //obtém fluxos para enviar e receber dados
    private void getStreams() throws IOException
    {
        //configura o fluxo de saída para objetos
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();//esvazia buffer de saída para enviar as informações de cabeçalho

        //configura o fluxo de entrada para objetos
        input = new ObjectInputStream(client.getInputStream());

        displayMessage("\nGot I/O streams\n");
    }

    //processa a conexão com o servidor
    private void processConnection() throws IOException
    {
        //ativa enterField de modo que o usuário cliente possa enviar mensagens
        setTextFieldEditable(true);

        do//processa as mensagens enviadas do servidor
        {
            try//lê e exibe a mensagem
            {
                message = (String)input.readObject(); //lê uma nova mensagem
                displayMessage("\n" + message); //exibe a mensagem
            }
            catch(ClassNotFoundException classNotFoundException)
            {
                displayMessage("\nUnknown object type received");
            }

        }while(!message.equals("SERVER>>> TERMINATE"));
    }

    //fecha os fluxos e o socket
    private void closeConnection()
    {
        displayMessage("\nClosing connection");
        setTextFieldEditable(false); //desativa enterField

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
            displayMessage("\nCLIENT>>> " + message);
        }
        catch(IOException ioException)
        {
            displayArea.append("\nError writing object");
        }
    }

    //manipula a displayArea na thread de despacho de eventos
    private void displayMessage(final String messageToDisplay)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()//atualiza a displayArea
                    {
                        displayArea.append(messageToDisplay);
                    }
                }
        );
    }

    //manipula o enterField na thread de despacho de eventos
    private void setTextFieldEditable(final boolean editable)
    {
       SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()//atualiza a displayArea
                    {
                        enterField.setEditable(editable);
                    }
                }
        );
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet.");
        runClient();
    }
}