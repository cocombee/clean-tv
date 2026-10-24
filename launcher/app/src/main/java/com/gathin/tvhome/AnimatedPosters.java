package com.gathin.tvhome;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.io.File;

/**
 * Glass TV Custom v3 — Liquid Glass hero preview and ambient lighting.
 * Keeps the stable v2 focus selection and local video playback unchanged.
 * Maintains API AnimatedPosters.attach(this, root) used by MainActivity.java.
 * Playback uses local, muted H.264 MP4 files in app external files/posters/slotN.mp4.
 * Does not alter HOME routing, system apps, wallpaper config, or dock navigation.
 */
final class AnimatedPosters {
    private static final String TAG = "GlassTV.Posters";
    private static final long FOCUS_DELAY_MS = 220;
    private final Activity activity;
    private final FrameLayout root;
    private final RecyclerView grid;
    private final FrameLayout card;
    private final View halo;
    private final View rim;
    private final TextView appName;
    private final TextView previewTag;
    private final TextureView video;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private MediaPlayer player;
    private boolean surfaceReady;
    private boolean foreground = true;
    private int requestedSlot = -1;
    private int playingSlot = -1;
    private int generation;
    private final Runnable delayedPlay = this::openRequested;
    // Order corresponds to the user's first four dock tiles.
    private static final String[] APP_NAMES = {"Disney+", "YouTube", "Netflix", "Nuvio"};
    private static final int[] ACCENTS = {0xFF76B8FF, 0xFFFF556C, 0xFFEF4659, 0xFF9B83FF};

    static void attach(Activity activity, FrameLayout root) {
        if (root == null) {
            Log.w(TAG, "Root container unavailable; animation skipped");
            return;
        }
        new AnimatedPosters(activity, root);
    }

    private AnimatedPosters(Activity owner, FrameLayout parent) {
        activity = owner;
        root = parent;
        grid = owner.findViewById(R.id.grid);
        final float density = owner.getResources().getDisplayMetrics().density;

        // Static gradients only: no shaky zoom, translation or shader-driven animations.
        // The glow is beneath the video and ignores remote focus/touches.
        halo = new View(owner);
        halo.setFocusable(false);
        halo.setClickable(false);
        halo.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        halo.setVisibility(View.GONE);
        halo.setAlpha(0f);

        card = new FrameLayout(owner);
        GradientDrawable chrome = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{Color.rgb(20, 25, 37), Color.rgb(8, 12, 22)});
        chrome.setCornerRadius(22f * density);
        chrome.setStroke(Math.max(1, Math.round(1.3f * density)), 0x99FFFFFF);
        card.setBackground(chrome);
        card.setClipToOutline(true);
        card.setFocusable(false);
        card.setClickable(false);
        card.setElevation(0f);
        card.setVisibility(View.GONE);
        card.setAlpha(0f);

        video = new TextureView(owner) {
            @Override protected void onWindowVisibilityChanged(int visibility) {
                super.onWindowVisibilityChanged(visibility);
                foreground = visibility == View.VISIBLE;
                if (!foreground) {
                    handler.removeCallbacks(delayedPlay);
                    if (player != null) {
                        try { if (player.isPlaying()) player.pause(); }
                        catch (RuntimeException ignored) { }
                    }
                } else if (player != null) {
                    try { player.start(); } catch (RuntimeException ignored) { }
                } else {
                    openRequested();
                }
            }
        };
        video.setOpaque(false);
        video.setClipToOutline(true);
        video.setOutlineProvider(new ViewOutlineProvider() {
            @Override public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 24f * density);
            }
        });
        video.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override public void onSurfaceTextureAvailable(SurfaceTexture surface, int w, int h) {
                surfaceReady = true;
                openRequested();
            }
            @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int w, int h) { }
            @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                surfaceReady = false;
                releasePlayer();
                return true;
            }
            @Override public void onSurfaceTextureUpdated(SurfaceTexture surface) { }
        });
        card.addView(video, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Bottom glass tint makes the caption legible across all four video colours.
        View lowerTint = new View(owner);
        GradientDrawable shade = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0x00101524, 0xCC0C1423});
        lowerTint.setBackground(shade);
        lowerTint.setFocusable(false);
        card.addView(lowerTint, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(78), Gravity.BOTTOM));

        appName = new TextView(owner);
        appName.setTextColor(0xF9FFFFFF);
        appName.setTextSize(20f);
        appName.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        appName.setLetterSpacing(0.015f);
        appName.setSingleLine(true);
        appName.setFocusable(false);
        FrameLayout.LayoutParams nameLp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.START);
        nameLp.leftMargin = dp(20);
        nameLp.bottomMargin = dp(18);
        card.addView(appName, nameLp);

        previewTag = new TextView(owner);
        previewTag.setText("GLASS TV  /  MOTION PREVIEW");
        previewTag.setTextSize(9f);
        previewTag.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        previewTag.setLetterSpacing(0.11f);
        previewTag.setTextColor(0xE0FFFFFF);
        previewTag.setPadding(dp(12), dp(7), dp(12), dp(7));
        GradientDrawable tagBg = new GradientDrawable();
        tagBg.setColor(0x9920293D);
        tagBg.setCornerRadius(dp(13));
        tagBg.setStroke(Math.max(1, dp(0.6f)), 0x50FFFFFF);
        previewTag.setBackground(tagBg);
        previewTag.setFocusable(false);
        FrameLayout.LayoutParams tagLp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.START);
        tagLp.leftMargin = dp(17);
        tagLp.topMargin = dp(16);
        card.addView(previewTag, tagLp);

        // The inner light rim floats over the moving video but never steals focus.
        rim = new View(owner);
        GradientDrawable rimChrome = new GradientDrawable();
        rimChrome.setColor(Color.TRANSPARENT);
        rimChrome.setCornerRadius(dp(21));
        rimChrome.setStroke(Math.max(1, dp(0.7f)), 0xA0FFFFFF);
        rim.setBackground(rimChrome);
        rim.setFocusable(false);
        rim.setClickable(false);
        card.addView(rim, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Both layers remain under the real controls: clock, app dock, settings.
        root.addView(halo, 0, new FrameLayout.LayoutParams(1, 1, Gravity.TOP | Gravity.END));
        root.addView(card, 1, new FrameLayout.LayoutParams(1, 1, Gravity.TOP | Gravity.END));

        // Dock position is measured from actual tile locations, not fixed dp guesses.
        // This prevents the hero preview from covering the apps row on different TVs.
        root.getViewTreeObserver().addOnGlobalLayoutListener(this::positionCard);
        if (grid != null) {
            root.getViewTreeObserver().addOnGlobalFocusChangeListener((oldFocus, newFocus) -> {
                int position = adapterPosition(newFocus);
                int slot = position >= 0 && position < MainActivity.COLUMNS ? position + 1 : -1;
                changeSlot(slot);
            });
        } else {
            Log.w(TAG, "Grid unavailable; no dock-driven poster selection");
        }
    }

    private int dp(float value) {
        return Math.round(value * activity.getResources().getDisplayMetrics().density);
    }

    private int adapterPosition(View focused) {
        if (grid == null) return -1;
        View child = focused;
        while (child != null && child != grid) {
            if (child.getParent() == grid) return grid.getChildAdapterPosition(child);
            if (!(child.getParent() instanceof View)) break;
            child = (View) child.getParent();
        }
        return -1;
    }

    private int firstDockTileTop() {
        final int screenH = root.getHeight();
        if (grid == null || screenH <= 0) return Math.round(screenH * 0.68f);
        int[] rootLocation = new int[2];
        int[] itemLocation = new int[2];
        root.getLocationOnScreen(rootLocation);
        int firstTop = Integer.MAX_VALUE;
        for (int i = 0; i < grid.getChildCount(); i++) {
            View child = grid.getChildAt(i);
            int index = grid.getChildAdapterPosition(child);
            if (index >= 0 && index < MainActivity.COLUMNS) {
                child.getLocationOnScreen(itemLocation);
                firstTop = Math.min(firstTop, itemLocation[1] - rootLocation[1]);
            }
        }
        // When first row is not laid out yet, use a conservative fallback.
        if (firstTop == Integer.MAX_VALUE || firstTop < screenH * 0.42f) {
            return Math.round(screenH * 0.68f);
        }
        return firstTop;
    }

    private void positionCard() {
        int w = root.getWidth();
        int h = root.getHeight();
        if (w <= 0 || h <= 0) return;

        int dockTop = firstDockTileTop();
        int topSafe = Math.max(dp(70), Math.round(h * 0.17f));
        int bottomGap = Math.max(dp(18), Math.round(h * 0.035f));
        int available = dockTop - bottomGap - topSafe;
        if (available < dp(90)) return;

        int maxWidth = Math.round(w * 0.44f);
        int maxHeightByWidth = Math.round(maxWidth * 9f / 16f);
        int previewH = Math.min(maxHeightByWidth, available);
        int previewW = Math.round(previewH * 16f / 9f);
        int sideGap = Math.max(dp(20), Math.round(w * 0.037f));
        int top = topSafe + Math.max(0, (available - previewH) / 2);

        FrameLayout.LayoutParams old = (FrameLayout.LayoutParams) card.getLayoutParams();
        if (old.width == previewW && old.height == previewH
                && old.topMargin == top && old.rightMargin == sideGap) return;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                previewW, previewH, Gravity.TOP | Gravity.END);
        params.topMargin = top;
        params.rightMargin = sideGap;
        card.setLayoutParams(params);
        // Give the static halo a few pixels beyond the hero; it never crosses the dock.
        int haloPad = Math.min(dp(14), Math.max(0, (dockTop - (top + previewH)) / 2));
        FrameLayout.LayoutParams haloParams = new FrameLayout.LayoutParams(
                previewW + haloPad * 2, previewH + haloPad * 2,
                Gravity.TOP | Gravity.END);
        haloParams.topMargin = Math.max(0, top - haloPad);
        haloParams.rightMargin = Math.max(0, sideGap - haloPad);
        halo.setLayoutParams(haloParams);
    }

    private void applyAccent(int slot) {
        if (slot < 1 || slot > APP_NAMES.length) return;
        int accent = ACCENTS[slot - 1];
        appName.setText(APP_NAMES[slot - 1]);
        int a = Color.red(accent), b = Color.green(accent), c = Color.blue(accent);
        GradientDrawable aura = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{Color.argb(112, a, b, c), Color.argb(24, a, b, c), Color.TRANSPARENT});
        aura.setCornerRadius(dp(32));
        halo.setBackground(aura);
        GradientDrawable outline = new GradientDrawable();
        outline.setColor(Color.TRANSPARENT);
        outline.setCornerRadius(dp(21));
        outline.setStroke(Math.max(1, dp(0.8f)), Color.argb(210, a, b, c));
        rim.setBackground(outline);
    }

    private File fileFor(int slot) {
        File dir = activity.getExternalFilesDir(null);
        return dir == null ? null : new File(new File(dir, "posters"), "slot" + slot + ".mp4");
    }

    private void hideCard() {
        card.animate().cancel();
        halo.animate().cancel();
        card.setAlpha(0f);
        halo.setAlpha(0f);
        card.setVisibility(View.GONE);
        halo.setVisibility(View.GONE);
    }

    private void changeSlot(int slot) {
        if (slot == requestedSlot) return;
        requestedSlot = slot;
        handler.removeCallbacks(delayedPlay);
        if (slot < 0) {
            releasePlayer();
            hideCard();
        } else {
            handler.postDelayed(delayedPlay, FOCUS_DELAY_MS);
        }
    }

    private void openRequested() {
        if (requestedSlot < 0 || !foreground) return;
        File file = fileFor(requestedSlot);
        if (file == null || !file.isFile() || !file.canRead()) {
            releasePlayer();
            hideCard();
            return;
        }
        if (player != null && playingSlot == requestedSlot) return;
        releasePlayer();
        positionCard();
        applyAccent(requestedSlot);
        card.animate().cancel();
        halo.animate().cancel();
        card.setAlpha(0f);
        halo.setAlpha(0f);
        halo.setVisibility(View.VISIBLE);
        card.setVisibility(View.VISIBLE);
        if (!surfaceReady || video.getSurfaceTexture() == null) return;

        final int token = ++generation;
        try {
            MediaPlayer candidate = new MediaPlayer();
            player = candidate;
            playingSlot = requestedSlot;
            candidate.setDataSource(file.getAbsolutePath());
            Surface surface = new Surface(video.getSurfaceTexture());
            candidate.setSurface(surface);
            surface.release();
            candidate.setVolume(0f, 0f);
            candidate.setLooping(true);
            candidate.setOnPreparedListener(mp -> {
                if (generation != token || mp != player || !foreground) return;
                try {
                    mp.start();
                    card.animate().alpha(1f).setDuration(240).start();
                    halo.animate().alpha(0.72f).setDuration(380).start();
                } catch (RuntimeException ex) {
                    fail(ex);
                }
            });
            candidate.setOnErrorListener((mp, what, extra) -> {
                Log.w(TAG, "MediaPlayer error " + what + "/" + extra);
                releasePlayer();
                hideCard();
                return true;
            });
            candidate.prepareAsync();
        } catch (Exception e) {
            fail(e);
        }
    }

    private void fail(Exception error) {
        Log.w(TAG, "Poster unavailable; normal Glass TV remains", error);
        releasePlayer();
        hideCard();
    }

    private void releasePlayer() {
        generation++;
        playingSlot = -1;
        MediaPlayer old = player;
        player = null;
        if (old != null) {
            try { old.reset(); } catch (RuntimeException ignored) { }
            old.release();
        }
    }
}
