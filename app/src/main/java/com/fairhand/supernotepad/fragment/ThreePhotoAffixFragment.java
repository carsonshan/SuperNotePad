package com.fairhand.supernotepad.fragment;

import android.view.View;

import com.fairhand.supernotepad.R;

/**
 * 三张图拼接的布局主题
 *
 * @author Phanton
 * @date 11/21/2018 - Wednesday - 8:59 PM
 */
public class ThreePhotoAffixFragment extends BasePhotoAffixFragment implements View.OnClickListener {
    
    @Override
    public int setView() {
        return R.layout.fragment_photo_affix_three;
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
                mCallBack.template(0, 0);
                break;
            // 模板拼图二
            case R.id.iv_template_two:
                mCallBack.template(1, 3);
                break;
            // 模板拼图三
            case R.id.iv_template_three:
                mCallBack.template(0, 5);
                break;
            default:
                break;
        }
    }
    
}
