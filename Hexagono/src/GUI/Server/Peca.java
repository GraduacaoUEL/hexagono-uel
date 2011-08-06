package GUI.Server;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;

public class Peca
{
    private Boolean existe;
    private Boolean desenha;
    private int player;
    private int nivel;
    private int x;
    private int y;
    private Polygon polygon;
    private Color color;

    public Peca()
    {
        this.existe = true;
        this.player = 0;
        this.nivel = 0;
        this.desenha=false;
    }

    public Boolean Contains(int mouseX, int mouseY)
    {
        if(polygon.contains(mouseX,mouseY))
        {
            return true;
        }
        else
        {
            return false;
        }
        
    }

    public Boolean getExiste()
    {
        return existe;
    }

    public void setExiste(Boolean existe)
    {
        this.existe = existe;
    }

    public int getPlayer()
    {
        return player;
    }

    public void setPlayer(int player)
    {
        this.player = player;
        Cor();
    }
    
    private void Cor(){
        if(this.nivel == 1)
        {
            this.color = Color.orange;
        }
        
        if(this.nivel == 2)
        {
            this.color = Color.RED;
        }
        
        if(this.nivel == 0)
        {
            this.color = Color.LIGHT_GRAY;
        }

        // if(this.player==0)
       //     this.color = Color.LIGHT_GRAY;
        
        if(this.player == 1)
        {
            this.color = Color.GREEN;
        }
        
        if(this.player == 2)
        {
            this.color = Color.BLUE;
        }
    }

    public int getNivel()
    {
        return nivel;
    }

    public void setNivel(int nivel)
    {
        this.nivel = nivel;
        Cor();   
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public Polygon getPolygon()
    {
        return polygon;
    }

    public void setPolygon(Polygon polygon)
    {
        this.polygon = polygon;
    }

    public Color getColor()
    {
        return color;
    }

    public Boolean getDesenha()
    {
        return desenha;
    }

    public void setDesenha(Boolean desenha)
    {
        this.desenha = desenha;
    }
}
