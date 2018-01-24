/**
 *
 */
package com.kount.kountaccess;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

import com.kount.kountaccess.AccessException.AccessErrorType;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

import org.json.JSONObject;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Unit Tests around the Access SDK
 *
 * @author gjd, abe, cwm
 *
 */
public class AccessSDKTest {

    private static final Logger logger = Logger.getLogger(AccessSDKTest.class);

    // Setup data for comparisons.
    int merchantId = 999999;
    String host = merchantId + ".kountaccess.com";
    String accessUrl = "https://" + host + "/access";
    String session = "askhjdaskdgjhagkjhasg47862345shg";
    String sessionUrl = "https://" + host + "/api/session=" + session;
    String user = "greg@test.com";
    String password = "password";
    String apiKey = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIxMDAxMDAiLCJhdWQiOiJLb3VudC4wIiwiaWF0IjoxNDI0OTg5NjExLCJzY3AiOnsia2MiOm51bGwsImFwaSI6ZmFsc2UsInJpcyI6ZmFsc2V9fQ.S7kazxKVgDCrNxjuieg5ChtXAiuSO2LabG4gzDrh1x8";
    String country = "US";
    String fingerprint = "75012bd5e5b264c4b324f5c95a769541";
    String ipAddress = "64.128.91.251";
    String ipGeo = "US";
    String region = "ID";
    String responseId = "bf10cd20cf61286669e87342d029e405";
    String decision = "A";
    String uniq = "uniqUserAccounUserAccountt";

    String deviceJSON = new JSONObject()
        .put("response_id", responseId)
        .put("device", new JSONObject()
                .put("country", country)
                .put("id", fingerprint)
                .put("ipAddress", ipAddress)
                .put("ipGeo", ipGeo)
                .put("mobile", 1)
                .put("proxy", 0)
                .put("region", region)
                .put("tor", 0)
        ).toString();

    String velocityJSON = new JSONObject()
        .put("response_id", responseId)
        .put("device", new JSONObject()
                .put("country", country)
                .put("id", fingerprint)
                .put("ipAddress", ipAddress)
                .put("ipGeo", ipGeo)
                .put("mobile", 1)
                .put("proxy", 0)
                .put("region", region)
                .put("tor", 0)
        )
        .put("velocity", new JSONObject()
                .put("account", new JSONObject()
                        .put("dlh", 1).put("dlm", 1).put("iplh", 1).put("iplm", 1).put("plh", 1).put("plm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("device", new JSONObject()
                        .put("alh", 1).put("alm", 1).put("iplh", 1).put("iplm", 1).put("plh", 1).put("plm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("ip_address", new JSONObject()
                        .put("alh", 1).put("alm", 1).put("dlh", 1).put("dlm", 1).put("plh", 1).put("plm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("password", new JSONObject()
                    .put("alh", 1).put("alm", 1).put("dlh", 1).put("dlm", 1).put("iplh", 1).put("iplm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("user", new JSONObject()
                    .put("alh", 1).put("alm", 1).put("dlh", 1).put("dlm", 1).put("iplh", 1).put("iplm", 1).put("plh", 1).put("plm", 1)
                )
        ).toString();

    String decisionJSON = new JSONObject()
        .put("response_id", responseId)
        .put("decision", new JSONObject()
                .put("errors", "[]")
                .put("reply", new JSONObject()
                    .put("ruleEvents", new JSONObject()
                        .put("decision", decision)
                        .put("ruleEvents", "[]")
                        .put("total", 0)
                    )
                    .put("warnings", "[]")
                )
        )
        .put("device", new JSONObject()
                .put("country", country)
                .put("id", fingerprint)
                .put("ipAddress", ipAddress)
                .put("ipGeo", ipGeo)
                .put("mobile", 1)
                .put("proxy", 0)
                .put("region", region)
        )
        .put("velocity", new JSONObject()
                .put("account", new JSONObject()
                        .put("dlh", 1).put("dlm", 1).put("iplh", 1).put("iplm", 1).put("plh", 1).put("plm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("device", new JSONObject()
                        .put("alh", 1).put("alm", 1).put("iplh", 1).put("iplm", 1).put("plh", 1).put("plm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("ip_address", new JSONObject()
                        .put("alh", 1).put("alm", 1).put("dlh", 1).put("dlm", 1).put("plh", 1).put("plm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("password", new JSONObject()
                    .put("alh", 1).put("alm", 1).put("dlh", 1).put("dlm", 1).put("iplh", 1).put("iplm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("user", new JSONObject()
                    .put("alh", 1).put("alm", 1).put("dlh", 1).put("dlm", 1).put("iplh", 1).put("iplm", 1).put("plh", 1).put("plm", 1)
                )
        ).toString();

    String gatherDeviceInfoJSON = new JSONObject()
        .put("response_id", responseId)
        .put("device", new JSONObject()
                .put("country", country)
                .put("id", fingerprint)
                .put("ipAddress", ipAddress)
                .put("ipGeo", ipGeo)
                .put("mobile", 1)
                .put("proxy", 0)
                .put("region", region)
                .put("tor", 0)
        )
        .put("velocity", new JSONObject()
                .put("account", new JSONObject()
                        .put("dlh", 1).put("dlm", 1).put("iplh", 1).put("iplm", 1)
                        .put("plh", 1).put("plm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("device", new JSONObject()
                        .put("alh", 1).put("alm", 1).put("iplh", 1).put("iplm", 1)
                        .put("plh", 1).put("plm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("ip_address", new JSONObject()
                        .put("alh", 1).put("alm", 1).put("dlh", 1).put("dlm", 1)
                        .put("plh", 1).put("plm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("password", new JSONObject()
                    .put("alh", 1).put("alm", 1).put("dlh", 1).put("dlm", 1)
                    .put("iplh", 1).put("iplm", 1).put("ulh", 1).put("ulm", 1)
                )
                .put("user", new JSONObject()
                    .put("alh", 1).put("alm", 1).put("dlh", 1).put("dlm", 1)
                    .put("iplh", 1).put("iplm", 1).put("plh", 1).put("plm", 1)
                )
        )
        .put("decision", new JSONObject()
                .put("errors", "[]")
                .put("warnings", ":[{\"code\":\"WARNING\",\"message\":\"No thresholds enabled or configured\"}]")
                .put("reply", new JSONObject()
                    .put("ruleEvents", new JSONObject()
                        .put("decision", decision)
                        .put("ruleEvents", "[]")
                        .put("total", 0)
                    )
                    .put("warnings", "[]")
                )
        ).toString();


    private static final Set<String> entityTypes =
            new HashSet<String>(Arrays.asList("account", "device", "ip_address", "password", "user"));

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#AccessSdk(java.lang.String, int, java.lang.String)}.
     */
    @Test
    public void testConstructorAccessSDKHappyPath() {
        try {
            AccessSdk sdk = new AccessSdk(host, merchantId, apiKey);
            assertNotNull(sdk);
        } catch (AccessException ae) {
            fail("Bad exception" + ae.getAccessErrorType().name() + ":" + ae.getMessage());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#AccessSdk(java.lang.String, int, java.lang.String)}.
     */
    @Test
    public void testConstructorAccessSDKMissingHost() {
        try {
            new AccessSdk(null, merchantId, apiKey);
            fail("Should have failed host");

        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#AccessSdk(java.lang.String, int, java.lang.String)}.
     */
    @Test
    public void testConstructorAccessSDKBadMerchant() {
        try {
            new AccessSdk(host, -1, apiKey);
            fail("Should have failed merchantId");

        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#AccessSdk(java.lang.String, int, java.lang.String)}.
     */
    @Test
    public void testConstructorAccessSDKMissingApiKey() {
        try {
            new AccessSdk(host, merchantId, null);
            fail("Should have failed apiKey");

        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#AccessSdk(java.lang.String, int, java.lang.String)}.
     */
    @Test
    public void testConstructorAccessSDKBlankApiKey() {
        try {
            new AccessSdk(host, merchantId, "    ");
            fail("Should have failed apiKey");

        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for
     * {@link com.kount.kountaccess.AccessSdk#getVelocity(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetVelocityHappyPath() {

        try {
            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);
            CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
            StatusLine mockStatus = mock(StatusLine.class);
            doReturn(mockResponse).when(mockHttpClient).execute((HttpPost) anyObject());
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));
            doReturn(mockHttpClient).when(sdk).getHttpClient();
            doReturn(velocityJSON).when(sdk).getResponseAsString(mockResponse);
            doReturn(mockStatus).when(mockResponse).getStatusLine();
            doReturn(200).when(mockStatus).getStatusCode();
            JSONObject accessInfo = sdk.getVelocity(session, user, password);
            assertTrue(accessInfo != null);
            // validate device
            JSONObject device = accessInfo.getJSONObject("device");
            assertTrue(accessInfo != null);
            assertEquals(fingerprint, device.get("id"));
            assertEquals(ipAddress, device.get("ipAddress"));
            assertEquals(ipGeo, device.get("ipGeo"));
            assertEquals(1, device.get("mobile"));
            assertEquals(0, device.get("proxy"));
            assertEquals(0, device.get("tor"));
            // validate id
            assertEquals(responseId, accessInfo.getString("response_id"));
            JSONObject velocities = accessInfo.getJSONObject("velocity");
            Iterator<String> iter = entityTypes.iterator();
            while (iter.hasNext()) {
                String entityType = (String) iter.next();
                JSONObject velocityInfo = velocities.getJSONObject(entityType);
                assertNotNull("Velocity Type was null " + entityType, velocityInfo);
                assertEquals(8, velocityInfo.keySet().size());
            }
        } catch (IOException ioe) {
            fail("Exception:" + ioe.getMessage());
        } catch (AccessException ae) {
            fail("Exception:" + ae.getMessage());
        }
    }

    /**
     * Test IllegalArgumentException Test method for
     * {@link com.kount.kountaccess.AccessSdk#getVelocity(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetVelocityIllegalArgumentException() {

        try {
            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);
            AccessSdk sdk = spy(new AccessSdk("whetever is bad", merchantId, apiKey));
            doReturn(mockHttpClient).when(sdk).getHttpClient();
            doReturn(mockPost).when(sdk).getHttpPost(accessUrl);
            sdk.getVelocity(session, user, password);
            fail("Exception Not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test Client Protocol Exception Test method for
     * {@link com.kount.kountaccess.AccessSdk#getVelocity(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetVelocityClientProtocolException() {

        try {
            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);
            doThrow(new ClientProtocolException()).when(mockHttpClient).execute((HttpPost) anyObject());
            AccessSdk sdk = spy(new AccessSdk("gty://bad.host.com", merchantId, apiKey));
            doReturn(mockHttpClient).when(sdk).getHttpClient();
            doReturn(mockPost).when(sdk).getHttpPost(accessUrl);
            sdk.getVelocity(session, user, password);
            fail("AccessException Not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.NETWORK_ERROR, ae.getAccessErrorType());
        } catch (ClientProtocolException e) {
            fail("Should not have thrown ClientProtocolException");
        } catch (IOException e) {
            fail("Should not have thrown IOException");
        }
    }

    /**
     * Test IO Exception Test method for
     * {@link com.kount.kountaccess.AccessSdk#getVelocity(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetVelocityIOException() {

        try {
            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);
            doThrow(new IOException()).when(mockHttpClient).execute((HttpPost) anyObject());
            AccessSdk sdk = spy(new AccessSdk("bad.host.com", merchantId, apiKey));
            doReturn(mockHttpClient).when(sdk).getHttpClient();
            doReturn(mockPost).when(sdk).getHttpPost(accessUrl);
            sdk.getVelocity(session, user, password);
            fail("AccessException Not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.NETWORK_ERROR, ae.getAccessErrorType());
        } catch (ClientProtocolException e) {
            fail("Should not have thrown ClientProtocolException");
        } catch (IOException e) {
            fail("Should not have thrown IOException");
        }
    }

    /**
     * Test UnknownHostException Test method for
     * {@link com.kount.kountaccess.AccessSdk#getVelocity(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetVelocityUnknownHostException() {

        try {
            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);
            AccessSdk sdk = spy(new AccessSdk("bad.host.com", merchantId, apiKey));
            doReturn(mockHttpClient).when(sdk).getHttpClient();
            doReturn(mockPost).when(sdk).getHttpPost(accessUrl);
            when(mockHttpClient.execute((HttpPost) anyObject())).thenThrow(new UnknownHostException());
            sdk.getVelocity(session, user, password);
            fail("AccessException Not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.NETWORK_ERROR, ae.getAccessErrorType());
        } catch (ClientProtocolException e) {
            fail("Should not have thrown ClientProtocolException");
        } catch (IOException e) {
            fail("Should not have thrown IOException");
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#getDevice(java.lang.String)}.
     */
    @Test
    public void testGetDevice() {
        try {
            // class to test
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));
            // mock objects
            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpGet mockGet = mock(HttpGet.class);
            CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
            StatusLine mockStatus = mock(StatusLine.class);
            // mock responses
            doReturn(mockHttpClient).when(sdk).getHttpClient();
            doReturn(mockGet).when(sdk).getHttpGet(sessionUrl);
            doReturn(mockResponse).when(mockHttpClient).execute((HttpGet) anyObject());
            doReturn(deviceJSON).when(sdk).getResponseAsString(mockResponse);
            doReturn(mockStatus).when(mockResponse).getStatusLine();
            doReturn(200).when(mockStatus).getStatusCode();
            // test method
            JSONObject deviceInfo = sdk.getDevice(session);
            logger.debug(deviceInfo);
            JSONObject device = deviceInfo.getJSONObject("device");
            assertTrue(device != null);
            assertEquals(fingerprint, device.get("id"));
            assertEquals(ipAddress, device.get("ipAddress"));
            assertEquals(ipGeo, device.get("ipGeo"));
            assertEquals(1, device.get("mobile"));
            assertEquals(0, device.get("proxy"));
            assertEquals(0, device.get("tor"));
            assertEquals(responseId, deviceInfo.get("response_id"));

        } catch (IOException ioe) {
            fail("Exception:" + ioe.getMessage());
        } catch (AccessException ae) {
            fail("Exception:" + ae.getMessage());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#getDecision(String, String, String)}.
     */
    @Test
    public void testGetDecisionHappyPath() {

        try {
            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);
            CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
            StatusLine mockStatus = mock(StatusLine.class);
            doReturn(mockResponse).when(mockHttpClient).execute((HttpPost) anyObject());
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));
            doReturn(mockHttpClient).when(sdk).getHttpClient();
            doReturn(mockPost).when(sdk).getHttpPost(accessUrl);
            doReturn(decisionJSON).when(sdk).getResponseAsString(mockResponse);
            doReturn(mockStatus).when(mockResponse).getStatusLine();
            doReturn(200).when(mockStatus).getStatusCode();
            JSONObject decisionInfo = sdk.getDecision(session, user, password);
            logger.debug(decisionInfo);
            assertTrue(decisionInfo != null);
            // validate device
            JSONObject decisionn = decisionInfo.getJSONObject("decision");
            JSONObject reply = decisionn.getJSONObject("reply");
            JSONObject ruleEvents = reply.getJSONObject("ruleEvents");
            assertEquals(decision, ruleEvents.get("decision"));
        } catch (IOException ioe) {
            fail("Exception:" + ioe.getMessage());
        } catch (AccessException ae) {
            fail("Exception:" + ae.getMessage());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#setDeviceTrust(String, String, String, String, String)}.
     */
    @Test
    public void testSetDeviceTrustHappyPath() {

        try {
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));

            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);
            CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
            StatusLine mockStatus = mock(StatusLine.class);

            doReturn(mockResponse).when(mockHttpClient).execute((HttpPost) anyObject());
            doReturn(mockHttpClient).when(sdk).getHttpClient();
            doReturn(null).when(sdk).getResponseAsString(mockResponse);
            doReturn(mockStatus).when(mockResponse).getStatusLine();
            doReturn(200).when(mockStatus).getStatusCode();

            JSONObject response = sdk.setDeviceTrust(fingerprint, uniq, "trusted");
            logger.debug(response);
            assertTrue(response == null);
        } catch (IOException ioe) {
            fail("Exception:" + ioe.getMessage());
        } catch (AccessException ae) {
            fail("Exception:" + ae.getMessage());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#setDeviceTrust(String, String, String, String, String)}.
     */
    @Test
    public void testSetDeviceBadIllegalTrustState() {

        try {
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));

            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);

            doReturn(mockHttpClient).when(sdk).getHttpClient();
            sdk.setDeviceTrust(fingerprint, uniq, "whatever is bad");
            fail("AccessException not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#setDeviceTrust(String, String, String, String, String)}.
     */
    @Test
    public void testSetDeviceTrustBadUniq() {

        try {
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));

            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);

            doReturn(mockHttpClient).when(sdk).getHttpClient();
            sdk.setDeviceTrust(fingerprint, "0123456789abcdefghijklmnopqrstuvwxyz", "trusted");
            fail("AccessException not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#setDeviceTrust(String, String, String, String, String)}.
     */
    @Test
    public void testSetDeviceTrustBadDeviceId() {

        try {
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));

            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);

            doReturn(mockHttpClient).when(sdk).getHttpClient();
            sdk.setDeviceTrust("0123456789abcdefghijklmnopqrstuvwxyz", uniq, "trusted");
            fail("AccessException not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#gatherDeviceInfo(String, String, String, String, String, String)}.
     */
    @Test
    public void testGatherDeviceInfoHappyPath() {

        try {
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));

            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);
            CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
            StatusLine mockStatus = mock(StatusLine.class);

            doReturn(mockResponse).when(mockHttpClient).execute((HttpPost) anyObject());
            doReturn(mockHttpClient).when(sdk).getHttpClient();
            doReturn(gatherDeviceInfoJSON).when(sdk).getResponseAsString(mockResponse);
            doReturn(mockStatus).when(mockResponse).getStatusLine();
            doReturn(200).when(mockStatus).getStatusCode();

            JSONObject deviceInfo = sdk.gatherDeviceInfo(session, user, password, "15", fingerprint, uniq);
            logger.debug(deviceInfo);

            JSONObject device = deviceInfo.getJSONObject("device");
            assertTrue(device != null);
            assertEquals(fingerprint, device.get("id"));

            JSONObject decision = deviceInfo.getJSONObject("decision");
            assertTrue(decision != null);

            JSONObject velocity = deviceInfo.getJSONObject("velocity");
            assertTrue(velocity != null);
        } catch (IOException ioe) {
            fail("Exception:" + ioe.getMessage());
        } catch (AccessException ae) {
            fail("Exception:" + ae.getMessage());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#gatherDeviceInfo(String, String, String, String, String, String)}.
     */
    @Test
    public void testBadReturnValueLessThanGatherDeviceInfo() {

        try {
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));

            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);

            doReturn(mockHttpClient).when(sdk).getHttpClient();

            sdk.gatherDeviceInfo(session, user, password, "-1", fingerprint, uniq);
            fail("AccessException not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#gatherDeviceInfo(String, String, String, String, String, String)}.
     */
    @Test
    public void testBadReturnValueGreaterThanGatherDeviceInfo() {

        try {
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));

            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);

            doReturn(mockHttpClient).when(sdk).getHttpClient();

            sdk.gatherDeviceInfo(session, user, password, "16", fingerprint, uniq);
            fail("AccessException not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#gatherDeviceInfo(String, String, String, String, String, String)}.
     */
    @Test
    public void testBadSessionGatherDeviceInfo() {

        try {
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));

            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);

            doReturn(mockHttpClient).when(sdk).getHttpClient();

            sdk.gatherDeviceInfo("badSessionID", user, password, "15", fingerprint, uniq);
            fail("AccessException not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#gatherDeviceInfo(String, String, String, String, String, String)}.
     */
    @Test
    public void testBadUniqGatherDeviceInfo() {

        try {
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));

            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);

            doReturn(mockHttpClient).when(sdk).getHttpClient();

            sdk.gatherDeviceInfo(session, user, password, "15", fingerprint, "0123456789abcdefghijklmnopqrstuvwxyz");
            fail("AccessException not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }

    /**
     * Test method for {@link com.kount.kountaccess.AccessSdk#gatherDeviceInfo(String, String, String, String, String, String)}.
     */
    @Test
    public void testBadDeviceIdGatherDeviceInfo() {

        try {
            AccessSdk sdk = spy(new AccessSdk(host, merchantId, apiKey));

            CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
            HttpPost mockPost = mock(HttpPost.class);

            doReturn(mockHttpClient).when(sdk).getHttpClient();

            sdk.gatherDeviceInfo(session, user, password, "15", "0123456789abcdefghijklmnopqrstuvwxyz", uniq);
            fail("AccessException not thrown");
        } catch (AccessException ae) {
            assertEquals(AccessErrorType.INVALID_DATA, ae.getAccessErrorType());
        }
    }
}

