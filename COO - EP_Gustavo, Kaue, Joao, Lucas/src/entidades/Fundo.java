package entidades;

import java.util.ArrayList;
import java.awt.Color;

import lib.GameLib;

// Classe do fundo do jogo
public class Fundo {
	private ArrayList<Double> estrelasX;
	private ArrayList<Double> estrelasY;
	
	private double velocidade;
	private double contagem;
	private Color cor;
	
	public Fundo(int quantidadeEstrelas, double velocidadeFundo, Color corFundo) {
		this.estrelasX = new ArrayList<Double>();
		this.estrelasY = new ArrayList<Double>();
		
		// As posições das estrelas (cuja quantidade é recebida pelo método) são definidas aleatoriamente de acordo com o tamanho da tela
		for (int i = 0; i < quantidadeEstrelas; i++) {
			estrelasX.add(Math.random()*GameLib.WIDTH);
			estrelasY.add(Math.random()*GameLib.HEIGHT);
		}
		
		this.velocidade = velocidadeFundo;
		this.contagem = 0;
		this.cor = corFundo;
	}
	
	// O método para desenhar o fundo seleciona a cor, atualiza o valor de contagem e desenha a partir da GameLib
	public void desenha(long delta) {
		GameLib.setColor(cor);
		this.contagem += this.velocidade * delta;
		
		for (int i = 0; i < this.estrelasX.size(); i++) {
			GameLib.fillRect(this.estrelasX.get(i), (this.estrelasY.get(i) + this.contagem) % GameLib.HEIGHT, 2, 2);
		}
	}
}