package entidades;

import java.util.ArrayList;
import java.awt.Color;

import lib.GameLib;

import entidades.enums.*;

// Classe do inimigo de tipo 2
public class InimigoComposto extends InimigoSimples {
	
	private static double proxInimigoPosX = GameLib.WIDTH * 0.20; // Coordenada x do próximo inimigo de tipo 2 a aparecer
	private static int contagem = 0; // Contagem de inimigos de tipo 2
	public static long proxInimigo; // Instante em que um novo inimigo de tipo 2 deve aparecer
	
	private boolean podeAtirar = false; // Booleano que define se é possível atirar
	
	public InimigoComposto(long tempoAtual) {
		super(InimigoComposto.proxInimigoPosX, -10.0, 0.42, (3 * Math.PI) / 2, 0.0, (long) 0.0, 12.0, Color.MAGENTA, Formato.DIAMOND);
		
		InimigoComposto.contagem++;
		
		// Atualiza o instante em que um novo inimigo de tipo 2 deve se criado, caso não exista um conjunto de dez inimigos
		if (InimigoComposto.contagem < 10) InimigoComposto.proxInimigo = tempoAtual + 120;
		// Caso exista um conjunto de dez inimigos de tipo 2, cria um novo conjunto
		else {
			InimigoComposto.contagem = 0;
			InimigoComposto.proxInimigoPosX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
			InimigoComposto.proxInimigo = (long) (tempoAtual + 3000 + Math.random() * 3000);
		}
	}
	
	// Verificação e atualização de estado do inimigo de tipo 2
	public Estado verificaEstado(long tempoAtual, long delta) {
		if(verificaFimExplosao(tempoAtual)) this.estado = Estado.INATIVO; // Define o estado como inativo ao fim da explosão
		if (this.estado == Estado.ATIVO) {
			if (this.posicao.getX() < -10 || this.posicao.getX() > GameLib.WIDTH + 10) this.estado = Estado.INATIVO; // Define o estado como inativo caso o inimigo tenha saído da tela
			else movimenta(delta, GameLib.HEIGHT * 0.30);
		}
		return this.estado;
	}
	
	// Movimentação do inimigo de tipo 2
	public void movimenta(long delta, double limite) {
		this.podeAtirar = false;
		double antY = this.posicao.getY();
		
		super.movimenta(delta);
		
		if (antY < limite && this.posicao.getY() >= limite) {
			if (this.posicao.getX() < GameLib.WIDTH / 2) this.rv = 0.003;
			else this.rv = -0.003;
		}
		
		if (this.rv > 0 && Math.abs(this.angulo - 3 * Math.PI) < 0.05) {
			this.rv = 0.0;
			this.angulo = 3 * Math.PI;
			this.podeAtirar = true;
		}
		
		if (this.rv < 0 && Math.abs(this.angulo) < 0.05) {
			this.rv = 0.0;
			this.angulo = 0.0;
			this.podeAtirar = true;
		}
	}
	
	// Disparo do inimigo de tipo 2
	public ArrayList<ProjetilInimigo> atira() {
		// Caso possa atirar (esteja na posição de tiro), retorna três projéteis
		if (this.podeAtirar) {
			ArrayList<Double> angulos = new ArrayList<Double>();
			angulos.add(Math.PI/2 + Math.PI/8);
			angulos.add(Math.PI/2);
			angulos.add(Math.PI/2 - Math.PI/8);
		
			ArrayList<ProjetilInimigo> projeteis = new ArrayList<ProjetilInimigo>();

			double a;
			
			for (int i = 0; i < angulos.size(); i++) {
				a = angulos.get(i) + Math.random() * Math.PI/6 - Math.PI/12;
				projeteis.add(new ProjetilInimigo(this.posicao.getX(), this.posicao.getY(), Math.cos(a) * 0.30, Math.sin(a) * 0.30));
			}
			
			return projeteis;
		}
		return null;
	}
}