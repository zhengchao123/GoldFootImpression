package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR
import com.google.gson.Gson

/**
 * 提交 订单
 */
class OrderModule() : BaseObservable() {


//    dingdanUid	string	订单Uid
//    dingdanhao	string	订单编号
//    dingdanSeq	Integer	订单行号
//    dingdanLaiyuanName	string	订单渠道
//    kehuLaiyuan	string	客户来源

    var dingdanUid = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.dingdanUid)
        }
    var dingdanhao = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.dingdanhao)
        }
    var dingdanSeq = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.dingdanSeq)
        }
    var dingdanLaiyuanName = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.dingdanLaiyuanName)
        }

    var kehuLaiyuan = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.kehuLaiyuan)
        }


    var mendianBianma = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.mendianBianma)
        }
    var huiyuanTel = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.huiyuanTel)
        }
    var huiyuanZhanghao = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.huiyuanZhanghao)
        }
    var paramStr = BaseInfo()
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.paramStr)
        }


    var paramFuwuStr = mutableListOf<ServiceInfo>()
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.paramFuwuStr)
        }

    var paramStrContent = { Gson().toJson(paramStr) }
    var paramFuwuStrContent = { Gson().toJson(paramFuwuStr) }

    class BaseInfo() : BaseObservable() {
//        dingdanhao	string	订单号：固定传空
//        huiyuanZhanghao	string	会员Id
//        huiyuanTel	string	会员手机
//        mendianBianma	string	门店编码
//        daodianTime	date	时间（yyyy-MM-dd HH:MM:SS）
//        daodianHMStr	string	时间（时分）格式：HH:MM
//        kehuLaiyuan	string	客户来源
//        tuangouHuodong	string	团购平台
//        tuangouquanHao	string	团购码
//        dingdanLaiyuan	Integer	订单来源：30109（接待端）
//        xiadanType	Integer	下单来源：1（会员正常下单）
//        lururen	string	录入人：固定传空


        var dingdanhao: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.dingdanhao)
            }

        var huiyuanZhanghao: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.huiyuanZhanghao)
            }

        var huiyuanTel: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.huiyuanTel)
            }


        var mendianBianma: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.mendianBianma)
            }
        var daodianTime: String = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.daodianTime)
            }
        var daodianHMStr: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.daodianHMStr)
            }

        var kehuLaiyuan: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.kehuLaiyuan)
            }

        var tuangouHuodong: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.tuangouHuodong)
            }

        var tuangouquanHao: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.tuangouquanHao)
            }

        var dingdanLaiyuan: String? = "30109"
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.dingdanLaiyuan)
            }
        var xiadanType: String? = "1"
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.xiadanType)
            }
        var lururen: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.lururen)
            }
    }

    class ServiceInfo() : BaseObservable() {
//        dingdanUid	string	订单UID：固定传空
//        fuwuXiangmuBianma	Integer	服务项目编码
//        jishiGonghao	string	技师工号
//        dianzhong	Boolean	是否点钟
//        dingdanJineFwdz	BigDecimal	服务折扣价（折后价）
//        tuangoujia	BigDecimal	团购价
//        zhongfangBianma	string	房间号


//         shoupaihao	Integer	手牌号
//         jiedaiGonghao	string	接待工号
//         xcrenCode	string	宣传人编码
//         xcrenName	string	宣传人姓名
//         youhuijia	BigDecimal	优惠价
//         buchajia	Short	是否补差价->说明：0=否；1=是

        var buchajia: String = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.buchajia)
            }
        var buchajiaValue: String = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.buchajiaValue)
            }
        var youhuijia: String = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.youhuijia)
            }
        var shoupaihao: String = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.shoupaihao)
            }
        var jiedaiGonghao: String = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.jiedaiGonghao)
            }

        var xcrenCode: String = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.xcrenCode)
            }

        var xcrenName: String = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.xcrenName)
            }


        var dingdanUid: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.dingdanUid)
            }

        var fuwuXiangmuBianma: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.fuwuXiangmuBianma)
            }

        var jishiGonghao: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.jishiGonghao)
            }

        var dianzhong: Boolean = false
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.dianzhong)
            }

        var dingdanJineFwdz = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.dingdanJineFwdz)
            }
        var tuangoujia = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.tuangoujia)
            }
        var zhongfangBianma = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.zhongfangBianma)
            }


    }


}