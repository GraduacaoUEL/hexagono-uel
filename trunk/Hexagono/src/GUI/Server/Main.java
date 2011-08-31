package GUI.Server;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main
{
    JPanel painel;

    Tabuleiro tabuleiro;
    Chat batePapo;

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
        JFrame janela = new JFrame("Hexagono");
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
                
        tabuleiro = new Tabuleiro();
        Thread tb = new Thread(tabuleiro);
        tb.start();
        
        batePapo = new Chat();
        Thread bp = new Thread(batePapo);
        bp.start();

       
 
        painel.setLayout(null);
        
        painel.add(tabuleiro);
        painel.add(batePapo);
        
        tabuleiro.setBounds(0, 0, 700, 400);
        batePapo.setBounds(50, 401, 700, 500);       
        
    } 
}