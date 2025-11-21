

import render.PixelProcessor;
import render.shader.Shader;
import render.shader.builtins.debugUV;
import javax.swing.*;

void main() {
    PixelProcessor display = new PixelProcessor();
    SwingUtilities.invokeLater(() -> display.setVisible(true));
    display.shader = new debugUV();

}
