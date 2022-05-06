

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ChageurImage {

    //Méthode permettant de charger l'image par rapport au chemin passer comme arguments a la méthode
    public static BufferedImage chargeImage(String path) {
        try {

            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;

    }
}
