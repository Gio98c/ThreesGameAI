package et3.threes;

import java.awt.Graphics2D;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Circular {

	private BufferedImage[] parties_menu;
	private AffineTransform transform;
	
	private static final double SCALE = 0.20;
	private static final double SPACE = 1.5;
	private static final int PARTIES = 4;
	private static double temp = SCALE*SPACE;
	
	public Circular() {
		parties_menu = new BufferedImage[PARTIES];
		// Chargement des images
		for (int i = 0; i < PARTIES; i++) {
			try {
				parties_menu[i] = ImageIO.read(new File("menu_circulaire.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void render(Graphics2D g) {
		
		for (int i = 0; i < PARTIES; i++) {

			transform = new AffineTransform();				
			
			switch(i){
			
				case 0:
					transform.translate(550 - parties_menu[i].getWidth() * SCALE / 2, 200 - (parties_menu[i].getHeight() * temp));
				break;
				
				case 1:
					transform.translate(550 + (parties_menu[i].getHeight() * temp), 200 - parties_menu[i].getWidth() * SCALE / 2);
				break;
				
				case 2:
					transform.translate(550 + parties_menu[i].getWidth() * SCALE / 2, 200 + (parties_menu[i].getHeight() * temp));
				break;
				
				case 3:
					transform.translate(550 - (parties_menu[i].getHeight() * temp), 200 + parties_menu[i].getWidth() * SCALE / 2);
				break;
			}
			

			transform.rotate(i * Math.PI / 2);
			transform.scale(SCALE, SCALE);
			g.drawImage(parties_menu[i], transform, null);
		}
	}

	public double getWidth() {
		return (parties_menu[0].getWidth() * SCALE);
	}
	
	public double getHeight() {
		return (parties_menu[0].getHeight() * temp);
	}
}
