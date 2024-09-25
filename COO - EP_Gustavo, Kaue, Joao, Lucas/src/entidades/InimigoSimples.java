package entidades;

import java.awt.Color;

import lib.GameLib;

import entidades.enums.*;

import entidades.interfaces.*;

// Classe do inimigo de tipo 1
public class InimigoSimples implements Personagem {
	
	protected Ponto2D posicao; // Utiliza-se composição para representar a posição do inimigo por meio da classe Ponto2D
	private Forma forma; // Utiliza-se composição para representar o desenho (com tamanho, cor e formato) por meio da classe Forma
	
	protected double angulo; // Ângulo (indica direção do movimento)
	protected double rv; // Velocidade de rotação
	
	private double inicioExplosao; // Instante de início da explosão
	private double fimExplosao; // Instante de fim da explosão
	
	protected long proximoTiro; // Instante do próximo tiro
	
	public static long proxInimigo; // Instante em que um novo inimigo de tipo 1 deve aparecer
	
	protected Estado estado; // Estado (ativo, inativo ou explodindo)
	
	public InimigoSimples(long tempoAtual) {
		this.posicao = new Ponto2D(Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0, 0.20 + Math.random() * 0.15);
		this.forma = new Forma(9.0, Color.CYAN, Formato.CIRCLE);
		
		this.angulo = 3 * Math.PI / 2;
		this.rv = 0.0;
		
		this.proximoTiro = tempoAtual + 500;
		
		InimigoSimples.proxInimigo = tempoAtual + 500;
		
		this.estado = Estado.ATIVO;
	}
	
	// Método get da coordenada X do inimigo
	public double getX() {
		return this.posicao.getX();
	}
	
	// Método get da coordenada Y do inimigo
	public double getY() {
		return this.posicao.getY();
	}
	
	// Método get do raio do inimigo
	public double getRaio() {
		return this.forma.getRaio();
	}
	
	// Construtor usado na classe derivada InimigoComposto
	public InimigoSimples(double posicaoX, double posicaoY, double velocidadeInimigo, double anguloInimigo, double velocidadeRotacaoInimigo, long proximoTiroInimigo, double tamanhoInimigo, Color corInimigo, Formato formatoInimigo) {
		this.posicao = new Ponto2D(posicaoX, posicaoY, 0, velocidadeInimigo);
		this.forma = new Forma(tamanhoInimigo, corInimigo, formatoInimigo);
		
		this.angulo = anguloInimigo;
		this.rv = velocidadeRotacaoInimigo;
		
		this.proximoTiro = proximoTiroInimigo;
		
		this.estado = Estado.ATIVO;
	}

	// Verifica a colisão do inimigo com um projétil do jogador
	public void colisaoComProjetil(long tempoAtual, double projetilX, double projetilY) {
		if (this.estado == Estado.ATIVO && this.posicao.distancia(projetilX, projetilY) < this.forma.getRaio()) {
			// Caso tenha ocorrido a colisão, atualiza o estado e os instantes da explosão
			this.estado = Estado.EXPLODINDO;
			this.inicioExplosao = tempoAtual;
			this.fimExplosao = tempoAtual + 500;
		}
	}
	
	// Verifica se o inimigo explodiu e, em caso positivo, se a explosão terminou
	public boolean verificaFimExplosao(long tempoAtual) {
		if (this.estado == Estado.EXPLODINDO && tempoAtual > this.fimExplosao) return true;
		return false;
	}

	// Verificação e atualização de estado do inimigo de tipo 1
	public Estado verificaEstado(long tempoAtual, long delta) {
		if(verificaFimExplosao(tempoAtual)) this.estado = Estado.INATIVO; // Define o estado como inativo ao fim da explosão
		if (this.estado == Estado.ATIVO) {
			if (this.posicao.getY() > GameLib.HEIGHT + 10) this.estado = Estado.INATIVO; // Define o estado como inativo caso o inimigo tenha saído da tela
			else movimenta(delta);
		}
		return this.estado;
	}

	// Movimentação do inimigo de tipo 1
	public void movimenta(long delta) {
		this.posicao.setX(this.posicao.getX() + (this.posicao.getvY() * Math.cos(this.angulo) * delta)); // No caso do inimigo de tipo 1, o ângulo é 3pi/2 e o cosseno resulta em zero, de forma que não há deslocamento horizontal
		this.posicao.setY(this.posicao.getY() + (this.posicao.getvY() * Math.sin(this.angulo) * delta * (-1.0)));
		this.angulo += this.rv * delta; 
	}
	
	// Verifica se é possível atirar e, em caso positivo, retorna um projétil novo
	public Projetil atira(long tempoAtual, double posYJogador) {
		if (tempoAtual > this.proximoTiro && this.posicao.getY() < posYJogador) {
			this.proximoTiro = (long) (tempoAtual + 200 + Math.random() * 500); // Atualiza o instante do próximo tiro
			return new ProjetilInimigo(this.posicao.getX(), this.posicao.getY(), Math.cos(this.angulo) * 0.45, Math.sin(this.angulo) * 0.45 * (-1.0)); // Cria um projétil novo
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
		// No estado ativo, desenha o inimigo em si
		if (this.estado == Estado.ATIVO) this.forma.desenha(this.posicao.getX(), this.posicao.getY());
	}
}