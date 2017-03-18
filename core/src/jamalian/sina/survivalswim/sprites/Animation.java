package jamalian.sina.survivalswim.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Sina on 03/10/16.
 */
public class Animation {
    private Array<TextureRegion> frames;
    // How long before changing frames.
    private float maxFrameTime;
    // How long the animation has been in the current frame.
    private float currentFrameTime;
    // How many frames we have in our animation.
    private int frameCount;
    // Current frame we're in.
    private int frame;

    // TextureRegion is all of the frames combined in one image.
    // frameCount is number of frames in region.
    // cycleTime is how long it will take to cycle through entire animation.
    public Animation(TextureRegion region, int frameCount, float cycleTime) {
        frames = new Array<TextureRegion>();
        // Get width of single frame in animation image.
        int frameWidth = region.getRegionWidth()/frameCount;
        for (int i = 0; i < frameCount; i++) {
            // (image, starting x, starting y (bottom), width of image, height of image)
            frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
        }
        this.frameCount = frameCount;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
    }

    public void update(float dt) {
        // How long current frame has been in view.
        currentFrameTime += dt;
        if (currentFrameTime > maxFrameTime) {
            frame++;
            currentFrameTime = 0;
        }
        if (frame >= frameCount) {
            frame = 0;
        }
    }

    public TextureRegion getFrame() {
        return frames.get(frame);
    }
}
