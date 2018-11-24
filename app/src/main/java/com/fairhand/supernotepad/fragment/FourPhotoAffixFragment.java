package com.fairhand.supernotepad.fragment;

import android.view.View;

import com.fairhand.supernotepad.R;

/**
 * 四张图拼接的布局主题
 *
 * @author Phanton
 * @date 11/21/2018 - Wednesday - 9:00 PM
 */
public class FourPhotoAffixFragment extends BasePhotoAffixFragment implements View.OnClickListener {
    
    @Override
    public int setView() {
        return R.layout.fragment_photo_affix_four;
    }
    
    @Override
    public void init(View view) {
        ivTemplateOne = view.findViewById(R.id.iv_template_one);
        ivTemplateTwo = view.findViewById(R.id.iv_template_two);
        ivTemplateThree = view.findViewById(R.id.iv_template_three);
        
        ivTemplateOne.setOnClickListener(this);
        ivTemplateTwo.setOnClickListener(this);
        ivTemplateThree.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 模板拼图一
            case R.id.iv_template_one:
                mCallBack.template(1, 2);
                break;
            // 模板拼图二
            case R.id.iv_template_two:
                mCallBack.template(1, 4);
                break;
            // 模板拼图三
            case R.id.iv_template_three:
                mCallBack.template(1, 1);
                break;
            default:
                break;
        }
    }
    
}
