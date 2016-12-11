package com.harlan.smonitor.monitor.core.job.http;

import java.io.IOException;
import java.util.List;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.http.HttpMonitorItem;
import com.harlan.smonitor.monitor.bean.http.check.CheckBody;
import com.harlan.smonitor.monitor.core.job.AbstractService;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CheckBodyServiceImpl extends AbstractService {
	private final static Logger logger = LoggerFactory.getLogger(CheckBodyServiceImpl.class);
	private String TITLE="http服务监控报警";
	@Override
	protected void run(MonitorItem item, CheckItem checkItem) throws Exception {

		logger.info("---------------------------check start---------------------------");
		HttpMonitorItem httpItem = (HttpMonitorItem) item;
		CheckBody bodyItem = (CheckBody) checkItem;
		// ip地址

		logger.info("开始检查,检查的类型为：{}", httpItem.getName());
		logger.info("检查http服务是否可以正常调用，服务的地址是:{}，调用的方式为:{},发送的数据为：{}",
				httpItem.getUrl(), httpItem.getMethod(), httpItem.getData());

		String response_string = null;
		if ("post".equalsIgnoreCase(httpItem.getMethod())) {
			response_string = callUrlGetResp_post(httpItem.getUrl(),
					httpItem.getData());
		} else {
			response_string = callUrlGetResp_get(httpItem.getUrl());
		}
		logger.info("调用服务所接收到的返回内容：{}", response_string);
		// 判断包含的关键字
		List<String> contains = bodyItem.getContains();
		boolean flag = false;
		for (String keyword : contains) {
			// 如果包含关键字则报警
			if (response_string.contains(keyword)) {
				flag = true;
				logger.info("调用当前服务得到的返回结果中包含{}关键字,满足单次报警条件", keyword);
				checkYesOrNotSendMsg(checkItem,TITLE,"调用"+httpItem.getUrl()+"服务得到的返回结果中包含"+keyword+"关键字");
				//一次有一个关键字满足就达到单次报警条件，不继续执行了
				break;
			}
		}
		if (!flag) {
			logger.info("包含关键字检查中没有匹配到配置的任何关键字,重置报警次数");
			restAlarmCount(checkItem.getId());
		}
		flag = false;
		// 判断不包含的关键字
		List<String> exclude = bodyItem.getExclude();
		for (String keyword : exclude) {
			// 如果包含这个关键字就不报警
			if (!response_string.contains(keyword)) {
				flag = true;
				logger.info("调用当前服务得到的返回结果中不包含{}关键字,满足单次报警条件", keyword);
				checkYesOrNotSendMsg(checkItem,TITLE,"调用"+httpItem.getUrl()+"服务得到的返回结果中不包含{}关键字");
				//一次有一个关键字满足就达到单次报警条件，不继续执行了
				break;
			}
		}
		if (!flag) {
			logger.info("“不包含关键字”检查中没有匹配到配置的任何关键字,重置报警次数");
			restAlarmCount(checkItem.getId());
		}
		logger.info("---------------------------check end---------------------------");
	}

	/**
	 * 调用url，获取url的访问结果:POST方式
	 * 
	 * @param url
	 * @param data
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private String callUrlGetResp_post(String url, String data)
			throws ParseException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost http_post = new HttpPost(url);
		HttpEntity entity = EntityBuilder.create().setContentEncoding("UTF-8")
				.setBinary(data.getBytes("UTF-8")).build();
		http_post.setEntity(entity);
		CloseableHttpResponse response = httpclient.execute(http_post);
		String response_string = EntityUtils.toString(response.getEntity());
		return response_string;
	}

	/**
	 * 调用url，获取url的访问结果:GET方式
	 * 
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private String callUrlGetResp_get(String url) throws ParseException,
			IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpUriRequest httpReq = new HttpGet(url);
		CloseableHttpResponse response = httpclient.execute(httpReq);
		String response_string = EntityUtils.toString(response.getEntity());
		return response_string;
	}
}
