package entidades;
import java.awt.Color;
import lib.GameLib;

public class BarraVida {
	// Dimensões da barra de vida
	private Jogador jogador;
    private int largura;
    private int altura;
    
    // Posição da barra de vida na tela
    private int posX;
    private int posY;
    
    // Cores da barra de vida
    private Color corFundo;
    private Color corVida;

    public BarraVida(Jogador jogador, int largura, int altura, int posX, int posY, Color corFundo, Color corVida) {
        this.jogador = jogador;
        this.largura = largura;
        this.altura = altura;
        this.posX = posX;
        this.posY = posY;
        this.corFundo = corFundo;
        this.corVida = corVida;
    }

    // Método para desenhar a barra de vida na tela
    public void desenha() {
        // Desenha o fundo da barra de vida
        GameLib.setColor(this.corFundo);
        GameLib.fillRect(this.posX, this.posY, this.largura, this.altura);
        
        // Calcula o comprimento proporcional da barra de vida
        double comprimentoAtual = (double) this.largura * this.jogador.getVidaAtual() / this.jogador.getVidaMaxima();
        
        // Desenha a parte preenchida da barra de vida
        GameLib.setColor(this.corVida);
        GameLib.fillRect(this.posX, this.posY, (int) comprimentoAtual, this.altura);
    }
}

