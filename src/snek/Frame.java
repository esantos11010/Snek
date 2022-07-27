/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snek;

import javax.swing.JFrame;

/**
 *
 * @author Edd
 */
public class Frame extends JFrame{
    public Frame (){
        add(new GameWindow());
        setResizable(false);
        pack();
        setTitle("SNEK");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
}
