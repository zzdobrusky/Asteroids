package com.studiorur.games.asteroids.Sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.FloatMath;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by zbynek on 11/22/2014.
 */
public class Sprite
{
    // Shared
    static int _Program = -1;
    static final int POSITION_ATTRIBUTE_ID = 0;
    static final int TEXTURE_COORDINATE_ATTRIBUTE_ID = 1;
    static FloatBuffer _QuadPointsBuffer = null;

    // Unique for each sprite
    // Texture related
    private Bitmap _texture = null;
    private FloatBuffer _quadTextureBuffer = null;
    private int _textureId;
    private RectF _textureRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f); // default is the whole texture

    // Sprite related
    protected PointF _center = new PointF(0.0f, 0.0f);
    protected float _width = 1.0f;
    protected float _height = 1.0f;
    protected float _rotation = 0.0f;

    public RectF getTextureRect()
    {
        return _textureRect;
    }

    public void setTextureRect(RectF textureRect)
    {
        _textureRect = textureRect;
    }

    public PointF getCenter()
    {
        return _center;
    }

    public void setCenter(PointF center)
    {
        _center = center;
    }

    public float getCenterX()
    {
        return _center.x;
    }

    public void setCenterX(float centerX)
    {
        _center = new PointF(centerX, _center.y);
    }

    public float getCenterY()
    {
        return _center.y;
    }

    public void setCenterY(float centerY)
    {
        _center = new PointF(_center.x, centerY);
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

    private void init()
    {
        String vertexShaderSource = "" +
                "attribute vec4 position; \n" +
                "uniform mat4 modelView; \n" +
                "attribute vec2 textureCoordinate; \n" +
                "varying vec2 textureCoordinateVarying; \n" +
                " \n" +
                "void main() \n" +
                "{ \n" +
                "  gl_Position = modelView * position; \n" +
                "  textureCoordinateVarying = textureCoordinate;\n" +
                "} \n" +
                " \n";

        String fragmentShaderSource = "" +
                "uniform sampler2D textureUnit; \n" +
                "varying highp vec2 textureCoordinateVarying; \n" +
                " \n" +
                "void main() \n" +
                "{ \n" +
                "  gl_FragColor = texture2D(textureUnit, textureCoordinateVarying); \n" +
                "} \n" +
                " \n";

        // enable transparency
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

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
        GLES20.glBindAttribLocation(_Program, TEXTURE_COORDINATE_ATTRIBUTE_ID, "textureCoordinate");
        // link it and use it
        GLES20.glLinkProgram(_Program);

        // quad vertices coordinates
        float[] quadPoints =
                {
                        -0.5f, -0.5f, 0.0f, 1.0f,
                        0.5f, -0.5f, 0.0f, 1.0f,
                        -0.5f, 0.5f, 0.0f, 1.0f,
                        0.5f, 0.5f, 0.0f, 1.0f,
                };

        ByteBuffer quadPointsByteBuffer = ByteBuffer.allocateDirect(quadPoints.length * 4);
        quadPointsByteBuffer.order(ByteOrder.nativeOrder());
        _QuadPointsBuffer = quadPointsByteBuffer.asFloatBuffer();
        _QuadPointsBuffer.put(quadPoints);
        _QuadPointsBuffer.rewind();
    }

    protected void setTextureCoordinates()
    {
        // quad texture coordinates
        float[] quadTextureCoordinates = {
                _textureRect.left, _textureRect.bottom,
                _textureRect.right, _textureRect.bottom,
                _textureRect.left, _textureRect.top,
                _textureRect.right, _textureRect.top,
        };

        ByteBuffer quadTextureByteBuffer = ByteBuffer.allocateDirect(quadTextureCoordinates.length * 4);
        quadTextureByteBuffer.order(ByteOrder.nativeOrder());
        _quadTextureBuffer = quadTextureByteBuffer.asFloatBuffer();
        _quadTextureBuffer.put(quadTextureCoordinates);
        _quadTextureBuffer.rewind();
    }

    protected void setModelView()
    {
        float[] modelView = new float[]
                {
                        _width * FloatMath.cos(_rotation), _width * FloatMath.sin(_rotation), 0.0f, 0.0f,
                        -_height * FloatMath.sin(_rotation), _height * FloatMath.cos(_rotation), 0.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 0.0f,
                        _center.x, _center.y, 0.0f, 1.0f,
                };

        int modelViewLocation = GLES20.glGetUniformLocation(_Program, "modelView");
        GLES20.glUniformMatrix4fv(modelViewLocation, 1, false, modelView, 0);
    }

    protected void loadTexture(Resources resourcers, int resourceIdentifier)
    {
        _texture = BitmapFactory.decodeResource(resourcers, resourceIdentifier);
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        _textureId = textureIds[0];
    }

    protected void bindTexture()
    {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _textureId);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, _texture, 0);
    }

    public void draw()
    {
        if ( GLES20.glIsProgram( _Program ) != true )
        {
            // Recreate
            init();
        }
        GLES20.glUseProgram(_Program);

        bindTexture();
        setTextureCoordinates();
        setModelView();

        GLES20.glEnableVertexAttribArray(POSITION_ATTRIBUTE_ID);
        GLES20.glVertexAttribPointer(POSITION_ATTRIBUTE_ID, 4, GLES20.GL_FLOAT, false, 4 * 4, _QuadPointsBuffer);
        GLES20.glEnableVertexAttribArray(TEXTURE_COORDINATE_ATTRIBUTE_ID);
        GLES20.glVertexAttribPointer(TEXTURE_COORDINATE_ATTRIBUTE_ID, 2, GLES20.GL_FLOAT, false, 2 * 4, _quadTextureBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
