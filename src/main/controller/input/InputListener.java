package main.controller.input;

import java.awt.event.KeyEvent;

/**
 *
 */
public class InputListener extends InputAdapter {

    // Keyboard variables
    private final int NUM_KEYS = 3;
    private boolean keyState[] = new boolean[NUM_KEYS];
    private boolean prevKeyState[] = new boolean[NUM_KEYS];

    public static final int PAUSE = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    // Keyboard methods

    private void keySet(int i, boolean b) {
        if (i == KeyEvent.VK_P) keyState[PAUSE] = b;
        else if (i == KeyEvent.VK_LEFT || i == KeyEvent.VK_A) keyState[LEFT] = b;
        else if (i == KeyEvent.VK_RIGHT || i == KeyEvent.VK_D) keyState[RIGHT] = b;
    }

    public void updateKeys() {
        for (int i = 0; i < NUM_KEYS; i++) {
            prevKeyState[i] = keyState[i];
        }
    }

    public boolean keyDown(int i) { // holding a key down
        return keyState[i];
    }

    public boolean isPressed(int i) {
        return keyState[i] && !prevKeyState[i];
    }

    public boolean isReleased(int i) {
        return !keyState[i] && prevKeyState[i];
    }

    public boolean anyKeyPress() {
        for (int i = 0; i < NUM_KEYS; i++) {
            if (keyState[i]) return true;
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keySet(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keySet(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
