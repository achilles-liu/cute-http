package org.johnny.http.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.johnny.http.StageContainer;
import org.johnny.http.StageMgr;
import org.johnny.http.model.RequestHeader;
import org.johnny.http.model.RequestInfo;
import org.johnny.http.model.RequestParameter;
import org.johnny.http.resource.R;
import org.johnny.http.utils.Constant.HttpMethod;
import org.johnny.http.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class AppController implements Initializable,IBaseController{
	private static final Logger logger = LoggerFactory.getLogger(AppController.class);
	@FXML private Button myButton;
	@FXML private TextField urlTextFiled;
	@FXML private ComboBox<String> reqMethodComboBox;
	@FXML private ComboBox<String> contentTypeComboBox;
	
	@FXML private TableView<RequestInfo> requestTable;
	
	//Parameter
	@FXML private TextField parameterName;
	@FXML private TextField parameterValue;
	@FXML private TableView<RequestParameter> parameterTable;
	private boolean isParameterTabSelected = false;
	
	//response
	@FXML private TextArea respTextAea;
	//history
	private Map<String,CloseableHttpResponse> responses = new LinkedHashMap<>();
	private Map<String,String> responseBody = new LinkedHashMap<>();
	
	//Header
	@FXML private TextField headerName;
	@FXML private TextField headerValue;
	@FXML private TableView<RequestHeader> headerTable;
	private boolean isHeaderTabSelected = false;
	
	//Content to Send
	@FXML private TextArea ctsContent;
	
	public void submitRequest() {
		try {
			String httpMethod = this.reqMethodComboBox.getSelectionModel().getSelectedItem();
			String url = this.urlTextFiled.getText();
			RequestInfo reqInfo = new RequestInfo();
			reqInfo.setRequestId(UUIDUtil.uuid());
			reqInfo.setRequest(url);
			reqInfo.setRequestDate(LocalDate.now());
			if(logger.isDebugEnabled()) {
				logger.info("method:{},requestId:{},url:{}",httpMethod,reqInfo.getRequestId(),reqInfo.getRequest());
			}
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			CloseableHttpResponse response = null;
			switch(httpMethod) {
			case HttpMethod.GET:
				reqInfo.setMethod(HttpMethod.GET);
				HttpGet httpGet = new HttpGet(url);
				this.headerTable.getItems().forEach(h ->{
					httpGet.addHeader(h.getName(), h.getValue());
				});
				String contentType = this.contentTypeComboBox.getEditor().getText();
				if(contentType != null && !"".equals(contentType)) {
					httpGet.addHeader("Content-Type", contentType);
				}
				response = httpClient.execute(httpGet);
				this.responses.put(reqInfo.getRequestId(), response);
				reqInfo.setResponse(String.valueOf(response.getStatusLine().getStatusCode()));
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String relStr = EntityUtils.toString(response.getEntity());
					this.respTextAea.setText(relStr);
				}else {
					this.respTextAea.setText(response.toString());
				}
				this.responseBody.put(reqInfo.getRequestId(), this.respTextAea.getText());
				this.requestTable.getItems().add(reqInfo);
				break;
			case HttpMethod.POST:
				reqInfo.setMethod(HttpMethod.POST);
				HttpPost httpPost = new HttpPost(url);
				this.headerTable.getItems().forEach(h ->{
					httpPost.addHeader(h.getName(), h.getValue());
				});
				String postContentType = this.contentTypeComboBox.getEditor().getText();
				if(postContentType != null && !"".equals(postContentType)) {
					httpPost.addHeader("Content-Type", postContentType);
				}
				String contentStr = this.ctsContent.getText();
				if(null != contentStr && !"".equals(contentStr)) {
					StringEntity entity = new StringEntity(contentStr,"utf-8");
					entity.setContentEncoding("UTF-8");
					entity.setContentType("application/json");
					httpPost.setEntity(entity);
				}
				response = httpClient.execute(httpPost);
				this.responses.put(reqInfo.getRequestId(), response);
				reqInfo.setResponse(String.valueOf(response.getStatusLine().getStatusCode()));
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String relStr = EntityUtils.toString(response.getEntity());
					this.respTextAea.setText(relStr);
				}else {
					this.respTextAea.setText(response.toString());
				}
				this.responseBody.put(reqInfo.getRequestId(), this.respTextAea.getText());
				this.requestTable.getItems().add(reqInfo);
				break;
			case HttpMethod.PUT:
				reqInfo.setMethod(HttpMethod.PUT);
				HttpPut httpPut = new HttpPut(url);
				this.headerTable.getItems().forEach(h ->{
					httpPut.addHeader(h.getName(), h.getValue());
				});
				String putContentType = this.contentTypeComboBox.getEditor().getText();
				if(putContentType != null && !"".equals(putContentType)) {
					httpPut.addHeader("Content-Type", putContentType);
				}
				String putContentStr = this.ctsContent.getText();
				if(null != putContentStr && !"".equals(putContentStr)) {
					StringEntity entity = new StringEntity(putContentStr,"utf-8");
					entity.setContentEncoding("UTF-8");
					entity.setContentType("application/json");
					httpPut.setEntity(entity);
				}
				response = httpClient.execute(httpPut);
				this.responses.put(reqInfo.getRequestId(), response);
				reqInfo.setResponse(String.valueOf(response.getStatusLine().getStatusCode()));
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String relStr = EntityUtils.toString(response.getEntity());
					this.respTextAea.setText(relStr);
				}else {
					this.respTextAea.setText(response.toString());
				}
				this.responseBody.put(reqInfo.getRequestId(), this.respTextAea.getText());
				this.requestTable.getItems().add(reqInfo);
				break;
			case HttpMethod.DELETE:
				reqInfo.setMethod(HttpMethod.DELETE);
				HttpDelete httpDelete = new HttpDelete(url);
				this.headerTable.getItems().forEach(h ->{
					httpDelete.addHeader(h.getName(), h.getValue());
				});
				String deleteContentType = this.contentTypeComboBox.getEditor().getText();
				if(deleteContentType != null && !"".equals(deleteContentType)) {
					httpDelete.addHeader("Content-Type", deleteContentType);
				}
				response = httpClient.execute(httpDelete);
				this.responses.put(reqInfo.getRequestId(), response);
				reqInfo.setResponse(String.valueOf(response.getStatusLine().getStatusCode()));
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String relStr = EntityUtils.toString(response.getEntity());
					this.respTextAea.setText(relStr);
				}else {
					this.respTextAea.setText(response.toString());
				}
				this.responseBody.put(reqInfo.getRequestId(), this.respTextAea.getText());
				this.requestTable.getItems().add(reqInfo);
				break;
				default:
					break;
			}
		} catch (Exception e) {
			logger.error("exception:{}",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {
		if(this.reqMethodComboBox != null) reqMethodComboBox.getItems().addAll("GET","POST","PUT","DELETE");
		if(this.contentTypeComboBox != null) 
			this.contentTypeComboBox.getItems().addAll("application/json","application/xml","text/plain","text/html");
		TableColumn<RequestInfo, String> requestHeader = new TableColumn<RequestInfo, String>("Request");
		requestHeader.setMinWidth(400);
		requestHeader.setCellValueFactory(new Callback<CellDataFeatures<RequestInfo, String>, ObservableValue<String>>(){
			public ObservableValue<String> call(CellDataFeatures<RequestInfo, String> param) {
				return param.getValue().request();
			}
		});
		requestHeader.setCellFactory(new Callback<TableColumn<RequestInfo,String>,TableCell<RequestInfo,String>>(){
			public TableCell<RequestInfo, String> call(TableColumn<RequestInfo, String> param) {
				TextFieldTableCell<RequestInfo, String> cell = new TextFieldTableCell<RequestInfo,String>();
				cell.setOnMouseClicked(e->{
					if(e.getClickCount() == 2) {
						doubleClickViewRequestInfo();
					}
				});
				return cell;
			}
		});
		TableColumn<RequestInfo, String> resposeHeader = new TableColumn<RequestInfo, String>("Response");
		resposeHeader.setCellValueFactory(new Callback<CellDataFeatures<RequestInfo, String>, ObservableValue<String>>(){
			public ObservableValue<String> call(CellDataFeatures<RequestInfo, String> param) {
				return param.getValue().response();
			}
		});
		TableColumn<RequestInfo, String> dateHeader = new TableColumn<RequestInfo, String>("Date");
		dateHeader.setCellValueFactory(new Callback<CellDataFeatures<RequestInfo, String>, ObservableValue<String>>(){
			public ObservableValue<String> call(CellDataFeatures<RequestInfo, String> param) {
				LocalDate requestDate = param.getValue().requestDate().get();
				return new SimpleStringProperty(requestDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}
		});
		this.requestTable.getColumns().addAll(requestHeader,resposeHeader,dateHeader);
	}
	
	public void addRequest() {
		String url = "http://localhost:8080/rest/user/1";
		String respStr = "200";
		if(logger.isDebugEnabled()) {
			logger.info("url:{},responseStr:{}",url,respStr);
		}
		RequestInfo p = new RequestInfo(url,respStr);
		this.requestTable.getItems().add(p);
	}
	
	public void clearHistory() {
		this.requestTable.getItems().clear();
		this.responses.clear();
		this.responseBody.clear();
		this.respTextAea.clear();
		if(logger.isDebugEnabled()) {
			logger.info("responses:{},responseBody:{}",this.responses,this.responseBody);
		}
	}
	
	public void deleteSelectedRequest() {
		int selIndex = this.requestTable.getSelectionModel().getSelectedIndex();
		if(logger.isDebugEnabled()) {
			logger.info("selIndex:{}",selIndex);
		}
		this.requestTable.getItems().remove(selIndex);
	}
	
	@SuppressWarnings("unchecked")
	public void paramTabSelection() {
		this.isParameterTabSelected = ! this.isParameterTabSelected;
		if(isParameterTabSelected) {
			if(this.parameterTable.getColumns().size() <= 0) {
				TableColumn<RequestParameter, String> nameHeader = new TableColumn<RequestParameter, String>("Name");
				nameHeader.setCellValueFactory(new Callback<CellDataFeatures<RequestParameter, String>, ObservableValue<String>>(){
					public ObservableValue<String> call(CellDataFeatures<RequestParameter, String> param) {
						return param.getValue().name();
					}
				});
				TableColumn<RequestParameter, String> valueHeader = new TableColumn<RequestParameter, String>("Value");
				valueHeader.setCellValueFactory(new Callback<CellDataFeatures<RequestParameter, String>, ObservableValue<String>>(){
					public ObservableValue<String> call(CellDataFeatures<RequestParameter, String> param) {
						return param.getValue().value();
					}
				});
				this.parameterTable.getColumns().addAll(nameHeader,valueHeader);
			}
		}
	}
	
	public void addParameter() {
		String paramName = this.parameterName.getText();
		String paramValue = this.parameterValue.getText();
		if(logger.isDebugEnabled()) {
			logger.info("paramName:{},paramValue:{}",paramName,paramValue);
		}
		if(paramName != null && !"".equals(paramName)) {
			RequestParameter rp = new RequestParameter(paramName,paramValue);
			this.parameterTable.getItems().add(rp);
			
			//append url
			String url = this.urlTextFiled.getText();
			StringBuilder fullUrl = new StringBuilder(url);
			if(fullUrl.indexOf("?") < 0) {
				fullUrl.append("?");
			}else {
				fullUrl.append("&");
			}
			fullUrl.append(paramName).append("=").append(paramValue);
			if(logger.isDebugEnabled()) {
				logger.info("fullUrl:{}",fullUrl.toString());
			}
			this.urlTextFiled.setText(fullUrl.toString());
		}
	}
	
	public void removeParameter() {
		int paramSelIndex = this.parameterTable.getSelectionModel().getSelectedIndex();
		if(paramSelIndex >= 0) {
			this.parameterTable.getItems().remove(paramSelIndex);
			String url = this.urlTextFiled.getText();
			int index = url.indexOf("?");
			if(index >= 0) {
				url = url.substring(0, index);
			}
			StringBuilder fullUrl = new StringBuilder(url);
			for(int i = 0;i < this.parameterTable.getItems().size();i++) {
				RequestParameter rp = this.parameterTable.getItems().get(i);
				if(i > 0) {
					fullUrl.append("&");
				}else {
					fullUrl.append("?");
				}
				fullUrl.append(rp.getName()).append("=").append(rp.getValue());
			}
			this.urlTextFiled.setText(fullUrl.toString());
		}
	}
	
	public void doubleClickViewRequestInfo() {
		try {
			RequestInfo reqInfo = this.requestTable.getSelectionModel().getSelectedItem();
			if(logger.isDebugEnabled()) {
				logger.info("requestId:{},url:{}",reqInfo.getRequestId(),reqInfo.getRequest());
			}
			CloseableHttpResponse resp = this.responses.get(reqInfo.getRequestId());
			Header[] headers = resp.getAllHeaders();
			StringBuilder sbHttp = new StringBuilder();
			String url = reqInfo.getRequest();
			url = url.indexOf("?")>0 ? url.substring(0, url.indexOf("?")-1) : url;
			sbHttp.append(reqInfo.getMethod()).append("\t").append(url).append("\n").append("\n");
			sbHttp.append("-- response --").append("\n").append(resp.getStatusLine().getStatusCode()).append("\n");
			StringBuilder  dataStr = null;
			for(Header header:headers) {
				if("Date".equals(header.getName())) {
					dataStr = new StringBuilder("Date: ").append(header.getValue()).append("\n");
					continue;
				}
				sbHttp.append(header.getName()).append(": ").append(header.getValue()).append("\n");
			}
			if(dataStr == null) {
				String curr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
				sbHttp.append("Date: ").append(curr).append("\n");
			}else {
				sbHttp.append(dataStr);
			}
			sbHttp.append("\n");
			sbHttp.append(this.responseBody.get(reqInfo.getRequestId()));
			StageContainer sc = StageMgr.INSTANCE.container();
			sc.load(R.id.POPWIN, R.layout.POPWIN);
			sc.show(R.id.POPWIN,sbHttp.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void headerTabSelection() {
		this.isHeaderTabSelected = !this.isHeaderTabSelected;
		if(this.isHeaderTabSelected) {
			if(this.headerTable.getColumns().size() <= 0) {
				TableColumn<RequestHeader, String> nameHeader = new TableColumn<RequestHeader, String>("Name");
				nameHeader.setCellValueFactory(new Callback<CellDataFeatures<RequestHeader, String>, ObservableValue<String>>(){
					public ObservableValue<String> call(CellDataFeatures<RequestHeader, String> param) {
						return param.getValue().name();
					}
				});
				TableColumn<RequestHeader, String> valueHeader = new TableColumn<RequestHeader, String>("Value");
				valueHeader.setCellValueFactory(new Callback<CellDataFeatures<RequestHeader, String>, ObservableValue<String>>(){
					public ObservableValue<String> call(CellDataFeatures<RequestHeader, String> param) {
						return param.getValue().value();
					}
				});
				this.headerTable.getColumns().addAll(nameHeader,valueHeader);
			}
		}
	}
	
	public void addHeader() {
		String headerName = this.headerName.getText();
		String headerValue = this.headerValue.getText();
		if(headerName != null && !"".equals(headerValue)) {
			RequestHeader rh = new RequestHeader(headerName,headerValue);
			this.headerTable.getItems().add(rh);
		}
	}
	
	public void removeHeader() {
		int headerSelIndex = this.headerTable.getSelectionModel().getSelectedIndex();
		if(headerSelIndex >= 0) {
			this.headerTable.getItems().remove(headerSelIndex);
		}
	}
	
	public void setFormType() {
		this.contentTypeComboBox.getEditor().setText("application/x-www-form-urlencoded");
	}
	
	public void getRequest() {
		try {
			String url = this.urlTextFiled.getText();
			RequestInfo reqInfo = new RequestInfo();
			reqInfo.setRequest(url);
			reqInfo.setRequestDate(LocalDate.now());
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			CloseableHttpResponse response = null;
			HttpGet httpGet = new HttpGet(url);
			this.headerTable.getItems().forEach(h ->{
				httpGet.addHeader(h.getName(), h.getValue());
			});
			String contentType = this.contentTypeComboBox.getEditor().getText();
			if(contentType != null && !"".equals(contentType)) {
				httpGet.addHeader("Content-Type", contentType);
			}
			response = httpClient.execute(httpGet);
			reqInfo.setResponse(String.valueOf(response.getStatusLine().getStatusCode()));
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String relStr = EntityUtils.toString(response.getEntity());
				this.respTextAea.setText(relStr);
			}else {
				this.respTextAea.setText(response.toString());
			}
			this.requestTable.getItems().add(reqInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void postRequest() {
		try {
			String url = this.urlTextFiled.getText();
			RequestInfo reqInfo = new RequestInfo();
			reqInfo.setRequest(url);
			reqInfo.setRequestDate(LocalDate.now());
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			CloseableHttpResponse response = null;
			HttpPost httpPost = new HttpPost(url);
			this.headerTable.getItems().forEach(h ->{
				httpPost.addHeader(h.getName(), h.getValue());
			});
			String postContentType = this.contentTypeComboBox.getEditor().getText();
			if(postContentType != null && !"".equals(postContentType)) {
				httpPost.addHeader("Content-Type", postContentType);
			}
			String contentStr = this.ctsContent.getText();
			if(null != contentStr && !"".equals(contentStr)) {
				StringEntity entity = new StringEntity(contentStr,"utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				httpPost.setEntity(entity);
			}
			response = httpClient.execute(httpPost);
			reqInfo.setResponse(String.valueOf(response.getStatusLine().getStatusCode()));
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String relStr = EntityUtils.toString(response.getEntity());
				this.respTextAea.setText(relStr);
			}else {
				this.respTextAea.setText(response.toString());
			}
			this.requestTable.getItems().add(reqInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void putRequest() {
		try {
			String url = this.urlTextFiled.getText();
			RequestInfo reqInfo = new RequestInfo();
			reqInfo.setRequest(url);
			reqInfo.setRequestDate(LocalDate.now());
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			CloseableHttpResponse response = null;
			HttpPut httpPut = new HttpPut(url);
			this.headerTable.getItems().forEach(h ->{
				httpPut.addHeader(h.getName(), h.getValue());
			});
			String putContentType = this.contentTypeComboBox.getEditor().getText();
			if(putContentType != null && !"".equals(putContentType)) {
				httpPut.addHeader("Content-Type", putContentType);
			}
			String putContentStr = this.ctsContent.getText();
			if(null != putContentStr && !"".equals(putContentStr)) {
				StringEntity entity = new StringEntity(putContentStr,"utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				httpPut.setEntity(entity);
			}
			response = httpClient.execute(httpPut);
			reqInfo.setResponse(String.valueOf(response.getStatusLine().getStatusCode()));
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String relStr = EntityUtils.toString(response.getEntity());
				this.respTextAea.setText(relStr);
			}else {
				this.respTextAea.setText(response.toString());
			}
			this.requestTable.getItems().add(reqInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void inject(Object data) {
		/**ignore*/
	}
	
	public void deleteRequest() {
		try {
			String url = this.urlTextFiled.getText();
			RequestInfo reqInfo = new RequestInfo();
			reqInfo.setRequest(url);
			reqInfo.setRequestDate(LocalDate.now());
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			CloseableHttpResponse response = null;
			HttpDelete httpDelete = new HttpDelete(url);
			this.headerTable.getItems().forEach(h ->{
				httpDelete.addHeader(h.getName(), h.getValue());
			});
			String postContentType = this.contentTypeComboBox.getEditor().getText();
			if(postContentType != null && !"".equals(postContentType)) {
				httpDelete.addHeader("Content-Type", postContentType);
			}
			response = httpClient.execute(httpDelete);
			reqInfo.setResponse(String.valueOf(response.getStatusLine().getStatusCode()));
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String relStr = EntityUtils.toString(response.getEntity());
				this.respTextAea.setText(relStr);
			}else {
				this.respTextAea.setText(response.toString());
			}
			this.requestTable.getItems().add(reqInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
