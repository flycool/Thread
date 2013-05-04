package com.javathreads.example.typegame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JComponent;

public class CharacterDisplayCanvas extends JComponent implements CharacterListener {
    protected FontMetrics fm;
    protected char[] tmpChar = new char[1];
    protected int fontHeight;
    
    public CharacterDisplayCanvas() {
        setFont(new Font("Monospaced", Font.BOLD, 18));
        fm = Toolkit.getDefaultToolkit().getFontMetrics(getFont());
        fontHeight = fm.getHeight();
    }
    
    public CharacterDisplayCanvas(CharacterSource cs) {
        this();
        setCharacterSource(cs);
    }
    
    public void setCharacterSource(CharacterSource cs) {
        cs.addCharacterListener(this);
    }
    
    @Override
    @Deprecated
    public Dimension preferredSize() {
        return new Dimension(fm.getMaxAscent() + 10, fm.getMaxAdvance() + 10);
    }

    public synchronized void newCharacter(CharacterEvent ce) {
        tmpChar[0] = (char) ce.character;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Dimension d = getSize();
        g.clearRect(0, 0, (int)d.getWidth(), (int)d.getHeight());
        if (tmpChar[0] == 0) return;
        int charWidth = fm.charWidth((int) tmpChar[0]);
        g.drawChars(tmpChar, 0, 1, (d.width - charWidth)/2 , fontHeight);
    }

}
