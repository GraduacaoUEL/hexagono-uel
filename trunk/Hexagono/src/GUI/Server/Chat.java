package GUI.Server;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import javax.print.PrintException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class Chat extends JPanel implements Runnable
{
    private JTextField enterField;
    private JTextArea displayArea;
    private PrintStream output;
    private BufferedReader input;
    private ServerSocket server;
    private Socket connection;
    private JScrollPane scroll;

    public Chat()
    {
        setLayout(null);

        
        displayArea = new JTextArea();
        displayArea.setRows(10);
        displayArea.setColumns(80);
        displayArea.setLineWrap(true);
        
        scroll = new JScrollPane(displayArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(0, 0, 500, 160);
        
        add(scroll);
        
        enterField = new JTextField();
        enterField.requestFocus();
        enterField.setEditable(false);
        enterField.setBounds(0, 170, 300, 25);
        
        enterField.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        enviaDados(event.getActionCommand());
                        enterField.setText("");
                    }
                }
            );

            add(enterField);
    }

    private void runServer()
    {
        try
        {
            server = new ServerSocket(12345, 1);

            while(true)
            {
               try
               {
                   esperaPorConexao();
                   obtemDadosDeEntradaESaida();
                   manipulaDadosNaJanela();
               }
               catch(EOFException eofException)
               {
                   exibeMensagens("\nServidor encerrou a conexão.");
                   System.exit(0);
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
        
        connection = server.accept();
        
        exibeMensagens("Connection received from: " +
                connection.getInetAddress().getHostName());
    }

    private void obtemDadosDeEntradaESaida() throws IOException
    {
        output = new PrintStream(connection.getOutputStream());
        
        //output.flush();

        //configura o fluxo de entrada para objetos
        
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

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
                mensagem = (String) input.readLine();
                exibeMensagens("\n" + mensagem); //exibe a mensagem
            }
            catch(IOException e)
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
            output.println(mensagem);
            output.flush();//esvazia a saída para o cliente
            exibeMensagens("\nSERVER>>> " + mensagem);
        }
        catch(BufferUnderflowException o)
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