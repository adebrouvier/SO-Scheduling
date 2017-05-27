package main.controller.input;

import java.awt.event.*;

/**
 * An abstract adapter class for receiving keyboard and mouse events.
 * The methods in this class are empty. This class exists as
 * convenience for creating listener objects.
 */
public abstract class InputAdapter implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {
    /**
     * Invoked when a key has been typed.
     * This event occurs when a key press is followed by a key release.
     */
    public void keyTyped(KeyEvent e) {}

    /**
     * Invoked when a key has been pressed.
     */
    public void keyPressed(KeyEvent e) {}

    /**
     * Invoked when a key has been released.
     */
    public void keyReleased(KeyEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseClicked(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseExited(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseWheelMoved(MouseWheelEvent e){}

    /**
     * {@inheritDoc}
     */
    public void mouseDragged(MouseEvent e){}

    /**
     * {@inheritDoc}
     */
    public void mouseMoved(MouseEvent e){}

}

