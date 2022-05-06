

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Titre extends JPanel implements MouseListener, MouseMotionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //souris cordonnées en x et en y 
    private int sourisX, sourisY;
    //Bordure du bouton jouer
    private Rectangle bordureJouer;
    //boolean qui détecte le clique gauche
    private boolean cliqueGauche = false;
    //BufferedImage pour le titre les règles et le bouton jouer
    private BufferedImage titre, regles, jouer;
    //variable de la classe Ecran
    private Ecran ecran;
    //Tableau d'image des 2 bouton jouer
    private BufferedImage[] boutonJouer = new BufferedImage[2];
    //timer
    private Timer timer;

    public Titre(Ecran window) {
        
        File cheminRelatif = new File(".");
        try {
            System.out.println("Le chemin relatif est : " + cheminRelatif.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(Titre.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            /*marche sur pc a vincent seulement 
             titre = ImageIO.read(new File( cheminRelatif +"\\src\\main\\java\\main\\Titre.png"));
            regles = ImageIO.read(new File(cheminRelatif +"\\src\\main\\java\\main\\regles.png"));
            jouer = ImageIO.read(new File(cheminRelatif +"\\src\\main\\java\\main\\jouer.png"));
            */
            titre = ImageIO.read(new File( cheminRelatif +"\\Titre.png"));
            regles = ImageIO.read(new File(cheminRelatif +"\\regles.png"));
            jouer = ImageIO.read(new File(cheminRelatif +"\\jouer.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //timer en milliseconde qui toutes les 1000/60 millisecondes rafrachit l'ecran du JPanel
        timer = new Timer(1000 / 60, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }

        });
        //lance le timer
        timer.start();
        sourisX = 0;
        sourisY = 0;

        boutonJouer[0] = jouer.getSubimage(0, 0, 100, 80);
        boutonJouer[1] = jouer.getSubimage(100, 0, 100, 80);

        //ancienne valeur de la bordure j'ai mis -70 a la place de -100  et -50 par -40car il y avait un 
        //décalage entre la détection du passage de la souris et le changement de l'image en X et en  Y 
        //bordureJouer = new Rectangle(Ecran.LARGEUR/2 - 50, Ecran.HAUTEUR/2 - 100, 100, 80);
        bordureJouer = new Rectangle(Ecran.LARGEUR / 2 - 45, Ecran.HAUTEUR / 2 - 70, 100, 80);
        this.ecran = window;

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //si on clique sur l'imager lancer le Tetris
        if (cliqueGauche && bordureJouer.contains(sourisX, sourisY)) {
            ecran.lancerTetris();
        }

        //on fixe la couleur du pinceau a noir
        g.setColor(Color.BLACK);
        //on peint le fond du rectangle en nour
        g.fillRect(0, 0, Ecran.LARGEUR, Ecran.HAUTEUR);
        //on affiche l'image du Titre
        g.drawImage(titre, Ecran.LARGEUR / 2 - titre.getWidth() / 2, Ecran.HAUTEUR / 2 - titre.getHeight() / 2 - 200, null);
        //on affiche l'image des règles
        g.drawImage(regles, Ecran.LARGEUR / 2 - regles.getWidth() / 2,
                Ecran.HAUTEUR / 2 - regles.getHeight() / 2 + 150, null);
        //on affiche les images du bouton jouer
        if (bordureJouer.contains(sourisX, sourisY)) {
            g.drawImage(boutonJouer[0], Ecran.LARGEUR / 2 - 50, Ecran.HAUTEUR / 2 - 100, null);
        } else {
            g.drawImage(boutonJouer[1], Ecran.LARGEUR / 2 - 50, Ecran.HAUTEUR / 2 - 100, null);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override//détection d'une presse sur le clique gauche
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            cliqueGauche = true;
        }
    }

    @Override//détection du relachement sur le clique gauche
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            cliqueGauche = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override//détection du dragde la souris
    public void mouseDragged(MouseEvent e) {
        sourisX = e.getX();
        sourisY = e.getY();
    }

    @Override//détection d'une mouvement de la souris
    public void mouseMoved(MouseEvent e) {
        sourisX = e.getX();
        sourisY = e.getY();
    }
}
