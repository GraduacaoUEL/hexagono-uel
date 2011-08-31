package GUI.Server;

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
    //Campo onde o usuário digita a mensagem que vai ser enviada
    private JTextField enterField;
    
    //Campo maior onde aparece o histórico de mensagens enviadas e recebidas
    private JTextArea displayArea;
    
    //Pacote com os dados prontos para sair
    private ObjectOutputStream output;
    
    //Pacote com os dados prontos para entrar
    private ObjectInputStream input;
    
    //Socket de servidor
    private ServerSocket server;
    
    //Conexão com o cliente
    private Socket connection;
    
    //Scroll para a janela onde aparecem as mensagens enviadas e recebidas.
    private JScrollPane scroll;

    //Configura a GUI
    public Chat()
    {
        setLayout(null);

        
        displayArea = new JTextArea(); //cria displayArea
        displayArea.setRows(10);
        displayArea.setColumns(80);
        displayArea.setLineWrap(true);//Força quebra de linha
        
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
                        enviaDados(event.getActionCommand());
                        enterField.setText("");
                    }
                }
            );

            add(enterField);
    }

    //configura e executa o servidor
    private void runServer()
    {
        try//configura o servidor para receber conexões; processa as conexões
        {
            server = new ServerSocket(12345, 1); //cria ServerSocket

            while(true)
            {
               try
               {
                   esperaPorConexao(); //espera uma conexão
                   obtemDadosDeEntradaESaida(); //obtém fluxos de entrada e saída
                   manipulaDadosNaJanela(); //processa a conexão
               }
               catch(EOFException eofException)
               {
                   exibeMensagens("\nServidor encerrou a conexão.");
               }
               finally
               {
                   fechaConexao();
               }
            }
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    //espera que a conexão chegue e então exibe informações sobre a conexão
    private void esperaPorConexao() throws IOException
    {
        exibeMensagens("Esperando por conexão...\n");
        
        //permite que o servidor aceite conexões
        connection = server.accept();
        
        exibeMensagens("Connection received from: " +
                connection.getInetAddress().getHostName());
    }

    //obtém fluxos para enviar e receber dados
    private void obtemDadosDeEntradaESaida() throws IOException
    {
        //configura fluxo de sáida para objetos
        output = new ObjectOutputStream(connection.getOutputStream());
        
        //esvaziar buffer de saída para enviar as informações de cabeçalho
        output.flush();

        //configura o fluxo de entrada para objetos
        input = new ObjectInputStream(connection.getInputStream());

        //exibeMensagens("\nGot I/O streams\n");
    }

    //processa a conexão com o cliente
    private void manipulaDadosNaJanela() throws IOException
    {
        String mensagem = "Conexão efetuada com sucesso...";
        enviaDados(mensagem); //envia uma mensagem de conexão bem-sucedida

        //ativa enterField de modo que o usuário do servidor possa enviar mensagens
        setTextFieldEditable(true);

        do//processa as mensagens enviadas pelo cliente
        {
            try//lê e exibe a mensagem
            {
                mensagem = (String) input.readObject();
                exibeMensagens("\n" + mensagem); //exibe a mensagem
            }
            catch(ClassNotFoundException classNotFoundException)
            {
                exibeMensagens("\nA mensagem recebida veio com problemas.");
            }

        }while(!mensagem.equals("CLIENT>>> exit"));
    }

    //fecha os fluxos e o socket
    private void fechaConexao()
    {
        exibeMensagens("\nEncerrando conexão\n");
        
        //Desabilita enterField
        setTextFieldEditable(false);

        try
        {
            output.close();
            input.close();
            connection.close();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    //envia a mensagem ao cliente
    private void enviaDados(String mensagem)
    {
        try//envia o objeto ao cliente
        {
            output.writeObject("SERVER>>> " + mensagem);
            output.flush();//esvazia a saída para o cliente
            exibeMensagens("\nSERVER>>> " + mensagem);
        }
        catch(IOException iOException)
        {
            displayArea.append("\nErro ao escrever mensagem...");
        }
    }

    //Manipula a displayArea na thread de despacho de eventos
    private void exibeMensagens(final String mensagemToDisplay)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()//atualiza a displayArea
                    {
                        displayArea.append(mensagemToDisplay);
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
        runServer();
    }
}