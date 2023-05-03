package com.ceir.CEIRPostman.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

public class KannelUtils {

    public static String sendSMS(String url, String from, String to, String username, String password, String message, String dlrMask, String dlrUrl, String coding, String correlationId, String operatorName) throws IOException {
//        String url = "http://your-kannel-server:13013/cgi-bin/sendsms";
        try {
            URI uri = new URI(dlrUrl);
            uri = new URIBuilder(uri)
                    .addParameter("answer", "%A")
                    .addParameter("status", "%d")
                    .addParameter("dlrvTime", "%T")
                    .addParameter("myId", correlationId)
                    .addParameter("operatorName", operatorName)
                    .build();

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);

            // Add the form parameters to the request
            List<NameValuePair> formParams = new ArrayList<>();
            formParams.add(new BasicNameValuePair("username", username));
            formParams.add(new BasicNameValuePair("password", password));
            formParams.add(new BasicNameValuePair("to", to));
            formParams.add(new BasicNameValuePair("text", message));
            formParams.add(new BasicNameValuePair("coding", coding));
            formParams.add(new BasicNameValuePair("charset", "UTF-8"));
            formParams.add(new BasicNameValuePair("dlr-mask", dlrMask));
            formParams.add(new BasicNameValuePair("dlr-url", uri.toString()));
            formParams.add(new BasicNameValuePair("from", from));

            httpPost.setEntity(new UrlEncodedFormEntity(formParams));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity entity = httpResponse.getEntity();

            // Get the response body as a string
            String responseBody = EntityUtils.toString(entity);

            if (statusCode >= 400 && statusCode <= 499) {
                throw new ClientProtocolException("HTTP " + statusCode + ": " + httpResponse.getStatusLine().getReasonPhrase());
            } else if (statusCode >= 500 && statusCode <= 599) {
                throw new ServerException("HTTP " + statusCode + ": " + httpResponse.getStatusLine().getReasonPhrase());
            }
            return responseBody;
        } catch (Exception e) {
            System.out.println("Exception while sending message through kanel: "+e);
            return null;
        }

    }

}
