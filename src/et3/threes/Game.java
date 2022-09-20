package et3.threes;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

// Classe principale du jeu, initialise les menus

public class Game extends JFrame {

	private static final long serialVersionUID = 1L;

	private final static Dimension WIDOW_SIZE = new Dimension(600, 500);
	private final static Dimension BUTTON_SIZE = new Dimension(280, 50);

	private final static Font TITLE_FONT = new Font("Arial", Font.BOLD, 40);
	private final static Font SCORES_FONT = new Font("Arial", Font.PLAIN, 28);

	private boolean isWin = false;
	private boolean isLost = false;
	private int score;
	private int[] score_tab;
	final private int win_score = 108;

	private JButton start_v1;
	private JButton start_v2;
	private JButton goTo_meilleursScores;
	private JButton exit_menu;
	private JButton replay;
	private JButton exit_game;
	private JButton toMenu;

	private String scoreFile = "scores.txt";

	private JPanel ecran_menu;
	private JPanel menu_buttons;
	private JPanel start_buttons;
	private JPanel best_and_exit_buttons;
	private JPanel end_buttons;
	private JPanel ecran_fin;
	private JPanel ecran_meilleursScores;
	private JPanel bestScores_holder;
	private JPanel best_buttons;
	private JPanel scores_holder;

	private JLabel titre;
	private JLabel end_label;
	private JLabel score_label;
	private JLabel bestScores_label;
	private JLabel[] topScores;

	protected boolean gameDone = false;

	private Timer timer;
	private ThisTimerTask timerTask;

	private static Handler handler;

	// Getters et Setters

	public int getWinScore() {
		return this.win_score;
	}

	public void setScore(int val) {
		this.score = val;
	}

	public boolean getWinStatut() {
		return this.isWin;
	}

	public void setWinStatut(boolean b) {
		this.isWin = b;
	}

	public boolean getLostStatut() {
		return this.isLost;
	}

	public void setLostStatut(boolean b) {
		this.isLost = b;
	}

	// Classe interne

	public class ThisTimerTask extends TimerTask {

		private Game myGame;

		public ThisTimerTask(Game game) {
			super();
			myGame = game;
		}

		public void run() {
			myGame.repaint();
		}
	}

	// Initialisation des menus

	// Menu principal

	public void init_menu_base() {

		// Proprietes de la JFrame
		setTitle("Threes Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDOW_SIZE);
		setVisible(true);
		setLocationRelativeTo(null);
		setFocusable(true);
		setResizable(false);

		timer = new Timer();
		timerTask = new ThisTimerTask(this);

		// Creation du JPanel pour le menu

		ecran_menu = new JPanel();
		menu_buttons = new JPanel();
		
		start_buttons = new JPanel();
		best_and_exit_buttons = new JPanel();
		
		titre = new JLabel(new ImageIcon("threes.jpg"));
		
		start_v1 = new JButton("Gioca alla versione di base");
		start_v1.addActionListener(new startBaseEvent());
		start_v1.setPreferredSize(BUTTON_SIZE);

		start_v2 = new JButton("Gioca alla versione avanzata");
		start_v2.addActionListener(new startAdvancedEvent());
		start_v2.setPreferredSize(BUTTON_SIZE);

		goTo_meilleursScores = new JButton("Vedi i punteggi migliori");
		goTo_meilleursScores.addActionListener(new toBestScores());
		goTo_meilleursScores.setPreferredSize(BUTTON_SIZE);
		
		exit_menu = new JButton("Esci");
		exit_menu.addActionListener(new exitEvent());
		exit_menu.setPreferredSize(BUTTON_SIZE);
		
		start_buttons.add(start_v1);
		start_buttons.add(start_v2);
		start_buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		best_and_exit_buttons.add(goTo_meilleursScores);
		best_and_exit_buttons.add(exit_menu);
		
		menu_buttons.setLayout(new BoxLayout(menu_buttons, BoxLayout.Y_AXIS));
		menu_buttons.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
		menu_buttons.add(start_buttons);
		menu_buttons.add(best_and_exit_buttons);
		
		ecran_menu.add(BorderLayout.NORTH, titre);
		ecran_menu.add(BorderLayout.SOUTH, menu_buttons);

		// On donne au ContentPane de la JFrame le menu créer

		this.setContentPane(ecran_menu);
		this.revalidate();
	}

	// Menu de fin de partie

	public void init_ecran_fin(boolean won) {

		// On initialise les elements
		end_buttons = new JPanel();
		
		replay = new JButton("Rigioca");
		replay.setPreferredSize(BUTTON_SIZE);

		exit_game = new JButton("Esci");
		exit_game.setPreferredSize(BUTTON_SIZE);
		
		end_buttons.add(replay);
		end_buttons.add(exit_game);
		end_buttons.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
		
		String s = won ? "you_win.png" : "you_lose.png";
		end_label = new JLabel(new ImageIcon(s));
		
		score_label = new JLabel("Punteggio : " + String.valueOf(score), SwingConstants.CENTER);
		score_label.setFont(TITLE_FONT);
		score_label.setPreferredSize(new Dimension(this.getWidth(),150));

		replay.addActionListener(new replayEvent());
		exit_game.addActionListener(new exitEvent());

		refreshScoreFile();

		ecran_fin = new JPanel();
		ecran_fin.add(BorderLayout.NORTH, end_label);
		ecran_fin.add(BorderLayout.NORTH, score_label);
		ecran_fin.add(BorderLayout.SOUTH, end_buttons);

		this.setContentPane(ecran_fin);
		this.revalidate();
	}

	// Menu des meilleurs scores

	public void init_ecran_meilleursScores() {

		// Initialisation des composantes

		ecran_meilleursScores = new JPanel();
		best_buttons = new JPanel();
		bestScores_holder = new JPanel();
		
		bestScores_label = new JLabel(new ImageIcon("HiScore.jpg"));
		bestScores_label.setFont(SCORES_FONT);
		
		bestScores_holder.setPreferredSize(new Dimension(this.getWidth(),130));
		bestScores_holder.add(bestScores_label);
		
		toMenu = new JButton("Ritorna al menù");
		toMenu.setPreferredSize(BUTTON_SIZE);
		toMenu.addActionListener(new toMenuEvent());
		
		best_buttons.add(toMenu);
		best_buttons.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));
		
		scores_holder = new JPanel();
		topScores = new JLabel[3];
		
		for (int i = 0; i < 3; i++) {
			topScores[i] = new JLabel();
		}

		readScoreFile();
		
		ecran_meilleursScores.add(BorderLayout.NORTH, bestScores_holder);
		ecran_meilleursScores.add(BorderLayout.CENTER, scores_holder);
		ecran_meilleursScores.add(BorderLayout.SOUTH, best_buttons);

		// On change le ContentPane de la JFrame pour afficher l'ecran des
		// meilleurs scores

		this.setContentPane(ecran_meilleursScores);
		this.revalidate();
	}

	// Fonctions utilitaires

	// Retour au menu et reinitialisation des composantes du jeu

	public void replay() {
		score = 0;
		isLost = false;
		isWin = false;
		gameDone = false;

		this.setContentPane(ecran_menu);
		this.revalidate();
	}

	// Actualise le fichier des meilleurs scores

	public void refreshScoreFile() {

		score_tab = new int[3];
		int[] temp_tab = new int[3];
		int j = 0;
		boolean ajoute = false;

		try {
			FileReader fileReader = new FileReader(scoreFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			for (int i = 0; i < score_tab.length; i++) {
				try {
					score_tab[i] = Integer.parseInt(bufferedReader.readLine());
				} catch (NumberFormatException e) {
					score_tab[i] = 0;
				}
			}

			for (int i = 0; i < temp_tab.length; i++) {

				if (j == 0) {

					if (this.score > score_tab[j] && !ajoute) {
						temp_tab[i] = this.score;
						ajoute = true;
					}

					else {
						temp_tab[i] = score_tab[j];
						j++;
					}
				}

				else {
					if (this.score > score_tab[j] && !ajoute && (this.score < score_tab[j - 1])) {
						temp_tab[i] = this.score;
						ajoute = true;
					}

					else {
						temp_tab[i] = score_tab[j];
						j++;
					}
				}
			}

			score_tab = temp_tab.clone();

			bufferedReader.close();
		}

		catch (FileNotFoundException e) {
			System.out.println("Impossibile aprire il file '" + scoreFile + "' perché non esiste, il file è stato creato.");
			try {
				FileWriter fileWriter = new FileWriter(scoreFile);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				for (int i = 0; i < score_tab.length; i++) {
					if (i == 0) {
						bufferedWriter.write(this.score);
						score_tab[i] = this.score;
					}

					else
						bufferedWriter.write(String.valueOf(0));

					bufferedWriter.newLine();
				}

				bufferedWriter.close();
			}

			catch (IOException ex) {
				System.out.println("Errore durante la lettura del file '" + scoreFile + "'");
			}
		}

		catch (IOException ex) {
			System.out.println("Errore durante la lettura del file '" + scoreFile + "'");
		}

		try {
			FileWriter fileWriter = new FileWriter(scoreFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (int i = 0; i < score_tab.length; i++) {
				bufferedWriter.write(String.valueOf(score_tab[i]));
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
		}

		catch (IOException ex) {
			System.out.println("Errore durante la lettura del file '" + scoreFile + "'");
		}
	}

	// Lit le fichier des meilleurs scores

	public void readScoreFile() {
		try {
			FileReader fileReader = new FileReader(scoreFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			for (int i = 0; i < 3; i++) {
				topScores[i].setText("Top " + (i + 1) + " - " + bufferedReader.readLine());
				topScores[i].setFont(SCORES_FONT);
				scores_holder.add(topScores[i]);
			}

			scores_holder.setLayout(new BoxLayout(scores_holder, BoxLayout.Y_AXIS));
			scores_holder.setPreferredSize(new Dimension(400, 100));
			scores_holder.setVisible(true);
			bufferedReader.close();
		}

		catch (FileNotFoundException e) {
			System.out.println("Impossibile aprire il file '" + scoreFile + "'");
		}

		catch (IOException ex) {
			System.out.println("Errore durante la lettura del file '" + scoreFile + "'");
		}
	}

	public ThisTimerTask getTimerTaskRef() {

		ThisTimerTask temp = new ThisTimerTask(this);
		return temp;
	}
	// Gestion des evenements

	// Lancement de la version de base

	public class startBaseEvent implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			Threes base_game = new Threes(Game.this);
			base_game.launch();
			base_game.toBoard();

			Game.this.setContentPane(base_game);
			Game.this.revalidate();
		}
	}

	// Lancement de la version Java2D

	public class startAdvancedEvent implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			handler = new DesktopHandler(new DLV2DesktopService("lib/dlv-2.1.1-win64.exe"));

			OptionDescriptor option = new OptionDescriptor("-n 0 --filter scelta/1");
			handler.addOption(option);

			try {
				ASPMapper.getInstance().registerClass(Tile_2D.class);
			} catch (ObjectNotValidException | IllegalAnnotationException ex) {
				ex.printStackTrace();
			}


			ThreesGraphics advanced_game = new ThreesGraphics(Game.this);
			advanced_game.init();
			advanced_game.randomFirstTiles();


			InputProgram facts = new ASPInputProgram();

			/*for(Tile_2D i : advanced_game.getArrayTile()) {
				//if(i.getVal() != 0) {
					try {
						fixedProgram.addObjectInput(new Tile_2D(i.getVal(), i.getX(), i.getY()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				//}
			}*/
			String id = "id";
			int cont = 0;

			for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++) {
					try {
						facts.addObjectInput(new MatriceIA(id+String.valueOf(cont++), i, j, advanced_game.tileAt(i, j).getVal()));
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			}

			handler.addProgram(facts);

			try {
				ASPMapper.getInstance().registerClass(Scelta.class);
			} catch (ObjectNotValidException | IllegalAnnotationException ex) {
				throw new RuntimeException(ex);
			}


			InputProgram program = new ASPInputProgram();
			program.addFilesPath("src/et3/encoding/programDLV");
			handler.addProgram(program);

			Output o = handler.startSync();
			AnswerSets answerSets = (AnswerSets) o;

			String scelta = "";
			for(AnswerSet a : answerSets.getAnswersets()) {
				try {
					for(Object obj : a.getAtoms()) {
						if(obj instanceof Scelta) {
							Scelta s = (Scelta) obj;
							scelta = s.getScelta();
							System.out.println(s);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			switch (scelta) {
				case "destra":
					// RIGHT
					advanced_game.moveEntireBoard(1);
					break;
				case "sinistra":
					// LEFT
					advanced_game.moveEntireBoard(-1);
					break;
				case "giu":
					// DOWN
					advanced_game.moveEntireBoard(4);
					break;
				case "su":
					// UP
					advanced_game.moveEntireBoard(-4);
					break;
				default:
					setLostStatut(true);
					return;
			}

			Game.this.setContentPane(advanced_game);
			Game.this.revalidate();

			advanced_game.repaint();
			
			// Fixe la frequence de rafraichissement de la fenetre a 60Hz
			
			timer = new Timer();
			timerTask = Game.this.getTimerTaskRef();
			timer.scheduleAtFixedRate(timerTask, 0, 7);
		}
	}

	// Lancement du menu des meilleurs scores

	public class toBestScores implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			init_ecran_meilleursScores();
		}
	}

	// Retour au menu principal

	public class toMenuEvent implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Game.this.setContentPane(ecran_menu);
			Game.this.revalidate();
		}
	}

	// Retour au menu principal et réinitialisation du jeu

	public class replayEvent implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			replay();
		}
	}

	// Quitter le jeu

	public class exitEvent implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	// Main

	public static void main(String[] args) {

		//WINDOWS
		//DLV2DesktopService service = new DLV2DesktopService("/lib/dlv-2.1.1-win64.exe");
		//LINUX
		//DLV2DesktopService service = new DLV2DesktopService("/lib/dlv-2.1.1-linux-x86_64");

		Game game = new Game();
		game.init_menu_base();
	}

}
