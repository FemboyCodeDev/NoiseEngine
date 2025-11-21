package render;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import render.shader.Pixel;
import render.shader.Shader;

/**
 * WindowSystem.java
 * A Java Swing application that demonstrates processing every pixel
 * in a rendered window area using a BufferedImage.
 * The 'function' applied here generates a continuous color gradient.
 */
public class WindowSystem extends JFrame implements KeyListener {

    // Define the dimensions of the internal buffer and window
    private static final int WIDTH = 800;

    //private static final int WIDTH = 64;
    private static final int HEIGHT = 600;
    //private static final int HEIGHT = 64;


    // The image where we will perform the per-pixel processing
    private final BufferedImage pixelBuffer;

    // A panel to display the image
    private final JPanel canvasPanel;

    public double x_position = 0;
    public double y_position = 0;
    public double scale = 1;
    public double brightness = 100;


    public Shader shader;
    @Override
    public void keyTyped(KeyEvent key){




    }
    @Override
    public void keyPressed(KeyEvent key){
        System.out.println(key.getKeyChar());

        double direction_x = 0;
        double direction_y = 0;
        if (key.getKeyChar() == 'w'){
            direction_y += 1;
        }
        if (key.getKeyChar() == 's'){
            direction_y -= 1;
        }
        if (key.getKeyChar() == 'a'){
            direction_x += 1;
        }
        if (key.getKeyChar() == 'd'){
            direction_x -= 1;
        }
        if (key.getKeyChar() == 'e'){
            scale *= 1.2;
        }
        if (key.getKeyChar() == 'q'){
            scale /= 1.2;
        }
        if (key.getKeyChar() == 'z'){
            brightness+=1;
        }
        if (key.getKeyChar() == 'x'){
            brightness-=1;
        }
        x_position -= (direction_x * 5) * scale;
        y_position -= (direction_y * 5) * scale;

        System.out.println(scale);


    }
    @Override
    public void keyReleased(KeyEvent key){

    }


    /**
     * Constructor sets up the JFrame and the internal BufferedImage.
     */
    public WindowSystem() {
        // 1. Initialize the BufferedImage
        this.pixelBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // 2. Create the display panel (Canvas)
        this.canvasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the generated image onto the panel
                g.drawImage(pixelBuffer, 0, 0, this.getWidth(), this.getHeight(), null);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(WIDTH, HEIGHT);
            }
        };

        // 3. Setup the main window (JFrame)
        this.setTitle("Java Per-Pixel Renderer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(canvasPanel, BorderLayout.CENTER);
        this.pack(); // Size the frame to fit the preferred size of the canvas
        this.setLocationRelativeTo(null); // Center the window
        this.setSize(WIDTH,HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(this);

        // 4. Run the pixel processing function once initially
        processPixels(0);

        // 5. Setup an animation loop to demonstrate dynamic processing
        // This will call processPixels repeatedly to create movement
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            // Use a time counter (System.nanoTime()) to make the gradient move
            long timeMs = System.nanoTime() / 1_000_000;
            processPixels(timeMs / 50.0);
            // Request the panel to redraw itself with the new buffer content
            canvasPanel.repaint();
        }, 0, 16, TimeUnit.MILLISECONDS); // ~60 FPS
    }

    /**
     * Core function that executes logic for every pixel (x, y).
     * This method iterates through every coordinate of the BufferedImage
     * and sets its color based on a custom function (here, a dynamic gradient).
     *
     * @param time A dynamic value to animate the effect.
     */
    private void processPixels(double time) {
        // Nested loop to visit every (x, y) coordinate
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {

                // --- START: CUSTOM PER-PIXEL FUNCTION ---

                // 1. Normalize coordinates to be between 0.0 and 1.0
                double normalizedX = (double) x / WIDTH;
                double normalizedY = (double) y / HEIGHT;

                /*

                // 2. Define color components based on the coordinates and time
                // This creates a colorful, moving sine wave pattern

                // Red component: Varies primarily with X, modulated by time
                double r = Math.sin(normalizedX * 2 * Math.PI + time * 0.05) * 0.5 + 0.5;
                // Green component: Varies primarily with Y
                double g = normalizedY;
                // Blue component: Varies based on the distance from center, also modulated
                double centerX = 0.5;
                double centerY = 0.5;
                double dist = Math.sqrt(Math.pow(normalizedX - centerX, 2) + Math.pow(normalizedY - centerY, 2));
                double b = Math.cos(dist * 10 + time * 0.1) * 0.5 + 0.5;

                // 3. Convert normalized (0.0 to 1.0) color components to 8-bit (0 to 255)
                int red = (int) (r * 255);
                int green = (int) (g * 255);
                int blue = (int) (b * 255);

                // Clamp values to ensure they are within the 0-255 range
                red = Math.min(255, Math.max(0, red));
                green = Math.min(255, Math.max(0, green));
                blue = Math.min(255, Math.max(0, blue));

                // 4. Combine R, G, B into a single integer color value (ARGB format)
                int rgb = (red << 16) | (green << 8) | blue;
                */
                // --- END: CUSTOM PER-PIXEL FUNCTION ---
                if (this.shader != null) {
                    if (shader.enabled()) {

                        shader.x_offset = x_position;
                        shader.y_offset = y_position;
                        shader.scale = (float)scale;
                        shader.brightness = brightness;
                        Pixel result = shader.render(normalizedX, normalizedY);


                        int red = result.r;
                        int green = result.g;
                        int blue = result.b;


                        int rgb = (red << 16) | (green << 8) | blue;

                        // 5. Apply the calculated color to the pixel in the buffer
                        pixelBuffer.setRGB(x, y, rgb);
                    }
                }
            }
        }
    }
    /**
     * Main method to start the application.
     */
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT) for safety
        SwingUtilities.invokeLater(() -> new WindowSystem().setVisible(true));
    }
}