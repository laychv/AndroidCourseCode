package com.assess15.ui.paint

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.assess15.ui.R;
import com.assess15.ui.paint.colorFilter.ColorFilterActivity
import com.assess15.ui.paint.demo.PwdEditTextActivity
import com.assess15.ui.paint.gradient.GradientActivity
import com.assess15.ui.paint.xfermode.XfermodeActivity
import kotlinx.android.synthetic.main.activity_paint.*

class PaintActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint)

        /**
         * 渐变色
         */
        paintGradient.setOnClickListener {
            startActivity(Intent(this, GradientActivity::class.java))
        }

        /**
         * xfermode
         */
        paintXfermode.setOnClickListener {
            startActivity(Intent(this, XfermodeActivity::class.java))
        }

        /**
         * 滤镜
         */
        paintColorFilter.setOnClickListener {
            startActivity(Intent(this, ColorFilterActivity::class.java))
//            startActivity(Intent(this, CFActivity::class.java))
        }

        /**
         * 自定义密码框
         */
        pwdEditText.setOnClickListener {
            startActivity(Intent(this, PwdEditTextActivity::class.java))
        }
    }
}
