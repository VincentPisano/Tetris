/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ChargeurSon {

    private Clip clip;
    //Méthode permettant de charger le son par rapport au chemin passer comme arguments a la méthode

    public ChargeurSon(String filepath) {
        System.out.println(filepath);
        File file = new File(filepath);
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            clip.open(inputStream);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void play() {
        if (clip.isActive()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop() {
        clip.stop();
    }

    public void loop() {
        if (!clip.isActive()) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

    }

    public boolean getActive() {
        return clip.isActive();
    }
}
