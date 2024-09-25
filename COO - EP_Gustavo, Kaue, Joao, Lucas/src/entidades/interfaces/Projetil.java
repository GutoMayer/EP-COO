package entidades.interfaces;

import entidades.enums.Estado;

import entidades.Ponto2D;

// A interface deve ser implementada pelas classes os projétis dos inimigos e do jogador
public interface Projetil {
	
	public double getX(); // Método get da coordenada X do projétil
	public double getY(); // Método get da coordenada Y do projétil
	
	public Estado verificaEstado(long tempoAtual, long delta); // Verifica o estado do projétil e invoca os métodos a seguir, dependendo do estado
	public void movimenta(long delta); // Realiza o deslocamento dos projéteis
	public void desenha(long tempoAtual); // Desenha o projétil na tela
}