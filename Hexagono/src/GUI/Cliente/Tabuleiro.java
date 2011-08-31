/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Cliente;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Graphics;

import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Tabuleiro extends JPanel implements Runnable {

    int tamanhho = 18;
    Peca[][] pecas = new Peca[18][18];
    int jogador;
    Boolean marcada;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String chatServer;
    private Socket client;
    private String message = "";
    private JLabel debug;

    
    public Tabuleiro() {
        jogador = 1;//random
        debug = new JLabel();
        debug.setText("Desconectado");
        debug.setBounds(0, 0, 100, 50);
        add(debug);
        
        
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

    Boolean VerificaPosicao(int x, int y) {
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

    public void CriaPoligonos() {
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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
    }

    public void MovePecaParaoSegundoNivelEConverte(int posicaoX, int posicaoY) {


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

    public void MovePecaParaoPrimeiroNivelEConverte(int posicaoX, int posicaoY) {


        pecas[posicaoX][posicaoY].setPlayer(jogador);
        VerificaEConverte(posicaoX, posicaoY);

        if (jogador == 1) {
            jogador = 2;
        } else {
            jogador = 1;
        }
        LimpaNiveis();
    }

    public int VerificaQuantidadeDePecasMarcadas(int marcada) {
        marcada++;
        if (marcada >= 1) {
            LimpaNiveis();
        } else {
            marcada = 0;
        }
        return marcada;

    }

    public class eventoMouse extends MouseAdapter {

        
        
        @Override
        public void mouseClicked(MouseEvent e) {
            
            
            
            int marcada = 0;
            for (int i = 1; i < 18; i++) {
                for (int j = 1; j < 18; j++) {
                    if (pecas[i][j].getDesenha() == true) {  //verifica se a peça existe
                        if (pecas[i][j].Contains((e.getX()), (e.getY()))) { //verifica se o clique esta dentro de um hexagono

                            if (pecas[i][j].getPlayer() == jogador) { //verifica se a peça é do jogador
                                marcada = VerificaQuantidadeDePecasMarcadas(marcada); //Desmarca caso clique em outra peça

                                pecas[i][j].setNivel(4);  //nivel 4 siginifica uma peca marcada

                                VerificaOPrimeiroNivel(i, j); //pinta o primeiro nivel da peca marcada
                                VerificaOSegundoNivel(i, j);  //pinta o segundo nivel da peca marca
                            }

                            if (pecas[i][j].getNivel() == 1) { //caso ele mova a peca marcada para o primeiro nivel
                                MovePecaParaoPrimeiroNivelEConverte(i, j);

                            }
                            if (pecas[i][j].getNivel() == 2) {//caso ele mova a peca marcada para o segundo nivel
                                MovePecaParaoSegundoNivelEConverte(i, j);

                            }
                            if (pecas[i][j].getNivel() == 0) { //caso clique em um local que naum tem peças
                                LimpaNiveis();
                            }

                        }
                    }
                }
            }
            repaint();
         //   enviaDados(e.getX() + ":" + e.getY());
         
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
        }
        catch(EOFException eofException)
        {
            JOptionPane.showMessageDialog(null, "Erro na conexão do lado cliente");
            //exibeMensagens("\nO Cliente encerrou a conexão");
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
        client = new Socket(InetAddress.getByName(chatServer), 12347);
        exibeMensagens("oi");
    }

    private void obtemDadosDeEntradaESaida() throws IOException
    {
        
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();

        input = new ObjectInputStream(client.getInputStream());
    }

    private void manipulaDadosNaJanela() throws IOException
    {
        do
        {
            try
            {
                message = (String)input.readObject();
            }
            catch(ClassNotFoundException classNotFoundException)
            {
                //exibeMensagens("\nUnknown object type received");
            }

        }while(!message.equals("exit"));
    }

    private void fechaConexao()
    {
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
            output.writeObject("CLIENT>>> " + message);
            output.flush(); //esvazia os dados para saída
            exibeMensagens("\nCLIENT>>> " + message);
        }
        catch(IOException ioException)
        {
            //displayArea.append("\nErro ao escrever");
        }
    }

    private void exibeMensagens(final String messageToDisplay)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()//atualiza a displayArea
                    {
                          debug.setText("Conectado");
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
