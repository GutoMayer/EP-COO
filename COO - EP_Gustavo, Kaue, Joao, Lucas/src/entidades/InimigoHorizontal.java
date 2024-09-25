package entidades;

import java.awt.Color;
import java.util.ArrayList;

import entidades.enums.Formato;
import lib.GameLib;

public class InimigoHorizontal extends InimigoSimples {
	public static long proxInimigo;
	private boolean colisaoBordaEsquerda = false; // Marca se o inimigo colidiu com a borda esquerda da tela para realizar a movimentação
	private boolean colisaoBordaDireita = false; // Marca se o inimigo colidiu com a borda direita da tela para realizar a movimentação
	
	public InimigoHorizontal(long tempoAtual) {
		super(Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.20, 3 * Math.PI / 2, 0.0, 500, 20.0, Color.YELLOW, Formato.CIRCLE);
		
		InimigoHorizontal.proxInimigo = tempoAtual + 20000;
	}
	
	// Movimentação do inimigo de tipo 3
	public void movimenta(long delta) {
		// Movimenta na vertical até alcançar uma posição específica
		if (this.posicao.getY() < GameLib.HEIGHT / 3) {
			this.posicao.setY(this.posicao.getY() + (this.posicao.getvY() * Math.sin(this.angulo) * delta * (-1.0)));
		}
		// Ao alcançar a posição, movimenta na horizontal, movendo-se para a esquerda e para a direita até alcançar as bordas
		else {
			if (!this.colisaoBordaEsquerda && !this.colisaoBordaDireita) this.posicao.setX(this.posicao.getX() - (this.posicao.getvY() * Math.sin(this.angulo) * delta * (-1.0)));
			if (this.posicao.getX() <= 0) {
				this.colisaoBordaEsquerda = true;
				this.colisaoBordaDireita = false;
			}
			if (this.posicao.getX() >= GameLib.WIDTH) {
				this.colisaoBordaDireita = true;
				this.colisaoBordaEsquerda = false;
			}
			if (this.colisaoBordaEsquerda) this.posicao.setX(this.posicao.getX() + (this.posicao.getvY() * Math.sin(this.angulo) * delta * (-1.0)));
			if (this.colisaoBordaDireita) this.posicao.setX(this.posicao.getX() - (this.posicao.getvY() * Math.sin(this.angulo) * delta * (-1.0)));
		} 
	}
	
	// Disparo do inimigo de tipo 3
	public ArrayList<ProjetilInimigo> atira(long tempoAtual) {
		// Caso possa atirar (esteja na posição de tiro), retorna três projéteis
		if (tempoAtual > this.proximoTiro) {
			
			ArrayList<Double> angulos = new ArrayList<Double>();
			angulos.add(3*Math.PI/4);
			angulos.add(Math.PI/2);
			angulos.add(Math.PI/4);
		
			ArrayList<ProjetilInimigo> projeteis = new ArrayList<ProjetilInimigo>();

			double a;
			
			for (int i = 0; i < angulos.size(); i++) {
				a = angulos.get(i) + Math.random() * Math.PI/6 - Math.PI/12;
				projeteis.add(new ProjetilInimigo(this.posicao.getX() + 10, this.posicao.getY() + 10, Math.cos(a) * 0.30, Math.sin(a) * 0.30));
			}
			
			this.proximoTiro = tempoAtual + 500;
			return projeteis;
		}
		return null;
	}
}