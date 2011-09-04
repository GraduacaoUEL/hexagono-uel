package GUI.Server;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main
{
    private JPanel painel;
    private Tabuleiro tabuleiro;
    private Chat batePapo;
    
    private int portaDoServidor;
    private final String nomeDoJogador;

    
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(
            new Runnable() {

                @Override
                public void run() {
                    exibir();
                }
            }
        );
    }

    private static void exibir() 
    {
        JFrame janela = new JFrame("Servidor");
        janela.setSize(650, 650);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);


        
        Main hexagono = new Main();
        hexagono.painel.setOpaque(true);

        janela.setContentPane(hexagono.painel);

        janela.setVisible(true);
        
    }

    public Main() {
        painel = new JPanel();
        
        portaDoServidor =  Integer.parseInt(JOptionPane.showInputDialog("Informe a porta do servidor"));
        nomeDoJogador = JOptionPane.showInputDialog("Informe o nome do jogador");
                
        tabuleiro = new Tabuleiro(portaDoServidor, nomeDoJogador);
        Thread tb = new Thread(tabuleiro);
        tb.start();
        
        batePapo = new Chat(++portaDoServidor, nomeDoJogador);
        Thread bp = new Thread(batePapo);
        bp.start();
 
        painel.setLayout(null);
        painel.add(tabuleiro);
        painel.add(batePapo);
        
        tabuleiro.setBounds(0, 0, 700, 400);
        batePapo.setBounds(50, 401, 700, 500);       
        
    } 
}