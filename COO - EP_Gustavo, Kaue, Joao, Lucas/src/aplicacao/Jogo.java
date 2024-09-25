package aplicacao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.Color;

import entidades.enums.Estado;
import entidades.BarraVida;
import entidades.Fundo;
import entidades.Jogador;
import entidades.PowerUP;
import entidades.InimigoSimples;
import entidades.InimigoComposto;
import entidades.InimigoHorizontal;
import entidades.ProjetilJogador;
import entidades.ProjetilInimigo;

public class Jogo {
	Fundo fundoDistante; // Objeto do fundo distante
	Fundo fundoProximo; // Objeto do fundo próximo
	
	private Jogador jogador;
	private BarraVida barraVida;
	
	private LinkedList<InimigoSimples> inimigosTipo1; // Coleção de inimigos de tipo 1
	private LinkedList<InimigoComposto> inimigosTipo2; // Coleção de inimigos de tipo 2
	private LinkedList<InimigoHorizontal> inimigosTipo3; // Coleção de inimigos de tipo 3
	
	private LinkedList<ProjetilJogador> projeteisJogador; // Coleção de projéteis do jogador
	private LinkedList<ProjetilInimigo> projeteisInimigos; // Coleção de projéteis dos inimigos
	
	private LinkedList<PowerUP> powerup; // coleção de powerups
	
	// Inicializa as entidades do jogo
	public void inicializaEntidades(long tempoAtual, int vidaJogador) {
		// Criação dos objetos dos fundos próximo e distante
		this.fundoDistante = new Fundo(50, 0.045, Color.DARK_GRAY);
		this.fundoProximo = new Fundo(20, 0.070, Color.GRAY);
		
		// Inicializa o objeto jogador
		jogador = new Jogador(tempoAtual, vidaJogador);
		jogador.setvX(0.25);
		jogador.setvY(0.25);
		barraVida = new BarraVida(this.jogador, 875, 20, 35, 35, Color.BLACK, Color.RED);
		
		this.inimigosTipo1 = new LinkedList<InimigoSimples>(); // Cria a coleção de inimigos de tipo 1
		InimigoSimples.proxInimigo = tempoAtual + 2000; // Salva o instante para ser criado o primeiro inimigo de tipo 1
		
		this.inimigosTipo2 = new LinkedList<InimigoComposto>(); // Cria a coleção de inimigos de tipo 2
		InimigoComposto.proxInimigo = tempoAtual + 7000; // Define o instante inicial em que um inimigo de tipo 2 deve ser criado
		
		this.inimigosTipo3 = new LinkedList<InimigoHorizontal>(); // Cria a coleção de inimigos de tipo 3
		InimigoHorizontal.proxInimigo = tempoAtual + 5000; // Define o instante inicial em que um inimigo de tipo 3 deve ser criado
		
		this.projeteisInimigos = new LinkedList<ProjetilInimigo>(); // Cria a coleção de projéteis dos inimigos (de todos os tipos)
		this.projeteisJogador = new LinkedList<ProjetilJogador>(); // Cria a coleção de projéteis do jogador
		
		this.powerup = new LinkedList<PowerUP>();
		PowerUP.proximoPowerUp = tempoAtual + 12000;
	}
	
	public void verificaColisoes(long tempoAtual) {	
		// Verifica se ocorreram colisões entre o jogador e os projéteis dos inimigos
		for (ProjetilInimigo pI : projeteisInimigos) this.jogador.colisaoComProjetil(tempoAtual, pI.getX(), pI.getY(), pI.getRaio());
		
		// Verifica se ocorreram colisões entre o jogador e os inimigos
		for (InimigoSimples i1 : this.inimigosTipo1) this.jogador.colisaoComInimigo(tempoAtual, i1.getX(), i1.getY(), i1.getRaio());
		for (InimigoComposto i2 : this.inimigosTipo2) this.jogador.colisaoComInimigo(tempoAtual, i2.getX(), i2.getY(), i2.getRaio());
		for (InimigoHorizontal i3 : this.inimigosTipo3) this.jogador.colisaoComInimigo(tempoAtual, i3.getX(), i3.getY(), i3.getRaio());
		
		for (PowerUP pw : this.powerup) this.jogador.colisaoComPowerUp(tempoAtual, pw.getX(), pw.getY(), pw.getRaio());
		
		// Verifica se ocorreram colisões entre os projéteis do jogador e os inimigos
		for (ProjetilJogador pJ : projeteisJogador) {
			for (InimigoSimples i1 : this.inimigosTipo1) i1.colisaoComProjetil(tempoAtual, pJ.getX(), pJ.getY());
			for (InimigoComposto i2 : this.inimigosTipo2) i2.colisaoComProjetil(tempoAtual, pJ.getX(), pJ.getY());
			for (InimigoHorizontal i3 : this.inimigosTipo3) i3.colisaoComProjetil(tempoAtual, pJ.getX(), pJ.getY());
		}
	}
	
	// Verifica o estado das entidades
	public void verificaEstados(long tempoAtual, long delta) {
		
		// Verifica o estado do jogador
		Estado estadoJogador = this.jogador.verificaEstado(tempoAtual, delta); 
		if (estadoJogador == Estado.ATIVO || estadoJogador == Estado.SOBEFEITO) {
			ProjetilJogador projetilJ = this.jogador.atira(tempoAtual);
			if (projetilJ != null) this.projeteisJogador.addLast(projetilJ);
		}
		
		// Cria um iterador para percorrer a coleção de inimigos de tipo 1
		Iterator<InimigoSimples> i1 = this.inimigosTipo1.iterator();
		
		while (i1.hasNext()) {
			InimigoSimples inimigo1 = i1.next();
			Estado estadoInimigo1 = inimigo1.verificaEstado(tempoAtual, delta); 
			
			// Caso um inimigo tenha ficado inativo (saído da tela), remove-o da coleção
			if (estadoInimigo1 == Estado.INATIVO) i1.remove();
			// Caso o inimigo esteja ativo, tenta atirar e armazena os projéteis na coleção, caso tenha ocorrido o disparo
			else if (estadoInimigo1 == Estado.ATIVO) {
				ProjetilInimigo projetilI1 = (ProjetilInimigo) inimigo1.atira(tempoAtual, this.jogador.getY());
				if (projetilI1 != null) this.projeteisInimigos.addLast(projetilI1);
			}
		}
		
		// Cria um iterador para percorrer a coleção de inimigos de tipo 2
		Iterator<InimigoComposto> i2 = this.inimigosTipo2.iterator();
		
		while (i2.hasNext()) {
			InimigoComposto inimigo2 = i2.next();
			Estado estadoInimigo2 = inimigo2.verificaEstado(tempoAtual, delta); 
			
			// Caso um inimigo tenha ficado inativo (saído da tela), remove-o da coleção
			if (estadoInimigo2 == Estado.INATIVO) i2.remove();
			// Caso o inimigo esteja ativo, tenta atirar e armazena os projéteis na coleção, caso tenha ocorrido o disparo
			else if (estadoInimigo2 == Estado.ATIVO) {
				ArrayList<ProjetilInimigo> projeteis = inimigo2.atira();
				if (projeteis != null) {
					for (ProjetilInimigo j : projeteis) this.projeteisInimigos.addLast(j);
				}
			}
		}
		
		// Cria um iterador para percorrer a coleção de inimigos de tipo 3
		Iterator<InimigoHorizontal> i3 = this.inimigosTipo3.iterator();
		
		while (i3.hasNext()) {
			InimigoHorizontal inimigo3 = i3.next();
			Estado estadoInimigo3 = inimigo3.verificaEstado(tempoAtual, delta); 
			
			// Caso um inimigo tenha ficado inativo (saído da tela), remove-o da coleção
			if (estadoInimigo3 == Estado.INATIVO) i3.remove();
			// Caso o inimigo esteja ativo, tenta atirar e armazena os projéteis na coleção, caso tenha ocorrido o disparo
			else if (estadoInimigo3 == Estado.ATIVO) {
				ArrayList<ProjetilInimigo> projeteis = inimigo3.atira(tempoAtual);
				if (projeteis != null) {
					for (ProjetilInimigo j : projeteis) this.projeteisInimigos.addLast(j);
				}
			}
		}
		
		// Cria um iterador para percorrer a coleção de projéteis do jogador
		Iterator<ProjetilJogador> pJ = this.projeteisJogador.iterator();
		
		while (pJ.hasNext()) {
			Estado estadoProjetil = pJ.next().verificaEstado(tempoAtual, delta); 
			
			// Caso um projétil tenha ficado inativo (saído da tela), remove-o da coleção
			if (estadoProjetil == Estado.INATIVO) pJ.remove();
		}
		
		// Cria um iterador para percorrer a coleção de projéteis dos inimigos
		Iterator<ProjetilInimigo> pI = this.projeteisInimigos.iterator();
		
		while (pI.hasNext()) {
			Estado estadoProjetil = pI.next().verificaEstado(tempoAtual, delta); 
			
			// Caso um projétil tenha ficado inativo (saído da tela), remove-o da coleção
			if (estadoProjetil == Estado.INATIVO) pI.remove();
		}
		
		Iterator<PowerUP> pw = this.powerup.iterator();
		while(pw.hasNext()) {
			Estado estadoPowerUP = pw.next().verificaEstado(tempoAtual, delta); 
			
			if (estadoPowerUP == Estado.INATIVO || estadoJogador == Estado.SOBEFEITO) pw.remove();
		}
	}
	
	// Verifica se é necessário criar novos inimigos e projéteis
	public void verificaCriacao(long tempoAtual) {
		// Verifica se é possível criar um inimigo de tipo 1 e, se for o caso, cria o inimigo e o adiciona à coleção
		if (tempoAtual > InimigoSimples.proxInimigo) this.inimigosTipo1.addLast(new InimigoSimples(tempoAtual));
		
		// Verifica se é possível criar um inimigo de tipo 2 e, se for o caso, cria o inimigo e o adiciona à coleção
		if (tempoAtual > InimigoComposto.proxInimigo) this.inimigosTipo2.addLast(new InimigoComposto(tempoAtual));
		
		// Verifica se é possível criar um inimigo de tipo 3 e, se for o caso, cria o inimigo e o adiciona à coleção
		if (tempoAtual > InimigoHorizontal.proxInimigo) this.inimigosTipo3.addLast(new InimigoHorizontal(tempoAtual));
		
		if (tempoAtual > PowerUP.proximoPowerUp) this.powerup.addLast(new PowerUP(tempoAtual));
	}
	
	// Desenha as entidades na tela
	public void desenhaEntidades(long tempoAtual, long delta) {
		// Desenha os fundos
		this.fundoDistante.desenha(delta);
		this.fundoProximo.desenha(delta);
		
		// Desenha o jogador
		this.jogador.desenha(tempoAtual);
		
		// Desenha barra de vida
		this.barraVida.desenha();
		
		// Desenha os inimigos de tipo 1
		for (InimigoSimples i1 : this.inimigosTipo1) i1.desenha(tempoAtual);
		
		// Desenha os inimigos de tipo 2
		for (InimigoComposto i1 : this.inimigosTipo2) i1.desenha(tempoAtual);
		
		// Desenha os inimigos de tipo 3
		for (InimigoHorizontal i3 : this.inimigosTipo3) i3.desenha(tempoAtual);
		
		for (PowerUP pw : this.powerup) pw.desenha(tempoAtual);
		
		// Desenha os projéteis dos inimigos
		for (ProjetilInimigo pI : this.projeteisInimigos) pI.desenha(tempoAtual);
		
		// Desenha os projéteis do jogador
		for (ProjetilJogador pJ : this.projeteisJogador) pJ.desenha(tempoAtual);
	}
}