package et3.threes;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

@Id("tile")
public class Tile_2D {

	public final static int TILES_SIZE = 100;
	public final static int TILES_ROUND = 20;
	public final static int TILES_MARGIN = 10;
	public final static int TEXT_HEIGHT = 60;
	
	private final static Font TILES_FONT = new Font("Arial", Font.PLAIN, 32);

	private final static Color TILES_1_BACKGROUNDCOLOR = new Color(0x11c7f5);
	private final static Color TILES_1_FOREGROUNDCOLOR = new Color(0xffffff);
	private final static Color TILES_2_BACKGROUNDCOLOR = new Color(0xf5116c);
	private final static Color TILES_2_FOREGROUNDCOLOR = new Color(0xffffff);
	private final static Color OTHER_TILES_BACKGROUNDCOLOR = new Color(0xffffff);
	private final static Color OTHER_TILES_FOREGROUNDCOLOR = new Color(0x000000);
	private final static Color EMPTY_TILES_COLOR = new Color(0x038787);

	@Param(0)
	private int val;
	@Param(1)
	private int x;
	@Param(2)
	private int y;
	protected RoundRectangle2D geom;
	
	// Constructeur
	
	public Tile_2D(int x_pos, int y_pos) {
		
		val = 0;
		x = x_pos;
		y = y_pos;
		geom = new RoundRectangle2D.Double(x, y, TILES_SIZE, TILES_SIZE, TILES_ROUND, TILES_ROUND);
	}
	
	// Renvoi la valeur de la tuile appelante
	
	public int getVal() {
		return val;
	}
	
	// Donne une valeur a la tuile appelante
	
	public void setVal(int value) {
		val = value;
	}
 
	// Donne la couleur de fond des tuiles

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

	// Donne la couleur des nombres des tuiles

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
	
	// Fonction qui trace la tuile avec les fonctions du contexte graphique 2D

	public void draw(Graphics2D g) {

		g.setColor(getBackground(this.val));
		g.fill(geom);
		g.setFont(TILES_FONT);
		g.setColor(getForeground(this.val));
		
		String s = Integer.toString(val);
		int textWidth = (int) g.getFontMetrics(TILES_FONT).stringWidth(s);


			g.drawString(s, (int) (geom.getX() + (TILES_SIZE - textWidth)/2), (int) (geom.getY() + TEXT_HEIGHT));
	}
}
