package GUI.Cliente;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.BufferOverflowException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Tabuleiro extends JPanel implements Runnable {

    private Peca[][] pecas = new Peca[18][18];
    private int jogador;
    private PrintStream output;
    private BufferedReader input;
    private Socket client;
    private JLabel statusDaConexao;
    private int pecasJogador1 = 0;
    private int pecasJogador2 = 0;
    private String ipDoServidor;
    private int portaDoServidor;
    private String nomeDoJogador;
    private String nomeDoAdversario;

    public Tabuleiro(String ipDoServidor, int portaDoServidor, String nomeDoJogador)
    {

        this.ipDoServidor = ipDoServidor;
        this.portaDoServidor = portaDoServidor;
        this.nomeDoJogador = nomeDoJogador;
        
        jogador = 1;//random
        statusDaConexao = new JLabel();
        statusDaConexao.setText("Tentando conectar");
        statusDaConexao.setBounds(0, 0, 100, 50);
        add(statusDaConexao);


        desenhaTabuleiro();
        eventoMouse evento = new eventoMouse();
        addMouseListener(evento);
    }

    private void desenhaTabuleiro() {
        CriaAsPecas();
        RetiraAsPecasInvalidas();

        ColocaAsPecasIniciais();
        LimpaNiveis();
        CriaPoligonos();

    }

    private void CriaAsPecas() {
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                this.pecas[i][j] = new Peca();

                this.pecas[i][j].setX(i);
                this.pecas[i][j].setY(j);
            }
        }
    }

    private void RetiraAsPecasInvalidas() {
        this.pecas[1][1].setExiste(false);
        this.pecas[1][3].setExiste(false);
        this.pecas[1][15].setExiste(false);
        this.pecas[1][17].setExiste(false);
        this.pecas[3][2].setExiste(false);
        this.pecas[3][16].setExiste(false);
        this.pecas[5][1].setExiste(false);
        this.pecas[5][17].setExiste(false);
        this.pecas[7][10].setExiste(false);
        this.pecas[9][7].setExiste(false);
        this.pecas[11][10].setExiste(false);
        this.pecas[13][1].setExiste(false);
        this.pecas[13][17].setExiste(false);
        this.pecas[15][2].setExiste(false);
        this.pecas[15][16].setExiste(false);
        this.pecas[17][1].setExiste(false);
        this.pecas[17][3].setExiste(false);
        this.pecas[17][15].setExiste(false);
        this.pecas[17][17].setExiste(false);
    }

    private void ColocaAsPecasIniciais() {
        this.pecas[1][5].setPlayer(1);
        this.pecas[17][5].setPlayer(1);
        this.pecas[9][17].setPlayer(1);

        this.pecas[1][13].setPlayer(2);
        this.pecas[17][13].setPlayer(2);
        this.pecas[9][1].setPlayer(2);
    }

    private void VerificaOPrimeiroNivel(int x, int y) {
        if (VerificaPosicao(x - 2, y + 1)) {
            this.pecas[x - 2][y + 1].setNivel(1);
        }

        if (VerificaPosicao(x + 2, y + 1)) {
            this.pecas[x + 2][y + 1].setNivel(1);
        }

        if (VerificaPosicao(x, y - 2)) {
            this.pecas[x][y - 2].setNivel(1);
        }

        if (VerificaPosicao(x, y + 2)) {
            this.pecas[x][y + 2].setNivel(1);
        }

        if (VerificaPosicao(x - 2, y - 1)) {
            this.pecas[x - 2][y - 1].setNivel(1);
        }

        if (VerificaPosicao(x + 2, y - 1)) {
            this.pecas[x + 2][y - 1].setNivel(1);
        }
    }

    private void VerificaEConverte(int x, int y) {
        if (ConvertePosicao(x - 2, y + 1)) {
            this.pecas[x - 2][y + 1].setPlayer(jogador);
        }

        if (ConvertePosicao(x + 2, y + 1)) {
            this.pecas[x + 2][y + 1].setPlayer(jogador);
        }

        if (ConvertePosicao(x, y - 2)) {
            this.pecas[x][y - 2].setPlayer(jogador);
        }

        if (ConvertePosicao(x, y + 2)) {
            this.pecas[x][y + 2].setPlayer(jogador);
        }

        if (ConvertePosicao(x - 2, y - 1)) {
            this.pecas[x - 2][y - 1].setPlayer(jogador);
        }

        if (ConvertePosicao(x + 2, y - 1)) {
            this.pecas[x + 2][y - 1].setPlayer(jogador);
        }
    }

    private Boolean ConvertePosicao(int x, int y) {
        if ((x >= 1) && (x <= 17)) {
            if ((y >= 1) && (y <= 17)) {
                if (pecas[x][y].getPlayer() != jogador && pecas[x][y].getPlayer() != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void VerificaOSegundoNivel(int x, int y) {
        if (VerificaPosicao(x - 4, y - 2)) {
            this.pecas[x - 4][y - 2].setNivel(2);
        }

        if (VerificaPosicao(x - 4, y)) {
            this.pecas[x - 4][y].setNivel(2);
        }

        if (VerificaPosicao(x - 4, y + 2)) {
            this.pecas[x - 4][y + 2].setNivel(2);
        }

        if (VerificaPosicao(x + 4, y - 2)) {
            this.pecas[x + 4][y - 2].setNivel(2);
        }

        if (VerificaPosicao(x + 4, y)) {
            this.pecas[x + 4][y].setNivel(2);
        }

        if (VerificaPosicao(x + 4, y + 2)) {
            this.pecas[x + 4][y + 2].setNivel(2);
        }

        if (VerificaPosicao(x - 2, y - 3)) {
            this.pecas[x - 2][y - 3].setNivel(2);
        }

        if (VerificaPosicao(x - 2, y + 3)) {
            this.pecas[x - 2][y + 3].setNivel(2);
        }

        if (VerificaPosicao(x + 2, y - 3)) {
            this.pecas[x + 2][y - 3].setNivel(2);
        }

        if (VerificaPosicao(x + 2, y + 3)) {
            this.pecas[x + 2][y + 3].setNivel(2);
        }

        if (VerificaPosicao(x, y - 4)) {
            this.pecas[x][y - 4].setNivel(2);
        }

        if (VerificaPosicao(x, y + 4)) {
            this.pecas[x][y + 4].setNivel(2);
        }
    }

    private Boolean VerificaPosicao(int x, int y) {
        if ((x >= 1) && (x <= 17)) {
            if ((y >= 1) && (y <= 17)) {
                if (pecas[x][y].getPlayer() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void LimpaNiveis() {
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                this.pecas[i][j].setNivel(0);
            }
        }
    }

    private void ContaPecas() {
        pecasJogador1 = 0;
        pecasJogador2 = 0;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                if (pecas[i][j].getPlayer() == 1 && pecas[i][j].getDesenha() == true) {
                    pecasJogador1++;
                }
                if (pecas[i][j].getPlayer() == 2 && pecas[i][j].getDesenha() == true) {
                    pecasJogador2++;
                }
            }
        }

    }

    private void CriaPoligonos() {
        int npoints = 6;
        int valorX = 30;
        int valorY = 21;
        Polygon p;

        for (int i = 1; i < 18; i++) {
            for (int j = 1; j < 18; j++) {
                int xpoints[] = {20 + i * valorX, 60 + i * valorX, 75 + i * valorX, 60 + i * valorX, 20 + i * valorX, 5 + i * valorX};
                int ypoints[] = {00 + j * valorY, 00 + j * valorY, 20 + j * valorY, 40 + j * valorY, 40 + j * valorY, 20 + j * valorY};
                p = new Polygon(xpoints, ypoints, npoints);

                if (pecas[i][j].getExiste() == true) {
                    if (i % 4 == 1 && j % 2 == 1) {
                        pecas[i][j].setPolygon(p);
                        pecas[i][j].setDesenha(true);
                    } else if (i % 4 == 3 && j % 2 == 0) {
                        pecas[i][j].setPolygon(p);
                        pecas[i][j].setDesenha(true);
                    }
                }
            }
        }
    }

    private boolean VerificaSeGanhou(int player) {
        LimpaNiveis();
        for (int i = 1; i < 18; i++) {
            for (int j = 1; j < 18; j++) {
                if (pecas[i][j].getPlayer() == player) {
                    if (pecas[i][j].getDesenha()) {
                        VerificaOPrimeiroNivel(i, j);
                        VerificaOSegundoNivel(i, j);
                    }
                }
            }
        }
        boolean retorno = true;
        for (int i = 1; i < 18; i++) {
            for (int j = 1; j < 18; j++) {
                if (pecas[i][j].getDesenha()) {
                    if ((pecas[i][j].getNivel() == 1 || pecas[i][j].getNivel() == 2) && pecas[i][j].getPlayer() == 0) {
                        retorno = false;
                    }
                }
            }
        }
        LimpaNiveis();
        return retorno;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        String pecasDoJogador1 = "" + pecasJogador1;
        String pecasDoJogador2 = "" + pecasJogador2;


        for (int i = 1; i < 18; i++) {
            for (int j = 1; j < 18; j++) {
                if (pecas[i][j].getDesenha() == true) {
                    {
                        g.setColor(pecas[i][j].getColor());
                        g.fillPolygon(pecas[i][j].getPolygon());
                        g.drawPolygon(pecas[i][j].getPolygon());
                    }
                }
            }
        }
        g.setColor(Color.GREEN);
        g.drawString(this.nomeDoJogador + " =  " + pecasDoJogador1, 10, 20);
        g.setColor(Color.BLUE);
        g.drawString(this.nomeDoAdversario + " =  " + pecasDoJogador2, 10, 40);


    }

    private void MovePecaParaoSegundoNivelEConverte(int posicaoX, int posicaoY) {


        pecas[posicaoX][posicaoY].setPlayer(jogador);
        VerificaEConverte(posicaoX, posicaoY);

        if (jogador == 1) {
            jogador = 2;
        } else {
            jogador = 1;
        }

        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                if (pecas[i][j].getNivel() == 4) {
                    pecas[i][j].setPlayer(0);
                    LimpaNiveis();
                }
            }
        }

    }

    private void MovePecaParaoPrimeiroNivelEConverte(int posicaoX, int posicaoY) {


        pecas[posicaoX][posicaoY].setPlayer(jogador);
        VerificaEConverte(posicaoX, posicaoY);

        if (jogador == 1) {
            jogador = 2;
        } else {
            jogador = 1;
        }
        LimpaNiveis();
    }

    private int VerificaQuantidadeDePecasMarcadas(int marcada) {
        marcada++;
        if (marcada >= 1) {
            LimpaNiveis();
        } else {
            marcada = 0;
        }
        return marcada;

    }

    private void movePeca(int x, int y) {

        int marcada = 0;
        for (int i = 1; i < 18; i++) {
            for (int j = 1; j < 18; j++) {
                if (pecas[i][j].getDesenha() == true) {  //verifica se a peça existe
                    if (pecas[i][j].Contains(x, y)) { //verifica se o clique esta dentro de um hexagono

                        if (pecas[i][j].getPlayer() == jogador) { //verifica se a peça é do jogador
                            marcada = VerificaQuantidadeDePecasMarcadas(marcada); //Desmarca caso clique em outra peça

                            pecas[i][j].setNivel(4);  //nivel 4 siginifica uma peca marcada

                            VerificaOPrimeiroNivel(i, j); //pinta o primeiro nivel da peca marcada
                            VerificaOSegundoNivel(i, j);  //pinta o segundo nivel da peca marca
                        }

                        if (pecas[i][j].getNivel() == 1) { //caso ele mova a peca marcada para o primeiro nivel
                            MovePecaParaoPrimeiroNivelEConverte(i, j);
                            if (VerificaSeGanhou(jogador)) {
                                if (jogador == 1) {
                                    JOptionPane.showMessageDialog(null, nomeDoAdversario + " ganhou");
                                } else {
                                    JOptionPane.showMessageDialog(null, nomeDoJogador + " ganhou");
                                }
                            }
                        }
                        if (pecas[i][j].getNivel() == 2) {//caso ele mova a peca marcada para o segundo nivel
                            MovePecaParaoSegundoNivelEConverte(i, j);
                            if (VerificaSeGanhou(jogador)) {
                                int imprimejogador;
                                if (jogador == 1) {
                                    JOptionPane.showMessageDialog(null, nomeDoAdversario + " ganhou");
                                } else {
                                    JOptionPane.showMessageDialog(null, nomeDoJogador + " ganhou");
                                }
                            }

                        }
                        if (pecas[i][j].getNivel() == 0) { //caso clique em um local que naum tem peças
                            LimpaNiveis();


                        }

                    }
                }
            }
        }
        ContaPecas();
    }

    public class eventoMouse extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {


            if (jogador == 1) {
                movePeca(e.getX(), e.getY());

            }
            enviaDados(nomeDoJogador + ":" + e.getX() + ":" + e.getY());

            repaint();

        }
    }
//conecta-se ao servidor e processa as mensagens a partir do servidor

    private void runClient() 
    {
        try
        {
            conectaNoServidor();
            obtemDadosDeEntradaESaida();
            manipulaDadosNaJanela();
        } catch (EOFException eofException) 
        {
            exibeMensagens("Desconectado");
            JOptionPane.showMessageDialog(null, "Erro na conexão");   
        }
        catch (IOException ioException) 
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
        client = new Socket(InetAddress.getByName(this.ipDoServidor), this.portaDoServidor);
        exibeMensagens("Conectado");
    }

    private void obtemDadosDeEntradaESaida() throws IOException 
    {
        output = new PrintStream(client.getOutputStream());
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    private void manipulaDadosNaJanela() throws IOException 
    {
        boolean tudoOk = true;
        do
        {
            try 
            {
                String coordenadas;
                coordenadas = (String) input.readLine();
                String[] TodasCoordenadas = coordenadas.split(":");
                this.nomeDoAdversario = TodasCoordenadas[0];
                int x = Integer.parseInt(TodasCoordenadas[1]);
                int y = Integer.parseInt(TodasCoordenadas[2]);

                if (jogador == 2) 
                {
                    movePeca(x, y);
                    repaint();
                }
            } 
            catch (IOException i) 
            {
                tudoOk = false;
                JOptionPane.showMessageDialog(null, "O tipo de dado recebido é desconhecido");
            }

        } while (tudoOk);
    }

    private void fechaConexao() 
    {
        try
        {
            output.close();
            input.close();
            client.close();
        } 
        catch (IOException ioException) 
        {
            ioException.printStackTrace();
        }
    }

    private void enviaDados(String message) 
    {
        try
        {
            output.println(message);
            output.flush();
        } 
        catch (BufferOverflowException ioException) 
        {
            JOptionPane.showMessageDialog(null, "Erro ao enviar dados");
        }
    }

    private void exibeMensagens(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {

                    public void run()//atualiza a displayArea
                    {
                        statusDaConexao.setText(messageToDisplay);
                    }
                });
    }

    @Override
    public void run()//Sem esse método a thread do Main não funciona
    {
        runClient();
    }
}