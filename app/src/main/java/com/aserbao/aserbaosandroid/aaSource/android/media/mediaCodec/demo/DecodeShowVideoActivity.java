package com.aserbao.aserbaosandroid.aaSource.android.media.mediaCodec.demo;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import com.aserbao.aserbaosandroid.AUtils.utils.screen.DisplayUtil;
import com.aserbao.aserbaosandroid.R;
import com.aserbao.aserbaosandroid.aaThird.pickvideo.VideoPickActivity;
import com.aserbao.aserbaosandroid.aaThird.pickvideo.beans.VideoFile;
import com.aserbao.aserbaosandroid.comon.base.BaseRecyclerViewActivity;
import com.aserbao.aserbaosandroid.comon.base.beans.BaseRecyclerBean;
import com.aserbao.aserbaosandroid.comon.commonData.StaticFinalValues;
import com.aserbao.aserbaosandroid.opengl.openGlCamera.recordCamera.ui.AspectTextureView;

import java.util.ArrayList;

import static com.aserbao.aserbaosandroid.aaThird.pickvideo.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.aserbao.aserbaosandroid.aaThird.pickvideo.VideoPickActivity.IS_NEED_CAMERA;
import static com.aserbao.aserbaosandroid.comon.commonData.StaticFinalValues.MAX_NUMBER;

/**
 * MediaCodec视频解码到Surface上播放。
 */
public class DecodeShowVideoActivity extends BaseRecyclerViewActivity {
    private static final String TAG = "DecodeShowVideoActivity";



    @Override
    public void initGetData() {
        mBaseRecyclerBeen.add(new BaseRecyclerBean(StaticFinalValues.VIEW_HOLDER_HEAD,"先选择视频，再播放视频"));
        mBaseRecyclerBeen.add(new BaseRecyclerBean("选择本地视频",0));
        mBaseRecyclerBeen.add(new BaseRecyclerBean("用SurfaceView显示解码视频",1));
        mBaseRecyclerBeen.add(new BaseRecyclerBean("用TexureView显示解码视频",2));
        mBaseRecyclerBeen.add(new BaseRecyclerBean("用AspectTextureView显示解码视频",3));

    }

    @Override
    public void itemClickBack(View view, int position, boolean isLongClick, int comeFrom) {
        switch (position){
            case 0:
                Intent intent2 = new Intent(this, VideoPickActivity.class);
                intent2.putExtra(IS_NEED_CAMERA, false);
                intent2.putExtra(MAX_NUMBER, 1);
                intent2.putExtra(IS_NEED_FOLDER_LIST, true);
                startActivityForResult(intent2, StaticFinalValues.COME_FROM_REQUEST_CODE_TAKE_VIDEO);
                break;
            case 1:
                useSurfaceView();
                break;
            case 2:
                useTextureView();
                break;
            case 3:
                useAspectTextureView(isLongClick);
                break;
        }
    }


    private MediaCodecDecode mMediaCodecDecode;
    private void useSurfaceView() {
        int width = DisplayUtil.dip2px(300);
        int height = DisplayUtil.dip2px(400);
        View root = addViewWHToFL(null,R.layout.surface_view_layout, false, true,width,height,false);
        SurfaceView mSurfaceView = ((SurfaceView) root.findViewById(R.id.surface_view));
        SurfaceHolder mSurfaceViewHolder = mSurfaceView.getHolder();
        mSurfaceViewHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mMediaCodecDecode = new MediaCodecDecode(videoFileName, mSurfaceViewHolder.getSurface());
                mMediaCodecDecode.start();
                Log.d(TAG, "surfaceCreated() called with: holder = [" + holder + "]");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d(TAG, "surfaceChanged() called with: holder = [" + holder + "], format = [" + format + "], width = [" + width + "], height = [" + height + "]");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(TAG, "surfaceDestroyed() called with: holder = [" + holder + "]");
            }
        });
    }
    private void useTextureView() {
        int width = DisplayUtil.dip2px(300);
        int height = DisplayUtil.dip2px(400);
        View root = addViewWHToFL(null,R.layout.texture_view_layout, false, true,width,height,false);
        TextureView textureView = (TextureView) root.findViewById(R.id.texture_view);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mMediaCodecDecode = new MediaCodecDecode(videoFileName, new Surface(surface));
                mMediaCodecDecode.start();
                Log.d(TAG, "onSurfaceTextureAvailable() called with: surface = [" + surface + "], width = [" + width + "], height = [" + height + "]");
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                Log.d(TAG, "onSurfaceTextureSizeChanged() called with: surface = [" + surface + "], width = [" + width + "], height = [" + height + "]");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                Log.e(TAG, "onSurfaceTextureUpdated: " + surface.toString());
            }
        });
    }
    private void useAspectTextureView(boolean isLongClick){
        int width = DisplayUtil.dip2px(300);
        int height = DisplayUtil.dip2px(400);
        View root = addViewWHToFL(null,R.layout.aspect_texture_view, false, true,width,height,false);
        AspectTextureView aspectTextureView = (AspectTextureView) root.findViewById(R.id.aspect_texture_view);
        double aspectRatio = (double) 720 / (double) 1280;
        aspectTextureView.setAspectRatio(AspectTextureView.MODE_INSIDE, aspectRatio);
        aspectTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mMediaCodecDecode = new MediaCodecDecode(videoFileName, new Surface(surface));
                mMediaCodecDecode.start();
                Log.d(TAG, "onSurfaceTextureAvailable() called with: surface = [" + surface + "], width = [" + width + "], height = [" + height + "]");
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                Log.d(TAG, "onSurfaceTextureSizeChanged() called with: surface = [" + surface + "], width = [" + width + "], height = [" + height + "]");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                Log.e(TAG, "onSurfaceTextureUpdated: " + surface.toString());
            }
        });
    }

    @Override
    protected void onPause() {
        if (mMediaCodecDecode != null) {
            mMediaCodecDecode.release();
        }
        super.onPause();
    }

    String videoFileName = "/storage/emulated/0/720aserbao5.mp4";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case StaticFinalValues.COME_FROM_REQUEST_CODE_TAKE_VIDEO:
                if (resultCode == RESULT_OK) {
                    ArrayList<VideoFile> list = data.getParcelableArrayListExtra(StaticFinalValues.RESULT_PICK_VIDEO);
                    for (VideoFile file : list) {
                        videoFileName = file.getPath();
                    }
                    Toast.makeText(this, "视频已选择成功\n" + videoFileName, Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }
}
