package et3.threes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Classe contenant le jeu de base

public class Threes extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel[] tiles;
	private int[] val;

	private final static int NB_CASES = 16;
	private final static int DEPLACEMENT_MIN_SOURIE = 5;
	private final static int NB_LIGNES = 4;

	private final static int BUTTON_GAP = 10;

	private final static Color TILES_1_BACKGROUNDCOLOR = new Color(0x11c7f5);
	private final static Color TILES_1_FOREGROUNDCOLOR = new Color(0xffffff);
	private final static Color TILES_2_BACKGROUNDCOLOR = new Color(0xf5116c);
	private final static Color TILES_2_FOREGROUNDCOLOR = new Color(0xffffff);
	private final static Color OTHER_TILES_BACKGROUNDCOLOR = new Color(0xffffff);
	private final static Color OTHER_TILES_FOREGROUNDCOLOR = new Color(0x000000);
	private final static Color EMPTY_TILES_COLOR = new Color(0x038787);
	private final static Color BACKGROUND_COLOR = new Color(0x06bfbf);

	private final static String TILE_FONT = "Arial";
	private static Font TILES_FONT;

	private final static int UP = -4;
	private final static int DOWN = 4;
	private final static int LEFT = -1;
	private final static int RIGHT = 1;

	private GridLayout grd;
	private Game myGame;

	// Constructeur

	public Threes(Game game) {

		myGame = game;

	}

	// Fonction utilitaires

	// Lance le jeu et initialise les eventListener

	public void launch() {

		init_Game();

		myGame.addKeyListener(new keyEvents());
		myGame.addMouseListener(new mouseEvents());

	}

	// Charge les premieres tuiles aléatoires et actualise l'écran

	public void toBoard() {

		randomFirstTiles();
		updateView();

	}

	// Recharge les composantes du jeu

	public void resetAll() {

		for (int i = 0; i < NB_CASES; i++) {
			val[i] = 0;
		}

		updateView();
	}

	// Recharge la vue du jeu

	public void updateView() {

		for (int i = 0; i < NB_CASES; i++) {

			tiles[i].setBackground(getBackground(val[i]));
			tiles[i].setForeground(getForeground(val[i]));

			if (val[i] != 0)
				tiles[i].setText(String.valueOf(val[i]));
		}
	}

	// Donne les couleurs de background des tuiles

	public Color getBackground(int value) {

		switch (value) {

		case 0:
			return EMPTY_TILES_COLOR;

		case 1:
			return TILES_1_BACKGROUNDCOLOR;

		case 2:
			return TILES_2_BACKGROUNDCOLOR;

		default:
			return OTHER_TILES_BACKGROUNDCOLOR;
		}
	}

	// Donne la couleur des chiffres

	public Color getForeground(int value) {

		switch (value) {

		case 0:
			return EMPTY_TILES_COLOR;

		case 1:
			return TILES_1_FOREGROUNDCOLOR;

		case 2:
			return TILES_2_FOREGROUNDCOLOR;

		default:
			return OTHER_TILES_FOREGROUNDCOLOR;
		}
	}

	// Envoi a Game le score final de la partie

	public void setFinalScore() {
		int scoreFinal = 0;

		for (int i = 0; i < NB_CASES; i++) {
			scoreFinal += val[i];
		}

		myGame.setScore(scoreFinal);

	}

	// Fonction d'initialisation du jeu

	public void init_Game() {

		grd = new GridLayout(NB_LIGNES, NB_LIGNES);
		grd.setHgap(BUTTON_GAP);
		grd.setVgap(BUTTON_GAP);

		setLayout(grd);
		setBorder(BorderFactory.createEmptyBorder(BUTTON_GAP, BUTTON_GAP, BUTTON_GAP, BUTTON_GAP));
		setBackground(BACKGROUND_COLOR);

		val = new int[NB_CASES];
		tiles = new JLabel[NB_CASES];
		TILES_FONT = new Font(TILE_FONT, Font.PLAIN, 20);

		for (int i = 0; i < NB_CASES; i++) {
			val[i] = 0;
			tiles[i] = new JLabel("", SwingConstants.CENTER);
			tiles[i].setFont(TILES_FONT);
			tiles[i].setOpaque(true);
			add(BorderLayout.CENTER, tiles[i]);
		}
	}

	// Fonctions de jeu

	// Initialise les premieres tuiles aléatoires

	public void randomFirstTiles() {

		// Pour avoir entre 4 et 6 tuiles au démarrage
		int nbTiles = (int) ((Math.random() * 3) + 4);
		int number, pos;

		for (int i = 0; i < nbTiles; i++) {
			// On doit faire une tuile 1 ou 2 au hasard

			number = (int) ((Math.random() * 2) + 1);
			pos = (int) (Math.random() * 16);

			while (val[pos] != 0) {
				pos = (int) (Math.random() * 16);
			}

			val[pos] = number;
			tiles[pos].setText(String.valueOf(number));

			tiles[pos].setBackground(getBackground(val[pos]));
			tiles[pos].setForeground(getForeground(val[pos]));
		}
	}

	// Ajoute une tuile aléatoire a la grille

	public void addRandomTile(int min, int max, int mouvement) {

		int newPosition;

		if ((mouvement == LEFT) || (mouvement == RIGHT)) {
			do {
				newPosition = (int) (Math.random() * 16);

			} while (((newPosition + max + min) % 4 != 0) || (val[newPosition] != 0));

			val[newPosition] = (int) ((Math.random() * 2) + 1);
		}

		else {
			do {
				newPosition = (int) (Math.random() * 16);

			} while (newPosition < max || newPosition > min || (val[newPosition] != 0));

			val[newPosition] = (int) ((Math.random() * 2) + 1);
		}
	}

	// Verifie si une tuile peut bouger

	public boolean tileCanMove(int i, int mouvement) {

		// Si les tuiles sont de même valeur mais ne sont pas 1-1 ou 2-2
		if (val[i] == val[i + mouvement]
				&& ((val[i] + val[i + mouvement] != 2) && (val[i] + val[i + mouvement] != 4) && (val[i] != 0)))
			return true;

		else if (val[i] == 1 && val[i + mouvement] == 2)
			return true;

		else if (val[i] == 2 && val[i + mouvement] == 1)
			return true;

		else if (val[i] != 0 && val[i + mouvement] == 0) {
			return true;
		}

		// Sinon
		else {
			return false;
		}

	}

	// Verifie si le joueur peut bouger

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

	// Verifie si le plateau est complet

	public boolean isFull() {

		for (int i = 0; i < NB_CASES; i++) {
			if (val[i] == 0) {
				return false;
			}
		}
		return true;
	}

	// Deplace une tuile

	private void moveTile(int i, int mouvement) {
		val[i + mouvement] += val[i];
		val[i] = 0;
	}

	// Deplace tout le plateau

	private void moveEntireBoard(int mouvement) {
		// Random r = new Random();
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

		updateView();
	}

	// Gestion des evenements

	// Evenements claviers

	public class keyEvents implements KeyListener {

		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				myGame.dispose();
			}

			if (!playerCanMove()) {
				myGame.setLostStatut(true);
			}

			if (!myGame.getWinStatut() == true && !myGame.getLostStatut() == true) {

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

			}

			if (myGame.getLostStatut() == true || myGame.getWinStatut() == true) {
				if (!myGame.gameDone) {
					Threes.this.setFinalScore();
					Threes.this.resetAll();
					myGame.gameDone = true;
				}
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
		}

		public void mouseReleased(MouseEvent e) {

			if (!playerCanMove()) {
				myGame.setLostStatut(true);
			}

			if (!myGame.getWinStatut() == true && !myGame.getLostStatut() == true) {

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

			}

			if (myGame.getLostStatut() == true || myGame.getWinStatut() == true) {
				if (!myGame.gameDone) {
					Threes.this.setFinalScore();
					Threes.this.resetAll();
					myGame.gameDone = true;
				}
				myGame.init_ecran_fin(false);
			}
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}
}