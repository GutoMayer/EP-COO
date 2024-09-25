package entidades;

// Classe que representa a posição (coordenadas e velocidades nos eixos) de uma entidade
public class Ponto2D {
	protected double x; // Coordenada x
	protected double y; // Coordenada y
	
	protected double vX; // Velocidade no eixo x
	protected double vY; // Velocidade no eixo y
	
	protected Ponto2D(double x, double y, double vX, double vY) {
		this.x = x;
		this.y = y;
		this.vX = vX;
		this.vY = vY;
	}

	public double getX() {
		return x;
	}

	protected void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	protected void setY(double y) {
		this.y = y;
	}

	protected double getvX() {
		return vX;
	}

	protected void setvX(double vX) {
		this.vX = vX;
	}

	protected double getvY() {
		return vY;
	}

	protected void setvY(double vY) {
		this.vY = vY;
	}
	
	// Calcula a distância entre dois pontos
	protected double distancia(double pX, double pY) {
		double dx = this.getX() - pX;
		double dy = this.getY() - pY;
		double dist = Math.sqrt(dx * dx + dy * dy);
		
		return dist;
	}
}