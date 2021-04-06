package ke.co.payment.integrations;

import com.squareup.okhttp.*;

import java.io.IOException;

public class Main
{
    public static String SANDBOX_PK = "sandbox.cer";
    public static void main(String args[]) throws Exception
    {
        //how to get initiator encrypted password

        InitiatorPasswordEncryption app = new InitiatorPasswordEncryption();
        String ciphertext = app.encryptInitiatorPassword(SANDBOX_PK,"Safaricom111!");
        System.out.println("Encrypted password:\n" + ciphertext);
        System.out.println();
       // ciphertext="hIjgsh6u4uXLLDUPWrw7Fi9Upnro9LAnD+9rMTtdmS2kVkK2mSahcm42oMqAKZJ6dzwJf3P7LHVp/29izrYWvCJW7UtciW3v74DVrffm2jYRgdJ4VCch933C12zlbLci5+TO0e0ymDiszpNnpcu/FSU1XuySwiaN4dy49rUJDJyfCn58mUTJKh78DgteYm3F1vonF2AU9BClZo9COssgea6OLqQCDWiuW5HA01nK6rX/Z47RPfFozI+bln9TbjaUGgxeY1Ga92W+1E3UUDhmz0Vh0pg0sOCpHxmbETu9nRBtMBpOTIWWuS5n9ymqDxlTrBuacLTUpIo4rplD9fPRSQ==";
        final String token  = "8OBqTqBC5bunS3LcIJtrfAvEXFhr";
        sendBalanceRequest(token,ciphertext);


    }



    private  static void sendBalanceRequest(final String token, String credential) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String apiUser="apitest354";
        String payBillNo="601354";
        String safcomCallBackUrl="https://integration.systems.co.ke/apihelper/balanceenquiry";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"Initiator\":\""+apiUser+"\","+
                " \"SecurityCredential\":\""+credential+"\","+
                "\"CommandID\":\"AccountBalance\", "+
                " \"PartyA\":\""+payBillNo+"\","+
                "\"IdentifierType\":\"4\","+
                "\"Remarks\":\"Remarks\","+
                "\"QueueTimeOutURL\":\""+safcomCallBackUrl+"\","+
                "\"ResultURL\":\""+safcomCallBackUrl+"\"}");
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/mpesa/accountbalance/v1/query")
                .post(body)
                .addHeader("authorization", "Bearer "+token)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected error code " + response);
        final String responseText = response.body().string();
        System.out.println(responseText);

    }


}
