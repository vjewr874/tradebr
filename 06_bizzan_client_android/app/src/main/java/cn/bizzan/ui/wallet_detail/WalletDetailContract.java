package cn.bizzan.ui.wallet_detail;


import cn.bizzan.base.Contract;
import cn.bizzan.entity.WalletDetailNew;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface WalletDetailContract {
    interface View extends Contract.BaseView<Presenter> {

        void allTransactionSuccess(WalletDetailNew obj);

        void allTransactionFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void allTransaction(String token, int pageNo, int pageSize,int memberId,String startTime,String endTime,String symbol,String type);
    }
}
