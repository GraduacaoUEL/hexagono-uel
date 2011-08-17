package GUI.Server;
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
    private int counter = 1; //contador de número de conexões
    private JScrollPane scroll;

    //Configura a GUI
    public Chat()
    {
        setLayout(null);

        
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

    //configura e executa o servidor
    public void runServer()
    {
        try//configura o servidor para receber conexões; processa as conexões
        {
            server = new ServerSocket(12345, 1); //cria ServerSocket

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
                   displayMessage("\nServer terminated connection");
               }
               finally
               {
                   closeConnection(); //fecha a conexão
                   counter++;
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
        displayMessage("Waiting for connection\n");
        connection = server.accept();//permite que o servidor aceite conexões
        displayMessage("Connection " + counter + " received from: " +
                connection.getInetAddress().getHostName());
    }

    //obtém fluxos para enviar e receber dados
    private void getStreams() throws IOException
    {
        //configura fluxo de sáida para objetos
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();//esvaziar buffer de saída para enviar as informações de cabeçalho

        //configura o fluxo de entrada para objetos
        input = new ObjectInputStream(connection.getInputStream());

        displayMessage("\nGot I/O streams\n");
    }

    //processa a conexão com o cliente
    private void processConnection() throws IOException
    {
        String message = "Connection successful";
        sendData(message); //envia uma mensagem de conexão bem-sucedida

        //ativa enterField de modo que o usuário do servidor possa enviar mensagens
        setTextFieldEditable(true);

        do//processa as mensagens enviadas pelo cliente
        {
            try//lê e exibe a mensagem
            {
                message = (String) input.readObject();//lê uma nova mensagem
                displayMessage("\n" + message); //exibe a mensagem
            }
            catch(ClassNotFoundException classNotFoundException)
            {
                displayMessage("\nUnknown object type received");
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
            displayArea.append("\nError writing object");
        }
    }

    //Manipula a displayArea na thread de despacho de eventos
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
                public void run() //configura a editabilidade do enterField
                {
                    enterField.setEditable(editable);
                }
            }
        );
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet.");
        runServer();
    }
}


