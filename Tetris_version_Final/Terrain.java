import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Terrain extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

    //Assets
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    
    //variable des image des blocs de couleurs, du fond d'écran du jeu, du button pause et recommencer
    private BufferedImage blocs, fond, pause, recommencer;

    //Variable constantes de la hauteur et de la largeur du terrain
    private final int terrainHauteur = 20, terrainLargeur = 10;

    //variable de la taille de un bloc = la taille du carré d'une couleur d'une pièce du tetris
    private final int blocTaille = 30;

    // double tableau de int qui représente le terrain de jeu de 20 de haut et 10 de large
    private int[][] terrain = new int[terrainHauteur][terrainLargeur];

    //tableau de forme qui contiendra toutes les formes différentes
    private Forme[] formes = new Forme[9];

    //  variable formeCourante représente la pièce que on est en train de jouer
    //  variable formeSuivante représente la pièce que on va avoir après celle courante
    private static Forme formeCourante, formeSuivante;

    // boucle du jeu. timer permet de rafraichir et de repeindre le jeu 
    private Timer timer;
    // FPS = image par seconde 60 est une vitessse normale
    private int FPS = 60;
    //60 fois par seconde c'est le délais
    private int delais = 1000 / FPS;

    // mouse events variables
    private int mouseX, mouseY;

    private boolean clicGauche = false;

    private Rectangle bordurePause, bordureRelancer;

    private boolean gamePaused = false;

    private boolean gameOver = false;

    // buttons press lapse
    private Timer buttonLapse = new Timer(300, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            buttonLapse.stop();
        }
    });

    // score
    private int score = 0;

    public Terrain() {
        // load Assets
        File cheminRelatif = new File(".");
        try {
            System.out.println("Le chemin relatif est : " + cheminRelatif.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(Titre.class.getName()).log(Level.SEVERE, null, ex);
        }

/*     Marche sur pc Vincent seulement
        blocs = ChageurImage.chargeImage( cheminRelatif + "\\src\\main\\java\\main\\tetrominos.png");

        fond = ChageurImage.chargeImage(cheminRelatif +"\\src\\main\\java\\main\\fond.png");
        pause = ChageurImage.chargeImage(cheminRelatif + "\\src\\main\\java\\main\\pause.png");
        recommencer = ChageurImage.chargeImage(cheminRelatif + "\\src\\main\\java\\main\\rafraichir.png");

        ChargeurSon musique = new ChargeurSon( cheminRelatif + "\\src\\main\\java\\main\\file.wav");
        
        */
        blocs = ChageurImage.chargeImage( cheminRelatif + "\\tetrominos.png");

        fond = ChageurImage.chargeImage(cheminRelatif +"\\fond.png");
        pause = ChageurImage.chargeImage(cheminRelatif + "\\pause.png");
        recommencer = ChageurImage.chargeImage(cheminRelatif + "\\rafraichir.png");

        ChargeurSon musique = new ChargeurSon( cheminRelatif + "\\music.wav");
        
        musique.loop();
        mouseX = 0;
        mouseY = 0;

        bordurePause = new Rectangle(350, 500, pause.getWidth(), pause.getHeight() + pause.getHeight() / 2);
        bordureRelancer = new Rectangle(350, 500 - recommencer.getHeight() - 20, recommencer.getWidth(),
                recommencer.getHeight() + recommencer.getHeight() / 2);

        // création de la bloucle du jeu
        timer = new Timer(delais, new BoucleJeu());
        //Créations des formes
        formes[0] = new Forme(new int[][]{
            {1, 1, 1, 1} // forme une barre de 4 carré;
        }, blocs.getSubimage(0, 0, blocTaille, blocTaille), this, 1);

        formes[1] = new Forme(new int[][]{
            {1, 1, 1},
            {0, 1, 0}, // forme un T;
        }, blocs.getSubimage(blocTaille, 0, blocTaille, blocTaille), this, 2);

        formes[2] = new Forme(new int[][]{
            {1, 1, 1},
            {1, 0, 0}, // Forme un L;
        }, blocs.getSubimage(blocTaille * 2, 0, blocTaille, blocTaille), this, 3);

        formes[3] = new Forme(new int[][]{
            {1, 1, 1},
            {0, 0, 1}, // Forme un J;
        }, blocs.getSubimage(blocTaille * 3, 0, blocTaille, blocTaille), this, 4);

        formes[4] = new Forme(new int[][]{
            {0, 1, 1},
            {1, 1, 0}, // Forme un S;
        }, blocs.getSubimage(blocTaille * 4, 0, blocTaille, blocTaille), this, 5);

        formes[5] = new Forme(new int[][]{
            {1, 1, 0},
            {0, 1, 1}, // Forme un Z;
        }, blocs.getSubimage(blocTaille * 5, 0, blocTaille, blocTaille), this, 6);

        formes[6] = new Forme(new int[][]{
            {1, 1},
            {1, 1}, // forme un carré;
        }, blocs.getSubimage(blocTaille * 6, 0, blocTaille, blocTaille), this, 7);

        formes[7] = new Forme(new int[][]{
            {1, 0},
            {0, 1},// nouvelle forme
        }, blocs.getSubimage(blocTaille * 5, 0, blocTaille, blocTaille), this, 6);
        formes[8] = new Forme(new int[][]{
            {1, 0},
            {1, 0},
            {0, 1},// nouvelle forme
        }, blocs.getSubimage(0, 0, blocTaille, blocTaille), this, 1);
    }

    private void miseAJour() {
        if (bordurePause.contains(mouseX, mouseY) && clicGauche && !buttonLapse.isRunning() && !gameOver) {
            buttonLapse.start();
            gamePaused = !gamePaused;
        }

        if (bordureRelancer.contains(mouseX, mouseY) && clicGauche) {
            lancerPartie();
        }

        if (gamePaused || gameOver) {
            return;
        }
        formeCourante.miseAJour();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(fond, 0, 0, null);

        for (int row = 0; row < terrain.length; row++) {
            for (int col = 0; col < terrain[row].length; col++) {
                //on peut dessiner l'image du carré d'une forme d'une tetris seulement s'il la 
                //du terrain a la ligne et a la colonne est différente de 0 
                if (terrain[row][col] != 0) {

                    //(terrain[row][col] - 1) * blocTaille permet de sélectionner une couleurs parmi celles de 
                    //l'image tiles -1 car index on commence a 0 et non a 1
                    g.drawImage(blocs.getSubimage((terrain[row][col] - 1) * blocTaille,
                            0, blocTaille, blocTaille), col * blocTaille, row * blocTaille, null);
                }

            }
        }
        for (int row = 0; row < formeSuivante.getCoords().length; row++) {
            for (int col = 0; col < formeSuivante.getCoords()[0].length; col++) {
                if (formeSuivante.getCoords()[row][col] != 0) {
                    g.drawImage(formeSuivante.getBloc(), col * 30 + 320, row * 30 + 50, null);
                }
            }
        }
        formeCourante.render(g);

        //afffichage du bouton pour mettre en pause la partie
        if (bordurePause.contains(mouseX, mouseY)) {
            g.drawImage(pause.getScaledInstance(pause.getWidth() + 3, pause.getHeight() + 3, BufferedImage.SCALE_DEFAULT),
                    bordurePause.x + 3, bordurePause.y + 3, null);
        } else {
            g.drawImage(pause, bordurePause.x, bordurePause.y, null);
        }

        //affichage du bouton pour relancer une partie
        if (bordureRelancer.contains(mouseX, mouseY)) {
            g.drawImage(recommencer.getScaledInstance(recommencer.getWidth() + 3, recommencer.getHeight() + 3,
                    BufferedImage.SCALE_DEFAULT), bordureRelancer.x + 3, bordureRelancer.y + 3, null);
        } else {
            g.drawImage(recommencer, bordureRelancer.x, bordureRelancer.y, null);
        }

        //si on a cliquer sur le bouton pause on met en pause le jeu
        if (gamePaused) {
            String gamePausedString = "GAME PAUSED";
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gamePausedString, 35, Ecran.HAUTEUR / 2);
        }
        //si on a perdu on dessine game over en blanc au milieu du terrain
        if (gameOver) {
            String gameOverString = "GAME OVER";
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 50));
            g.drawString(gameOverString, 50, Ecran.HAUTEUR / 2);
        }
        g.setColor(Color.RED);

        g.setFont(new Font("Georgia", Font.BOLD, 20));

        g.drawString("SCORE", Ecran.LARGEUR - 125, Ecran.HAUTEUR / 2);
        g.drawString(score + "", Ecran.LARGEUR - 125, Ecran.HAUTEUR / 2 + 30);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(0, 0, 0, 100));

        // on dessine les lignes horizontales qui délimite le terrain en hauteur = 20 
        for (int i = 0; i <= terrainHauteur; i++) {
            g2d.drawLine(0, i * blocTaille, terrainLargeur * blocTaille, i * blocTaille);
        }
        // on dessine les lignes verticales qui délimite le terrain en largeur = 10 
        for (int j = 0; j <= terrainLargeur; j++) {
            g2d.drawLine(j * blocTaille, 0, j * blocTaille, terrainHauteur * 30);
        }
    }

    public void setProchaineForme() {
        //on génère un int random entre 0 et la taille du tableau des formes
        int index = (int) (Math.random() * formes.length);
        //on affecte a la variable formeSuivante la prochaine forme
        formeSuivante = new Forme(formes[index].getCoords(), formes[index].getBloc(), this, formes[index].getCouleur());
    }

    public void setFormeCourante() {
        formeCourante = formeSuivante;
        setProchaineForme();

        for (int row = 0; row < formeCourante.getCoords().length; row++) {
            for (int col = 0; col < formeCourante.getCoords()[0].length; col++) {
                if (formeCourante.getCoords()[row][col] != 0) {
                    //si on a atteint le haut du terrain
                    if (terrain[formeCourante.getY() + row][formeCourante.getX() + col] != 0) {
                        //on a perdu 
                        gameOver = true;
                    }
                }
            }
        }

    }

    public int[][] getTerrain() {
        return terrain;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {//flèche du haut
            formeCourante.rotationForme();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {// flèche droite
            formeCourante.setDeltaX(1);//1 car on doit augmenter la position en X de 1 de la pièce
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {//flèche gauche
            formeCourante.setDeltaX(-1);//-1 car on doit diminuer la position en X de 1 de la pièce
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {//on appui sur la flèche bas 
            //Donc on accèlère la vitesse de descente de la pièce
            formeCourante.augmenterVitesse();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {//on relache l'appui sur la touche du bas
            //Donc on remet la vitesse de descente de base a la pièce
            formeCourante.diminuerVitesse();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void lancerPartie() {
        arreterPartie();
        setProchaineForme();
        setFormeCourante();
        gameOver = false;
        timer.start();

    }

    public void arreterPartie() {
        score = 0;

        for (int row = 0; row < terrain.length; row++) {
            for (int col = 0; col < terrain[row].length; col++) {
                terrain[row][col] = 0;
            }
        }
        timer.stop();
    }

    class BoucleJeu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            miseAJour();
            repaint();
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            clicGauche = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            clicGauche = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void ajouterScore() {
        score += 100;
    }

}
