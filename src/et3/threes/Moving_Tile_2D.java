package et3.threes;

public class Moving_Tile_2D extends Tile_2D {

	public final static int TILES_SPEED = 20;

	private int translation_x;
	private int translation_y;
	private boolean moving;

	// Constructeur

	public Moving_Tile_2D(int x, int y) {

		super(x + ThreesGraphics.XOFFSET, y);
		translation_x = 0;
		translation_y = 0;

	}

	// Donne les coordonnées du mouvement a effectuer

	public void setLocation(int x, int y) {
		moving = true;
		translation_x = x;
		translation_y = y;
	}

	// Effectue une translation

	public void mouvement_to(int x, int y) {
		geom.setFrame(x, y, geom.getWidth(), geom.getHeight());
	}

	// Retourne l'etat de la tuile mouvante pour savoir si on doit l'afficher ou
	// non

	public boolean moving() {
		return moving;
	}

	// Fonction qui s'occupe des mouvements

	public void move() {

		if (translation_x > TILES_MARGIN) {
			mouvement_to(((int) geom.getX() - TILES_SPEED), (int) geom.getY());
			translation_x -= TILES_SPEED;
		}

		else if (translation_x < -TILES_MARGIN) {
			mouvement_to((int) (geom.getX() + TILES_SPEED), (int) geom.getY());
			translation_x += TILES_SPEED;
		}

		else if (translation_y > TILES_MARGIN) {
			mouvement_to((int) geom.getX(), (int) (geom.getY() - TILES_SPEED));
			translation_y -= TILES_SPEED;
		}

		else if (translation_y < -TILES_MARGIN) {
			mouvement_to((int) geom.getX(), (int) (geom.getY() + TILES_SPEED));
			translation_y += TILES_SPEED;
		}

		else {

			if (translation_x > 0)
				mouvement_to((int) (geom.getX() - TILES_MARGIN), (int) geom.getY());

			else if (translation_x < 0)
				mouvement_to((int) (geom.getX() + TILES_MARGIN), (int) geom.getY());

			else if (translation_y > 0)
				mouvement_to((int) geom.getX(), (int) (geom.getY() - TILES_MARGIN));

			else if (translation_y < 0)
				mouvement_to((int) geom.getX(), (int) (geom.getY() + TILES_MARGIN));

			moving = false;
		}
	}
}
