package GUI.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Chat extends JPanel
{
    private JTextField entradaMensagem;
    private JTextArea mensagensEnviadasEREcebidas;
    private JScrollPane scrollMensagens;
    private JButton botaoEnviar;
    private String nomeDoJogador;

    public Chat(String nomeDoJogador)
    {
        this.nomeDoJogador = nomeDoJogador;
        setLayout(null);

        mensagensEnviadasEREcebidas = new JTextArea();
        mensagensEnviadasEREcebidas.setRows(10);
        mensagensEnviadasEREcebidas.setColumns(80);
        mensagensEnviadasEREcebidas.setEditable(true); //por enquanto
        mensagensEnviadasEREcebidas.setLineWrap(true);//Força quebra de linha
        mensagensEnviadasEREcebidas.setAutoscrolls(false);
        mensagensEnviadasEREcebidas.setBorder(null);
        mensagensEnviadasEREcebidas.setFocusable(false);

        scrollMensagens = new JScrollPane(mensagensEnviadasEREcebidas);
        scrollMensagens.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //A linha abaixo só faz sentido se o setLineWrap do mensagensEnvidadasERecebidas for true
        scrollMensagens.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMensagens.setBounds(0, 0, 500, 160);
        add(scrollMensagens);
        
        entradaMensagem = new JTextField();
        entradaMensagem.requestFocus();
        entradaMensagem.setBounds(0, 170, 300, 25);
        add(entradaMensagem);

        botaoEnviar = new JButton("Enviar");
        botaoEnviar.addActionListener(new cliqueBotaoEnviar());
        botaoEnviar.setBounds(310, 170, 100, 25);
        add(botaoEnviar);
    }

    public class cliqueBotaoEnviar implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            mensagensEnviadasEREcebidas.append(nomeDoJogador + " >>> " +entradaMensagem.getText() + "\n");
            entradaMensagem.setText("");
            entradaMensagem.requestFocus();
        }

    }



}
