package jamalian.sina.survivalswim.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Sina on 02/10/16.
 */
public class Fish {
    // Used to bring fish back down.
    private static final int GRAVITY = -15;

    // Horizontal movement.
    private static final int MOVEMENT = 120;

    // 2D world - will only use X and Y.
    private Vector3 position;

    // Direction and speed of travel.
    private Vector3 velocity;

    // Used to store fish animation image with 4 frames.
    private Texture fishFrames;

    // Bounds around fish to determine collision.
    private Rectangle bounds;

    private Animation fishAnimation;

    // Sound made when user touches screen to move fish up.
    private Sound swimUp;

    // The number of frames in the fish animation image.
    public static final int FRAME_COUNT = 4;

    private float scaleHeight;

    public Fish(int x, int y, int maxWidth) {
        // Starting position and velocity.
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);

        // Do breakdown of fish TextureRegion inside of Animation class.
        fishFrames = new Texture("fishanimation.png");
        fishAnimation = new Animation(new TextureRegion(fishFrames), FRAME_COUNT, 0.5f);

        // Scales fish based on screen size.
        scaleHeight = (float)fishFrames.getHeight()/(float)(fishFrames.getWidth()/FRAME_COUNT);
        bounds = new Rectangle(x, y, maxWidth, maxWidth*scaleHeight);

        // Sound made when fish swims up.
        swimUp = Gdx.audio.newSound(Gdx.files.internal("touch.wav"));
    }

    public void update(float dt, float minHeight, float maxHeight) {
        fishAnimation.update(dt);

        velocity.add(0, GRAVITY, 0);

        // Scale: multiply everything by delta time (dt)
        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y, 0);

        // Don't let sprite fall below ground.
        if (position.y < minHeight) {
            position.y = minHeight;
        }

        // Don't let sprite go above screen.
        if (position.y > maxHeight) {
            position.y = maxHeight;
        }

        // Reverse previous so velocity can be added again on next frame.
        velocity.scl(1/dt);

        // Reposition bounding rectangle to follow bird.
        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    // Gets a frame from the fish animation.
    public TextureRegion getFishFrames() {
        return fishAnimation.getFrame();
    }

    public void jump() {
        // Positive y to counteract negative GRAVITY of -15.
        velocity.y = 250;
        swimUp.play(0.5f);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        fishFrames.dispose();
        swimUp.dispose();
    }
}
