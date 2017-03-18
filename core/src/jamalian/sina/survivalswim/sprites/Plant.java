package jamalian.sina.survivalswim.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Sina on 05/10/16.
 */
public class Plant {
    // Stores plant textures, position, and bounding rectangle.
    private Texture plant;
    private Vector2 posPlant;
    private Rectangle boundsPlant;

    // Used to keep track if fish has passed the obstacle to update the score.
    private boolean passed;

    private Random rand;

    private int select;
    private float scale;

    private float obstacleWidth;
    private float widthFactor;

    public Plant(float x, float hookHeight, float maxWidth, float obstacleGap) {
        rand = new Random();
        select = rand.nextInt(9);

        widthFactor = 1.0f/2.0f;

        // Loads a plant texture with a possibility for an octopus, turtle, or shark instead.
        if (select == 0 || select == 1) {
            plant = new Texture("greenplants.png");
        }
        else if (select == 2 || select == 3) {
            plant = new Texture("orangeplants.png");
        }
        else if (select == 4 || select == 5){
            plant = new Texture("purpleplants.png");
        }
        else if (select == 6) {
            plant = new Texture("octopus.png");
        }
        else if (select == 7) {
            plant = new Texture("turtle.png");
        }
        else {
            plant = new Texture("shark.png");
            widthFactor = 2.0f/3.0f;
        }

        scale = (float)plant.getHeight()/(float)plant.getWidth();
        float width = maxWidth*widthFactor;
        float height = width*scale;
        obstacleWidth = width;

        // Sets up plant's position and bounding rectangle.
        if (select < 6) {
            posPlant = new Vector2(x, hookHeight - obstacleGap - plant.getHeight());
            boundsPlant = new Rectangle(posPlant.x, posPlant.y, plant.getWidth(), plant.getHeight());
        }
        else {
            posPlant = new Vector2(x, hookHeight - obstacleGap - height);
            boundsPlant = new Rectangle(posPlant.x, posPlant.y, width, height);
        }

        // Fish hasn't passed the obstacle yet.
        passed = false;
    }

    public Texture getPlant() {
        return plant;
    }

    public Vector2 getPosPlant() {
        return posPlant;
    }

    // Reposition hooks.
    public void reposition(float x, float hookHeight, float maxWidth, float obstacleGap) {
        select = rand.nextInt(9);

        widthFactor = 1.0f/2.0f;

        // Loads a plant texture with a possibility for an octopus/shark instead.
        if (select == 0 || select == 1) {
            plant = new Texture("greenplants.png");
        }
        else if (select == 2 || select == 3) {
            plant = new Texture("orangeplants.png");
        }
        else if (select == 4 || select == 5){
            plant = new Texture("purpleplants.png");
        }
        else if (select == 6) {
            plant = new Texture("octopus.png");
        }
        else if (select == 7) {
            plant = new Texture("turtle.png");
        }
        else {
            plant = new Texture("shark.png");
            widthFactor = 2.0f/3.0f;
        }

        scale = (float)plant.getHeight()/(float)plant.getWidth();
        float width = maxWidth*widthFactor;
        float height = width*scale;
        obstacleWidth = width;

        // Updates plant's position and bounding rectangle.
        if (select < 6) {
            posPlant = new Vector2(x, hookHeight - obstacleGap - plant.getHeight());
            boundsPlant = new Rectangle(posPlant.x, posPlant.y, plant.getWidth(), plant.getHeight());
        }
        else {
            posPlant = new Vector2(x, hookHeight - obstacleGap - height);
            boundsPlant = new Rectangle(posPlant.x, posPlant.y, width, height);
        }

        // Fish hasn't passed the obstacle yet.
        passed = false;
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsPlant);
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
        if (select < 6)
            return plant.getWidth();
        else
            return obstacleWidth;
    }

    public void dispose() {
        plant.dispose();
    }
}
