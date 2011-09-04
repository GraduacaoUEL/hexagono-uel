package GUI.Server;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.BufferUnderflowException;
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
    private int portaDoServidor;
    private String nomeDoJogador;

    public Chat(int portaDoServidor, String nomeDoJogador)
    {
        setLayout(null);

        this.portaDoServidor = portaDoServidor;
        this.nomeDoJogador = nomeDoJogador;
        
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
            server = new ServerSocket(this.portaDoServidor, 1);

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

    private void esperaPorConexao() throws IOException
    {
        exibeMensagens("Esperando por conexão...\n");
        connection = server.accept();
        exibeMensagens("Conexão estabelecida com: " + connection.getInetAddress().getHostName());
    }

    private void obtemDadosDeEntradaESaida() throws IOException
    {
        output = new PrintStream(connection.getOutputStream());
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private void manipulaDadosNaJanela() throws IOException
    {
        boolean tudoOk = true;
        String mensagem = "Conexão efetuada com sucesso...";
        enviaDados(mensagem);
        setTextFieldEditable(true);

        do
        {
            try
            {
                mensagem = (String) input.readLine();
                exibeMensagens("\n" + mensagem); //exibe a mensagem
            }
            catch(IOException e)
            {
                tudoOk = false;
                exibeMensagens("\nA mensagem recebida veio com problemas.");
            }

        }while(tudoOk);
    }

    private void fechaConexao()
    {
        exibeMensagens("\nEncerrando conexão\n");
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

    private void enviaDados(String mensagem)
    {
        try
        {
            output.println(this.nomeDoJogador + ": " + mensagem);
            output.flush();
            exibeMensagens("\n" + this.nomeDoJogador + ": " + mensagem);
        }
        catch(BufferUnderflowException o)
        {
            displayArea.append("\nErro ao enviar dados");
        }
    }

    private void exibeMensagens(final String mensagemToDisplay)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()
                    {
                        displayArea.append(mensagemToDisplay);
                    }
                }
          );
    }

    private void setTextFieldEditable(final boolean editable)
    {
        SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run()
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