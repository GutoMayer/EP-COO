package entidades;

import java.awt.Color;

import lib.GameLib;

import entidades.enums.*;

import entidades.interfaces.*;

// Classe do jogador
public class Jogador implements Personagem {
	protected Ponto2D posicao; // Utiliza-se composição para representar a posição do inimigo por meio da classe Ponto2D
	private Forma forma; // Utiliza-se composição para representar o desenho (com tamanho, cor e formato) por meio da classe Forma
	
	private int vidaMaxima; // Quantidade máxima de pontos de vida
	private int vidaAtual; // Quantidade atual de pontos de vida
	
	private double inicioExplosao; // Instante de início da explosão
	private double fimExplosao; // Instante de fim da explosão
	
	private long proximoTiro; // Instante do próximo tiro
	private long tempoInvulneravel; // Instante até o qual o jogador não perde vida (após ter tomado dano)
	private long tempoSobEfeito;
	
	protected Estado estado; // Estado (ativo, inativo ou explodindo)
	protected double vX, vY;
	
	public Jogador(long tempoAtual, int vida) {
		this.posicao = new Ponto2D(GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, getvX(), getvY());
		this.forma = new Forma(12.0, Color.BLUE, Formato.PLAYER);
		
		this.vidaMaxima = vida;
		this.vidaAtual = this.vidaMaxima;
		
		this.proximoTiro = tempoAtual;
		
		this.estado = Estado.ATIVO;
	}
// só para utilizar composição no pwup
	public Jogador() {
		super();
	}



	// Método get da coordenada X do jogador
	public double getX() {
		return this.posicao.getX();
	}
	
	// Método get da coordenada Y do jogador
	public double getY() {
		return this.posicao.getY();
	}
	
	// Verifica se o jogador zerou seus pontos de vida e, em caso positivo, realiza a explosão
	public void verificaExplosao(long tempoAtual) {
		if (this.vidaAtual == 0) {
			this.estado = Estado.EXPLODINDO;
			this.forma.setCor(Color.BLUE);
			this.inicioExplosao = tempoAtual;
			this.fimExplosao = tempoAtual + 2000;
		}
	}
	
	// Verifica a colisão do jogador com um projétil dos inimigos
	public boolean colisaoComProjetil(long tempoAtual, double projetilX, double projetilY, double projetilRaio) {
		if (this.estado == Estado.ATIVO && this.posicao.distancia(projetilX, projetilY) < (this.forma.getRaio() + projetilRaio) * 0.8) {
			this.vidaAtual--; // Caso tenha ocorrido a colisão, diminui um ponto de vida
			// Define o estado como atingido, alterando a cor como feedback visual e deixando o jogador invulnerável por certo tempo
			this.estado = Estado.ATINGIDO;
			this.forma.setCor(Color.WHITE);
			this.tempoInvulneravel = tempoAtual + 100;
			this.verificaExplosao(tempoAtual); // Verifica se a colisão resultou em uma explosão
			return true;
		}
		return false;
	}
	
	// Verifica a colisão do jogador com os inimigos
	public void colisaoComInimigo(long tempoAtual, double inimigoX, double inimigoY, double inimigoRaio) {
		if (this.estado == Estado.ATIVO && this.posicao.distancia(inimigoX, inimigoY) < (this.forma.getRaio() + inimigoRaio) * 0.8) {
			this.vidaAtual--; // Caso tenha ocorrido a colisão, diminui um ponto de vida
			// Define o estado como atingido, alterando a cor como feedback visual e deixando o jogador invulnerável por certo tempo
			this.estado = Estado.ATINGIDO;
			this.forma.setCor(Color.WHITE);
			this.tempoInvulneravel = tempoAtual + 100;
			this.verificaExplosao(tempoAtual); // Verifica se a colisão resultou em uma explosão
		}
	}
	
	public boolean colisaoComPowerUp(long tempoAtual, double powerUpX, double powerUpY, double powerUpRaio) {
		if(this.estado == Estado.ATIVO && this.posicao.distancia(powerUpX, powerUpY) < (this.forma.getRaio() + (powerUpRaio * 3)) * 0.8){
			this.estado = Estado.SOBEFEITO;
			this.forma.setCor(Color.MAGENTA); // Muda a cor do personagem
			this.posicao.setvX(this.posicao.getvX() + 0.125);
			this.posicao.setvY(this.posicao.getvY() + 0.125);
			this.tempoSobEfeito = tempoAtual + 3000;
			return true;
		}
		return false;
	}
	
	// Verifica se o jogador explodiu e, em caso positivo, se a explosão terminou
	public boolean verificaFimExplosao(long tempoAtual) {
		if (this.estado == Estado.EXPLODINDO && tempoAtual > this.fimExplosao) {
			this.vidaAtual = this.vidaMaxima;
			return true;
		}
		return false;
	}
	
	// Verificação e atualização de estado do jogador
	public Estado verificaEstado(long tempoAtual, long delta) {
		// Verifica se a explosão terminou
		if (this.verificaFimExplosao(tempoAtual)) this.estado = Estado.ATIVO;
		// Verifica se a invulnerabilidade do jogador terminou
		if (this.estado == Estado.ATINGIDO && tempoAtual > this.tempoInvulneravel) {
			this.estado = Estado.ATIVO;
			this.forma.setCor(Color.BLUE);
		}
		if (this.estado == Estado.SOBEFEITO && tempoAtual > this.tempoSobEfeito) { // isso aq ta funcionando legal
			this.estado = Estado.ATIVO;
			this.forma.setCor(Color.BLUE);
			posicao.setvX(0.25); 
			posicao.setvY(0.25);
		}
		// Caso possível, realiza o movimento
		if (this.estado == Estado.ATIVO || this.estado == Estado.ATINGIDO || this.estado == Estado.SOBEFEITO) this.movimenta(delta);
		return this.estado;
		
	}
	
	// Movimentação do jogador
	public void movimenta(long delta) {
		if (GameLib.iskeyPressed(GameLib.KEY_UP)) this.posicao.setY(this.posicao.getY() - delta * this.posicao.getvY());
		if (GameLib.iskeyPressed(GameLib.KEY_DOWN)) this.posicao.setY(this.posicao.getY() + delta * this.posicao.getvY());
		if (GameLib.iskeyPressed(GameLib.KEY_LEFT)) this.posicao.setX(this.posicao.getX() - delta * this.posicao.getvX());
		if (GameLib.iskeyPressed(GameLib.KEY_RIGHT)) this.posicao.setX(this.posicao.getX() + delta * this.posicao.getvX());
		
		// Mantém o jogador dentro da tela após processar a entrada do usuário
		if (this.posicao.getX() < 0.0) this.posicao.setX(0.0);
		if (this.posicao.getX() >= GameLib.WIDTH) this.posicao.setX(GameLib.WIDTH - 1);
		if (this.posicao.getY() < 25.0) this.posicao.setY(25.0);
		if (this.posicao.getY() >= GameLib.HEIGHT) this.posicao.setY(GameLib.HEIGHT - 1);
	}
	
	// Verifica se é possível atirar e, em caso positivo, retorna um projétil novo
	public ProjetilJogador atira(long tempoAtual) {
		if (GameLib.iskeyPressed(GameLib.KEY_CONTROL) && tempoAtual > this.proximoTiro) {
			this.proximoTiro = tempoAtual + 100;
			return new ProjetilJogador(this.posicao.getX(), this.posicao.getY() - 2 * this.forma.getRaio(), 0.0, -1.0);
		}
		return null;
	}
	
	// Desenha o inimigo na tela
	public void desenha(long tempoAtual) {
		// Desenha a explosão, caso o estado seja "explodindo"
		if (this.estado == Estado.EXPLODINDO) {
			double alpha = (tempoAtual - this.inicioExplosao) / (this.fimExplosao - this.inicioExplosao);
			GameLib.drawExplosion(this.posicao.getX(), this.posicao.getY(), alpha);
		}
		// No estado ativo ou atingido, desenha o jogador em si
		else if (this.estado == Estado.ATIVO || this.estado == Estado.ATINGIDO || this.estado == Estado.SOBEFEITO) this.forma.desenha(this.posicao.getX(), this.posicao.getY());
		
	}

	public double getvX() {
		return vX;
	}

	public void setvX(double vX) {
		this.posicao.vX = vX;
	}

	public double getvY() {
		return vX;
	}

	public void setvY(double vY) {
		this.posicao.vY = vY;
	}
	public int getVidaMaxima() {
		return vidaMaxima;
	}
	public void setVidaMaxima(int vidaMaxima) {
		this.vidaMaxima = vidaMaxima;
	}
	public int getVidaAtual() {
		return vidaAtual;
	}
	public void setVidaAtual(int vidaAtual) {
		this.vidaAtual = vidaAtual;
	}
	
	
	
}