package com.javathreads.example.typegame;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ScoreLabel extends JLabel implements CharacterListener {
    
    private volatile int score = 0;
    private int char2Type = -1;
    private CharacterSource generator = null;
    private CharacterSource typist = null;

    public ScoreLabel(CharacterSource generator, CharacterSource typist) {
        this.generator = generator;
        this.typist = typist;
        
        if (generator != null) {
            generator.addCharacterListener(this);
        }
        if (typist != null) {
            typist.addCharacterListener(this);
        }
    }
    
    public ScoreLabel() {
        this(null, null);
    }
    
    
    public synchronized void resetGenerator(CharacterSource newGenerator) {
        if (generator != null) {
            generator.removeCharacterListener(this);
        }
        generator = newGenerator;
        if (generator != null) {
            generator.addCharacterListener(this);
        }
    }
    
    public synchronized void resetTypist(CharacterSource newTypist) {
        if (typist != null) {
            typist.removeCharacterListener(this);
        }
        typist = newTypist;
        if (typist != null) {
            typist.addCharacterListener(this);
        }
    }
    
    public synchronized void resetScore() {
        score = 0;
        char2Type = -1;
        setScore();
    }
    
    private synchronized void setScore() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setText(Integer.toString(score));
            }
        });
    }
    
    @Override
    public void newCharacter(CharacterEvent ce) {
        if (ce.source != generator) {
            synchronized (this) {
                if (char2Type != -1) {
                    score--;
                    setScore();
                }
                char2Type = ce.character;
            }
        } else {
            synchronized (this) {
                if (char2Type != ce.character) {
                    score--;
                } else {
                    score++;
                    char2Type = -1;
                }
                setScore();
            }
        }
    }

}
