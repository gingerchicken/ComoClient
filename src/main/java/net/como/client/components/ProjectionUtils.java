package net.como.client.components;

import com.mojang.blaze3d.systems.RenderSystem;

import net.como.client.ComoClient;
import net.como.client.interfaces.mixin.IMatrix4f;
import net.como.client.structures.maths.Vec3;
import net.como.client.structures.maths.Vec4;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class ProjectionUtils {
    private static final Vec4 vec4 = new Vec4();
    private static final Vec4 mmMat4 = new Vec4();
    private static final Vec4 pmMat4 = new Vec4();
    private static final Vec3 camera = new Vec3();

    private static final Vec3 cameraNegated = new Vec3();

    private static Matrix4f model;
    private static Matrix4f projection;

    private static double windowScale;
    public static double scale;

    public static void update(MatrixStack matrices, Matrix4f projection) {
        MinecraftClient mc = ComoClient.getClient();

        model = matrices.peek().getPositionMatrix().copy();
        ProjectionUtils.projection = projection;

        camera.set(mc.gameRenderer.getCamera().getPos());
        cameraNegated.set(camera);
        cameraNegated.negate();

        windowScale = mc.getWindow().calculateScaleFactor(1, mc.forcesUnicodeFont());
    }

    public static boolean to2D(Vec3 pos, double scale) {
        MinecraftClient mc = ComoClient.getClient();
        ProjectionUtils.scale = getScale(pos) * scale;

        vec4.set(cameraNegated.x + pos.x, cameraNegated.y + pos.y, cameraNegated.z + pos.z, 1);

        ((IMatrix4f) (Object) model).multiplyMatrix(vec4, mmMat4);
        ((IMatrix4f) (Object) projection).multiplyMatrix(mmMat4, pmMat4);

        if (pmMat4.w <= 0.0f) return false;

        pmMat4.toScreen();
        double x = pmMat4.x * mc.getWindow().getFramebufferWidth();
        double y = pmMat4.y * mc.getWindow().getFramebufferHeight();

        if (Double.isInfinite(x) || Double.isInfinite(y)) return false;

        pos.set(x / windowScale, mc.getWindow().getFramebufferHeight() - y / windowScale, pmMat4.z);
        return true;
    }

    private static double getScale(Vec3 pos) {
        return Math.sqrt(camera.distanceTo(pos));
    }

    public static void unscaledProjection() {
        MinecraftClient mc = ComoClient.getClient();

        RenderSystem.setProjectionMatrix(Matrix4f.projectionMatrix(0, mc.getWindow().getFramebufferWidth(), 0, mc.getWindow().getFramebufferHeight(), 1000, 3000));
    }

    public static void scaledProjection() {
        MinecraftClient mc = ComoClient.getClient();
        
        RenderSystem.setProjectionMatrix(Matrix4f.projectionMatrix(0, (float) (mc.getWindow().getFramebufferWidth() / mc.getWindow().getScaleFactor()), 0, (float) (mc.getWindow().getFramebufferHeight() / mc.getWindow().getScaleFactor()), 1000, 3000));
    }
}
