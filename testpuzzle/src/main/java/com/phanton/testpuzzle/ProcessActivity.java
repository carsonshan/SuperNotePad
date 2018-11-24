package com.phanton.testpuzzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.photolayout.R;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.PhotoPicker;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */
public class ProcessActivity extends AppCompatActivity implements View.OnClickListener {
    
    private static final int FLAG_CONTROL_LINE_SIZE = 1;
    
    private static final int FLAG_CONTROL_CORNER = 1 << 1;
    
    private PuzzleLayout puzzleLayout;
    private List<String> bitmapPaint;
    private PuzzleView squarePuzzleView;
    private DegreeSeekBar degreeSeekBar;
    
    private List<Target> targets = new ArrayList<>();
    
    private int deviceWidth = 0;
    
    private int controlFlag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        
        deviceWidth = getResources().getDisplayMetrics().widthPixels;
        
        int type = getIntent().getIntExtra("type", 0);
        int pieceSize = getIntent().getIntExtra("piece_size", 0);
        int themeId = getIntent().getIntExtra("theme_id", 0);
        bitmapPaint = getIntent().getStringArrayListExtra("photo_path");
        puzzleLayout = PuzzleUtils.getPuzzleLayout(type, pieceSize, themeId);
        
        initView();
        
        squarePuzzleView.post(new Runnable() {
            @Override
            public void run() {
                loadPhoto();
            }
        });
    }
    
    private void initView() {
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });
        
        squarePuzzleView = findViewById(R.id.puzzle_view);
        degreeSeekBar = findViewById(R.id.degree_seek_bar);
        
        //TODO the method we can use to change the puzzle view's properties
        squarePuzzleView.setPuzzleLayout(puzzleLayout);
        squarePuzzleView.setTouchEnable(true);
        squarePuzzleView.setNeedDrawLine(false);
        squarePuzzleView.setNeedDrawOuterLine(false);
        squarePuzzleView.setLineSize(4);
        // 边界线颜色
        squarePuzzleView.setLineColor(Color.RED);
        // 选择图片的外接线颜色
        squarePuzzleView.setSelectedLineColor(Color.BLUE);
        // 拖动图片的bar颜色
        squarePuzzleView.setHandleBarColor(Color.GREEN);
        squarePuzzleView.setAnimateDuration(300);
        squarePuzzleView.setOnPieceSelectedListener(new PuzzleView.OnPieceSelectedListener() {
            @Override
            public void onPieceSelected(PuzzlePiece piece, int position) {
                Snackbar.make(squarePuzzleView, "Piece " + position + " selected", Snackbar.LENGTH_SHORT).show();
            }
        });
        
        // currently the SlantPuzzleLayout do not support padding
        squarePuzzleView.setPiecePadding(0);
        
        ImageView btnReplace = findViewById(R.id.btn_replace);
        ImageView btnRotate = findViewById(R.id.btn_rotate);
        ImageView btnFlipHorizontal = findViewById(R.id.btn_flip_horizontal);
        ImageView btnFlipVertical = findViewById(R.id.btn_flip_vertical);
        ImageView btnBorder = findViewById(R.id.btn_border);
        ImageView btnCorner = findViewById(R.id.btn_corner);
        
        btnReplace.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        btnFlipHorizontal.setOnClickListener(this);
        btnFlipVertical.setOnClickListener(this);
        btnBorder.setOnClickListener(this);
        btnCorner.setOnClickListener(this);
        
        TextView btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                File file = FileUtils.getNewFile(ProcessActivity.this, "Puzzle");
                FileUtils.savePuzzle(squarePuzzleView, file, 100, new Callback() {
                    @Override
                    public void onSuccess() {
                        Snackbar.make(view, R.string.prompt_save_success, Snackbar.LENGTH_SHORT).show();
                    }
                    
                    @Override
                    public void onFailed() {
                        Snackbar.make(view, R.string.prompt_save_failed, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
        
        degreeSeekBar.setCurrentDegrees(squarePuzzleView.getLineSize());
        degreeSeekBar.setDegreeRange(0, 30);
        degreeSeekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {
            @Override
            public void onScrollStart() {
            
            }
            
            @Override
            public void onScroll(int currentDegrees) {
                switch (controlFlag) {
                    case FLAG_CONTROL_LINE_SIZE:
                        squarePuzzleView.setLineSize(currentDegrees);
                        break;
                    case FLAG_CONTROL_CORNER:
                        squarePuzzleView.setPieceRadian(currentDegrees);
                        break;
                    default:
                        break;
                }
            }
            
            @Override
            public void onScrollEnd() {
            
            }
        });
    }
    
    private void loadPhoto() {
        if (bitmapPaint == null) {
            loadPhotoFromRes();
            return;
        }
        
        final List<Bitmap> pieces = new ArrayList<>();
        
        final int count = bitmapPaint.size() > puzzleLayout.getAreaCount() ? puzzleLayout.getAreaCount()
                                  : bitmapPaint.size();
        
        for (int i = 0; i < count; i++) {
            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    pieces.add(bitmap);
                    if (pieces.size() == count) {
                        if (bitmapPaint.size() < puzzleLayout.getAreaCount()) {
                            for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
                                squarePuzzleView.addPiece(pieces.get(i % count));
                            }
                        } else {
                            squarePuzzleView.addPieces(pieces);
                        }
                    }
                    targets.remove(this);
                }
                
                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                
                }
                
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                
                }
            };
            
            Picasso.with(this)
                    .load("file:///" + bitmapPaint.get(i))
                    .resize(deviceWidth, deviceWidth)
                    .centerInside()
                    .config(Bitmap.Config.RGB_565)
                    .into(target);
            
            targets.add(target);
        }
    }
    
    private void loadPhotoFromRes() {
        final List<Bitmap> pieces = new ArrayList<>();
        
        final int[] resIds = new int[] {
                R.drawable.demo1, R.drawable.demo2, R.drawable.demo3,
                R.drawable.demo4, R.drawable.demo5, R.drawable.demo6,
                R.drawable.demo7, R.drawable.demo8, R.drawable.demo9,
        };
        
        final int count = resIds.length > puzzleLayout.getAreaCount() ? puzzleLayout.getAreaCount() : resIds.length;
        
        for (int i = 0; i < count; i++) {
            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    pieces.add(bitmap);
                    if (pieces.size() == count) {
                        if (resIds.length < puzzleLayout.getAreaCount()) {
                            for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
                                squarePuzzleView.addPiece(pieces.get(i % count));
                            }
                        } else {
                            squarePuzzleView.addPieces(pieces);
                        }
                    }
                    targets.remove(this);
                }
                
                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                
                }
                
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                
                }
            };
            
            Picasso.with(this).load(resIds[i]).config(Bitmap.Config.RGB_565).into(target);
            
            targets.add(target);
        }
    }
    
    private void share() {
        final File file = FileUtils.getNewFile(this, "Puzzle");
        
        FileUtils.savePuzzle(squarePuzzleView, file, 100, new Callback() {
            @Override
            public void onSuccess() {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                //Uri uri = Uri.fromFile(file);
                Uri uri = FileProvider.getUriForFile(ProcessActivity.this,
                        "com.phanton.testpuzzle.fileprovider", file);
                
                if (uri != null) {
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.prompt_share)));
                }
            }
            
            @Override
            public void onFailed() {
                Snackbar.make(squarePuzzleView, R.string.prompt_share_failed, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_replace:
                showSelectedPhotoDialog();
                break;
            case R.id.btn_rotate:
                squarePuzzleView.rotate(90f);
                break;
            case R.id.btn_flip_horizontal:
                squarePuzzleView.flipHorizontally();
                break;
            case R.id.btn_flip_vertical:
                squarePuzzleView.flipVertically();
                break;
            case R.id.btn_border:
                controlFlag = FLAG_CONTROL_LINE_SIZE;
                squarePuzzleView.setNeedDrawLine(!squarePuzzleView.isNeedDrawLine());
                if (squarePuzzleView.isNeedDrawLine()) {
                    degreeSeekBar.setVisibility(View.VISIBLE);
                    degreeSeekBar.setCurrentDegrees(squarePuzzleView.getLineSize());
                    degreeSeekBar.setDegreeRange(0, 30);
                } else {
                    degreeSeekBar.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btn_corner:
                if (controlFlag == FLAG_CONTROL_CORNER && degreeSeekBar.getVisibility() == View.VISIBLE) {
                    degreeSeekBar.setVisibility(View.INVISIBLE);
                    return;
                }
                degreeSeekBar.setCurrentDegrees((int) squarePuzzleView.getPieceRadian());
                controlFlag = FLAG_CONTROL_CORNER;
                degreeSeekBar.setVisibility(View.VISIBLE);
                degreeSeekBar.setDegreeRange(0, 100);
                break;
            default:
                break;
        }
    }
    
    private void showSelectedPhotoDialog() {
        PhotoPicker.newInstance().setMaxCount(1).pick(this);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Define.DEFAULT_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> paths = data.getStringArrayListExtra(Define.PATHS);
            String path = paths.get(0);
            
            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    squarePuzzleView.replace(bitmap);
                }
                
                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Snackbar.make(squarePuzzleView, "Replace Failed!", Snackbar.LENGTH_SHORT).show();
                }
                
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                
                }
            };
            
            //noinspection SuspiciousNameCombination
            Picasso.with(this)
                    .load("file:///" + path)
                    .resize(deviceWidth, deviceWidth)
                    .centerInside()
                    .config(Bitmap.Config.RGB_565)
                    .into(target);
        }
    }
    
}
