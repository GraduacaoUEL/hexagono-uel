package GUI.Cliente;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.nio.BufferOverflowException;
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
    private String ipDoServidor;
    private int portaDoServidor;
    private String nomeDoJogador;
    private Socket client;
    private String message = "";
    private JScrollPane scroll;

    public Chat(String ipDoServidor, int portaDoServidor, String nomeDoJogador)
    {
        setLayout(null);

        this.ipDoServidor = ipDoServidor;
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

    private void runClient()
    {
        try
        {
            conectaNoServidor();
            obtemDadosDeEntradaESaida();
            manipulaDadosNaJanela();
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

    private void conectaNoServidor() throws IOException
    {
        exibeMensagens("Tentando conectar...\n");

        client = new Socket(InetAddress.getByName(ipDoServidor), this.portaDoServidor);

        exibeMensagens("Conexão estabelecida com: " + client.getInetAddress().getHostName());
    }

    private void obtemDadosDeEntradaESaida() throws IOException
    {
        output = new PrintStream(client.getOutputStream());
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    private void manipulaDadosNaJanela() throws IOException
    {
        boolean tudoOk = true;
        setTextFieldEditable(true);

        do
        {
            try
            {
                message = (String)input.readLine();
                exibeMensagens("\n" + message);
            }
            catch(IOException e)
            {
                tudoOk = false;
                exibeMensagens("\nO tipo de mensagem recebida é desconhecido");
            }

        }while(tudoOk);
    }

    private void fechaConexao()
    {
        exibeMensagens("\nFechando a conexão");
        setTextFieldEditable(false);

        try
        {
            output.close();
            input.close();
            client.close();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    private void enviaDados(String message)
    {
        try
        {
            output.println(this.nomeDoJogador + ": " + message);
            output.flush();
            exibeMensagens("\n" + this.nomeDoJogador + ": " + message);
        }
        catch(BufferOverflowException e)
        {
            displayArea.append("\nErro ao enviar dados");
        }
    }

    private void exibeMensagens(final String messageToDisplay)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()
                    {
                        displayArea.append(messageToDisplay);
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
    public void run()
    {
        runClient();
    }
}