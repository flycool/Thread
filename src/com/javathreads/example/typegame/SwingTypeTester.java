package com.javathreads.example.typegame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingTypeTester extends JFrame implements CharacterSource {
    protected RandomCharacterGenerator producer;
    private AnimatedCharacterDisplayCanvas displayCanvas;
    private CharacterDisplayCanvas feedbackCanvas;
    private JButton quitButton;
    private JButton startButton;
    private JButton stopButton;
    private CharacterEventHandler handler;
    
    public SwingTypeTester() {
        initComponents();
    }
    
    @SuppressWarnings("deprecation")
    private void initComponents() {
        handler = new CharacterEventHandler();
        displayCanvas = new AnimatedCharacterDisplayCanvas();
        feedbackCanvas = new CharacterDisplayCanvas(this);
        
        quitButton = new JButton();
        startButton = new JButton();
        stopButton = new JButton();
        
        add(displayCanvas, BorderLayout.NORTH);
        add(feedbackCanvas, BorderLayout.CENTER);
        
        JPanel p = new JPanel();
        startButton.setLabel("Start");
        quitButton.setLabel("Quit");
        stopButton.setLabel("Stop");
        p.add(startButton);
        p.add(quitButton);
        p.add(stopButton);
        
        add(p, BorderLayout.SOUTH);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        
        feedbackCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (c != KeyEvent.CHAR_UNDEFINED) {
                    nextCharacter((int) c);
                }
            }
        });
        
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                producer = new RandomCharacterGenerator();
                displayCanvas.setCharacterSource(producer);
                producer.start();
                displayCanvas.setDone(false);
                Thread t = new Thread(displayCanvas);
                t.start();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                feedbackCanvas.setEnabled(true);
                feedbackCanvas.requestFocus();
            }
        });
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                producer.interrupt();
                displayCanvas.setDone(true);
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                feedbackCanvas.setEnabled(false);
            }
        });
        
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                quit();
            }
        });
        
        pack();
    }
    
    private void quit() {
        System.exit(0);
    }

    @Override
    public void addCharacterListener(CharacterListener cl) {
        handler.addCharacterListener(cl);
    }

    @Override
    public void removeCharacterListener(CharacterListener cl) {
        handler.removeCharacterListener(cl);
    }
    
    public void nextCharacter(int c) {
        handler.fireNewCharacter(this, c);
    }

    @Override
    public void nextCharacter() {
        
    }
    
    public static void main(String[] args) {
        new SwingTypeTester().show();
    }

}
