package com.studiorur.games.asteroids.Shapes;

import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.FloatMath;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by zbynek on 11/24/2014.
 */
public class RectangleShape
{
    static int _Program = -1;
    static final int POSITION_ATTRIBUTE_ID = 3;
    static FloatBuffer _RectanglePointsBuffer = null;

    protected PointF _center = new PointF(0.0f, 0.0f);
    protected float _width = 1.0f;
    protected float _height = 1.0f;
    protected float _rotation = 0.0f;
    protected int _color = Color.BLUE;;

    public float getCenterX()
    {
        return _center.x;
    }

    public void setCenterX(float centerX)
    {
        _center.x = centerX;
    }

    public float getCenterY()
    {
        return _center.y;
    }

    public void setCenterY(float centerY)
    {
        _center.y = centerY;
    }

    public float getWidth()
    {
        return _width;
    }

    public void setWidth(float width)
    {
        _width = width;
    }

    public float getHeight()
    {
        return _height;
    }

    public void setHeight(float height)
    {
        _height = height;
    }

    public float getRotation()
    {
        return _rotation;
    }

    public void setRotation(float rotation)
    {
        _rotation = rotation;
    }

    public int getColor()
    {
        return _color;
    }

    public void setColor(int color)
    {
        _color = color;
    }

    private void init()
    {
        String vertexShaderSource = "" +
                "attribute vec4 position; \n" +
                "uniform mat4 modelView; \n" +
                " \n" +
                "void main() \n" +
                "{ \n" +
                "  gl_Position = modelView * position; \n" +
                "} \n" +
                " \n";

        String fragmentShaderSource = "" +
                "uniform vec4 color; \n" +
                " \n" +
                "void main() \n" +
                "{ \n" +
                "  gl_FragColor = color; \n" +
                "} \n" +
                " \n";

        // set up shaders
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vertexShaderSource);
        GLES20.glCompileShader(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderSource);
        GLES20.glCompileShader(fragmentShader);

        // set up program
        _Program = GLES20.glCreateProgram();
        GLES20.glAttachShader(_Program, vertexShader);
        GLES20.glAttachShader(_Program, fragmentShader);
        // Bind variables
        GLES20.glBindAttribLocation(_Program, POSITION_ATTRIBUTE_ID, "position");
        // link it and use it
        GLES20.glLinkProgram(_Program);

        // quad vertices coordinates
        float[] rectanglePoints =
                {
                        -0.5f, -0.5f, 0.0f, 1.0f,
                        0.5f, -0.5f, 0.0f, 1.0f,
                        -0.5f, 0.5f, 0.0f, 1.0f,
                        0.5f, 0.5f, 0.0f, 1.0f,
                };

        ByteBuffer rectanglePointsByteBuffer = ByteBuffer.allocateDirect(rectanglePoints.length * 4);
        rectanglePointsByteBuffer.order(ByteOrder.nativeOrder());
        _RectanglePointsBuffer = rectanglePointsByteBuffer.asFloatBuffer();
        _RectanglePointsBuffer.put(rectanglePoints);
        _RectanglePointsBuffer.rewind();
    }

    public void draw()
    {
        if ( GLES20.glIsProgram( _Program ) != true )
        {
            // Recreate
            init();
        }

        GLES20.glUseProgram(_Program);

        // transform matrix
        float[] modelView = new float[]
                {
                        _width * FloatMath.cos(_rotation), _width * FloatMath.sin(_rotation), 0.0f, 0.0f,
                        -_height * FloatMath.sin(_rotation), _height * FloatMath.cos(_rotation), 0.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 0.0f,
                        _center.x, _center.y, 0.0f, 1.0f,
                };
        int modelViewLocation = GLES20.glGetUniformLocation(_Program, "modelView");
        GLES20.glUniformMatrix4fv(modelViewLocation, 1, false, modelView, 0);

        // color
        float[] color = new float[]
                {
                        Color.red(_color)/255.0f,
                        Color.green(_color)/255.0f,
                        Color.blue(_color)/255.0f,
                        Color.alpha(_color)/255.0f,
                };
        int colorLocation = GLES20.glGetUniformLocation(_Program, "color");
        GLES20.glUniform4fv(colorLocation, 1, color, 0);

        GLES20.glEnableVertexAttribArray(POSITION_ATTRIBUTE_ID);
        GLES20.glVertexAttribPointer(POSITION_ATTRIBUTE_ID, 4, GLES20.GL_FLOAT, false, 4 * 4, _RectanglePointsBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
