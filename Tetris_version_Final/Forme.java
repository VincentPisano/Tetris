
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Forme {

    //variable qui stocke la couleurs sous forme de int
    private int couleur;
    //
    private int x, y;

    private long temps, dernierTemps;

    private int vitesseNormale = 600, vitesseRapide = 50;

    private int vitesseCourante;

    private BufferedImage bloc;

    private int[][] coords;

    private int[][] reference;
    // variable qui permet de savoir s'il faut déplacer en X la position de la pièce si on appui sur 
    //flèche gauche ou droite
    private int deltaX;

    private Terrain terrain;

    private boolean collision = false, deplacementX = false;

    public Forme(int[][] coords, BufferedImage bloc, Terrain terrain, int couleur) {
        this.coords = coords;
        this.bloc = bloc;
        this.terrain = terrain;
        this.couleur = couleur;
        deltaX = 0;
        //pourquoi x = 4 pour faire apparaittre la pièce au centre du terrain
        x = 4;
        // pourquoi y = 0 pour que apparaitre la pièce tout en haut du terrain
        y = 0;
        vitesseCourante = vitesseNormale;
        temps = 0;
        //affecte dernierTemps avec le temps en millisecondes
        dernierTemps = System.currentTimeMillis();
        reference = new int[coords.length][coords[0].length];

        System.arraycopy(coords, 0, reference, 0, coords.length);

    }

    public void miseAJour() {
        deplacementX = true;
        //temps actuel 
        temps += System.currentTimeMillis() - dernierTemps;
        //affecte dernierTemps avec le temps en millisecondes
        dernierTemps = System.currentTimeMillis();

        //s'il y a une collision
        if (collision) {
            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[0].length; col++) {
                    //on affecte la couleur seulement si les coordonnées a la ligne et a la colonne sont différents de 0
                    if (coords[row][col] != 0) {
                        // x et y sont  les position en haut a gauche de la pièce
                        //on fixe la couleur de la pièce quand il y a collision a la couleur de la pièce courante
                        terrain.getTerrain()[y + row][x + col] = couleur;
                    }
                }
            }
            verifierLigne();

            terrain.setFormeCourante();
        }

        //vérifier si on rentre en collision avec la droite ou la gauche du terrain
        if (!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0)) {

            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[row].length; col++) {
                    //on affecte la couleur seulement si les coordonnées a la ligne et a la colonne sont différents de 0
                    if (coords[row][col] != 0) {
                        //on affecte le déplacement en X seulement si valeur du Terrain en ligne et a la colonne 
                        //sont différents de 0
                        //pk [x + deltaX + col] car on a gauche ou a droite s'il y a une autre pièce alors on 
                        //met que le deplacementX = false;
                        if (terrain.getTerrain()[y + row][x + deltaX + col] != 0) {
                            deplacementX = false;
                        }

                    }
                }
            }

            //S'il est possible de se déplacer vers la gauche ou la droite
            if (deplacementX) {
                x += deltaX;// on ajoute 1 ou -1 au x de la pièce que on joue
            }

        }

        //vérifier si on rentre en collision avec le bas du terrain
        if (!(y + 1 + coords.length > 20)) {

            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[row].length; col++) {
                    //on affecte la couleur seulement si les coordonnées a la ligne et a la colonne sont 
                    //différents de 0
                    if (coords[row][col] != 0) {
                        //on affecte la collision a vrai seulement si valeur du Terrain en ligne et a la colonne
                        //sont différents de 0
                        //pk y + 1 + row car on regarde 1 ligne en dessous de notre pièce et s'il y a une autre 
                        //pièce alors collision = vrai
                        if (terrain.getTerrain()[y + 1 + row][x + col] != 0) {
                            collision = true;
                        }
                    }
                }
            }

            // si le temps actuel est plus grand que le vitesseCourante = 600 ou vitesseCourante = 50 millisecondes     
            //si on appui sur fleche bas
            if (temps > vitesseCourante) {
                y++;// augmente la valeur en y de la pièce = la fait descendre de 1 case en 1 case
                //on remet le temps a 0
                temps = 0;
            }
        } else {
            //si la pièce a atteint le bas du terrai on passe la variable collision a vrai
            collision = true;
        }

        deltaX = 0;
    }

    //permet de dessiner la pièce sur le terrain
    public void render(Graphics g) {

        for (int row = 0; row < coords.length; row++) {
            for (int col = 0; col < coords[0].length; col++) {
                //s'il y a une pièce on la dessine seulement si les coordonnées en ligne et a la colonne sont différents de 0
                if (coords[row][col] != 0) {
                    g.drawImage(bloc, col * 30 + x * 30, row * 30 + y * 30, null);
                }
            }
        }

        for (int row = 0; row < reference.length; row++) {
            for (int col = 0; col < reference[0].length; col++) {
                if (reference[row][col] != 0) {
                    g.drawImage(bloc, col * 30 + 320, row * 30 + 160, null);
                }

            }

        }

    }

    private void verifierLigne() {
        int size = terrain.getTerrain().length - 1;

        for (int i = terrain.getTerrain().length - 1; i > 0; i--) {
            int count = 0;
            for (int j = 0; j < terrain.getTerrain()[0].length; j++) {
                //s'il le carré du terrain a la ligne et la colonne est occupé par une pièce
                if (terrain.getTerrain()[i][j] != 0) {
                    count++;//augmenter compteur de 1
                }

                //permet de enlever une ligne quand elle est pleine
                terrain.getTerrain()[size][j] = terrain.getTerrain()[i][j];
            }
            //si la ligne est pleine alors on augmente le score de 100 
            if (count == 10) {
                terrain.ajouterScore();
            }
            if (count < terrain.getTerrain()[0].length) {
                size--;
            }
        }

    }

    //méthode permettant la rotation d'une pièce tetris
    public void rotationForme() {

        //matrice = tableau de double int qui sera alimenter par les nouvelles coordonnées de la pièce une 
        //fois transposer puis le lignes inverser
        int[][] formeRetrouner = null;

        formeRetrouner = transposerMatrice(coords);

        formeRetrouner = inverserLignes(formeRetrouner);

        //annule la rotation de la pièce si après la rotation la nouvelle position de 
        //la pièce dépasse les bordure du terrain en X ou en Y 
        if ((x + formeRetrouner[0].length > 10) || (y + formeRetrouner.length > 20)) {
            //return rien car on ne retourne pas les nouvelles coordonnées de la pièce sinon bugs
            return;
        }

        //permet d'enpêcher la rotation si la nouvelle position de la forme chevauche une autre pièce
        for (int row = 0; row < formeRetrouner.length; row++) {
            for (int col = 0; col < formeRetrouner[row].length; col++) {
                if (formeRetrouner[row][col] != 0) {
                    //si le if en dessous est vrai alors il y aurais un chevauchement entre les 2 pièces après 
                    //la rotation de la pièce courante
                    if (terrain.getTerrain()[y + row][x + col] != 0) {
                        //c'est pour cela que on retourne rien
                        return;
                    }
                }
            }
        }
        coords = formeRetrouner;
    }

    //méthode qui retourne un double tableau de int qui est la transposition de la matrice en  matrice
    //ce qu'il y avait a la ligne i et a la colonne j devient ce qu'il y a a la ligne j et a la colonne i
    //voici un site qui explique simplement la transposition 
    //https://www.javatpoint.com/java-program-to-transpose-matrice
    private int[][] transposerMatrice(int[][] matrice) {
        //on créer la matrice temp que on va retourner qui sera la transposition de la mtrice matrice
        int[][] temp = new int[matrice[0].length][matrice.length];

        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[0].length; j++) {
                temp[j][i] = matrice[i][j];
            }
        }
        return temp;
    }

    //méthode qui inverse les valeurs d'une lignes
    //très bon site explicatif de cette méthode
    //https://www.java67.com/2016/10/3-ways-to-reverse-array-in-java-coding-interview-question.html
    private int[][] inverserLignes(int[][] matrice) {
        //on prend le milieu de la ligne
        int milieu = matrice.length / 2;
        //tant que i est inférieur au milieu
        for (int i = 0; i < milieu; i++) {
            //on créer une matrice temp qui prend comme valeur la valeur de la case i de matrice
            int[] temp = matrice[i];
            //dans le première case de matrice je met la dernière valeur
            matrice[i] = matrice[matrice.length - i - 1];
            //dans la dernière case de matrice je met la preemière valeur qui était sauvergarder dans temp
            matrice[matrice.length - i - 1] = temp;
        }

        return matrice;

    }

    public int getCouleur() {
        return couleur;
    }

    //setteur de la variable DeltaX 
    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    //méthode qui fixe la vitesseCourante a la vitesseRapide = 50
    public void augmenterVitesse() {
        vitesseCourante = vitesseRapide;
    }

    //méthode qui fixe la vitesseCourante a la vitesseNormale = 600
    public void diminuerVitesse() {
        vitesseCourante = vitesseNormale;
    }

    public BufferedImage getBloc() {
        return bloc;
    }

    public int[][] getCoords() {
        return coords;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
