// SUGGESTION (AI-generated reference). Rename to Theme.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 1 (shared glue)
package app;

import java.awt.Color;
import java.awt.Font;

/** Shared look-and-feel constants. Phone-sized window. SDG 4 accent colour is red (#C5192D). */
public final class Theme {
    private Theme() {}
    public static final int APP_WIDTH = 360;
    public static final int APP_HEIGHT = 720;
    public static final Color PRIMARY = new Color(0xC5192D);
    public static final Color BG = Color.WHITE;
    public static final Color TEXT = new Color(0x222222);
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 14);
}
