package cn.bizzan.ui.ctc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import cn.bizzan.R;
import cn.bizzan.app.Injection;
import cn.bizzan.app.MyApplication;
import cn.bizzan.base.BaseActivity;
import cn.bizzan.entity.CTCOrder;
import cn.bizzan.entity.CTCOrderDetail;
import cn.bizzan.entity.CTCPrice;
import cn.bizzan.entity.SafeSetting;
import cn.bizzan.utils.WonderfulCodeUtils;
import cn.bizzan.utils.WonderfulStringUtils;

public class CTCOrderDetailActivity  extends BaseActivity implements CTCContract.View{
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.view_back)
    View view_back;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tvDirection)
    TextView tvDirection;
    @BindView(R.id.tvCountDown)
    TextView tvCountDown;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.llBank)
    LinearLayout llBank;
    @BindView(R.id.llWechatpay)
    LinearLayout llWechatpay;
    @BindView(R.id.llAlipay)
    LinearLayout llAlipay;
    @BindView(R.id.tvMyAccountTitle)
    TextView tvMyAccountTitle;
    @BindView(R.id.tvRealName)
    TextView tvRealName;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.tvBankBranch)
    TextView tvBankBranch;
    @BindView(R.id.tvBankCardNo)
    TextView tvBankCardNo;
    @BindView(R.id.tvWechatNo)
    TextView tvWechatNo;
    @BindView(R.id.tvAliNo)
    TextView tvAliNo;
    @BindView(R.id.ivCodeImageAli)
    ImageView ivCodeImageAli;
    @BindView(R.id.ivCodeImageWechat)
    ImageView ivCodeImageWechat;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvOrderSn)
    TextView tvOrderSn;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.btnPay)
    TextView btnPay;
    @BindView(R.id.btnCancel)
    TextView btnCancel;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;

    private CTCOrderDetail detailOrder;
    private CTCContract.Presenter presenter;

    private CountDownTimer timer;
    private Long leftTime = Long.valueOf(0);

    public static void show(Context activity, CTCOrderDetail order) {
        Intent intent = new Intent(activity, CTCOrderDetailActivity.class);
        intent.putExtra("order", order);
        activity.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_ctcorderdetail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new CTCPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            detailOrder = (CTCOrderDetail) intent.getSerializableExtra("order");
        }

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.ctcOrderPay(getToken(), detailOrder.getId());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.ctcOrderCancel(getToken(), detailOrder.getId());
            }
        });
    }

    @Override
    protected void obtainData() {
        tvDirection.setText(detailOrder.getDirection() == 0 ? "买入" : "卖出");
        tvDirection.setTextColor(detailOrder.getDirection() == 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
        String temStatus = "";
        switch(detailOrder.getStatus()){
            case 0: temStatus = "[待接单] "; break;
            case 1: temStatus = "[已接单] "; break;
            case 2: temStatus = "[已付款] "; break;
            case 3: temStatus = "[已完成] "; break;
            case 4: temStatus = "[已取消] "; break;
        }
        tvOrderSn.setText(detailOrder.getOrderSn());
        tvAmount.setText(String.valueOf(detailOrder.getAmount()));
        tvPrice.setText(String.valueOf(detailOrder.getPrice()));
        tvMoney.setText(String.valueOf(detailOrder.getMoney()));
        tvMoney.setTextColor(detailOrder.getDirection() == 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
        if(detailOrder.getStatus() == 3 || detailOrder.getStatus() == 4) {
            tvRealName.setText("* * *");
        }else{
            tvRealName.setText(detailOrder.getRealName());
        }
        tvTime.setText(detailOrder.getCreateTime());
        if(detailOrder.getPayMode().equals("bank")) {
            llBank.setVisibility(View.VISIBLE); llAlipay.setVisibility(View.GONE);llWechatpay.setVisibility(View.GONE);
            if(detailOrder.getStatus() == 3 || detailOrder.getStatus() == 4) {
                tvBankName.setText("* * * *");
                tvBankBranch.setText("* * * *");
                tvBankCardNo.setText("* * * * * * * * * * * *");
            }else{
                tvBankName.setText(detailOrder.getBankInfo().getBank());
                tvBankBranch.setText(detailOrder.getBankInfo().getBranch());
                tvBankCardNo.setText(detailOrder.getBankInfo().getCardNo());
            }
        }
        if(detailOrder.getPayMode().equals("alipay")) {
            llBank.setVisibility(View.GONE); llAlipay.setVisibility(View.VISIBLE);llWechatpay.setVisibility(View.GONE);
            if(detailOrder.getStatus() == 3 || detailOrder.getStatus() == 4) {
                tvAliNo.setText("* * * * * *");
                Glide.with(getApplicationContext()).load(R.mipmap.pay_zfb_default).into(ivCodeImageAli);
            }else {
                tvAliNo.setText(detailOrder.getAlipay().getAliNo());
                Glide.with(getApplicationContext()).load(WonderfulStringUtils.isEmpty(detailOrder.getAlipay().getQrCodeUrl()) ? R.mipmap.pay_zfb_default : detailOrder.getAlipay().getQrCodeUrl()).into(ivCodeImageAli);
            }
        }
        if(detailOrder.getPayMode().equals("wechatpay")) {
            llBank.setVisibility(View.GONE); llAlipay.setVisibility(View.GONE);llWechatpay.setVisibility(View.VISIBLE);
            if(detailOrder.getStatus() == 3 || detailOrder.getStatus() == 4){
                tvWechatNo.setText("* * * * * *");
                Glide.with(getApplicationContext()).load(R.mipmap.pay_wechat_default).into(ivCodeImageWechat);
            }else {
                tvWechatNo.setText(detailOrder.getWechatPay().getWechatNo());
                Glide.with(getApplicationContext()).load(WonderfulStringUtils.isEmpty(detailOrder.getWechatPay().getQrCodeUrl()) ? R.mipmap.pay_wechat_default : detailOrder.getWechatPay().getQrCodeUrl()).into(ivCodeImageWechat);
            }
        }
        if(detailOrder.getDirection() == 0 && detailOrder.getStatus() == 0){
            tvTips.setText(temStatus + "等待承兑商接单中...");
        }
        if(detailOrder.getDirection() == 0 && detailOrder.getStatus() == 1){
            tvTips.setText(temStatus + "承兑商已接单，请您尽快付款");
        }
        if(detailOrder.getDirection() == 0 && detailOrder.getStatus() == 2){
            tvTips.setText(temStatus + "等待承兑商放币中...");
        }
        if(detailOrder.getDirection() == 0 && detailOrder.getStatus() == 3){
            tvTips.setText(temStatus + "恭喜！订单已完成！");
        }
        if(detailOrder.getDirection() == 0 && detailOrder.getStatus() == 4){
            tvTips.setText(temStatus + "("+detailOrder.getCancelReason()+")");
        }
        if(detailOrder.getDirection() == 0 && detailOrder.getStatus() == 0){
            tvTips.setText(temStatus + "等待承兑商接单中...");
        }
        if(detailOrder.getDirection() == 1 && detailOrder.getStatus() == 1){
            tvTips.setText(temStatus + "承兑商已接单，正在付款中...");
        }
        if(detailOrder.getDirection() == 1 && detailOrder.getStatus() == 2){
            tvTips.setText(temStatus + "承兑商已付款，确认放币中...");
        }
        if(detailOrder.getDirection() == 1 && detailOrder.getStatus() == 3){
            tvTips.setText(temStatus + "恭喜！订单已完成！");
        }
        if(detailOrder.getDirection() == 1 && detailOrder.getStatus() == 4){
            tvTips.setText(temStatus + "("+detailOrder.getCancelReason()+")");
        }
        tvMyAccountTitle.setText(detailOrder.getDirection() == 0 ? "请转账至下列承兑商账户" : "您的以下账户将收到承兑商汇款");

        if((detailOrder.getDirection() == 0 && detailOrder.getStatus() < 2) || (detailOrder.getDirection() == 1 && detailOrder.getStatus() == 0)){
            llBottom.setVisibility(View.VISIBLE);
            if((detailOrder.getDirection() == 0 && detailOrder.getStatus() < 2) || (detailOrder.getDirection() == 1 && detailOrder.getStatus() == 0)){
                btnCancel.setVisibility(View.VISIBLE);
            }else{
                btnCancel.setVisibility(View.GONE);
            }
            if(detailOrder.getDirection() == 0 && detailOrder.getStatus() == 1){
                btnPay.setVisibility(View.VISIBLE);
            }else{
                btnPay.setVisibility(View.GONE);
            }
        }else{
            llBottom.setVisibility(View.GONE);
        }
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        presenter.ctcOrderDetail(getToken(), detailOrder.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void setPresenter(CTCContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }
    private void startCounter(){
        tvCountDown.setVisibility(View.GONE);
        if(detailOrder.getStatus() == 1 && detailOrder.getDirection() == 0) {
            // 30分钟内付款倒计时
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                Date currentDate = format.parse(detailOrder.getCurrentTime());
                Long current = currentDate.getTime();
                Date confirmDate = format.parse(detailOrder.getConfirmTime());
                Long confirm = confirmDate.getTime();

                if(current - confirm < 1800000) {
                    leftTime = 1800000 - (current - confirm );
                    // 启动计时器
                    tvCountDown.setVisibility(View.VISIBLE);
                    startCountTimer(leftTime);
                }else{
                    if(timer != null) {
                        timer.cancel();
                        timer = null;
                        tvCountDown.setVisibility(View.GONE);
                    }
                    tvCountDown.setVisibility(View.VISIBLE);
                    tvCountDown.setText("订单已超时");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            if(timer != null) {
                timer.cancel();
                timer = null;
                tvCountDown.setVisibility(View.GONE);
            }
        }
    }

    private void startCountTimer(long time) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long m = (millisUntilFinished / 1000) / 60;
                long s = (millisUntilFinished /1000) % 60;
                String minute = String.valueOf(m);
                if(m < 10){
                    minute = "0" + minute;
                }
                String seconds = String.valueOf(s);
                if(s < 10){
                    seconds = "0" + seconds;
                }
                tvCountDown.setText("剩余时间：" + minute + "分" + seconds + "秒");
            }

            @Override
            public void onFinish() {
                tvCountDown.setText("订单已超时");
                timer.cancel();
                timer = null;
            }
        };
        timer.start();
    }
    @Override
    public void safeSettingSuccess(SafeSetting obj) {

    }

    @Override
    public void safeSettingFail(Integer code, String toastMessage) {

    }

    @Override
    public void ctcOrderListFail(Integer code, String toastMessage) {

    }

    @Override
    public void ctcOrderListSuccess(CTCOrder obj) {

    }

    @Override
    public void ctcOrderDetailFail(Integer code, String toastMessage) {

    }

    @Override
    public void ctcOrderDetailSuccess(CTCOrderDetail obj) {
        detailOrder = obj;
        obtainData();
        startCounter();
    }

    @Override
    public void ctcOrderPayFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void ctcOrderPaySuccess(CTCOrderDetail obj) {
        detailOrder = obj;
        obtainData();
        startCounter();
    }

    @Override
    public void ctcOrderCancelFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void ctcOrderCancelSuccess(CTCOrderDetail obj) {
        detailOrder = obj;
        obtainData();
        startCounter();
    }

    @Override
    public void ctcPriceFail(Integer code, String toastMessage) {

    }

    @Override
    public void ctcPriceSuccess(CTCPrice obj) {

    }

    @Override
    public void ctcNewOrderFail(Integer code, String toastMessage) {

    }

    @Override
    public void ctcNewOrderSuccess(CTCOrderDetail obj) {

    }

    @Override
    public void ctcSendNewOrderPhoneCodeFail(Integer code, String toastMessage) {

    }

    @Override
    public void ctcSendNewOrderPhoneCodeSuccess(String obj) {

    }
}
