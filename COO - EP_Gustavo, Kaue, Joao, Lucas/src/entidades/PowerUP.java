package entidades;

import java.awt.Color;

import entidades.enums.Estado;
import entidades.enums.Formato;
import lib.GameLib;

public class PowerUP {
	// o que é um pwoe up? é um objeto que aparece na tela e, caso colida com o player, incrementa velocidade vertical e horizonatal.
	
	private Ponto2D posicao;
	private Estado estado;
	private Forma forma;
	public static long proximoPowerUp;
	public static long tempoDeDuracaoNaTela;
	private Jogador jogador;
	
	public PowerUP(long tempoAtual) {
		this.posicao  = new Ponto2D(Math.random() * (GameLib.WIDTH) + 40.0, Math.random() * (GameLib.WIDTH) + 45.0, 0, 0);
		this.forma = new Forma(4.6, Color.MAGENTA , Formato.CIRCLE);
		this.estado = Estado.ATIVO;
		this.jogador = new Jogador(); 
		this.tempoDeDuracaoNaTela = tempoAtual + 2500;
		this.proximoPowerUp = this.tempoDeDuracaoNaTela + 400;
	}
	
	public double getX() {
		return this.posicao.getX();
	}
	
	// Método get da coordenada Y do powerUp
	public double getY() {
		return this.posicao.getY();
	}
	
	// Método get do raio do powerUp
	public double getRaio() {
		return this.forma.getRaio();
	}
	
	
	public Estado verificaEstado(long tempoAtual, long delta) {
		if(jogador.colisaoComPowerUp(tempoDeDuracaoNaTela, this.posicao.getX(), this.posicao.getY(), this.forma.getRaio()) ||(tempoAtual > tempoDeDuracaoNaTela) ) {
			this.estado = Estado.INATIVO;
			return this.estado;
		}
		else {
			this.estado = Estado.ATIVO;
			desenha(tempoAtual);
			return this.estado;
		}
	
	}
	//logica: tem espaço, tamanho e cor
	
	// o estado fica ativo a cada período de tempo.
	// quando o player toca, ele fica inativo, ai inicia a contagem pro proximo
	// se o player n tocar, ele fica inativo depois de algum curto período de tempo, eai a contagem se inicia.
	public void desenha(long tempoAtual) {
		if (this.estado == Estado.ATIVO) this.forma.desenha(this.posicao.getX(), this.posicao.getY());
	}
	
	
}
