package jamalian.sina.survivalswim.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Sina on 05/10/16.
 */
public class Hook {
    // Width/height of hook and rope.
    public static final int ROPE_WIDTH = 6;
    public static final int HOOK_WIDTH = 20;
    public static final int HOOK_HEIGHT = 15;

    // Min spacing between bottom of hook and middle of screen.
    public static final int HOOK_GAP = 50;

    // Hook texture, position, and bounds.
    private Texture hook;
    private Vector2 posHook;
    private Rectangle boundsHook, boundsRope;

    // Used to keep track if fish has passed the obstacle to update the score.
    private boolean passed;

    private Random rand;
    private int select;
    private float scale;

    private float obstacleWidth;
    private float widthFactor;

    public Hook(int x, int maxY, float maxWidth) {
        rand = new Random();
        select = rand.nextInt(7);

        widthFactor = 1.0f/2.0f;

        // Load texture of either a hook, octopus, turtle or shark.
        if (select < 4) {
            hook = new Texture("hook.png");
        }
        else if (select == 4) {
            hook = new Texture("octopus.png");
        }
        else if (select == 5) {
            hook = new Texture("turtle.png");
        }
        else {
            hook = new Texture("shark.png");
            widthFactor = 2.0f/3.0f;
        }

        scale = (float)hook.getHeight()/(float)hook.getWidth();
        float width = maxWidth*widthFactor;
        float height = width*scale;
        obstacleWidth = width;

        // Setup hook position and bounds.
        if (select < 4) {
            posHook = new Vector2(x, Math.min(maxY - 30, maxY - rand.nextInt(maxY/2) + HOOK_GAP));
            boundsHook = new Rectangle(posHook.x, posHook.y, hook.getWidth(), HOOK_HEIGHT);
            boundsRope = new Rectangle(posHook.x + (hook.getWidth() - ROPE_WIDTH), posHook.y + HOOK_HEIGHT, hook.getWidth(), hook.getHeight() - HOOK_HEIGHT);
        }
        else {
            posHook = new Vector2(x, Math.min(maxY - height, maxY - rand.nextInt(maxY/2) + HOOK_GAP));
            boundsHook = new Rectangle(posHook.x, posHook.y, width, height);
            boundsRope = new Rectangle(posHook.x, posHook.y, width, height);
        }

        // Fish hasn't passed the obstacle yet.
        passed = false;
    }

    public Texture getHook() {
        return hook;
    }

    public Vector2 getPosHook() {
        return posHook;
    }

    // Reposition hooks.
    public void reposition(float x, int maxY, float maxWidth) {
        select = rand.nextInt(7);

        widthFactor = 1.0f/2.0f;

        // Load texture of either a hook, octopus, turtle or shark.
        if (select < 4) {
            hook = new Texture("hook.png");
        }
        else if (select == 4) {
            hook = new Texture("octopus.png");
        }
        else if (select == 5) {
            hook = new Texture("turtle.png");
        }
        else {
            hook = new Texture("shark.png");
            widthFactor = 2.0f/3.0f;
        }

        scale = (float)hook.getHeight()/(float)hook.getWidth();
        float width = maxWidth*widthFactor;
        float height = width*scale;
        obstacleWidth = width;

        // Update hook position and bounds.
        if (select < 4) {
            posHook = new Vector2(x, Math.min(maxY - 30, maxY - rand.nextInt(maxY/2) + HOOK_GAP));
            boundsHook = new Rectangle(posHook.x, posHook.y, hook.getWidth(), HOOK_HEIGHT);
            boundsRope = new Rectangle(posHook.x + (hook.getWidth() - ROPE_WIDTH), posHook.y + HOOK_HEIGHT, hook.getWidth(), hook.getHeight() - HOOK_HEIGHT);
        }
        else {
            posHook = new Vector2(x, Math.min(maxY - height, maxY - rand.nextInt(maxY/2) + HOOK_GAP));
            boundsHook = new Rectangle(posHook.x, posHook.y, width, height);
            boundsRope = new Rectangle(posHook.x, posHook.y, width, height);
        }

        // Fish hasn't passed the obstacle yet.
        passed = false;
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsHook) || player.overlaps(boundsRope);
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getSelect() {
        return select;
    }

    public float getWidth() {
        if (select < 4)
            return hook.getWidth();
        else
            return obstacleWidth;
    }

    public void dispose() {
        hook.dispose();
    }
}
