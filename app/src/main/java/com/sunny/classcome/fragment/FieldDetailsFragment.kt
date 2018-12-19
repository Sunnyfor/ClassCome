package com.sunny.classcome.fragment

import android.view.View
import com.sunny.classcome.MyApplication
import com.sunny.classcome.R
import com.sunny.classcome.activity.MapActivity
import com.sunny.classcome.base.BaseFragment
import com.sunny.classcome.bean.ClassDetailBean
import com.sunny.classcome.http.Constant
import kotlinx.android.synthetic.main.fragment_field_details.*

/**
 * Desc 场地详情
 * Author JoannChen
 * Mail Q8622268@gmail.com
 * Date 2018/12/15 20:40
 */
open class FieldDetailsFragment : BaseFragment() {
    private var lan = "0"
    private var lon = "0"
    private var name = ""

    override fun setLayout(): Int = R.layout.fragment_train_details

    override fun initView() {

        MyApplication.getApp().getData<ClassDetailBean>(Constant.CLASS_DETAIL, true)?.content?.resCourseVO?.course?.let { bean ->

            lan = bean.latitude?:""

            lon = bean.longitude?:""

            name = bean.classAddress

            // 使用时长/容纳人数
            txt_use_long.text = bean.onetime
            txt_people_count.text = bean.captynum

            // 场地空间/营业时间/场地位置
            txt_square.text = bean.workspace
            txt_time.text = bean.worktime
            txt_address.text = bean.classDetailAdress
        }

        ll_map.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_map -> MapActivity.start(requireContext(), lan, lon, name)
        }
    }
}