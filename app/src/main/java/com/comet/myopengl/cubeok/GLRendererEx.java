package com.comet.myopengl.cubeok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.comet.myopengl.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRendererEx implements GLSurfaceView.Renderer {

    private Context context;
    private float time;

    // Resources TODO: maybe some simple asset manager (nothing too fancy)
    int inside;
    int textures[];
    int begin;
    int leftarrow;
    int rightarrow;
    int warning;
    int noccube;
    int quit;

    private final float guiMatrix[]
            = new float[16];

    // Cube graphic
    public CubeRenderer cube;
    // Global rotation variables
    private float latitude, longitude;
    private float eyeX, eyeY, eyeZ;
    private float radius = 10.0f;
    // Local rotation variables
    public int face0; // initial face
    public float point0[]; // Initial touch point
    // Matrices
    private final float viewMatrix[]           // Matrix represents where the "camera" is/is facing
            = new float[16];
    private final float projectionMatrix[]     // Matrix used to project eye coords to clip coords
            = new float[16];
    private final float vpMatrix[]             // viewM * projectionM
            = new float[16];

    // Game states
    private static final int MAIN_MENU = 0;
    private static final int PLAY = 1;
    private int state = MAIN_MENU;

    // Shader constants/variables
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec4 aColour;" +
                    "varying vec4 vColour;" +
                    "attribute vec2 aTexCoord;" +
                    "varying vec2 vTexCoord;" +
                    "void main() {" +
                    "   vColour = aColour;" +
                    "   vTexCoord = aTexCoord;" +
                    "   gl_Position = uMVPMatrix * aPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColour;" +
                    "uniform sampler2D uTex;" +
                    "varying vec2 vTexCoord;" +
                    "uniform float uAlpha;" +
                    "void main() {" +
                    "   gl_FragColor = texture2D(uTex, vTexCoord);" +
                    "   gl_FragColor.a *= uAlpha;" +
                    "}";

    private int program;

    public GLRendererEx(final Context context) {
        this.context = context;

        // NOTE: Do NOT put any OpenGL code in this constructor
        // since the context has not yet been made.
        // Instead, use onSurfaceCreated
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set clock
        time = SystemClock.uptimeMillis() / 1000.0f;

        // Set view matrix
        setViewMatrix(latitude, longitude);

        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        // Necessary for maintaining correct alpha
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Enable blending (for alpha)
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Set up shader
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(program, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(program, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(program);

        GLES20.glUseProgram(program);

        // Load resources
        inside = loadTexture(context, R.drawable.keyboard_1);
        textures = new int[]{
                loadTexture(context, R.drawable.keyboard_1),
                loadTexture(context, R.drawable.keyboard_2),
                loadTexture(context, R.drawable.keyboard_3),
                loadTexture(context, R.drawable.keyboard_4),
                loadTexture(context, R.drawable.keyboard_5),
                loadTexture(context, R.drawable.keyboard_6)
        };
        begin = loadTexture(context, R.drawable.keyboard_1);
        leftarrow = loadTexture(context, R.drawable.keyboard_1);
        rightarrow = loadTexture(context, R.drawable.keyboard_1);
        warning = loadTexture(context, R.drawable.keyboard_1);
        noccube = loadTexture(context, R.drawable.keyboard_1);
        quit = loadTexture(context, R.drawable.keyboard_1);

        // GUI setup
        // =========
        cube = new DisplayCubeRenderer(textures);

        Matrix.setIdentityM(guiMatrix, 0);
    }

    public void onDrawFrame(GL10 unused) {
        // Get delta time
        float dt = SystemClock.uptimeMillis() / 1000.0f - time;
        time = SystemClock.uptimeMillis() / 1000.0f;

        // Redraw background colour
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        cube.draw(vpMatrix, program, dt);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        // For taking into account the non-square aspect ratio of the device's display
        float ratio = (float) width / height;

        // Set projection matrix, to be used on eye co-ordinates on frame draw
        Matrix.frustumM(projectionMatrix, 0, ratio * -1, ratio * 1, -1, 1, 3, 32);

    }

    public void adjustViewAngle(float latAdj, float longAdj) {
        latitude += latAdj;
        longitude += longAdj;
        setViewMatrix(latitude, longitude);
    }

    private void setViewMatrix(float latitude, float longitude) {
        // Sphere rotation (eye co-ordinates)
        eyeX = (float) (-radius * Math.cos(latitude) * Math.sin(longitude)); // Since equation considers east positive, we must negate
        eyeY = (float) (radius * Math.sin(latitude));
        eyeZ = (float) (radius * Math.cos(latitude) * Math.cos(longitude));

        final float upX = 0.0f; // Does not change state as we are not rotating around the z axis
        final float upY = (float) Math.signum(Math.cos(latitude)); // d/dy (sin(y)) [1 | -1 | 0]
        final float upZ = upY == 0.0f ? (float) Math.signum(-Math.sin(latitude)) : 0.0f; // d/dt (cos(y)) [cannot be 0 if upY is 0]

        // View                          |---EYE--------|  |---CENTER-----|  |--UP VECTOR-|
        // NOTE: vectors eye and up must be sufficiently perpendicular (i.e. not parallel), else viewMatrix is undefined
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, 0.0f, 0.0f, 0.0f, upX, upY, upZ);
    }

    // Loader/setup functions
    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public static int loadTexture(final Context context, final int resourceId) {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false; // No pre-scaling

            // Read in resource (decode into a format understood by Android)
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL (Subsequent GL calls will refer to this texture)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering (Nearest picks closest texel)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }
}