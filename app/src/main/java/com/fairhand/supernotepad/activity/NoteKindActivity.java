package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ImageView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.adapter.CardAdapter;
import com.fairhand.supernotepad.affair.AffairNoteActivity;
import com.fairhand.supernotepad.entity.Card;
import com.fairhand.supernotepad.puzzle.affix.PhotoAffixNoteActivity;
import com.fairhand.supernotepad.recording.view.RecordNoteActivity;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.video.view.VideoNoteActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 展示所有记事类型Activity
 *
 * @author FairHand
 * @date 2018/11/5
 */
public class NoteKindActivity extends AppCompatActivity {
    
    public static final String IMAGE_PATH = "IMAGE_PATH";
    
    @BindView(R.id.grid_view_kind)
    GridView gridViewKind;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    
    /**
     * 卡片数据
     */
    private Card[] cardArray = {
            new Card("普通记事", R.drawable.iv_common_note),
            new Card("手绘记事", R.drawable.ic_paint),
            new Card("事件记事", R.drawable.iv_affair_note),
            new Card("照片记事", R.drawable.iv_pictures),
            new Card("录音记事", R.drawable.ic_record),
            new Card("拼图记事", R.drawable.iv_affix),
            new Card("摄像记事", R.drawable.iv_video_note)
    };
    private ArrayList<Card> cards = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_kind);
        ButterKnife.bind(this);
        
        initData();
        setGridViewListener();
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        cards.addAll(Arrays.asList(cardArray));
        ivBack.setOnClickListener(v -> finish());
    }
    
    /**
     * 设置GridView的监听
     */
    private void setGridViewListener() {
        // 设置mGridViewCard的适配器、点击事件
        gridViewKind.setAdapter(new CardAdapter(this, cards));
        gridViewKind.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(NoteKindActivity.this, CommonNoteActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(NoteKindActivity.this, HandPaintNoteActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(NoteKindActivity.this, AffairNoteActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(NoteKindActivity.this, PictureNoteActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(NoteKindActivity.this, RecordNoteActivity.class));
                    break;
                case 5:
                    pick();
                    break;
                case 6:
                    startActivity(new Intent(NoteKindActivity.this, VideoNoteActivity.class));
                    break;
                default:
                    break;
            }
        });
    }
    
    private void pick() {
        // 进入相册
        PictureSelector.create(this)
                // 图片类型
                .openGallery(PictureMimeType.ofImage())
                // DIY主题
                .theme(R.style.picture_diy_style)
                // 最大选择5张
                .maxSelectNum(5)
                // 最少选择2张
                .minSelectNum(2)
                // 每行显示4个
                .imageSpanCount(4)
                // 多选模式
                .selectionMode(PictureConfig.MULTIPLE)
                // 可预览图片
                .previewImage(true)
                // 不显示拍照按钮
                .isCamera(false)
                // 压缩
                .compress(true)
                // 压缩质量
                .cropCompressQuality(60)
                // 图片列表点击缩放效果
                .isZoomAnim(true)
                // 不显示GIF图
                .isGif(false)
                // 结果回调onActivityResult
                .forResult(PictureConfig.CHOOSE_REQUEST);
        Toaster.showShort(this, "选择2~5张图片开始拼图");
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    ArrayList<String> path = new ArrayList<>();
                    for (LocalMedia item : selectList) {
                        if (item.isCompressed()) {
                            path.add(item.getCompressPath());
                        }
                    }
                    Intent intent = new Intent(NoteKindActivity.this, PhotoAffixNoteActivity.class);
                    intent.putStringArrayListExtra(IMAGE_PATH, path);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
    
}
