package GUI.Cliente;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main
{
    JPanel painel;
    Tabuleiro tabuleiro;
    Chat batePapo;

    private String ipDoServidor;
    private int portaDoServidor;
    private String nomeDoJogador;
    
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
        JFrame janela = new JFrame("Cliente");
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
                
        ipDoServidor = JOptionPane.showInputDialog("Informe o ip do servidor");
        portaDoServidor =  Integer.parseInt(JOptionPane.showInputDialog("Informe a porta do servidor"));
        nomeDoJogador = JOptionPane.showInputDialog("Informe o nome do jogador");
        
        tabuleiro = new Tabuleiro(ipDoServidor, portaDoServidor, nomeDoJogador);

        Thread tb = new Thread(tabuleiro);
        tb.start();

        
        batePapo = new Chat(ipDoServidor, ++portaDoServidor, nomeDoJogador);
        Thread bp = new Thread(batePapo);
        bp.start();
        
        painel.setLayout(null);
        
        painel.add(tabuleiro);
        painel.add(batePapo);
        
        tabuleiro.setBounds(0, 0, 700, 400);
        batePapo.setBounds(50, 401, 700, 500);       
    } 
}