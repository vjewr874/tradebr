package com.bizzan.bc.wallet.controller;

import java.util.HashMap;
import java.util.Map;

import com.bizzan.bc.wallet.service.AccountService;
import com.bizzan.bc.wallet.util.HttpClientUtil;
import com.bizzan.bc.wallet.util.MessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/rpc")
public class WalletController {
	
	@Value("${bizzan.blockApi}")
	private String blockApi;
	
	@Autowired
    private AccountService accountService;
	
	private Logger logger = LoggerFactory.getLogger(WalletController.class);
	
	@GetMapping("height")
    public MessageResult getHeight(){
		int[] addressIndex = {0}; 
    	// 获取高度
    	JSONObject param = new JSONObject();
    	param.put("jsonrpc", "2.0");
    	param.put("id", "0");
    	param.put("method", "get_height");
    	
    	Map<String, String> headerParam = new HashMap<String, String>();
    	headerParam.put("Content-Type", "application/json");
    	
        try {
			String result = HttpClientUtil.doHttpPost(this.blockApi, param.toJSONString(), headerParam);
			JSONObject obj = JSONObject.parseObject(result);
			Long height = obj.getJSONObject("result").getLong("height");
			MessageResult ret = new MessageResult(0, "success");
			ret.setData(height);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return MessageResult.error(500,"查询失败");
    }
	
}
