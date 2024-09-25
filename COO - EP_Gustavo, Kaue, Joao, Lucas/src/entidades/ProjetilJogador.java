package entidades;

import java.awt.Color;

import entidades.enums.*;

import entidades.interfaces.Projetil;

// Classe do projétil do jogador
public class ProjetilJogador implements Projetil {
	public Ponto2D posicao; // Utiliza-se composição para representar a posição do projétil inimigo por meio da classe Ponto2D
	private Forma forma = new Forma(2.0, Color.GREEN, Formato.LINE); // Utiliza-se composição para representar o desenho (com tamanho, cor e formato) por meio da classe Forma
	
	private Estado estado; // Estado do projétil
	
	// Construtor do projétil (define o estado como ativo e define a posição/velocidade)
	public ProjetilJogador (double posicaoX, double posicaoY, double velocidadeX, double velocidadeY) {
		this.posicao = new Ponto2D(posicaoX, posicaoY, velocidadeX, velocidadeY);
		this.estado = Estado.ATIVO;
	}
	
	// Método get da coordenada X do projétil
	public double getX() {
		return this.posicao.getX();
	}
	
	// Método get da coordenada Y do projétil
	public double getY() {
		return this.posicao.getY();
	}
	
	// Verificação e atualização de estado do projétil
	public Estado verificaEstado(long tempoAtual, long delta) {
		if (this.estado == Estado.ATIVO) {
			if (this.posicao.getY() < 0) this.estado = Estado.INATIVO; // Define o estado como inativo caso o projétil tenha saído da tela
			else movimenta(delta); // Caso não tenha saído da tela, realiza o movimento
		}
		return this.estado;
	}

	// Movimentação do projétil
	public void movimenta(long delta) {
		this.posicao.setX(this.posicao.getX() + (this.posicao.getvX() * delta));   
		this.posicao.setY(this.posicao.getY() + (this.posicao.getvY() * delta));   
	}

	// Desenho do projétil na tela
	public void desenha(long tempoAtual) {
		if (this.estado == Estado.ATIVO) this.forma.desenha(this.posicao.getX(), this.posicao.getY());
	}
}
