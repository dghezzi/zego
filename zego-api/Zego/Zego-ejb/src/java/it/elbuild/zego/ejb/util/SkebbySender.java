/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb.util;

/**
 *
 * @author Lu
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Lu
 */
public class SkebbySender {

    private  String ASCIISENDER;
    private  String USERNAME;
    private  String PASSWORD;
    private  String TYPE;

    public SkebbySender() {
        
    }

    public String send(ArrayList<String> targets, String text) throws Exception {
        String[] recipients = new String[targets.size()];

        // DUMMY CONVERSION
        int i = 0;
        for (String targ : targets) {
            
            if(targ.length()<11){
                targ = "39"+targ;
            }
            
            if(targ.startsWith("+")){
             targ = targ.replace("+", "");
            }
            
            recipients[i] = targ;
            i++;
        }
        // Invio SMS Classic con mittente personalizzato di tipo alfanumerico
        String result = skebbyGatewaySendSMS(USERNAME, PASSWORD, recipients, text, TYPE, null, ASCIISENDER);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "SKEBBY SAID: {0}", result);
        return result;
    }

    protected String skebbyGatewaySendSMS(String username, String password, String[] recipients, String text, String smsType, String senderNumber, String senderString) throws IOException {
        return skebbyGatewaySendSMS(username, password, recipients, text, smsType, senderNumber, senderString, "UTF-8");
    }

    protected String skebbyGatewaySendSMS(String username, String password, String[] recipients, String text, String smsType, String senderNumber, String senderString, String charset) throws IOException {
        String endpoint = "http://gateway.skebby.it/api/send/smseasy/advanced/http.php";
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
        paramsBean.setVersion(HttpVersion.HTTP_1_1);
        paramsBean.setContentCharset(charset);
        paramsBean.setHttpElementCharset(charset);

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("method", smsType));
        formparams.add(new BasicNameValuePair("username", username));
        formparams.add(new BasicNameValuePair("password", password));
        if (null != senderNumber) {
            formparams.add(new BasicNameValuePair("sender_number", senderNumber));
        }
        if (null != senderString) {
            formparams.add(new BasicNameValuePair("sender_string", senderString));
        }

        for (String recipient : recipients) {
            formparams.add(new BasicNameValuePair("recipients[]", recipient));
        }
        formparams.add(new BasicNameValuePair("text", text));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, charset);
        HttpPost post = new HttpPost(endpoint);
        post.setEntity(entity);

        HttpResponse response = httpclient.execute(post);
        HttpEntity resultEntity = response.getEntity();
        if (null != resultEntity) {
            return EntityUtils.toString(resultEntity);
        }
        return null;
    }

    public  String getASCIISENDER() {
        return ASCIISENDER;
    }

    public  void setASCIISENDER(String ASCIISENDER) {
        this.ASCIISENDER = ASCIISENDER;
    }

    public  String getUSERNAME() {
        return USERNAME;
    }

    public  void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public  String getPASSWORD() {
        return PASSWORD;
    }

    public  void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
    
    
}
