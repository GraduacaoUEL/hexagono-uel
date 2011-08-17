package GUI.Cliente;

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
    //Campo onde o usuário digita a mensagem que vai ser enviada
    private JTextField enterField;
    
    //Campo maior onde aparece o histórico de mensagens enviadas e recebidas
    private JTextArea displayArea;
    
    //Pacote com os dados prontos para sair
    private ObjectOutputStream output;
    
    //Pacote com os dados prontos para entrar
    private ObjectInputStream input;
   
    //Armazena o nome do servidor ao qual esse chat vai se conectar (ex: 127.0.0.1)
    private String chatServer;
    
    //Contém o socket da criado para a conexão com o servidor
    private Socket client;
    
    //Campo auxiliar para manipular mensagens do buffer e mensagens que vão para a tela
    private String message = "";
    
    //Scroll para a janela de mensagens
    private JScrollPane scroll;

    //Configura a GUI
    public Chat(String host)
    {
        setLayout(null);

        chatServer = host; //configura o servidor ao qual esse cliente se conecta

        
        //Compo onde vão aparecer mensagens enviadas e recebidas
        displayArea = new JTextArea();
        
        displayArea.setRows(10);
        displayArea.setColumns(80);
        
        //Força quebra de linha no displayArea
        displayArea.setLineWrap(true);
        
        
        scroll = new JScrollPane(displayArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        /*
         * Posição onde vai ficar o scrool, canto superior esquerdo fica em x = 0, y = 0.
         * No eixo x tem comprimento de 500 pixels e no eixo y tem altura 160 pixels.... 
         */
        scroll.setBounds(0, 0, 500, 160);
        
        add(scroll);
        
        enterField = new JTextField(); //cria enterField
        enterField.requestFocus();
        enterField.setEditable(false);
        enterField.setBounds(0, 170, 300, 25);
        
        //Responde a um enter informando que ser quer enviar a mensagem
        enterField.addActionListener(
                new ActionListener()
                {
                    //envia a mensagem ao cliente
                    public void actionPerformed(ActionEvent event)
                    {
                        enviaDados(event.getActionCommand());
                        enterField.setText("");
                    }
                }
            );

            add(enterField);
    }

    //conecta-se ao servidor e processa as mensagens a partir do servidor
    private void runClient()
    {
        try
        {
            conectaNoServidor();
            obtemDadosDeEntradaESaida();
            manipulaDadosNaJanela(); //processa a conexão
        }
        catch(EOFException eofException)
        {
            exibeMensagens("\nO Cliente encerrou a conexão");
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            fechaConexao();
        }
    }

    //conecta-se ao servidor
    private void conectaNoServidor() throws IOException
    {
        exibeMensagens("Attempting connection\n");

        //cria Socket para fazer a conexão ao servidor
        client = new Socket(InetAddress.getByName(chatServer), 12345);

        //Mostra mensagem no textArea de que a conexão foi feita com sucesso
        exibeMensagens("Conectado a " +
                client.getInetAddress().getHostName());
    }

    //obtém fluxos para enviar e receber dados
    private void obtemDadosDeEntradaESaida() throws IOException
    {
        //configura o fluxo de saída para objetos
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();//esvazia buffer de saída para enviar as informações de cabeçalho

        //configura o fluxo de entrada para objetos
        input = new ObjectInputStream(client.getInputStream());

        //exibeMensagens("\nGot I/O streams\n");
    }

    //processa a conexão com o servidor
    private void manipulaDadosNaJanela() throws IOException
    {
        //ativa enterField de modo que o usuário cliente possa enviar mensagens
        setTextFieldEditable(true);

        do//processa as mensagens enviadas do servidor
        {
            try//lê e exibe a mensagem
            {
                message = (String)input.readObject(); //lê uma nova mensagem
                exibeMensagens("\n" + message); //exibe a mensagem
            }
            catch(ClassNotFoundException classNotFoundException)
            {
                exibeMensagens("\nUnknown object type received");
            }

        }while(!message.equals("SERVER>>> exit"));
    }

    //fecha os fluxos e o socket
    private void fechaConexao()
    {
        exibeMensagens("\nClosing connection");
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
    private void enviaDados(String message)
    {
        try//envia o objeto ao servidor
        {
            output.writeObject("CLIENT>>> " + message);
            output.flush(); //esvazia os dados para saída
            exibeMensagens("\nCLIENT>>> " + message);
        }
        catch(IOException ioException)
        {
            displayArea.append("\nErro ao escrever");
        }
    }

    //manipula a displayArea na thread de despacho de eventos
    private void exibeMensagens(final String messageToDisplay)
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
    public void run()//Sem esse método a thread do Main não funciona
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        runClient();
    }
}