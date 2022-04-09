package et3.threes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// Classe contenant le jeu graphique

public class ThreesGraphique extends JPanel {

	private static final long serialVersionUID = 1L;

	private Tile_2D[] tiles;
	private ArrayList<Moving_Tile_2D> movingTiles_Array;

	private Game myGame;

	private final static int NB_CASES = 16;
	private final static int NB_LIGNES = 4;
	private final static int DEPLACEMENT_MIN_SOURIE = 5;
	protected final static int XOFFSET = 50;

	private final static Color BACKGROUND_COLOR = new Color(0x06bfbf);

	private final static int UP = -4;
	private final static int DOWN = 4;
	private final static int LEFT = -1;
	private final static int RIGHT = 1;

	private boolean rightClick;
	private boolean leftClick;
	private Circular circular_menu;
	private boolean popMenu;

	// Constructeur

	public ThreesGraphique(Game game) {

		myGame = game;
		circular_menu = new Circular();

		movingTiles_Array = new ArrayList<Moving_Tile_2D>();
		tiles = new Tile_2D[NB_CASES];

		int temp = Tile_2D.TILES_SIZE + Tile_2D.TILES_MARGIN;

		for (int i = 0; i < NB_CASES; i++) {
			tiles[i] = new Tile_2D(((i % NB_LIGNES) * temp + Tile_2D.TILES_MARGIN + XOFFSET),
					((i / NB_LIGNES) * temp + Tile_2D.TILES_MARGIN));
		}
	}

	// Fonctions utilitaires

	// Fonction d'affichage qui dessine toutes les tuiles

	public void paint(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// Affichage des tuiles immobiles
		for (int i = 0; i < NB_CASES; i++) {
			tiles[i].draw(g);
		}

		// Affichage des tuiles qui bougent
		for (int i = 0; i < movingTiles_Array.size(); i++) {
			movingTiles_Array.get(i).move();
			movingTiles_Array.get(i).draw(g);
			if (movingTiles_Array.get(i).moving() == false)
				movingTiles_Array.remove(i);
		}

		if (movingTiles_Array.isEmpty() && popMenu && !myGame.getWinStatut() && !myGame.getLostStatut()) {
			circular_menu.render(g);
		}
	}

	// Reinitialise les valeurs du jeu

	public void resetAll() {

		for (int i = 0; i < NB_CASES; i++) {
			tiles[i].setVal(0);
		}

	}

	// Initialise les eventListeners

	public void init() {

		myGame.addKeyListener(new keyEvents());
		myGame.addMouseListener(new mouseEvents());

	}

	// Donne le score actuel de la partie

	public int getScore() {

		int actual_score = 0;

		for (int i = 0; i < NB_CASES; i++) {
			actual_score += tiles[i].getVal();
		}

		return actual_score;
	}

	// Donne le score final de la partie

	public void setFinalScore() {

		int final_score = 0;

		for (int i = 0; i < NB_CASES; i++) {
			final_score += tiles[i].getVal();
		}

		myGame.setScore(final_score);
	}

	// Fonctions de jeu

	// Genere les premieres tuiles aléatoires

	public void randomFirstTiles() {

		// Pour avoir entre 4 et 6 tuiles au démarrage
		int nbTiles = (int) ((Math.random() * 3) + 4);
		int number, pos;

		for (int i = 0; i < nbTiles; i++) {
			// On doit faire une tuile 1 ou 2 au hasard

			number = (int) ((Math.random() * 2) + 1);
			pos = (int) (Math.random() * NB_CASES);

			while (tiles[pos].getVal() != 0) {
				pos = (int) (Math.random() * NB_CASES);
			}

			tiles[pos].setVal(number);
		}
	}

	// Ajoute une tuile aléatoire au plateau

	public void addRandomTile(int min, int max, int mouvement) {

		int newPosition, temp;

		if ((mouvement == LEFT) || (mouvement == RIGHT)) {
			do {
				newPosition = (int) (Math.random() * NB_CASES);

			} while (((newPosition + max + min) % 4 != 0) || (tiles[newPosition].getVal() != 0));

			temp = (int) ((Math.random() * 2) + 1);
			tiles[newPosition].setVal(temp);
		}

		else {
			do {
				newPosition = (int) (Math.random() * NB_CASES);

			} while (newPosition < max || newPosition > min || (tiles[newPosition].getVal() != 0));

			temp = (int) ((Math.random() * 2) + 1);
			tiles[newPosition].setVal(temp);
		}
	}

	// Ajoute une tuile animée

	public void addMovingTile(int prevPos, int mouvement) {

		int temp = Tile_2D.TILES_SIZE + Tile_2D.TILES_MARGIN;
		movingTiles_Array.add(new Moving_Tile_2D(temp * (prevPos % NB_LIGNES) + Tile_2D.TILES_MARGIN,
				temp * (prevPos / NB_LIGNES) + Tile_2D.TILES_MARGIN));

		Moving_Tile_2D moving = movingTiles_Array.get(movingTiles_Array.size() - 1);
		moving.setVal(tiles[prevPos].getVal());

		switch (mouvement) {
		case UP:
			moving.setLocation(0, temp);
			break;

		case DOWN:
			moving.setLocation(0, -temp);
			break;

		case LEFT:
			moving.setLocation(temp, 0);
			break;

		case RIGHT:
			moving.setLocation(-temp, 0);
			break;
		}
	}

	// Verifie qu'une tuile peut bouger

	public boolean tileCanMove(int i, int mouvement) {

		// Si les tuiles sont de même valeur mais ne sont pas 1-1 ou 2-2
		if (tiles[i].getVal() == tiles[i + mouvement].getVal()
				&& ((tiles[i].getVal() + tiles[i + mouvement].getVal() != 2)
						&& (tiles[i].getVal() + tiles[i + mouvement].getVal() != 4) && (tiles[i].getVal() != 0)))
			return true;

		else if (tiles[i].getVal() == 1 && tiles[i + mouvement].getVal() == 2)
			return true;

		else if (tiles[i].getVal() == 2 && tiles[i + mouvement].getVal() == 1)
			return true;

		else if (tiles[i].getVal() != 0 && tiles[i + mouvement].getVal() == 0) {
			return true;
		}

		// Sinon
		else {
			return false;
		}

	}

	// Verifie que le joueur peut bouger

	public boolean playerCanMove() {

		if (!isFull()) {
			return true;
		}

		else if (myGame.getLostStatut() == true) {
			return false;
		}

		else {
			int i;
			// Mouvement vers le haut
			for (i = 4; i < NB_CASES; i++) {
				if (tileCanMove(i, UP)) {
					return true;
				}
			}
			// Mouvement vers le bas
			for (i = (3 * NB_LIGNES) - 1; i >= 0; i--) {
				if (tileCanMove(i, DOWN)) {
					return true;
				}
			}
			// Mouvement vers la gauche
			for (i = 1; i < NB_CASES; i++) {
				if (i % 4 != 0 && tileCanMove(i, LEFT)) {
					return true;
				}
			}
			// Mouvement vers la droite
			for (i = 14; i >= 0; i--) {
				if (i % 4 != 3 && tileCanMove(i, RIGHT)) {
					return true;
				}
			}
		}

		return false;
	}

	// Verifie que le plateau n'est pas plein

	public boolean isFull() {

		for (int i = 0; i < NB_CASES; i++) {
			if (tiles[i].getVal() == 0) {
				return false;
			}
		}
		return true;
	}

	// Bouge une tuile

	private void moveTile(int i, int mouvement) {
		addMovingTile(i, mouvement);
		tiles[i + mouvement].setVal(tiles[i + mouvement].getVal() + tiles[i].getVal());
		tiles[i].setVal(0);
	}

	// Bouge le plateau

	private void moveEntireBoard(int mouvement) {

		boolean mouvementMade = false;

		switch (mouvement) {
		case UP:
			for (int i = 4; i < NB_CASES; i++) {
				if (tileCanMove(i, UP)) {
					moveTile(i, UP);
					mouvementMade = true;
				}
			}

			if (mouvementMade)
				addRandomTile(NB_CASES, NB_CASES - NB_LIGNES, UP);
			break;

		case RIGHT:
			for (int i = NB_CASES - 1; i >= 0; i--) {
				if ((i % 4) != 3 && tileCanMove(i, RIGHT)) {
					moveTile(i, RIGHT);
					mouvementMade = true;
				}
			}

			if (mouvementMade)
				addRandomTile(0, 0, RIGHT);
			break;

		case LEFT:
			for (int i = 0; i < NB_CASES; i++) {
				if (i % 4 != 0 && tileCanMove(i, LEFT)) {
					moveTile(i, LEFT);
					mouvementMade = true;
				}
			}

			if (mouvementMade)
				addRandomTile(1, 0, LEFT);
			break;

		case DOWN:
			for (int i = (3 * NB_LIGNES) - 1; i >= 0; i--) {
				if (tileCanMove(i, DOWN)) {
					moveTile(i, DOWN);
					mouvementMade = true;
				}
			}

			if (mouvementMade)
				addRandomTile(NB_LIGNES, 0, DOWN);
			break;
		}
	}

	// Gestion des evenements

	// Evenements claviers

	public class keyEvents implements KeyListener {

		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				myGame.dispose();
			}

			if (!playerCanMove() && !myGame.getWinStatut() == true) {
				myGame.setLostStatut(true);

			}

			if (ThreesGraphique.this.getScore() >= myGame.getWinScore()) {
				myGame.setWinStatut(true);
			}

			if (myGame.getWinStatut() == false && myGame.getLostStatut() == false) {

				switch (e.getKeyCode()) {

				case KeyEvent.VK_UP:
					moveEntireBoard(UP);
					break;

				case KeyEvent.VK_DOWN:
					moveEntireBoard(DOWN);
					break;

				case KeyEvent.VK_LEFT:
					moveEntireBoard(LEFT);
					break;

				case KeyEvent.VK_RIGHT:
					moveEntireBoard(RIGHT);
					break;
				}

				repaint();
			}

			if (myGame.getLostStatut() == true || myGame.getWinStatut() == true) {
				if (!myGame.gameDone) {
					ThreesGraphique.this.setFinalScore();
					ThreesGraphique.this.resetAll();
					myGame.gameDone = true;
				}
				if (myGame.getWinStatut() == true)
					myGame.init_ecran_fin(true);
				else
					myGame.init_ecran_fin(false);
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}

	// Evenements sourie

	public class mouseEvents implements MouseListener {

		int mouse_x = 0;
		int mouse_dx = 0;

		int mouse_y = 0;
		int mouse_dy = 0;

		public void mousePressed(MouseEvent e) {
			mouse_x = e.getX();
			mouse_y = e.getY();

			if (e.getButton() == 1){
				leftClick = true;
			}
				

			else if (e.getButton() == 3){
				rightClick = true;
				popMenu = true;
			}
		}

		public void mouseReleased(MouseEvent e) {

			if (leftClick) {

				if (!playerCanMove() && !(myGame.getWinStatut() == true)) {
					myGame.setLostStatut(true);
				}

				if (ThreesGraphique.this.getScore() >= myGame.getWinScore()) {
					myGame.setWinStatut(true);
				}

				if (myGame.getWinStatut() == false && myGame.getLostStatut() == false) {

					mouse_dx = mouse_x - e.getX();
					mouse_dy = mouse_y - e.getY();

					if (Math.abs(mouse_dx) > Math.abs(mouse_dy) && (Math.abs(mouse_dx) > DEPLACEMENT_MIN_SOURIE)) {

						if (mouse_dx > 0) {
							moveEntireBoard(LEFT);
						}

						else {
							moveEntireBoard(RIGHT);
						}
					}

					else if (Math.abs(mouse_dy) > DEPLACEMENT_MIN_SOURIE) {

						if (mouse_dy > 0) {
							moveEntireBoard(UP);
						}

						else {
							moveEntireBoard(DOWN);
						}
					}

					repaint();
				}

				if (myGame.getLostStatut() == true || myGame.getWinStatut() == true) {
					if (!myGame.gameDone) {
						ThreesGraphique.this.setFinalScore();
						ThreesGraphique.this.resetAll();
						myGame.gameDone = true;
					}
					if (myGame.getWinStatut() == true)
						myGame.init_ecran_fin(true);
					else
						myGame.init_ecran_fin(false);

				}

				leftClick = false;

				if (rightClick)
					rightClick = false;
			}
		}

		public void mouseClicked(MouseEvent e) {
			
			if (!playerCanMove() && !(myGame.getWinStatut() == true)) {
				myGame.setLostStatut(true);
			}

			if (ThreesGraphique.this.getScore() >= myGame.getWinScore()) {
				myGame.setWinStatut(true);
			}
			
			if (!myGame.getWinStatut() && !myGame.getLostStatut() && ThreesGraphique.this.movingTiles_Array.isEmpty()
					&& popMenu && e.getButton() == 1) {
				
				// Case haute
				if((e.getX() >= 530) && (e.getX() <= 570) && (e.getY() >= 179) && (e.getY() <= 190))
					moveEntireBoard(UP);
				
				// Case basse
				if((e.getX() >= 530) && (e.getX() <= 570) && (e.getY() >= 248) && (e.getY() <= 268))
					moveEntireBoard(DOWN);
				
				// Case gauche
				if((e.getX() >= 508) && (e.getX() <= 529) && (e.getY() >= 200) && (e.getY() <= 247))
					moveEntireBoard(LEFT);
				
				// Case gauche
				if((e.getX() >= 576) && (e.getX() <= 596) && (e.getY() >= 195) && (e.getY() <= 238))
					moveEntireBoard(RIGHT);
				
				if (myGame.getLostStatut() == true || myGame.getWinStatut() == true) {
					if (!myGame.gameDone) {
						ThreesGraphique.this.setFinalScore();
						ThreesGraphique.this.resetAll();
						myGame.gameDone = true;
					}
					if (myGame.getWinStatut() == true)
						myGame.init_ecran_fin(true);
					else
						myGame.init_ecran_fin(false);

				}
				
				repaint();
					
			}
			
			if(popMenu && e.getButton() ==1)
				popMenu = false;
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}
}