package game.level;

import game.enums.Gravity;
import game.physics.AABoundingRect;
import game.physics.BoundingShape;
import java.io.Serializable;

public abstract class LevelObject implements Serializable {

    protected float x;
    protected float y;
    protected BoundingShape boundingShape;
    protected String uniqid;
    protected float x_velocity = 0;
    protected float y_velocity = 0;
    protected float maximumFallSpeed = 1;
    protected float decelerationSpeed = 1;

    protected boolean onGround = true;

    public LevelObject(float x, float y) {
        this.x = x;
        this.y = y;

        //default bounding shape is a 32 by 32 box
        boundingShape = new AABoundingRect(x, y, 32, 32);
        uniqid = x + "-" + y;
    }

    public String getUniqid() {
        return uniqid;
    }

    
    public void applyGravity(float force, Gravity gravity) {
        switch (gravity) {
            case DOWN:
                //if we aren't already moving at maximum speed
                if (y_velocity < maximumFallSpeed) {
                    //accelerate
                    y_velocity += force;
                    if (y_velocity > maximumFallSpeed) {
                        //and if we exceed maximum speed, set it to maximum speed
                        y_velocity = maximumFallSpeed;
                    }
                }
                break;
            case UP:
                if (y_velocity > -maximumFallSpeed) {
                    //accelerate
                    y_velocity -= force;
                    if (y_velocity < -maximumFallSpeed) {
                        //and if we exceed maximum speed, set it to maximum speed
                        y_velocity = -maximumFallSpeed;
                    }
                }
                break;
        }
    }

    public float getYVelocity() {
        return y_velocity;
    }

    public void setYVelocity(float f) {
        y_velocity = f;
    }

    public float getXVelocity() {
        return x_velocity;
    }

    public void setXVelocity(float f) {
        x_velocity = f;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float f) {
        x = f;
        updateBoundingShape();
    }

    public void setY(float f) {
        y = f;
        updateBoundingShape();
    }

    public void updateBoundingShape() {
        boundingShape.updatePosition(x, y);
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean b) {
        onGround = b;
    }

    public BoundingShape getBoundingShape() {
        return boundingShape;
    }

    //move towards x_velocity = 0
    public void decelerate(int delta, Gravity gravity) {
        //if we are on the down or up state we have to decelerate horizontally, else vertically
        switch (gravity) {
            case DOWN:
            case UP:
                if (x_velocity > 0) {
                    x_velocity -= decelerationSpeed * delta;
                    if (x_velocity < 0) {
                        x_velocity = 0;
                    }
                } else if (x_velocity < 0) {
                    x_velocity += decelerationSpeed * delta;
                    if (x_velocity > 0) {
                        x_velocity = 0;
                    }
                }
                break;
        }
    }

    public abstract void render(float offset_x, float offset_y, Gravity gravity);

}
