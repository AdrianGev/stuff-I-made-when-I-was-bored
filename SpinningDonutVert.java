package randomJunk;
public class SpinningDonutVert {
    public static void main(String[] args) throws InterruptedException {
        final int WIDTH = 40;
        final int HEIGHT = 20;
        final char[] SHADE_CHARS = {' ', '.', '-', '=', '*', '#', '@'};
        
        double[][] zBuffer = new double[WIDTH][HEIGHT];
        char[][] screen = new char[WIDTH][HEIGHT];

        double A = 0, B = 0; // Rotation angles
        while (true) {
            // Clear the screen and z-buffer
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    screen[x][y] = ' ';
                    zBuffer[x][y] = 0;
                }
            }

            for (double theta = 0; theta < 2 * Math.PI; theta += 0.07) {
                for (double phi = 0; phi < 2 * Math.PI; phi += 0.03) {
                    double costheta = Math.cos(theta);
                    double sintheta = Math.sin(theta);
                    double cosphi = Math.cos(phi);
                    double sinphi = Math.sin(phi);

                    // 3D rotation of the donut
                    double circleX = cosphi * (3 + costheta);
                    double circleY = sintheta;
                    double circleZ = sinphi * (3 + costheta);

                    // Apply rotation around the X axis
                    double x = circleX * Math.cos(B) - circleZ * Math.sin(B);
                    double y = circleY;
                    double z = circleX * Math.sin(B) + circleZ * Math.cos(B);

                    // Apply rotation around the Y axis
                    double finalX = x;
                    double finalY = y * Math.cos(A) - z * Math.sin(A);
                    double finalZ = y * Math.sin(A) + z * Math.cos(A);

                    double ooz = 1 / (finalZ + 5);  // Perspective projection

                    int px = (int) (WIDTH / 2 + ooz * finalX * WIDTH / 2);
                    int py = (int) (HEIGHT / 2 - ooz * finalY * HEIGHT / 2);

                    double L = cosphi * costheta * sinphi + cosphi * sintheta;
                    int brightness = (int) ((L + 1) * 3.5);

                    if (px >= 0 && px < WIDTH && py >= 0 && py < HEIGHT) {
                        if (ooz > zBuffer[px][py]) {
                            zBuffer[px][py] = ooz;
                            screen[px][py] = SHADE_CHARS[Math.max(0, Math.min(brightness, SHADE_CHARS.length - 1))];
                        }
                    }
                }
            }

            // Print the screen
            System.out.print("\033[H\033[2J"); // Clear screen
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    System.out.print(screen[x][y]);
                }
                System.out.println();
            }

            // Rotate angles
            A += 0.04;
            B += 0.02;

            // Delay for smooth animation
            Thread.sleep(30);
        }
    }
}


