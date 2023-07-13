package cn.bizzan.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import cn.bizzan.R;
import cn.bizzan.ui.account_pwd.AccountPwdActivity;
import cn.bizzan.ui.account_pwd.EditAccountPwdActivity;
import cn.bizzan.ui.bind_account.BindAccountActivity;
import cn.bizzan.ui.ctc.CTCActivity;
import cn.bizzan.ui.entrust.TrustListActivity;
import cn.bizzan.ui.login.LoginActivity;
import cn.bizzan.ui.my_ads.AdsActivity;
import cn.bizzan.ui.my_order.MyOrderActivity;
import cn.bizzan.ui.myinfo.MyInfoActivity;
import cn.bizzan.ui.setting.SettingActivity;
import cn.bizzan.ui.wallet.WalletActivity;
import cn.bizzan.ui.wallet_detail.WalletDetailActivity;
import cn.bizzan.app.MyApplication;
import cn.bizzan.base.BaseTransFragment;
import cn.bizzan.entity.Coin;
import cn.bizzan.entity.SafeSetting;
import cn.bizzan.entity.User;
import cn.bizzan.app.UrlFactory;
import cn.bizzan.utils.SharedPreferenceInstance;
import cn.bizzan.utils.WonderfulCommonUtils;
import cn.bizzan.utils.WonderfulStringUtils;
import cn.bizzan.utils.WonderfulToastUtils;
import cn.bizzan.utils.okhttp.StringCallback;
import cn.bizzan.utils.okhttp.WonderfulOkhttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/1/29.
 */

public class FiveFragment extends BaseTransFragment implements MainContract.FiveView {
    public static final String TAG = FiveFragment.class.getSimpleName();
    @BindView(R.id.llTop)
    LinearLayout llTop;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
    @BindView(R.id.llOrder)
    LinearLayout llOrder;
    @BindView(R.id.llAds)
    LinearLayout llAds;
    @BindView(R.id.line_top)
    LinearLayout line_top;
    @BindView(R.id.line_zican)
    LinearLayout line_zican;
    @BindView(R.id.line_shoukuan)
    LinearLayout line_shoukuan;
    @BindView(R.id.line_jypwd)
    LinearLayout line_jypwd;
    @BindView(R.id.line_bibi)
    LinearLayout line_bibi;
    @BindView(R.id.line_ctc)
    LinearLayout line_ctc;
    @BindView(R.id.line_security)
    LinearLayout line_security;
    @BindView(R.id.llSafe)
    ImageView llSafe;
    @BindView(R.id.llSettings)
    ImageView llSettings;
    @BindView(R.id.llMyinfo)
    LinearLayout llMyinfo;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvAccount)
    TextView tvAccount;

    @BindView(R.id.tvLevelOneCount)
    TextView tvLevelOneCount;
    @BindView(R.id.tvLevelTwoCount)
    TextView tvLevelTwoCount;
    @BindView(R.id.tvEstimatedReward)
    TextView tvEstimatedReward;
    @BindView(R.id.tvCurrentLevel)
    TextView tvCurrentLevel;
    @BindView(R.id.tvMyPromotionCode)
    TextView tvMyPromotionCode;

    @BindView(R.id.ivHeader)
    ImageView ivHeader;
    public static double sumUsd = 0;
    double sumCny = 0;
    @BindView(R.id.llEntrust)
    LinearLayout llEntrust;
    Unbinder unbinder;
    private MainContract.FivePresenter presenter;
    private SafeSetting safeSetting;
    private PopupWindow loadingPopup;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_five;
    }

    /**
     * 初始化加载dialog
     */
    private void initLoadingPopup() {
        View loadingView = getLayoutInflater().inflate(R.layout.pop_loading, null);
        loadingPopup = new PopupWindow(loadingView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingPopup.setFocusable(true);
        loadingPopup.setClippingEnabled(false);
        loadingPopup.setBackgroundDrawable(new ColorDrawable());
    }

    /**
     * 显示加载框
     */
    @Override
    public void displayLoadingPopup() {
        loadingPopup.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 隐藏加载框
     */
    @Override
    public void hideLoadingPopup() {
        if (loadingPopup != null) {
            loadingPopup.dismiss();
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        biaoshi = -1;
        initLoadingPopup();

        line_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginOrCenter();
//                if (MyApplication.getApp().isLogin()) {
//                    displayLoadingPopup();
//                    accountClick();
//                } else {
//                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
//                }
            }
        });

        line_shoukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    displayLoadingPopup();
                    accountClick();
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });
        line_jypwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    displayLoadingPopup();
                    jyPwdClick();
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });
        llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    displayLoadingPopup();
                    WalletActivity.actionStart(getActivity());
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }

            }
        });

        line_zican.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    displayLoadingPopup();
                    WalletDetailActivity.actionStart(getmActivity());
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });

        line_ctc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CTCActivity.show(getActivity());
            }
        });

        line_bibi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    displayLoadingPopup();
                    TrustListActivity.show(getActivity());
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });
        line_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginOrCenter();
            }
        });
        llOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    displayLoadingPopup();
                    MyOrderActivity.actionStart(getActivity(), 0);
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });
        llAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    if (MyApplication.realVerified == 1) {
                        WonderfulOkhttpUtils.get().url(UrlFactory.getShangjia())
                                .addHeader("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN())
                                .build().execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {
                                Log.i("miao", "商家认证" + response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int code = jsonObject.optInt("code");
                                    if (code == 0) {
                                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                                        int certifiedBusinessStatus = jsonObject1.optInt("certifiedBusinessStatus");
                                        if (certifiedBusinessStatus == 2) {
                                            displayLoadingPopup();
                                            AdsActivity.actionStart(getActivity(), MyApplication.getApp().getCurrentUser().getUsername(), MyApplication.getApp().getCurrentUser().getAvatar());
                                        } else {
                                            WonderfulToastUtils.showToast(WonderfulToastUtils.getString(R.string.shangjia));
                                        }
                                    } else {
                                        WonderfulToastUtils.showToast(WonderfulToastUtils.getString(R.string.unknown_error));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });
        llSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginOrCenter();
            }
        });
        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    displayLoadingPopup();
                    SettingActivity.actionStart(getActivity());
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });

        tvMyPromotionCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    User user = MyApplication.getApp().getCurrentUser();
                    String myInviteLink = user.getPromotionPrefix() + user.getPromotionCode();
                    WonderfulCommonUtils.copyText(getActivity(), myInviteLink);
                    WonderfulToastUtils.showToast(R.string.copy_success);
                }else{
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoadingPopup();

    }

    @Override
    public void onStart() {
        super.onStart();
        hideLoadingPopup();
    }


    private void toLoginOrCenter() {
        if (MyApplication.getApp().isLogin()) {
            MyInfoActivity.actionStart(getActivity());
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @Override
    protected void obtainData() {
    }

    @Override
    protected void fillWidget() {
    }

    @Override
    protected void loadData() {
        if (MyApplication.getApp().isLogin()) {
            loginingViewText();
        } else {
            notLoginViewText();
        }
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(getActivity(), llMyinfo);
            isSetTitle = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LoginActivity.RETURN_LOGIN:
                if (resultCode == Activity.RESULT_OK && getUserVisibleHint() && MyApplication.getApp().isLogin()) {
                    loginingViewText();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    notLoginViewText();
                }
                break;
            default:
        }
    }

    private void notLoginViewText() {
        try {
            sumCny = 0.00;
            sumUsd = 0.000000;
            tvNickName.setText(WonderfulToastUtils.getString(R.string.not_logged_in));
            tvLevelOneCount.setText("—");
            tvLevelTwoCount.setText("—");
            tvEstimatedReward.setText("—");
            tvCurrentLevel.setText("—");
            Glide.with(getActivity().getApplicationContext()).load(R.mipmap.icon_default_header).into(ivHeader);
        } catch (Exception e) {

        }
    }

    private void loginingViewText() {
        try {
            presenter.myWallet(getmActivity().getToken());
            presenter.safeSetting(getmActivity().getToken());
            User user = MyApplication.getApp().getCurrentUser();
            tvNickName.setText(user.getUsername());
            tvLevelOneCount.setText(String.valueOf(user.getFirstLevel()));
            tvLevelTwoCount.setText(String.valueOf(user.getSecondLevel()));
            tvCurrentLevel.setText(this.getPartnerNameByCount(user.getFirstLevel()));
            tvEstimatedReward.setText("0");
            if (!WonderfulStringUtils.isEmpty(user.getAvatar())) {
                Glide.with(getActivity().getApplicationContext()).load(user.getAvatar()).into(ivHeader);
            } else {
                Glide.with(getActivity().getApplicationContext()).load(R.mipmap.icon_default_header).into(ivHeader);
            }

            this.presenter.myPromotion(getmActivity().getToken());
        } catch (Exception e) {

        }

    }
    private String getPartnerNameByCount(int count){
        if(count > 0) {
            return "L1";
        }
        if(count > 10) {
            return "L2";
        }
        if(count > 100) {
            return "L3";
        }
        if(count > 500) {
            return "L4";
        }
        if(count > 1500) {
            return "L5";
        }
        if(count > 3000) {
            return "L6";
        }
        return "L0";
    }

    @Override
    public void setPresenter(MainContract.FivePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void myPromotionSuccess(JSONObject ret) {
        if(ret == null) {
            return;
        }
        try{
            tvEstimatedReward.setText(ret.getString("estimatedReward"));
            tvLevelOneCount.setText(ret.getString("levelOne"));
            tvLevelTwoCount.setText(ret.getString("levelTwo"));
        } catch (JSONException e) {
            tvEstimatedReward.setText("—");
        }
    }
    @Override
    public void myPromotionFail(Integer code, String toastMessage){
        tvEstimatedReward.setText("-");
    }
    @Override
    public void myWalletSuccess(List<Coin> obj) {
        if (obj == null) {
            return;
        }
        calcuTotal(obj);
    }

    private void calcuTotal(List<Coin> coins) {
        sumUsd = 0;
        sumCny = 0;
        for (Coin coin : coins) {
            sumUsd += (coin.getBalance() * coin.getCoin().getUsdRate());
            sumCny += (coin.getBalance() * coin.getCoin().getCnyRate());
        }
    }

    @Override
    public void myWalletFail(Integer code, String toastMessage) {
//        WonderfulCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
        biaoshi = 1;
//        SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
//        SharedPreferenceInstance.getInstance().saveLockPwd("");
        MyApplication.getApp().setCurrentUser(null);
        notLoginViewText();
        if (code == 4000) {
//            MyApplication.getApp().loginAgain(getmActivity());
            SharedPreferenceInstance.getInstance().saveaToken("");
            SharedPreferenceInstance.getInstance().saveTOKEN("");
        }
    }

    private void accountClick() {
        if (safeSetting == null) {
            return;
        }
        hideLoadingPopup();
        if (safeSetting.getRealVerified() == 1 && safeSetting.getFundsVerified() == 1) {
            BindAccountActivity.actionStart(getmActivity());
        } else {
            WonderfulToastUtils.showToast(WonderfulToastUtils.getString(R.string.password_realname));
        }
    }
    public void jyPwdClick(){
        if (safeSetting == null) return;
        if (safeSetting.getFundsVerified() == 0) AccountPwdActivity.actionStart(getmActivity());
        else if (safeSetting.getFundsVerified() == 1) EditAccountPwdActivity.actionStart(getmActivity());
    }

    private int biaoshi = -1;

    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        if (obj == null) {
            return;
        }
        this.safeSetting = obj;
        MyApplication.number = safeSetting.getMobilePhone();

//        if (tvIdCredit==null){
//            return;
//        }
//        if (safeSetting.getRealVerified() == 1) {
//            tvIdCredit.setEnabled(false);
//            tvIdCredit.setText(R.string.verification);
//        } else if (safeSetting.getRealAuditing() == 1) {
//            tvIdCredit.setEnabled(false);
//            tvIdCredit.setText(R.string.creditting);
//        } else {
//            tvIdCredit.setEnabled(true);
//            tvIdCredit.setText(R.string.unverified);
//        }
    }

    @Override
    public void safeSettingFail(Integer code, String toastMessage) {
        if (code == 4000) {
            MyApplication.getApp().setCurrentUser(null);
            SharedPreferenceInstance.getInstance().saveaToken("");
            SharedPreferenceInstance.getInstance().saveTOKEN("");
            notLoginViewText();
        }
        //do nothing
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
