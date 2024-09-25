package entidades;

import java.awt.Color;

import lib.GameLib;

import entidades.enums.*;

// Classe que representa a forma (cor, tamanho e formato) de uma entidade
public class Forma {
	private double raio;
	private Color cor;
	private Formato formato;
	
	protected Forma(double raio, Color cor, Formato formato) {
		this.raio = raio;
		this.cor = cor;
		this.formato = formato;
	}
	
	// MANTER GETTERS E SETTERS?
	protected double getRaio() {
		return raio;
	}

	protected void setRaio(double raio) {
		this.raio = raio;
	}

	protected Color getCor() {
		return cor;
	}

	protected void setCor(Color cor) {
		this.cor = cor;
	}

	protected Formato getFormato() {
		return formato;
	}

	protected void setFormato(Formato formato) {
		this.formato = formato;
	}
	
	// Desenha na tela de acordo com a cor e o formato
	public void desenha(double x, double y) {
		GameLib.setColor(this.cor);
		
		if (this.formato == Formato.CIRCLE ) GameLib.drawCircle(x, y, this.raio);
		else if (this.formato == Formato.PLAYER) GameLib.drawPlayer(x, y, this.raio);
		else if (this.formato == Formato.DIAMOND) GameLib.drawDiamond(x, y, this.raio);
		else if (this.formato == Formato.LINE) {
			GameLib.drawLine(x, y - 5, x, y + 5);
			GameLib.drawLine(x - 1, y - 3, x - 1, y + 3);
			GameLib.drawLine(x + 1, y - 3, x + 1, y + 3);
		
	}
	}
}
