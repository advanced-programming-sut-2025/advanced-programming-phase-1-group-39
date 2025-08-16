package com.StardewValley.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;

/**
 * نقطه شروع اصلی برنامه سرور.
 * این کلاس یک اپلیکیشن Headless (بدون رابط گرافیکی) LibGDX را راه‌اندازی می‌کند.
 */
public class ServerLauncher {
    public static void main(String[] args) {
        // یک شیء پیکربندی برای برنامه Headless ایجاد می‌شود.
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();

        // برنامه سرور (ServerApplication) به عنوان یک اپلیکیشن Headless اجرا می‌شود.
        // این کار باعث می‌شود Gdx.files و سایر APIهای LibGDX در سرور قابل استفاده باشند.
        MockGraphics mockGraphics = new MockGraphics();
        Gdx.graphics = mockGraphics;
        Gdx.gl = mockGraphics.getGL20();
        Gdx.gl20 = mockGraphics.getGL20();
        // Gdx.gl30 = mockGraphics.getGL30(); // اگر از OpenGL ES 3.0 استفاده می‌کنید

        new HeadlessApplication(new ServerApplication(), config);
    }
}