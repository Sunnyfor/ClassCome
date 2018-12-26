package com.sunny.classcome.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.sunny.classcome.MyApplication
import com.sunny.classcome.R
import com.sunny.classcome.base.BaseActivity
import com.sunny.classcome.bean.ClassBean
import com.sunny.classcome.bean.ClassDetailBean
import com.sunny.classcome.bean.OrderDetailBean
import com.sunny.classcome.http.ApiManager
import com.sunny.classcome.http.Constant
import com.sunny.classcome.utils.DateUtil
import com.sunny.classcome.utils.GlideUtil
import com.sunny.classcome.utils.showBlueBtn
import com.sunny.classcome.utils.showGrayBtn
import kotlinx.android.synthetic.main.activity_order_detail.*

/**
 * Desc 订单详情页
 * Author JoannChen
 * Mail Q8622268@gmail.com
 * Date 2018/12/4 22:09
 */
class OrderDetailActivity : BaseActivity() {

    /**
     * 订单类型
     */

    override fun setLayout(): Int = R.layout.activity_order_detail

    private var classBean: ClassBean.Bean.Data? = null

    private var isAuthor = false

    companion object {
        fun start(context: Context, id: String,isAuthor:Boolean) {
            context.startActivity(Intent(context, OrderDetailActivity::class.java)
                    .putExtra("id", id)
                    .putExtra("isAuthor",isAuthor)
                    .putExtra("type", 1))
        }

        fun start(context: Context, classBean: ClassBean.Bean.Data,isAuthor:Boolean) {
            MyApplication.getApp().setData(Constant.COURSE, classBean)
            context.startActivity(Intent(context, OrderDetailActivity::class.java)
                    .putExtra("isAuthor",isAuthor)
                    .putExtra("type", 2))
        }
    }

    override fun initView() {
        showTitle(titleManager.defaultTitle(getString(R.string.order_detail)))

        isAuthor = intent.getBooleanExtra("isAuthor",false)

        view_detail.setOnClickListener(this)
        rl_info.setOnClickListener(this)
    }

    private fun showAudited() {
        txt_info.text = "审核通过，未中标"
        txt_order_number.text = ("订单编号：${classBean?.order?.orderNum}")
        txt_order_remark.text = getTime()
        showGrayBtn(txt_order_left, "取消订单")
        showBlueBtn(txt_order_mid, "邀请用户")
        showBlueBtn(txt_order_right, "应聘者")
    }

    private fun showField() {
        txt_info.text = "订单进行中"
        txt_prompt.text = "您的信息正在发布中"
        showGrayBtn(txt_order_right, "取消发布")
    }

    private fun showPurchaser() {
        txt_info.text = "订单进行中"
        txt_prompt.text = "系统默认将在核销完成后7天，对订单进行结算"
        txt_order_number.text = ("订单编号：${classBean?.order?.orderNum}")
        txt_order_remark.text = "验证码：2344 3455 3545"
        showBlueBtn(txt_order_right, "核销")

        rl_money.visibility = View.VISIBLE
        txt_money_desc.text = "支付金额"
        txt_money_count.text = ("￥${classBean?.course?.sumPrice}")

        rl_contact.visibility = View.VISIBLE
        txt_contact_desc.text = "联系方式"
        txt_contact_phone.text = classBean?.user?.telephone

        rl_info.visibility = View.VISIBLE
        txt_info_desc.text = "购买者信息"
    }

    private fun showClassFinish() {
        txt_info.text = "课程已结束"
        showBlueBtn(txt_order_left, "再次发布")
        showBlueBtn(txt_order_right, "评价")
    }

    private fun showClassIng() {
        txt_info.text = "订单进行中"
        txt_prompt.text = "系统默认将在课程结束后7天，对课程进行结算"
        txt_order_number.text = ("订单编号：${classBean?.order?.orderNum}")
        txt_order_remark.text = getTime()
        showGrayBtn(txt_order_left, "取消订单")
        showBlueBtn(txt_order_right, "结算")

        rl_money.visibility = View.VISIBLE
        txt_money_desc.text = "实付款"
        txt_money_count.text = ("￥${classBean?.course?.sumPrice}")

        rl_info.visibility = View.VISIBLE
        txt_info_desc.text = "代课者信息"
    }

    private fun showClassPay() {
        txt_info.text = "课程已中标，付款后订单生效"
        txt_order_number.text = ("订单编号：${classBean?.order?.orderNum}")
        txt_order_remark.text = getTime()
        showGrayBtn(txt_order_left, "取消订单")
        showBlueBtn(txt_order_right, "去支付")

        rl_money.visibility = View.VISIBLE
        txt_money_desc.text = "实付款"
        txt_money_count.text = ("￥${classBean?.course?.sumPrice}")

        rl_info.visibility = View.VISIBLE
        txt_info_desc.text = "代课者信息"
    }

    private fun showSettlement() {

        txt_info.text = "订单进行中"
        txt_prompt.text = "系统默认将在课程结束后7天，对课程进行结算"

        rl_money.visibility = View.VISIBLE
        txt_money_desc.text = "代课款"
        txt_money_count.text = ("￥${classBean?.course?.sumPrice}")

        rl_info.visibility = View.VISIBLE
        txt_info_desc.text = "发布者信息"
    }

    private fun showWinningBid() {
        txt_info.text = "您已中标，请按时完成代课"
        showGrayBtn(txt_order_right, "取消订单")

        rl_money.visibility = View.VISIBLE
        txt_money_desc.text = "代课款"
        txt_money_count.text = ("￥${classBean?.course?.sumPrice}")

        rl_info.visibility = View.VISIBLE
        txt_info_desc.text = "发布者信息"
    }

    private fun showPayFinish() {
        txt_info.text = "订单已完成"
        txt_order_number.text = ("订单编号：${classBean?.order?.orderNum}")
        txt_order_remark.text = "验证码：2344 3455 3545"
        showBlueBtn(txt_order_right, "评价")
    }

    private fun showPaying() {
        txt_info.text = "订单进行中"
        txt_prompt.text = "您已付款成功"
        txt_order_number.text = ("订单编号：${classBean?.order?.orderNum}")
        txt_order_remark.text = "验证码：2344 3455 3545"
        showGrayBtn(txt_order_right, "取消订单")
    }

    //场地、培训待支付
    private fun showPayWait() {
        txt_info.text = "订单已生成，付款后订单生效"
        txt_order_number.text = ("订单编号：${classBean?.course?.id}")
        showGrayBtn(txt_order_mid, "取消订单")
        txt_order_mid.setOnClickListener{
            CancelPromptActivity.start(this,if (isAuthor) 1 else 2,classBean?.course?.id?:"")
        }

        showBlueBtn(txt_order_right, "去支付")
        txt_order_right.setOnClickListener {
            classBean?.let {
                PayActivity.start(this@OrderDetailActivity,it)
            }
        }
        rl_money.visibility = View.VISIBLE
        txt_date.text = DateUtil.dateFormatYYMMddHHssmm(classBean?.order?.createTime?:"")
        txt_money_desc.text = "实付款"
        txt_money_count.text = ("￥${classBean?.course?.price}")
    }

    private fun showOffShelf() {
        txt_info.text = "已取消"
        txt_prompt.text = "您的信息已下架"
//        showGrayBtn(txt_order_mid, "删除")
        showBlueBtn(txt_order_right, "再次发布")
    }

    private fun showUnaudited() {
        txt_info.text = "审核未通过"
        txt_red_prompt.visibility = View.VISIBLE
    }

    private fun showOrderToBeAudited() {
        txt_info.text = "发布信息待审核"
        showGrayBtn(txt_order_right, "取消发布")
    }

    private fun getTime(): String {
        val startTime = classBean?.course?.startTime?.let {
            it.split(" ")[0]
        }
        val endTime = classBean?.course?.endTime?.let {
            it.split(" ")[0]
        }
        return "$startTime 至 $endTime"
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.view_detail -> {
                PublishDetailsActivity.startPublishDetail(this, classBean?.course?.coursetype
                        ?: "", classBean?.course?.id ?: "")
            }
            R.id.rl_info -> {
                MyProfileActivity.start(this, classBean?.course?.winningBidder ?: "")
            }
        }
    }


    override fun loadData() {

        if (intent.getIntExtra("type", 1) == 2) {
            classBean =  MyApplication.getApp().getData<ClassBean.Bean.Data>(Constant.COURSE,true)
            when (classBean?.order?.state) {
                "1" -> showPayWait()
            }

            txt_date.text = DateUtil.dateFormatYYMMddHHssmm(classBean?.course?.createTime ?: "")
            txt_class.text = classBean?.course?.title
            classBean?.materialList?.let {
                if (it.isNotEmpty()) {
                    GlideUtil.loadPhoto(this@OrderDetailActivity, img_class, it[0].url ?: "")
                }
            }
            return
        }

        showLoading()
        val params = HashMap<String, String>()
        params["id"] = intent.getStringExtra("id") ?: ""
        ApiManager.post(composites, params, Constant.ORDER_GETORDERDETAILNEW, object : ApiManager.OnResult<OrderDetailBean>() {
            override fun onSuccess(data: OrderDetailBean) {
                hideLoading()
                classBean = data.content

                when (data.content?.order?.state) {
                    "-1" -> showOffShelf()
                    "3" -> showClassIng()
                    "4" -> showClassPay()
                    "5" -> showClassFinish()

//                    order_tobe_audited -> showOrderToBeAudited()
//                    order_unaudited -> showUnaudited()
//                    order_off_shelf -> showOffShelf()
//                    order_audited -> showAudited()
//
//                    order_class_pay -> showClassPay()
//                    order_class_ing -> showClassIng()
//                    order_class_finish -> showClassFinish()
//
//                    order_pay_wait -> showPayWait()
//                    order_paying -> showPaying()
//                    order_pay_finish -> showPayFinish()
//
//                    order_field -> showField()
//                    order_purchaser -> showPurchaser()
//                    order_winning_bid -> showWinningBid()
//                    order_settlement -> showSettlement()

                }

//                when (intent.getIntExtra("type", order_tobe_audited)) {
//
//                    order_tobe_audited -> showOrderToBeAudited()
//                    order_unaudited -> showUnaudited()
//                    order_off_shelf -> showOffShelf()
//                    order_audited -> showAudited()
//
//                    order_class_pay -> showClassPay()
//                    order_class_ing -> showClassIng()
//                    order_class_finish -> showClassFinish()
//
//                    order_pay_wait -> showPayWait()
//                    order_paying -> showPaying()
//                    order_pay_finish -> showPayFinish()
//
//                    order_field -> showField()
//                    order_purchaser -> showPurchaser()
//                    order_winning_bid -> showWinningBid()
//                    order_settlement -> showSettlement()
//
//                }

                txt_date.text = DateUtil.dateFormatYYMMddHHssmm(classBean?.course?.createTime ?: "")
                txt_class.text = classBean?.course?.title
                classBean?.materialList?.let {
                    if (it.isNotEmpty()) {
                        GlideUtil.loadPhoto(this@OrderDetailActivity, img_class, it[0].url ?: "")
                    }
                }

            }

            override fun onFailed(code: String, message: String) {
                hideLoading()
            }

        })

    }
}