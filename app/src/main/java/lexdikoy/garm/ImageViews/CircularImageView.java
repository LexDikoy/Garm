package lexdikoy.garm.ImageViews;

import android.content.Context;
import android.util.AttributeSet;
import lexdikoy.garm.ImageViews.shader.CircleShader;
import lexdikoy.garm.ImageViews.shader.ShaderHelper;


public class CircularImageView extends ShaderImageView {

    private CircleShader shader;

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        shader = new CircleShader();
        return shader;
    }

    public float getBorderRadius() {
        if(shader != null) {
            return shader.getBorderRadius();
        }
        return 0;
    }

    public void setBorderRadius(final float borderRadius) {
        if(shader != null) {
            shader.setBorderRadius(borderRadius);
            invalidate();
        }
    }
}
