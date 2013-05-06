package com.javathreads.example.typegame;

import java.awt.Dimension;
import java.awt.Graphics;

public class AnimatedCharacterDisplayCanvas extends CharacterDisplayCanvas 
                implements CharacterListener, Runnable {

    private volatile boolean done = false;
    private int curX = 0;
    
    public AnimatedCharacterDisplayCanvas() {
    }
    
    public AnimatedCharacterDisplayCanvas(CharacterSource cs) {
        super(cs);
    }
    
    @Override
    public synchronized void newCharacter(CharacterEvent ce) {
        curX = 0;
        tmpChar[0] = (char) ce.character;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Dimension d = getSize();
        g.clearRect(0, 0, (int)d.getWidth(), (int)d.getHeight());
        if (tmpChar[0] == 0) return;
        int charWidth = fm.charWidth(tmpChar[0]);
        g.drawChars(tmpChar, 0, 1, curX++, fontHeight);
    }

    @Override
    public void run() {
        while (!done) {
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
    
    public void setDone(boolean b) {
        done = b;
    }

}
