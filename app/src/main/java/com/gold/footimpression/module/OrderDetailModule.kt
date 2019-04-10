package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR
import com.google.gson.Gson

/**
 * 订单预览
 */
class OrderDetailModule(firstItem: Boolean = false) : BaseObservable() {
    var firstItem = false

    init {
        this.firstItem = firstItem
    }

    //    dingdanUid	string	订单Uid
    var zhongfangMingcheng=""
    var dingdanUid = ""
    var dingdanhao = ""//	string	订单编号
    var dingdanSeq = ""//	int	订单行号
    var dingdanLaiyuanName = ""//	string	订单渠道
    var kehuLaiyuan = ""//	string	客户来源
    var dingdanRiqi = ""//		Date	订单日期
    var fuwuXiangmuMingcheng = ""//	string	服务项目名称
    var dingdanJineFw = ""//		Decimal	项目金额
    var daodianTime = ""//		datetime	服务开始时间（到店时间）
    var daodianTimeStr = ""//		string	到店时间（HH:MM）
    var daojishi = ""//		long	倒计时(分）
    var dianzhong = ""//		boolean	是否点钟
    var dingdanJineZz = ""//		Decimal	订单金额（增值项目）
    var dingdanStatus = ""//		int	订单状态（编码）
    var dingdanStatusName = ""//		string	订单状态（名称）
    var endTime = "-"//		datetime	预计结束时间
    var fuwuShichang = "-"//		int	服务时长
    var huiyuanAcc = "0"//		Decimal	会员账户余额
    var huiyuanName = "-"//		string	会员姓名
    var huiyuanTel = "-"//		string	会员手机
    var huiyuanZhanghao = ""//	string	会员Id
    var jiedaiGonghao = "-"//	string	接待工号
    var jiedaiName = "-"//	string	接待姓名
    var jiesuanStatus = 0//	int	是否结算->说明：0：否；1：是
    var jishiGonghao = "-"//	string	技师工号
    var jishiName = "-"//	string	技师姓名
    var mendianBianma = "-"//	string	门店编码
    var mendianMingcheng = "-"//	string	门店名称
    var price = "-"//	Decimal	挂牌价
    var priceMember = "-"//		Decimal	会员价
    var shangzhongShijian = "-"//	datetime	上钟时间
    var shangzhongShijianStr = "-"//		string	上钟时间（HH:MM）
    var shifouLiusu = "-"//	string	是否留宿
    var shoupaiReturn = "-"//		int	是否归还手牌->说明：0：否；1：是
    var shoupaihao = "-"//		int	是否归还手牌->说明：0：否；1：是
    var weizhifu = "-"
    var yizhifu = "0"
    var xiazhongShijian = ""
    var xiazhongShijianStr = ""

    var history = false
    fun jiesuanStatusValue(): String {
        if (jiesuanStatus == 1) {
            return "是"
        }
        return "否"
    }

    //增值服务
    var orderIncrements = mutableListOf<OrderIncrementModule>()

    //在编辑的增值服务
    var orderEditIncrements = mutableListOf<OrderIncrementModule>()
}