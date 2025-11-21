

import render.WindowSystem;
import render.shader.builtins.debugUV;
import render.shader.builtins.simplex;

import javax.swing.*;

void main() {
    WindowSystem display = new WindowSystem();
    SwingUtilities.invokeLater(() -> display.setVisible(true));
    display.shader = new debugUV();
    display.shader = new simplex();

}
