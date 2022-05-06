
import javax.swing.JFrame;

public class Ecran {
    //On peut changer la hauteur et la largeur

    public static final int LARGEUR = 450, HAUTEUR = 630;

    private Terrain terrain;
    private Titre titre;
    private JFrame ecran;

    //Création et affichage de l'écran d'accueil du jeu de tetris
    public Ecran() {

        ecran = new JFrame("Tetris");
        //Fixe la taille de la JFrame a la hauteur et largeur prédéfini
        ecran.setSize(LARGEUR, HAUTEUR);
        ecran.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //L'affiche au centre de l'écran
        ecran.setLocationRelativeTo(null);
        //rend impossible le changement de taille l'écran de jeu une fois lancer
        ecran.setResizable(false);

        //création du terrain de jeu        
        terrain = new Terrain();

        //création de l'écran de titre = accueil du jeu de tetris
        titre = new Titre(this);

        //Ajout d'un écouteur sur le clavier
        ecran.addKeyListener(terrain);
        //Ajout d'un écouteur sur les mouvements de la souris
        ecran.addMouseMotionListener(titre);
        //Ajout d'un écouteur sur le clique de la souris
        ecran.addMouseListener(titre);

        ecran.add(titre);

        ecran.setVisible(true);
    }

    //Méthode nous permettant de lancer le jeu de tetris en quittant l'écran d'accueil.
    public void lancerTetris() {
        //quitter écran titre = accueil
        ecran.remove(titre);
        ecran.addMouseMotionListener(terrain);
        ecran.addMouseListener(terrain);
        //ajout de l'écran de jeu terrain a la JFrame
        ecran.add(terrain);
        //On lance le jeu
        terrain.lancerPartie();
        //Rafraichir l'écran avec les MAJ comme le terrain 
        ecran.revalidate();
    }

    public static void main(String[] args) {
        new Ecran();
    }

}
