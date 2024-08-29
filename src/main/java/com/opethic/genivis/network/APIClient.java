package com.opethic.genivis.network;

import com.opethic.genivis.controller.master.ledger.create.LedgerCreateController;
import com.opethic.genivis.sql_lite.UserToken;
import com.opethic.genivis.utils.Globals;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class APIClient extends Service<String> {


    HttpResponse<String> response = null;
    private String endPoint = "";
    private String requestBody = "";
    private Map<String, String> textBody = new HashMap<>();
    private Map<String, File> multipartBody = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private RequestType requestType = null;
    private String responseBody = "";

    private static final Logger logger = LogManager.getLogger(APIClient.class);

    public APIClient(String endPoint, String requestBody, RequestType requestType) {
        this.endPoint = endPoint;
        this.requestBody = requestBody;
        this.requestType = requestType;
    }

    public APIClient(String endPoint, Map<String, String> textBody, Map<String, String> headers,
                     Map<String, File> multipartBody, RequestType requestType) {
        this.endPoint = endPoint;
        this.textBody = textBody;
        this.headers = headers;
        this.multipartBody = multipartBody;
        this.requestType = requestType;
    }

    public static HttpResponse<String> postJsonRequest(String requestBody, String endPoint) {
        HttpResponse<String> response = null;
        try {
//            System.out.println("requestBody=>" + requestBody);

            HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

            HttpRequest request = null;
            request = HttpRequest.newBuilder().uri(URI.create(Globals.baseUrl + endPoint))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-type", "application/json")
                    .header("Authorization", "Bearer " + Globals.USER_TOKEN)
                    .header("Accept", "application/json")
                    .header("branch", Globals.headerBranch)
                    .timeout(Duration.ofMinutes(2000)).build();
            CompletableFuture<HttpResponse<String>> res = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            response = res.get();


        } catch (Exception x) {
            x.printStackTrace();
            System.out.println("Error=>" + x.toString());
        }
        return response;
    }

    public static HttpResponse<String> postJsonRequestNoToken(String requestBody, String endPoint) {
        HttpResponse<String> response = null;
        try {
            HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

            URI uri = URI.create(Globals.baseUrl + endPoint);
            HttpRequest request = null;

            request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-type", "application/json")
                    .header("Accept", "application/json")
                    .header("branch", Globals.headerBranch)
                    .timeout(Globals.TWO_MIN_API_TIMEOUT).build();

            CompletableFuture<HttpResponse<String>> res = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            response = res.get();

        } catch (Exception x) {
            x.printStackTrace();
            System.out.println("Error=>" + x.toString());
        }
        return response;
    }

    public static HttpResponse<String> postMultipartFormDataRequest(String endPoing) {
        HttpResponse<String> response = null;

        return response;
    }

    public static HttpResponse<String> postFormDataRequest(String formData, String endPoint) {
        HttpResponse<String> response = null;
        try {
            HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(Globals.baseUrl + endPoint)).POST(HttpRequest.BodyPublishers.ofString(formData)).header("Content-Type", "application/x-www-form-urlencoded").header("Authorization", "Bearer " + Globals.USER_TOKEN)
                    .header("branch", Globals.headerBranch)
                    .timeout(Globals.TWO_MIN_API_TIMEOUT).build();

            CompletableFuture<HttpResponse<String>> res = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            response = res.get();

        } catch (Exception x) {
            x.printStackTrace();
            System.out.println("Error => " + x.toString());
        }
        return response;
    }

    public static HttpResponse<String> getRequest(String endPoint) {
        HttpResponse<String> response = null;
        try {
            HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(Globals.baseUrl + endPoint)).GET()
                    .header("Authorization", "Bearer " + Globals.USER_TOKEN)
                    .header("branch", Globals.headerBranch)
                    .timeout(Globals.TWO_MIN_API_TIMEOUT).build();

            CompletableFuture<HttpResponse<String>> res = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            response = res.get();

        } catch (Exception x) {
            x.printStackTrace();
            System.out.println("Error => " + x.toString());
        }
        return response;
    }

    public static String    postMultipartRequest(Map<String, String> textBody, Map<String, File> multipartBody, String endPoint, Map<String, String> headers) {
        String response = "";
        try {

            //Creating CloseableHttpClient object
            CloseableHttpClient httpclient = HttpClients.createDefault();

            //Creating the MultipartEntityBuilder
            MultipartEntityBuilder entitybuilder = MultipartEntityBuilder.create();

            //Setting the mode
            entitybuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            if (textBody != null) {
                for (Map.Entry<String, String> entry : textBody.entrySet()) {
                    String key = entry.getKey();
                    System.out.print("Key:" + key);
                    String value = entry.getValue();
                    System.out.println("\tValue:" + value);
                    entitybuilder.addTextBody(key, value);
                }
            }

            if (multipartBody != null) {
                for (Map.Entry<String, File> entry : multipartBody.entrySet()) {
                    String key = entry.getKey();

                    File value = entry.getValue();

                    entitybuilder.addBinaryBody(key, value);
                }
            }

            //Building a single entity using the parts
            HttpEntity mutiPartHttpEntity = entitybuilder.build();

            //Building the RequestBuilder request object
            RequestBuilder reqbuilder = RequestBuilder.post(Globals.baseUrl + endPoint);

            //Set the entity object to the RequestBuilder
            reqbuilder.setEntity(mutiPartHttpEntity);

            //Building the request
            HttpUriRequest multipartRequest = reqbuilder.build();
            multipartRequest.setHeader("Authorization", "Bearer " + Globals.USER_TOKEN);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    multipartRequest.setHeader(key, value);
                }
            }

            //Executing the request
            org.apache.http.HttpResponse httpresponse = httpclient.execute(multipartRequest);

            //Printing the status and the contents of the response
            response = EntityUtils.toString(httpresponse.getEntity());

        } catch (Exception x) {
            x.printStackTrace();
            System.out.println("Error => " + x.toString());
        }
        return response;
    }

    @Override
    protected Task<String> createTask() {
        logger.debug("API Client Create Task Started");
        return new Task<String>() {
            @Override
            protected String call() throws IOException, MalformedURLException {
                try {

                    if (requestType == RequestType.GET) {
                        response = getRequest(endPoint);
                        responseBody = response.body();
                    } else if (requestType == RequestType.FORM_DATA) {
                        response = postFormDataRequest(requestBody, endPoint);
                        responseBody = response.body();
                    } else if (requestType == RequestType.JSON) {
                        response = postJsonRequest(requestBody, endPoint);
                        responseBody = response.body();
                    } else if (requestType == RequestType.JSON_NO_TOKEN) {
                        response = postJsonRequestNoToken(requestBody, endPoint);
                        responseBody = response.body();
                    } else if (requestType == RequestType.MULTI_PART) {
                        responseBody = postMultipartRequest(textBody, multipartBody, endPoint, headers);
                    }


                } catch (Exception x) {
                    logger.error("Exception in call()---> " + x.getMessage());
                }
                return responseBody;
            }
        };

    }

}
