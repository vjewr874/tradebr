package cn.bizzan.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.bizzan.R;
import cn.bizzan.entity.WalletDetailNew;
import cn.bizzan.utils.WonderfulLogUtils;
import cn.bizzan.utils.WonderfulStringUtils;
import cn.bizzan.utils.WonderfulToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class WalletDetailAdapter extends BaseQuickAdapter<WalletDetailNew.ContentBean, BaseViewHolder> {

    public WalletDetailAdapter(@LayoutRes int layoutResId, @Nullable List<WalletDetailNew.ContentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletDetailNew.ContentBean contentBean) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(contentBean.getCreateTime());
            WonderfulLogUtils.logi("WalletDetailAdapter", "date  " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", date));
        String amount = WonderfulStringUtils.getLongFloatString(contentBean.getAmount(), 8);
        String fee = WonderfulStringUtils.getLongFloatString(contentBean.getFee(), 8);
        String discountFee = WonderfulStringUtils.getLongFloatString(Float.parseFloat(contentBean.getDiscountFee()), 8);
        String realFee = WonderfulStringUtils.getLongFloatString(Float.parseFloat(contentBean.getRealFee()), 8);
        String type = "";
        switch (contentBean.getType()) {
            case 0:
                type = WonderfulToastUtils.getString(R.string.top_up);
                break;
            case 1:
                type = WonderfulToastUtils.getString(R.string.withdrawal);
                break;
            case 2:
                type = WonderfulToastUtils.getString(R.string.transfer);
                break;
            case 3:
                type = WonderfulToastUtils.getString(R.string.coinCurrencyTrading);
                break;
            case 4:
                type = WonderfulToastUtils.getString(R.string.FiatMoneyBuy);
                break;
            case 5:
                type = WonderfulToastUtils.getString(R.string.FiatMoneySell);
                break;
            case 6:
                type = WonderfulToastUtils.getString(R.string.activitiesReward);
                break;
            case 7:
                type = WonderfulToastUtils.getString(R.string.promotionRewards);
                break;
            case 8:
                type = WonderfulToastUtils.getString(R.string.shareOutBonus);
                break;
            case 9:
                type = WonderfulToastUtils.getString(R.string.vote);
                break;
            case 10:
                type = WonderfulToastUtils.getString(R.string.ArtificialTop_up);
                break;
            case 11:
                type = WonderfulToastUtils.getString(R.string.matchMoney);
                break;
            case 12:
                type = WonderfulToastUtils.getString(R.string.activityexchange);
                break;
            case 13:
                type = WonderfulToastUtils.getString(R.string.ctcbuy);
                break;
            case 14:
                type = WonderfulToastUtils.getString(R.string.ctcsell);
                break;
            default:
        }
        helper.setText(R.id.trade_time_value, time).setText(R.id.trade_amount_value, amount).setText(R.id.trust_symbol, contentBean.getSymbol()
        ).setText(R.id.fee_value, fee).setText(R.id.discount_fee_value, discountFee).setText(R.id.real_fee_value, realFee).setText(R.id.trade_type_value, type);
    }


}
