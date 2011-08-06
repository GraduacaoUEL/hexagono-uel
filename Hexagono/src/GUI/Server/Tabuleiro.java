/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Tabuleiro extends JPanel implements MouseListener
{
    Peca[][] pecas = new Peca[18][18];
    int jogador;
    Boolean marcada;
public Tabuleiro() {
        jogador = 1;//random

        CriaAsPecas();
        RetiraAsPecasInvalidas();
        ColocaAsPecasIniciais();
        LimpaNiveis();
        CriaPoligonos();

        //repaint();


    }
    private void CriaAsPecas()
    {
        for (int i = 0; i < 18; i++)
        {
            for (int j = 0; j < 18; j++)
            {
                this.pecas[i][j] = new Peca();

                this.pecas[i][j].setX(i);
                this.pecas[i][j].setY(j);
            }
        }
    }

    private void RetiraAsPecasInvalidas()
    {
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

    private void ColocaAsPecasIniciais()
    {
        this.pecas[1][5].setPlayer(1);
        this.pecas[17][5].setPlayer(1);
        this.pecas[9][17].setPlayer(1);

        this.pecas[1][13].setPlayer(2);
        this.pecas[17][13].setPlayer(2);
        this.pecas[9][1].setPlayer(2);
    }

    private void VerificaOPrimeiroNivel(int x, int y)
    {
        if (VerificaPosicao(x - 2, y + 1))
        {
            this.pecas[x - 2][y + 1].setNivel(1);
        }

        if (VerificaPosicao(x + 2, y + 1))
        {
            this.pecas[x + 2][y + 1].setNivel(1);
        }
        
        if (VerificaPosicao(x, y - 2))
        {
            this.pecas[x][y - 2].setNivel(1);
        }

        if (VerificaPosicao(x, y + 2))
        {
            this.pecas[x][y + 2].setNivel(1);
        }

        if (VerificaPosicao(x - 2, y - 1))
        {
            this.pecas[x - 2][y - 1].setNivel(1);
        }

        if (VerificaPosicao(x + 2, y - 1))
        {
            this.pecas[x + 2][y - 1].setNivel(1);
        }
    }

    private void VerificaEConverte(int x, int y)
    {
        if (ConvertePosicao(x - 2, y + 1))
        {
            this.pecas[x - 2][y + 1].setPlayer(jogador);
        }

        if (ConvertePosicao(x + 2, y + 1))
        {
            this.pecas[x +2][y + 1].setPlayer(jogador);
        }
        
        if (ConvertePosicao(x, y - 2))
        {
            this.pecas[x ][y - 2].setPlayer(jogador);
        }

        if (ConvertePosicao(x, y + 2))
        {
            this.pecas[x][y + 2].setPlayer(jogador);
        }

        if (ConvertePosicao(x - 2, y - 1))
        {
            this.pecas[x - 2][y - 1].setPlayer(jogador);
        }

        if (ConvertePosicao(x + 2, y - 1))
        {
            this.pecas[x + 2][y - 1].setPlayer(jogador);
        }
    }

    private Boolean ConvertePosicao(int x, int y)
    {
        if ((x >= 1) && (x <= 17))
        {
            if ((y >= 1) && (y <= 17))
            {
                if (pecas[x][y].getPlayer() != jogador && pecas[x][y].getPlayer() != 0)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void VerificaOSegundoNivel(int x, int y)
    {
        if (VerificaPosicao(x - 4, y - 2))
        {
            this.pecas[x - 4][y - 2].setNivel(2);
        }

        if (VerificaPosicao(x - 4, y))
        {
            this.pecas[x - 4][y].setNivel(2);
        }

        if (VerificaPosicao(x - 4, y + 2))
        {
            this.pecas[x - 4][y + 2].setNivel(2);
        }

        if (VerificaPosicao(x + 4, y - 2))
        {
            this.pecas[x + 4][y - 2].setNivel(2);
        }

        if (VerificaPosicao(x + 4, y))
        {
            this.pecas[x + 4][y].setNivel(2);
        }

        if (VerificaPosicao(x + 4, y + 2))
        {
            this.pecas[x + 4][y + 2].setNivel(2);
        }

        if (VerificaPosicao(x - 2, y - 3))
        {
            this.pecas[x - 2][y - 3].setNivel(2);
        }

        if (VerificaPosicao(x - 2, y + 3))
        {
            this.pecas[x - 2][y + 3].setNivel(2);
        }

        if (VerificaPosicao(x + 2, y - 3))
        {
            this.pecas[x + 2][y - 3].setNivel(2);
        }

        if (VerificaPosicao(x + 2, y + 3))
        {
            this.pecas[x + 2][y + 3].setNivel(2);
        }

        if (VerificaPosicao(x, y - 4))
        {
            this.pecas[x][y - 4].setNivel(2);
        }

        if (VerificaPosicao(x, y + 4))
        {
            this.pecas[x][y + 4].setNivel(2);
        }
    }

    Boolean VerificaPosicao(int x, int y)
    {
        if ((x >= 1) && (x <= 17))
        {
            if ((y >= 1) && (y <= 17))
            {
                if (pecas[x][y].getPlayer() == 0)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void LimpaNiveis()
    {
        for (int i = 0; i < 18; i++)
        {
            for (int j = 0; j < 18; j++)
            {
                this.pecas[i][j].setNivel(0);
            }
        }
    }

    public void CriaPoligonos() {
        int npoints = 6;
        int valorX = 30;
        int valorY = 21;
        Polygon p;

        for (int i = 1; i < 18; i++)
        {
            for (int j = 1; j < 18; j++)
            {
                int xpoints[] = {20 + i * valorX, 60 + i * valorX, 75 + i * valorX, 60 + i * valorX, 20 + i * valorX, 5 + i * valorX};
                int ypoints[] = {00 + j * valorY, 00 + j * valorY, 20 + j * valorY, 40 + j * valorY, 40 + j * valorY, 20 + j * valorY};
                p = new Polygon(xpoints, ypoints, npoints);

                if (pecas[i][j].getExiste() == true)
                {
                    if (i % 4 == 1 && j % 2 == 1)
                    {
                        pecas[i][j].setPolygon(p);
                        pecas[i][j].setDesenha(true);
                    }
                    else if (i % 4 == 3 && j % 2 == 0)
                    {
                        pecas[i][j].setPolygon(p);
                        pecas[i][j].setDesenha(true);
                    }
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (int i = 1; i < 18; i++)
        {
            for (int j = 1; j < 18; j++)
            {
                if (pecas[i][j].getDesenha() == true)
                {
                    {
                        g.setColor(pecas[i][j].getColor());
                        g.fillPolygon(pecas[i][j].getPolygon());
                        g.drawPolygon(pecas[i][j].getPolygon());
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        int marcada = 0;



        for (int i = 1; i < 18; i++) {
            for (int j = 1; j < 18; j++) {
                if (pecas[i][j].getDesenha() == true) {
                    if (pecas[i][j].Contains((e.getX() ), (e.getY()) )) {

                        if (pecas[i][j].getPlayer() == jogador) {
                            marcada++;

                            if (marcada >= 1) {
                                LimpaNiveis();
                            } else {
                                marcada = 0;
                            }

                            pecas[i][j].setNivel(4);
                            VerificaOPrimeiroNivel(i, j);
                            VerificaOSegundoNivel(i, j);
                        }

                        if (pecas[i][j].getNivel() == 1) {
                            pecas[i][j].setPlayer(jogador);
                            VerificaEConverte(i, j);
                            if (jogador == 1) {
                                jogador = 2;
                            } else {
                                jogador = 1;
                            }
                            LimpaNiveis();
                        }
                        if (pecas[i][j].getNivel() == 2) {

                            pecas[i][j].setPlayer(jogador);
                            VerificaEConverte(i, j);

                            if (jogador == 1) {
                                jogador = 2;
                            } else {
                                jogador = 1;
                            }
                            for (int l = 0; l < 18; l++) {
                                for (int k = 0; k < 18; k++) {
                                    if (pecas[l][k].getNivel() == 4) {
                                        pecas[l][k].setPlayer(0);
                                        LimpaNiveis();
                                    }
                                }
                            }
                        }
                        if (pecas[i][j].getNivel() == 0) {
                            LimpaNiveis();
                        }

                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("x" + e.getX() + "y" + e.getY());
    }

    public void mouseExited(MouseEvent e) {
        System.out.println("exited");
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("pressed");
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("released");
    }
}
